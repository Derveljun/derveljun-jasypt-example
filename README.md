스프링부트 2.6.2 버전을 기준으로 작성되었습니다.



# 예제 Github 주소



# Jasypt란

Java 의 단방향 암호화와 양방향 암복호화를 돕는 라이브러리입니다.

특히 스프링과 연계되면서 Properties를 암호화하고 Properties를 가져오는 
`@Bean` 활용에 있어서 암호화되어 있는 Properties 값을 자동으로 복호화하는 
편리함을 제공하여 많이 활용하고 있습니다.

이런 찰떡같은 편리성 때문에 스프링부트부터는 Starter 팩으로 제공하고 있습니다.

- 공식 홈페이지: http://www.jasypt.org/
- SpringBoot Starter: https://github.com/ulisesbocchio/jasypt-spring-boot





# Jasypt 설정

## Maven / Gradle 추가

```xml
<!-- https://mvnrepository.com/artifact/com.github.ulisesbocchio/jasypt-spring-boot-starter -->
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>3.0.4</version>
</dependency>
```

```groovy
// https://mvnrepository.com/artifact/com.github.ulisesbocchio/jasypt-spring-boot-starter
implementation 'com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4'
```

- 스프링5 & 부트 2.x 버전에서는 위에 버전을 사용하시면 됩니다.
- 스프링부트가 아니거나 버전 상황에서는 공식 maven repo를 참고하셔서 버전 적용 바랍니다. 
  - https://mvnrepository.com/artifact/org.jasypt
- 참고로 아래 예제는 모두 스프링부트 2.6.2 버전을 기준으로 작성되었습니다.





## Jasypt Spring Config

```java
package com.derveljun.jasyptexample.frame.config.properties;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@EnableEncryptableProperties
//@EncryptablePropertySource(name="EncryptedProperties", value = "classpath:application-{profile_name}.yml")
@Configuration
public class JasyptConfig {

    public static final String JASYPT_STRING_ENCRYPTOR = "jasyptStringEncryptor";

    /*
       복호화 키값(jasypt.encryptor.password)는
       Application 실행 시 외부 Environment 를 통해 주입 받는다.

      # JAR 예
      -Djasypt.encryptor.password=jasypt_password.!
      # WAR 예
      -Djasypt.encryptor.password="jasypt_password.!"
     */
    @Value("${jasypt.encryptor.password}")
    private String encryptKey;

    @Bean(JASYPT_STRING_ENCRYPTOR)
    public StringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(encryptKey);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        log.info("Jasypt Config Completed.");

        return encryptor;
    }

}

```

- `@EnableEncryptableProperties` 를 Config에 사용해야
  `@Value`이외 프로퍼티를 가져오는 상황에서도 암복호화를 사용할 수 있습니다.





## Jasypt Properties 설정 (Properties / YAML)

```yaml
jasypt:
  encryptor:
    bean: jasyptStringEncryptor
    property:
      prefix: ENC(
      suffix: )
```

- 참고
  deafult 설정이 되지 않기 때문에 prefix와 suffix는 꼭 설정 해주셔야 
  `Envrionment Bean` , `@PropertySource` 활용에서 오류가 나지 않습니다.





# Jasypt를 통한 Properties 값 암호화와 복호화

## Properties 암호화

### 암호화 전 Properties / YAML 데이터

```yaml
derveljun:
  datasource:
    derveljun-mysql-db:
      driver-class-name: com.mysql.cj.jdbc.Driver
      jdbc-url: jdbc:mysql://localhost:3306
      username: root
      password: 1234
```



### 암호화 후 Properties / YAML 데이터

```yaml
derveljun:
  datasource:
    derveljun-mysql-db:
      driver-class-name: com.mysql.cj.jdbc.Driver
      jdbc-url: jdbc:mysql://localhost:3306
      username: ENC(qh5AnXwcec5YdYaSng957w==)
      password: ENC(a+PBo+HKU/7aGVl+oN6iQQ==)
```





## Properties 암호화

1. 암복호화에 사용할 암호화 키
   여기서는 `jasypt_password.!`를 기준으로 암복호화를 해보겠습니다.
   참고로 암호화에 사용한 키 또한 사용자가 쉽게 인식하지 못하도록 암호화하기도 합니다만 
   여기선 우선 생략하겠습니다.
2. 상황에 맞는 암복호화 알고리즘을 선택한다. 
   여기서는 `PBEWithMD5AndTripleDES`를 사용했습니다.



암호화 알고리즘과 패스워드를 기본 예제를 응용해서 
Open-API 3.0 으로 암복호화를 할 수 있게 간단한 API를 만들었습니다.

코드를 보시면 아시겠지만 내용 자체는 어렵지 않습니다.

Jasypt를 통한 암호화 프로그램을 통해 만들어서 사용하셔도 되고,
온라인 Jasypt 테스터를 통해 만드셔도 됩니다.

