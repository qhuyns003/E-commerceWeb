package com.intern.e_commerce.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.e_commerce.dto.request.CartAddProductRequest;
import com.intern.e_commerce.dto.request.CartRemoveProductRequest;
import com.intern.e_commerce.dto.response.CartResponse;
import com.intern.e_commerce.entity.Cart;
import com.intern.e_commerce.entity.Product;
import com.intern.e_commerce.entity.UserEntity;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.mapper.ProductMapper;
import com.intern.e_commerce.repository.ProductRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {

    UserRepositoryInterface userRepository;
    ProductMapper productMapper;
    ProductRepository productRepository;

    public CartResponse getAll() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Cart cart = user.getCart();
        // chua clean
        return CartResponse.builder()
                .products(new HashSet<>(cart.getProducts().stream()
                        .map(productMapper::toProductResponse)
                        .toList()))
                .build();
    }

    public CartResponse addProduct(CartAddProductRequest request) {
        Set<Product> products = new HashSet<>(productRepository.findAllById(request.getProductIds()));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.getCart().getProducts().addAll(products);
        userRepository.save(user);
        return CartResponse.builder()
                .products(new HashSet<>(user.getCart().getProducts().stream()
                        .map(productMapper::toProductResponse)
                        .toList()))
                .build();
    }

    public CartResponse removeProduct(CartRemoveProductRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // thua
        Set<Product> products = new HashSet<>(productRepository.findAllById(request.getProductIds()));
        UserEntity user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.getCart().getProducts().removeAll(products);

        userRepository.save(user);
        return CartResponse.builder()
                .products(new HashSet<>(user.getCart().getProducts().stream()
                        .map(productMapper::toProductResponse)
                        .toList()))
                .build();
    }
}
