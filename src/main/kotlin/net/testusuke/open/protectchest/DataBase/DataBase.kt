package net.testusuke.open.protectchest.DataBase

import net.testusuke.open.protectchest.Main.Companion.plugin
import net.testusuke.open.protectchest.Main.Companion.prefix
import net.testusuke.open.protectchest.Permission
import org.bukkit.Bukkit
import java.io.File
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Created on 2020/06/25
 * Author testusuke
 */
class DataBase(private val db: String) {

    //  Connection url
    private val connectionUrl = "jdbc:sqlite"

    //  Connection DataBase
    init {

        //  Class loader
        loadClass()
        //  Create db file
        try {
            val directory = plugin.dataFolder
            if (!directory.exists()) directory.mkdir()
            val file = File(directory, db)
            if (!file.exists()) {
                file.createNewFile()
                plugin.logger.info("create new file. ${file.path}")
            }
        } catch (e: IOException) {
            plugin.logger.info("Error: can not create new file. $db")
        }
        //  TestConnect
        testConnect()
        //  Create Table
        createTable()
    }

    fun getConnection(): Connection? {
        var connection: Connection? = null
        try {
            connection = DriverManager.getConnection("${connectionUrl}:$db")
        } catch (e: Exception) {
            plugin.logger.info("Error: can not get connection.")
        }
        return connection
    }

    private fun loadClass(): Boolean {
        return try {
            Class.forName("org.sqlite.JDBC")
            plugin.logger.info("success load class")
            true
        } catch (e: Exception) {
            plugin.logger.info("Error: can not load class")
            false
        }
    }

    private fun testConnect(): Boolean {
        val connection: Connection? = getConnection()
        return if (connection == null) {
            plugin.logger.info("Error: null connection.")
            false
        } else {
            plugin.logger.info("success connect.")
            true
        }
    }

    private fun createTable() {
        val connection = getConnection()
        if(connection == null){
            plugin.logger.info("can not access db. method: DataBase/createTable")
            return
        }
        try {
            val TABLE_SQL = "CREATE TABLE IF NOT EXISTS chest_info (" +
                    "location   TEXT NOT NULL," +
                    "material  TEXT   NOT NULL," +
                    "author  TEXT NOT NULL," +
                    "date TEXT NOT NULL)"
            val st = connection.createStatement()
            st.execute(TABLE_SQL)
            plugin.logger.info("created table")
            st.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            plugin.logger.info("Error: can not create table")
        }
    }

    //  DB Error
    var dbError: Boolean = false
    fun sendDBError(msg: String) {
        if (!dbError) {
            Bukkit.broadcastMessage("${prefix}§c§lデータベースエラーです。運営に連絡してください。")
            for (player in Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(Permission.ADMIN)) {
                    player.sendMessage("${prefix}§c§lデータベースエラーです。担当の開発者にお伝えください。 ErrorMessage: $msg")
                }
            }
        }
        plugin.logger.info("DataBase Error: $msg")
        dbError = true
    }
}