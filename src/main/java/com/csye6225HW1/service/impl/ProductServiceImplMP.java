package com.csye6225HW1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csye6225HW1.Exceptions.ProductException.CreateOrUpdateProductException;
import com.csye6225HW1.Exceptions.ProductException.NoContentException;
import com.csye6225HW1.Exceptions.ProductException.ProductNotExistException;
import com.csye6225HW1.Exceptions.UserException.ChangeOthersInfoException;
import com.csye6225HW1.POJO.Product;
import com.csye6225HW1.dao.ProductsDao;
import com.csye6225HW1.service.IProductsService;
import com.csye6225HW1.util.ErrorMessage;
import com.csye6225HW1.util.UserPassingLoginAuth;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

//after extends UsersDao, Users, all method included by IusersService(MybatisPlus provided) will be impl atuomatically.
@Service
@Slf4j
public class ProductServiceImplMP extends ServiceImpl<ProductsDao, Product> implements IProductsService{



   @Autowired
    ProductsDao productDao;


    public Product createProduct(Product product) {
        ProductIsValid(product);
        List<Product> ProductsIsExist =
                productDao.selectBySku(product.getSku());
        if(ProductsIsExist!=null && ProductsIsExist.size()>0){
            throw new CreateOrUpdateProductException(ErrorMessage.REPEAT_SKU);
        }

        product.setOwnerUserId(UserPassingLoginAuth.getUser().getId());

        //product.setDateLastUpdated(String.valueOf(System.currentTimeMillis()));

        productDao.insert(product);
        return product;
    }

    public Product getProduct(Long productId){
        Optional<Product> product = Optional.ofNullable(productDao.selectById(productId));
        if(product.isPresent()){
            return product.get();
        }else{
            throw new ProductNotExistException(ErrorMessage.PRODUCT_NOT_EXIST);  //400
        }

    }

    public void patchProduct(Product product, Long productId) {
        Product ProductToBePatched = getProduct(productId);
        checkAuth(ProductToBePatched);
        //BeanUtils.copyProperties(product, ProductToBePatched, "id","ownerUserId","dateAdded");

        if( product.getId()!=null||product.getDateAdded()!=null|| product.getDateLastUpdated()!=null){
            throw new RuntimeException("not allowed to update/create");
        }
        //even you setUp name="",the name won't be updated.since it is Empty.
        if( !StringUtils.isEmpty(product.getName())){
            ProductToBePatched.setName(product.getName());
        }
        if( !StringUtils.isEmpty(product.getDescription())){
            ProductToBePatched.setDescription(product.getDescription());
        }
        if(!StringUtils.isEmpty(product.getSku())) {
            List<Product> existingProducts = productDao.selectBySku(product.getSku());
            if(!ProductToBePatched.getSku().equals(product.getSku())
                    && existingProducts!=null && existingProducts.size()>0){
                throw new CreateOrUpdateProductException(ErrorMessage.REPEAT_SKU);
            }

            ProductToBePatched.setSku(product.getSku());
        }
        if(!StringUtils.isEmpty(product.getManufacturer())){
            ProductToBePatched.setManufacturer(product.getManufacturer());
        }

        if(!(product.getQuantity() ==null)){
            if(!product.getQuantity().getClass().equals(Long.class)) {
                throw new RuntimeException("Quantity must be Integer");
            }
            if( product.getQuantity()< 0){
                throw new CreateOrUpdateProductException(ErrorMessage.PRODUCT_QUANTITY_ERROR);
            }
            ProductToBePatched.setQuantity(product.getQuantity());
        }

        //不能要这些因为ID和时间已经有了
        //ProductIsValid(ProductToBePatched);
        productDao.updateById(ProductToBePatched);



    }

    public void updateProduct(Product product,Long productId) {
        Product ProductToBeUpdated = getProduct(productId);
        checkAuth(ProductToBeUpdated);
        ProductIsValid(product);
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

        // update the product
        //I do not need to ignore "dateAdded"因为他不会变。update方法只会改变datelastupdated的值
        //但是如果有人在请求体里面修改是可以成功的，我们要不做个异常类。要不ignore
        BeanUtils.copyProperties(product, ProductToBeUpdated, "id","ownerUserId","dateAdded");
        productDao.updateById(ProductToBeUpdated);
    }




    public void deleteProduct(Long productId) {
        Product ProductToBeDeleted = getProduct(productId);
        checkAuth(ProductToBeDeleted);
        productDao.deleteById(productId);
    }


    private void ProductIsValid(Product product){

        if( product.getId()!=null||product.getDateAdded()!=null|| product.getDateLastUpdated()!=null){
            throw new RuntimeException("not allowed to update/create");
        }
        if( StringUtils.isEmpty(product.getName()) || StringUtils.isEmpty(product.getDescription()) || StringUtils.isEmpty(product.getManufacturer()) || StringUtils.isEmpty(product.getSku()) || product.getQuantity() ==null){
            throw new NoContentException(ErrorMessage.NO_CONTENT);
        }
        log.info("The product is ready to create");
        if( product.getQuantity()< 0){
            throw new CreateOrUpdateProductException(ErrorMessage.PRODUCT_QUANTITY_ERROR);
        }
    }


    private void checkAuth(Product ProductToBeAuth){
        // check is quantity is correct  404
        if(!Objects.equals(ProductToBeAuth.getOwnerUserId(),(UserPassingLoginAuth.getUser().getId()))){
            log.warn("User {} is trying to the get User {} 's product with ProductId: {} "
                    ,UserPassingLoginAuth.getUser().getId(), ProductToBeAuth.getOwnerUserId(), ProductToBeAuth.getId());
            throw new ChangeOthersInfoException(ErrorMessage.CHANGE_OTHER_INFORMATION);
        }
    }



}





