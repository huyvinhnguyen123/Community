package Blind.Sight.community.config.database;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "readEntityManagerFactory",
        transactionManagerRef = "readTransactionManager",
        basePackages = {"Blind.Sight.community.domain.repository.mysql"}
)
public class MySQLConfig {
    @Value("${mysql.datasource.url}")
    private String url;
    @Value("${mysql.datasource.username}")
    private String username;
    @Value("${mysql.datasource.password}")
    private String password;
    @Value("${mysql.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${mysql.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    @Bean(name = "readDataSource")
    @ConfigurationProperties(prefix="mysql.datasource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    @Bean(name = "readEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder,
                                                                            @Qualifier("readDataSource") DataSource dataSource) {
        HashMap<String, Object> props = new HashMap<>();
        props.put("hibernate.dialect", hibernateDialect);

        return builder
                .dataSource(dataSource)
                .properties(props)
                .packages("Blind.Sight.community.domain")
                .persistenceUnit("bs_community")
                .build();
    }

    @Bean(name = "readTransactionManager")
    @ConfigurationProperties("spring.jpa")
    public PlatformTransactionManager transactionManager(
            @Qualifier("readEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}