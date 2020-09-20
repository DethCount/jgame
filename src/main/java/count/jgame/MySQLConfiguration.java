package count.jgame;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@PropertySource({ "classpath:persistence-mysql.properties" })
@EntityScan(basePackages = {"count.jgame.models"})
@EnableJpaRepositories(
    basePackages = {"count.jgame.repositories"}, 
    entityManagerFactoryRef = "mainEntityManager"
)
public class MySQLConfiguration {
   @Autowired
    private Environment env;
    
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean mainEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "count.jgame.models" });
 
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        HashMap<String, Object> properties = new HashMap<>();
        properties
        	.put("hibernate.hbm2ddl.auto", env.getProperty("jgame.mysql.jpa.hibernate.ddl-auto"));
        properties
        	.put("hibernate.dialect", env.getProperty("jgame.mysql.jpa.hibernate.dialect"));
        
        em.setJpaPropertyMap(properties);
 
        return em;
    }
 
    @Primary
    @Bean
    public DataSource dataSource() {
 
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jgame.mysql.datasource.driverClassName"));
        dataSource.setUrl(env.getProperty("jgame.mysql.datasource.url"));
        dataSource.setUsername(env.getProperty("jgame.mysql.datasource.username"));
        dataSource.setPassword(env.getProperty("jgame.mysql.datasource.password"));
 
        return dataSource;
    }
}