package com.js0507dev.kotlin_test.config

import com.js0507dev.kotlin_test.enums.DbType
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

class RoutingDataSource : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any {
        val isTxReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly()
        return DbType.valueOfReadOnly(isTxReadOnly)
    }
}