/* (C) 2025 */ 

package com.example.demo.bookstore.tenancy;

import java.util.List;
import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("tenancy")
public class TenantDataSourceProperties {

    private List<DataSourceProperties> datasources;
}
