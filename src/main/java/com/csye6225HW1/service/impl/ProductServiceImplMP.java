package com.csye6225HW1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csye6225HW1.Exceptions.ProductException.CreateOrUpdateProductException;
import com.csye6225HW1.Exceptions.ProductException.NoContentException;
import com.csye6225HW1.Exceptions.ProductException.ProductNotExistException;
import com.csye6225HW1.Exceptions.UserException.ChangeOthersInfoException;
import com.csye6225HW1.POJO.Product;
import com.csye6225HW1.POJO.User;
import com.csye6225HW1.dao.ProductsDao;
import com.csye6225HW1.dao.UsersDao;
import com.csye6225HW1.service.IProductsService;
import com.csye6225HW1.service.IUsersService;
import com.csye6225HW1.util.ErrorMessage;
import com.csye6225HW1.util.UserDemo;
import com.csye6225HW1.util.UserPassingLoginAuth;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

//after extends UsersDao, Users, all method included by IusersService(MybatisPlus provided) will be impl atuomatically.
@Service
@Slf4j
//usersDetailService provide user query service
public class ProductServiceImplMP extends ServiceImpl<ProductsDao, Product> implements IProductsService{



   @Autowired
    ProductsDao productDao;


    public Product createProduct(Product product) {
        // check the product quantity  400 bad request or 204 no content
        checkProduct(product);
        // check if sku is repeated   404
        List<Product> existingProducts =
                productDao.selectBySku(product.getSku());
        if(existingProducts!=null && existingProducts.size()>0){
            throw new CreateOrUpdateProductException(ErrorMessage.REPEAT_SKU);
        }

        // set the user_owner_id
        product.setOwnerUserId(UserPassingLoginAuth.getUser().getId());

        //product.setDateLastUpdated(String.valueOf(System.currentTimeMillis()));

        productDao.insert(product);
        return product;
    }

    public Product getProduct(Long productId){
        // check for the product
        Optional<Product> product = Optional.ofNullable(productDao.selectById(productId));
        if(product.isPresent()){
           // log.info("User {}: Get the product {}", UserPassingLoginAuth.getUser().getId(), productId);
            return product.get();
        }else{
            //log.warn("User {}: The product doesn't exist ",UserPassingLoginAuth.getUser().getId());
            throw new ProductNotExistException(ErrorMessage.PRODUCT_NOT_EXIST);  //400
        }

    }

    public void updateProduct(Long productId,Product product) {
        // if product not exist, return 400 bad request
        Product ProductToBeUpdated = getProduct(productId);
        // auth id is different from product owner_id  return unauthorized 403
        checkAuth(ProductToBeUpdated);
        // check the product quantity  400 bad request or 204 no content
        checkProduct(product);
        // check if sku is repeated   400 bad request
        List<Product> existingProducts = productDao.selectBySku(product.getSku());
        //如果你请求体里面的sku没有改的话，那么！ProductToBeUpdated.getSku().equals(product.getSku是假的。不走if.
        //如果你改了的话，第一个条件就是真，我就找是不是有existingproduct，如果有的话，就不空，那么第二个条件就是真的，第三个也是真的，
        //那么我们就返回重复了
        if(!ProductToBeUpdated.getSku().equals(product.getSku())
                && existingProducts!=null && existingProducts.size()>0){
            throw new CreateOrUpdateProductException(ErrorMessage.REPEAT_SKU);
        }
        //quantity must be integer
        if(!product.getQuantity().getClass().equals(Long.class)){
            throw new RuntimeException("Quantity must be Integer");
        }
        //prevent people to change the time
        if( product.getDateAdded()!=null|| product.getDateLastUpdated()!=null)

        {
            throw new RuntimeException("not support update");
        }
        // update the product
        //I do not need to ignore "dateAdded"因为他不会变。update方法只会改变datelastupdated的值
        //但是如果有人在请求体里面修改是可以成功的，我们要不做个异常类。要不ignore
        BeanUtils.copyProperties(product, ProductToBeUpdated, "id","ownerUserId","dateAdded");
        productDao.updateById(ProductToBeUpdated);
    }




    public void deleteProduct(Long productId) {
        // if product not exist, return 400 bad request
        Product ProductToBeDeleted = getProduct(productId);
        // auth id is different from product owner id  return unauthorized 403
        checkAuth(ProductToBeDeleted);
        // delete the product
        productDao.deleteById(productId);
    }


    private void checkProduct(Product product){
        // check if all field exists  no content 204

        if( StringUtils.isEmpty(product.getName())
                || StringUtils.isEmpty(product.getDescription())
                || StringUtils.isEmpty(product.getManufacturer())
                || StringUtils.isEmpty(product.getSku())
                || product.getQuantity() ==null){
            log.warn("Missing data for required field");
            throw new NoContentException(ErrorMessage.NO_CONTENT);
        }
        log.info("The product has all the required fields");
        // check is quantity is correct  400
        if( product.getQuantity()< 0){
            log.warn("The product quantity is less than 0");
            throw new CreateOrUpdateProductException(ErrorMessage.PRODUCT_QUANTITY_ERROR);
        }
    }


    private void checkAuth(Product oldProduct){
        // check is quantity is correct  404
        if(!Objects.equals(oldProduct.getOwnerUserId(),(UserPassingLoginAuth.getUser().getId()))){
            log.warn("User {} is trying to the get User {} 's product with ProductId: {} "
                    ,UserPassingLoginAuth.getUser().getId(), oldProduct.getOwnerUserId(), oldProduct.getId());
            throw new ChangeOthersInfoException(ErrorMessage.CHANGE_OTHER_INFORMATION);
        }
    }



       }





