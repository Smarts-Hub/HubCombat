package dev.smartshub.hubCombat.storage.data.database

import org.jetbrains.exposed.sql.Table

object Players : Table() {
    val id = uuid("id")
    val kills = integer("kills").default(0)
    val deaths = integer("deaths").default(0)
    val hits = integer("hits").default(0)

    override val primaryKey = PrimaryKey(id)
}