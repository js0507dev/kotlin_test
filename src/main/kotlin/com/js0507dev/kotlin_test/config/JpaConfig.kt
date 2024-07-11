package com.js0507dev.kotlin_test.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@EnableJpaRepositories(
    basePackages = ["com.js0507dev.kotlin_test.repository"],
    entityManagerFactoryRef = JpaConfig.ENTITY_MANAGER_FACTORY,
    transactionManagerRef = JpaConfig.TRANSACTION_MANAGER
)
@Configuration
class JpaConfig {
    @Bean(ENTITY_MANAGER_FACTORY)
    fun entityManagerFactory(
        @Qualifier(DataSourceConfig.APPLICATION_DATA_SOURCE) applicationDataSource: DataSource
    ): LocalContainerEntityManagerFactoryBean {
        return LocalContainerEntityManagerFactoryBean().apply {
            this.setDataSource(applicationDataSource)
            this.setJpaVendorAdapter(HibernateJpaVendorAdapter())
            this.setPersistenceUnitName("entityManager")
            this.setPackagesToScan("com.js0507dev.kotlin_test.entity")
        }
    }

    @Bean(TRANSACTION_MANAGER)
    fun platformTransactionManager(
        @Qualifier(ENTITY_MANAGER_FACTORY) entityManagerFactory: LocalContainerEntityManagerFactoryBean
    ): PlatformTransactionManager {
        return JpaTransactionManager().apply {
            this.setEntityManagerFactory(entityManagerFactory.getObject())
        }
    }

    companion object {
        const val ENTITY_MANAGER_FACTORY = "entityManagerFactory"
        const val TRANSACTION_MANAGER = "transactionManager"
    }
}