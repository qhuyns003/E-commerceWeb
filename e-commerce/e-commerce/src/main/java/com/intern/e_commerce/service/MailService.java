package com.intern.e_commerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.intern.e_commerce.dto.request.MailStructure;
import com.intern.e_commerce.entity.Product;
import com.intern.e_commerce.entity.UserEntity;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.repository.ProductRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailService {
    JavaMailSender mailSender;
    ProductRepository productRepository;
    UserRepositoryInterface userRepository;

    @Value("${spring.mail.username}")
    @NonFinal
    String fromMail;

    public void sendMail(String mail, MailStructure mailStructure) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(mailStructure.getSubject());
        simpleMailMessage.setText(mailStructure.getMessage());
        simpleMailMessage.setTo(mail);
        mailSender.send(simpleMailMessage);
    }

    public void sendToCustomer(Integer id) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject("Ban da dat thanh cong don hang " + product.getName());
        simpleMailMessage.setText(
                "Cam on ban da tin tuong va su dung san pham ben shop, shop xin tang ban voucher giam gia 5% cho lan sau");
        simpleMailMessage.setTo(user.getEmail());
        mailSender.send(simpleMailMessage);
    }
}
