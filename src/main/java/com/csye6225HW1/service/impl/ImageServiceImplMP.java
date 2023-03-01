package com.csye6225HW1.service.impl;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csye6225HW1.Exceptions.ImageException.ChangeOtherImageException;
import com.csye6225HW1.Exceptions.ProductException.CreateOrUpdateProductException;
import com.csye6225HW1.Exceptions.ProductException.ImageNotExistException;
import com.csye6225HW1.Exceptions.UserException.ChangeOthersInfoException;
import com.csye6225HW1.POJO.Image;
import com.csye6225HW1.POJO.Product;
import com.csye6225HW1.config.AwsConfig;
import com.csye6225HW1.dao.ImagesDao;
import com.csye6225HW1.dao.ProductsDao;
import com.csye6225HW1.service.IImagesService;
import com.csye6225HW1.util.ErrorMessage;
import com.csye6225HW1.util.UserPassingLoginAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.io.File;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;


//after extends UsersDao, Users, all method included by IusersService(MybatisPlus provided) will be impl atuomatically.
@Service
@Slf4j
public class ImageServiceImplMP extends ServiceImpl<ImagesDao, Image> implements IImagesService {


    @Autowired
    ImagesDao imagesDao;
    @Autowired
    ProductsDao productsDao;
    @Autowired
    ProductServiceImplMP productsService;

    @Autowired
    private AmazonS3 amazons3;

    public static String s3BucketName = System.getenv("BUCKET_NAME");


    private void checkImageAuth(Image imageToBeAuth){
        // check is quantity is correct  404
        if(!Objects.equals(imageToBeAuth.getUserId(),UserPassingLoginAuth.getUser().getId().toString())){
            log.warn("User {} is trying to the get User {} 's product image with ProductId: {} "
                    ,UserPassingLoginAuth.getUser().getId(), imageToBeAuth.getUserId(), imageToBeAuth.getImageId());
            throw new ChangeOthersInfoException(ErrorMessage.CHANGE_OTHER_INFORMATION);
        }
    }

    private void checkProductAuth(Long productId){
        Product ProductToCheck = productsService.getProduct(productId);
        // check is quantity is correct  404
        if(!Objects.equals(ProductToCheck.getOwnerUserId(),UserPassingLoginAuth.getUser().getId())){

            throw new ChangeOtherImageException(ErrorMessage.CHANGE_OTHER_IMAGE);
        }
    }


//    public Image getImage(Long productID,String imageId) {
//
//
//        Optional<Image> image = Optional.ofNullable(imagesDao.selectById(imageId));
//        if (image.isPresent()) {
//            if (!Objects.equals(UserPassingLoginAuth.getUser().getId().toString(), image.get().getUserId())) {
//                throw new RuntimeException("No right to access this image");
//            } else {return image.get();}
//
//        } else {
//            throw new ImageNotExistException(ErrorMessage.IMAGE_NOT_EXIST);  //400
//        }
//
//    }


    public Image getImage(Long productId, String imageId) throws IOException {
        Optional<Image> optionalImage = Optional.ofNullable(imagesDao.selectById(imageId));
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            if (!Objects.equals(UserPassingLoginAuth.getUser().getId().toString(), image.getUserId())) {
                throw new RuntimeException("No right to access this image");
            }
            String s3BucketPath = image.getS3BucketPath();
            String[] s3PathParts = s3BucketPath.replaceFirst("^s3://", "").split("/");
            String s3BucketName = s3PathParts[0];
            String s3ObjectKey = String.join("/", Arrays.copyOfRange(s3PathParts, 1, s3PathParts.length));
            S3Object s3Object = amazons3.getObject(s3BucketName, s3ObjectKey);
            InputStream inputStream = s3Object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(inputStream);
//            image.setBytes(bytes);
            return image;
        } else {
            throw new ImageNotExistException(ErrorMessage.IMAGE_NOT_EXIST);  //400
        }
    }

    public List<Image> getImageList(Long productId) {
        checkProductAuth(productId);
        List<Image> imageList = imagesDao.selectList(productId);
        if (!imageList.isEmpty()) {
            for (Image image : imageList) {
                String s3BucketPath = image.getS3BucketPath();
                String[] s3PathParts = s3BucketPath.replaceFirst("^s3://", "").split("/");
                String s3BucketName = s3PathParts[0];
                String s3ObjectKey = String.join("/", Arrays.copyOfRange(s3PathParts, 1, s3PathParts.length));
                String imageUrl = amazons3.getUrl(s3BucketName, s3ObjectKey).toString();

            }
            return imageList;
        }
        else {
            throw new ImageNotExistException(ErrorMessage.IMAGE_NOT_EXIST);
        }
    }








    public Image uploadImage(Long productId, MultipartFile file) throws IOException {
            checkProductAuth(productId);
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            byte[] bytes = file.getBytes();

            // Generate a unique image ID for this upload
           String imageId = UUID.randomUUID().toString();



        // Save the image to S3


        //String s3BucketName= "dc-s3-1";
       // String s3BucketName=   UUID.randomUUID().toString();


           String s3ObjectKey = String.format("product/%d/image/%s", productId, imageId);
           ObjectMetadata metadata = new ObjectMetadata();
           metadata.setContentType(contentType);
            metadata.setContentLength(bytes.length);
            amazons3.putObject(s3BucketName, s3ObjectKey, new ByteArrayInputStream(bytes), metadata);

            // Save image metadata to the database
            Image image = new Image();

            image.setImageId(imageId);
            image.setUserId(String.valueOf(UserPassingLoginAuth.getUser().getId()));
            image.setProductId(productId);
            //image.setUserId(image.getUserId());
            image.setFileName(fileName);

           image.setS3BucketPath(String.format("s3://%s/%s", s3BucketName, s3ObjectKey));
            imagesDao.insert(image);

            return image;
        }

        public void deleteImage(Long productId,String imageId) throws IOException {

            // Get the image metadata from the database
            Optional<Image> optionalImage = Optional.ofNullable(getImage(productId, imageId));
            if (!optionalImage.isPresent()) {
                throw new RuntimeException("Image not found");
            }
            Image image = optionalImage.get();
            checkImageAuth(image);
            checkProductAuth(productId);


            // Delete the image from S3
            String s3BucketPath = image.getS3BucketPath();
            String[] s3PathParts = s3BucketPath.replaceFirst("^s3://", "").split("/");
            String s3BucketName = s3PathParts[0];
            String s3ObjectKey = String.join("/", Arrays.copyOfRange(s3PathParts, 1, s3PathParts.length));
            amazons3.deleteObject(s3BucketName, s3ObjectKey);

             //Delete the image metadata from the database
            imagesDao.deleteById(imageId);


        }



//
//






}








