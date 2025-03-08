package com.intern.e_commerce.controller;

import com.intern.e_commerce.dto.MailStructure;
import com.intern.e_commerce.service.MailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailController {

    MailService mailService;

    @PostMapping("/send/{mail}")
    public String sendMail(@PathVariable String mail, @RequestBody MailStructure mailStructure) {
        mailService.sendMail(mail, mailStructure);
        return "Successfully sent mail !!";
    }

}
