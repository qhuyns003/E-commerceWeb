package com.intern.e_commerce.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.intern.e_commerce.dto.request.ProductCreateRequest;
import com.intern.e_commerce.dto.request.ProductUpdateRequest;
import com.intern.e_commerce.dto.response.ProductResponse;
import com.intern.e_commerce.entity.Product;
import com.intern.e_commerce.entity.ProductImage;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.mapper.ProductMapper;
import com.intern.e_commerce.repository.CategoryRepository;
import com.intern.e_commerce.repository.ProductRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepositoryInterface userRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) throws IOException {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        for (MultipartFile image : productCreateRequest.getImages()) {
            Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(image.getOriginalFilename());
            try (OutputStream os = Files.newOutputStream(file)) {
                os.write(image.getBytes());
            }
        }

        Product product = productMapper.toProduct(productCreateRequest);
        product.setImages(new ArrayList<>());
        for (MultipartFile image : productCreateRequest.getImages()) {
            ProductImage productImage = ProductImage.builder()
                    .url(imagePath.resolve(image.getOriginalFilename()).toString())
                    .build();
            productImage.setProduct(product);
            product.getImages().add(productImage);
        }
        product.setCategory(categoryRepository
                .findById(productCreateRequest.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
        ProductResponse productResponse = productMapper.toProductResponse(productRepository.save(product));
        productResponse.setImages(product.getImages().stream()
                .map(multiPart -> multiPart.getUrl())
                .toList());
        productResponse.setCategory(categoryRepository
                .findById(productCreateRequest.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
                .getName());
        return productResponse;
    }

    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        //        List<ProductResponse> productResponses = products.stream()
        //                .map(product -> productMapper.toProductResponse(product))
        //                .collect(Collectors.toList());
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            ProductResponse productResponse = productMapper.toProductResponse(product);
            List<String> productImages =
                    product.getImages().stream().map(ProductImage::getUrl).collect(Collectors.toList());
            productResponse.setImages(productImages);
            productResponses.add(productResponse);
        }
        ;
        return productResponses;
    }

    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) throws IOException {
        Product product = productRepository
                .findById(id.intValue())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productMapper.updateProduct(product, request);
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        for (MultipartFile image : request.getImages()) {
            Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(image.getOriginalFilename());
            try (OutputStream os = Files.newOutputStream(file)) {
                os.write(image.getBytes());
            }
        }
        product.getImages().clear();
        for (MultipartFile image : request.getImages()) {
            ProductImage productImage = ProductImage.builder()
                    .url(imagePath.resolve(image.getOriginalFilename()).toString())
                    .build();
            product.getImages().add(productImage);
        }

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id.intValue());
    }

    public List<ProductResponse> findProductByName(String keyword) {
        List<Product> products = productRepository.findProductByName(keyword);
        if (products.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        List<ProductResponse> productResponses = products.stream()
                .map(product -> productMapper.toProductResponse(product))
                .collect(Collectors.toList());
        return productResponses;
    }

    public List<ProductResponse> findProductByCategoryId(Long id) {
        List<Product> products = productRepository.findProductByCategoryId(id);
        List<ProductResponse> productResponses = products.stream()
                .map(product -> productMapper.toProductResponse(product))
                .collect(Collectors.toList());
        return productResponses;
    }
}
