package com.js0507dev.kotlin_test.enums

enum class DbType (private val readOnly: Boolean) {
    MASTER(false), REPLICA(true);

    companion object {
        fun valueOfReadOnly(isReadOnly: Boolean): DbType {
            return entries.find { it.readOnly == isReadOnly }
                ?: throw RuntimeException("Not Found DB(${entries.joinToString { it.name }}) type")
        }
    }
}