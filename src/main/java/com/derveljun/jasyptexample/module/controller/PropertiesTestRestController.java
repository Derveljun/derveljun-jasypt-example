package com.derveljun.jasyptexample.module.controller;

import com.derveljun.jasyptexample.module.dao.mapper.JasyptExampleDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PropertiesTestRestController {

    @Value("${derveljun.datasource.derveljun-mysql-db.username}")
    private String username;

    @Value("${derveljun.datasource.derveljun-mysql-db.password}")
    private String password;

    private final Environment environment;

    private final JasyptExampleDAO dao;

    @GetMapping("/test/values/username")
    public String getUsernameFromValueAnnotation() {
        return username;
    }

    @GetMapping("/test/values/password")
    public String getPasswordFromValueAnnotation() {
        return password;
    }

    @GetMapping("/test/search/properties/{key}")
    public String searchPropertiesFromEnvironment( @PathVariable String key ) {
        log.info("Search Properties Key is {}", key);
        String propertiesValue = environment.getProperty(key);
        log.info("Search Properties Value is {}", propertiesValue);
        return propertiesValue;
    }

    @GetMapping("/test/datasource/test")
    public String datasourceConnectionTest() {

        String result = dao.selectTest().orElseGet(String::new);

        log.info("Search Properties Value is {}", result);
        return result;
    }
}
