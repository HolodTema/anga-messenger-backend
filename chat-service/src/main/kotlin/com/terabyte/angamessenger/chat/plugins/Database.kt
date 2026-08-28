package com.terabyte.angamessenger.chat.plugins

import com.terabyte.angamessenger.common.table.Messages
import com.terabyte.angamessenger.common.table.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val config =
        HikariConfig().apply {
            jdbcUrl = environment.config.property("database.url").getString()
            driverClassName = environment.config.property("database.driver").getString()
            username = environment.config.property("database.user").getString()
            password = environment.config.property("database.password").getString()
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_READ_COMMITTED"
        }

    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            Messages,
            Users,
        )
    }
}
