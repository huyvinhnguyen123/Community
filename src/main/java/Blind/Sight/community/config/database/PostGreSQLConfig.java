package Blind.Sight.community.config.database;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = "writeEntityManagerFactory",
        transactionManagerRef = "writeTransactionManager",
        basePackages = {"Blind.Sight.community.domain.repository.postgresql"}
)
public class PostGreSQLConfig {
    @Value("${postgresql.datasource.url}")
    private String url;
    @Value("${postgresql.datasource.username}")
    private String username;
    @Value("${postgresql.datasource.password}")
    private String password;
    @Value("${postgresql.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${postgresql.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    @Primary
    @Bean(name = "writeDataSource")
    @ConfigurationProperties(prefix="postgresql.datasource", ignoreInvalidFields=true)
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    @Primary
    @Bean(name = "writeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder,
                                                                            @Qualifier("writeDataSource") DataSource dataSource) {
        HashMap<String, Object> props = new HashMap<>();
        props.put("hibernate.dialect", hibernateDialect);

        return builder
                .dataSource(dataSource)
                .properties(props)
                .packages("Blind.Sight.community.domain")
                .persistenceUnit("bs_community")
                .build();
    }

    @Primary
    @Bean(name = "writeTransactionManager")
    @ConfigurationProperties("spring.jpa")
    public PlatformTransactionManager transactionManager(
            @Qualifier("writeEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
