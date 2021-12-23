package com.derveljun.jasyptexample.module.dao.mapper;

import com.derveljun.jasyptexample.frame.config.database.DerveljunMysqlDataSourceConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JasyptExampleDAO {

    @Qualifier(DerveljunMysqlDataSourceConfig.SQL_SESSION_TEMPLATE_NAME)
    @Autowired
    private final SqlSessionTemplate derveljunMysqlDb;

    public Optional<String> selectTest(){
        return Optional.ofNullable(derveljunMysqlDb.selectOne("selectTest"));
    }

}
