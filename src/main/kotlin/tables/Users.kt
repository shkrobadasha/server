package tables

import models.User
import org.jetbrains.exposed.sql.*

object Users : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()
    val password = varchar("password", 100)

    override val primaryKey = PrimaryKey(id)

    fun findByUsername(username: String): User? {
        return Users.select { Users.username eq username }
            .map { row ->
                User(
                    username = row[Users.username],
                    password = row[Users.password]
                )
            }
            .singleOrNull()
    }
} 