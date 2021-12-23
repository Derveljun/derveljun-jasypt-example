package com.derveljun.jasyptexample.module.controller;

import com.derveljun.jasyptexample.module.model.dto.JasyptDecryptRequestDTO;
import com.derveljun.jasyptexample.module.model.dto.JasyptEncryptRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class JasyptExampleRestController {

    @PostMapping("/test/encrypt")
    public String encryptUsingRequest(@RequestBody JasyptEncryptRequestDTO reqDto) {

        log.info(reqDto.toString());

        StringEncryptor newStringEncryptor = createEncryptor(reqDto.getEncryptionKey());
        String resultText = newStringEncryptor.encrypt(reqDto.getPlainText());
        log.info(resultText);

        return resultText;
    }

    @PostMapping("/test/decrypt")
    public String decryptUsingRequest(@RequestBody JasyptDecryptRequestDTO reqDto) {

        log.info(reqDto.toString());

        StringEncryptor newStringEncryptor = createEncryptor(reqDto.getDecryptionKey());
        String resultText = newStringEncryptor.decrypt(reqDto.getEncryptedText());
        log.info(resultText);

        return resultText;
    }

    private StringEncryptor createEncryptor(String password){

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndTripleDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        return encryptor;
    }

}
