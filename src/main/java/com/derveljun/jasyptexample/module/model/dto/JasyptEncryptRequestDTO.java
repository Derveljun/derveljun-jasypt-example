package com.derveljun.jasyptexample.module.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JasyptEncryptRequestDTO {

    private String encryptionKey;
    private String plainText;

}
