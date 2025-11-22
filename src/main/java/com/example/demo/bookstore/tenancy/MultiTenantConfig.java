/* (C) 2025 */ 

package com.example.demo.bookstore.tenancy;

import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MultiTenantConfig {

    private static final String DEFAULT_TENANT_ID = "default";

    @Autowired
    private TenantDataSourceProperties dataSourceProperties;

    @Bean
    public AbstractRoutingDataSource multiTenantDataSource() {
        MultiTenantDataSource multiTenantDataSource = new MultiTenantDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        for (DataSourceProperties entry : dataSourceProperties.getDatasources()) {
            String tenantId = entry.getName();
            DataSource dataSource = DataSourceBuilder.create()
                    .url(entry.getUrl())
                    .username(entry.getUsername())
                    .password(entry.getPassword())
                    .driverClassName(entry.getDriverClassName())
                    .build();

            targetDataSources.put(tenantId, dataSource);
        }

        multiTenantDataSource.setTargetDataSources(targetDataSources);
        multiTenantDataSource.setDefaultTargetDataSource(targetDataSources.get(DEFAULT_TENANT_ID));

        return multiTenantDataSource;
    }

    // Other configuration beans for EntityManagerFactory and TransactionManager
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, DataSource dataSource) {
        return builder.dataSource(dataSource)
                .packages("com.example.demo.bookstore.entity")
                .persistenceUnit("multiTenant")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
