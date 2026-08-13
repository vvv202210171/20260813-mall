package com.macro.mall.common.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import javax.sql.DataSource;

@Component
public class DbSchemaInitializer implements CommandLineRunner {

    private final DataSource dataSource;

    @Autowired
    public DbSchemaInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("schema-web-log.sql"));
            populator.execute(dataSource);
        } catch (Exception e) {
            // 如果创建表失败，不要阻止应用启动
            System.err.println("web_log schema init failed: " + e.getMessage());
        }
    }
}
