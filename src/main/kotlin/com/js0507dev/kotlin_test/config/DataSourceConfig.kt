package com.js0507dev.kotlin_test.config

import com.js0507dev.kotlin_test.enums.DbType
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import javax.sql.DataSource

@Configuration
class DataSourceConfig {
    @Bean(MASTER_DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    fun masterDataSource(): DataSource {
        return DataSourceBuilder
            .create()
            .type(
                HikariDataSource::class.java
            )
            .build()
    }

    @Bean(SLAVE_DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.replica.hikari")
    fun slaveDataSource(): DataSource {
        return DataSourceBuilder
            .create()
            .type(HikariDataSource::class.java)
            .build()
    }

    @Bean(ROUTING_DATA_SOURCE)
    fun routingDataSource(
        @Qualifier(MASTER_DATA_SOURCE) masterDataSource: DataSource,
        @Qualifier(SLAVE_DATA_SOURCE) slaveDataSource: DataSource
    ): DataSource {
        val dataSourceMap = mapOf(
            DbType.MASTER to masterDataSource,
            DbType.REPLICA to slaveDataSource
        )

        return RoutingDataSource().apply {
            this.setTargetDataSources(dataSourceMap.toMap())
            this.setDefaultTargetDataSource(masterDataSource)
        }
    }

    @Primary
    @Bean(APPLICATION_DATA_SOURCE)
    fun dataSource(
        @Qualifier(ROUTING_DATA_SOURCE) routingDataSource: DataSource
    ): DataSource {
        return LazyConnectionDataSourceProxy(routingDataSource)
    }

    companion object {
        const val MASTER_DATA_SOURCE = "masterDataSource"
        const val SLAVE_DATA_SOURCE = "slaveDataSource"
        const val ROUTING_DATA_SOURCE = "routingDataSource"
        const val APPLICATION_DATA_SOURCE = "applicationDataSource"
    }
}