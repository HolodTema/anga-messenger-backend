package com.terabyte.angamessenger.auth.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val config = HikariConfig()
    config.apply {
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

    // create tables when we start server first time
    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            com.terabyte.angamessenger.common.table.Users,
            com.terabyte.angamessenger.common.table.RefreshTokens,
        )
    }
}
