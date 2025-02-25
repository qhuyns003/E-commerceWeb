package com.intern.e_commerce.service;

import com.intern.e_commerce.dto.request.ProductCreateRequest;
import com.intern.e_commerce.dto.response.ProductResponse;
import com.intern.e_commerce.entity.Product;
import com.intern.e_commerce.entity.UserEntity;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.mapper.ProductMapper;
import com.intern.e_commerce.repository.ProductRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepositoryInterface userRepository;
    @Autowired
    private ProductMapper productMapper;


    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Product product = productMapper.toProduct(productCreateRequest);
        product.setUser(userEntity);
        userEntity.getProductList().add(product);
        userRepository.save(userEntity);

        return productMapper.toProductResponse(product);
    }

    public List<ProductResponse> getAllProductsOfUser(String userName) {
        UserEntity userEntity = userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<Product> productList = userEntity.getProductList();
        return productList.stream().map(prod ->productMapper.toProductResponse(prod)).toList();
    }
}
