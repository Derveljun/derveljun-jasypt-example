package com.derveljun.jasyptexample.module.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JasyptDecryptRequestDTO {

    private String decryptionKey;
    private String encryptedText;

}