- 온라인 Jasypt 테스트 사이트: https://www.devglan.com/online-tools/jasypt-online-encryption-decryption



```java
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

    // 암호화 예제
    @PostMapping("/test/encrypt")
    public String encryptUsingRequest(@RequestBody JasyptEncryptRequestDTO reqDto) {

        log.info(reqDto.toString());

        StringEncryptor newStringEncryptor = createEncryptor(reqDto.getEncryptionKey());
        String resultText = newStringEncryptor.encrypt(reqDto.getPlainText());
        log.info(resultText);

        return resultText;
    }

    // 복호화 예제
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
        config.setAlgorithm("PBEWithMD5AndTripleDES"); // 권장되는 기본 알고리즘
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        return encryptor;
    }
}
```

알고리즘과 관련한 내용은 아래 사이트를 더 참고해보세요.

http://www.jasypt.org/howtoencryptuserpasswords.html







## Properties 복호화

### 스프링 시작 시, 복호화 키 주입 받기 (JAR, WAR(Tomcat))

위에서 암호화에 사용한 키를 스프링 내부 프로퍼티에 저장하면 암호화를 한 의미가 없겠죠.
대게는 스프링 외부 프로퍼티에서 받거나, 실행 인자값으로 받습니다. 

위에서 사용한 `jasypt_password.!` 값을 인자값으로 넣어보겠습니다.



#### Jar 구동 시, 인자 값 넣기

`-Djasypt.encryptor.password=jasypt_password.!`



#### 외부 톰캣에서 WAR를 구동 시

톰캣을 실행할 때 VM 인자값으로 넣어도 되고, catalina.properties를 이용하는 방법, web.xml을 통해 넣는 방법 등 다양합니다. 
외부 톰캣 외에도 Servlet 기반 WAS는 비슷한 방식이 존재합니다.

- 톰캣 실행 VM 인자값으로 사용할 때
  - `-Djasypt.encryptor.password=jasypt_password.!`

- catalina.properties를 사용할 때
  - `톰캣 폴더/conf/catalina.properties` 
  - `jasypt.encryptor.password=jasypt_password.!`






### 스프링에서 Properties를 사용하는 방법 

Properties 값을 가져오는 방법은 여러 방법이 존재합니다. 
그 중에서 회사내 개발자가 작성한 코드를 기반으로 
자주 사용한 방식을 추려 보았습니다만 **사실 기존 사용법과 다르지 않습니다.**

- **@Value 사용** 예시

  - 기존 @Value 사용법과 동일하다.
  - `@Value("${derveljun.datasource.derveljun-mysql-db.username}")`

  

- **Environment Bean**

  - 기존 `Environment Bean`을 사용하는 방법과 다르지 않습니다.

  - ```java
    @Autowired
    private Environment environment;
    ```
    
    
  
- **@ConfigurationProperties**

  - 기존 @ConfigurationProperties를 그대로 사용합니다.
  
  - ```java
    @ConfigurationProperties(prefix = "derveljun.datasource.derveljun-mysql-db")
    public DataSource dataSource() {
    	log.info("{} created.", DATASOURCE_NAME);
    	return new HikariDataSource();
    }
    ```







# Jasypt를 사용하면서 본 이슈 / 트러블 슈팅

- `@Value` 로 가져올 땐 잘 나오는 데, `@ConfigurationProperties`와 `Environment Bean`과 같이 주입 받아 사용할 때 복호화가 안되는 문제

  - Jasypt application 설정 시, `prefix`와 `suffix`가 지정 되어 있지 않을 때 문제가 발생함.

  - 아래 코드를 추가해야 정상적으로 Decryption이 됩니다.

  - ```yaml
    prefix: ENC(
    suffix: )
    ```

  

- 스프링부트 구동 시 에러 발생

  - 실행인자값으로 받은 패스워드가 맞지 않아 복호화가 진행되지 않을 시 발생합니다.

  - ```java
    nested exception is com.ulisesbocchio.jasyptspringboot.exception.DecryptionException: 
    	Unable to decrypt property: ENC(qh5AnXwcec5YdYaSng957w==) resolved to: ENC(qh5AnXwcec5YdYaSng957w==). 
    Decryption of Properties failed,  make sure encryption/decryption passwords match
    ```







# 예제 Github 주소







# 참고자료

https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/env/EnvironmentPostProcessor.html

https://docs.spring.io/spring-boot/docs/1.5.x-SNAPSHOT/reference/htmlsingle/#howto-customize-the-environment-or-application-context

https://blog.kingbbode.com/39

http://www.jasypt.org/howtoencryptuserpasswords.html

https://mvnrepository.com/artifact/org.jasypt

https://github.com/ulisesbocchio/jasypt-spring-boot