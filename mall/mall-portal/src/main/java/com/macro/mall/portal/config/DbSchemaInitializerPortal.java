package com.macro.mall.portal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DbSchemaInitializerPortal implements CommandLineRunner {

    private final DataSource dataSource;

    @Autowired
    public DbSchemaInitializerPortal(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("schema-mongo-to-sql.sql"));
            populator.execute(dataSource);
        } catch (Exception e) {
            System.err.println("portal schema init failed: " + e.getMessage());
        }
    }
}
