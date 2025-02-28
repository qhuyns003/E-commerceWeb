package com.intern.e_commerce.service;

import com.intern.e_commerce.controller.User;
import com.intern.e_commerce.dto.request.ProductCreateRequest;
import com.intern.e_commerce.dto.response.ProductResponse;
import com.intern.e_commerce.entity.Product;
import com.intern.e_commerce.entity.ProductImage;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepositoryInterface userRepository;
    @Autowired
    private ProductMapper productMapper;

    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) throws IOException {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        for(MultipartFile image : productCreateRequest.getImages()){
            Path file = CURRENT_FOLDER.resolve(staticPath)
                    .resolve(imagePath).resolve(image.getOriginalFilename());
            try (OutputStream os = Files.newOutputStream(file)) {
                os.write(image.getBytes());
            }
        }

        Product product = productMapper.toProduct(productCreateRequest);
        product.setImages(new ArrayList<>());
        for(MultipartFile image : productCreateRequest.getImages()){
            ProductImage productImage = ProductImage.builder()
                    .url(imagePath.resolve(image.getOriginalFilename()).toString())
                    .build();
            product.getImages().add(productImage);
        }
        ProductResponse productResponse = productMapper.toProductResponse(productRepository.save(product));
        productResponse.setImages(product.getImages().stream().map(multiPart -> multiPart.getUrl()).toList());
        return productResponse;

    }

}
