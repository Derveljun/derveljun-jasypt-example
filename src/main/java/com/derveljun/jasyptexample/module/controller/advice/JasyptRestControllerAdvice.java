package com.derveljun.jasyptexample.module.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Slf4j
@RestControllerAdvice
public class JasyptRestControllerAdvice {

    @ExceptionHandler({EncryptionOperationNotPossibleException.class})
    public ResponseEntity<?> encryptionOperationNotPossibleExceptionHandler(
            EncryptionOperationNotPossibleException e, Locale locale) {

        String resultText = "Something wrong about 'en/decryption key' or 'target text'. Please check your input parameter.";
        log.error(resultText);
        log.error("Jasypt Exception : {}", ExceptionUtils.getStackTrace(e));

        ResponseEntity<String> responseEntity = ResponseEntity.ok(resultText);

        return ResponseEntity.ok(resultText);
    }

}
