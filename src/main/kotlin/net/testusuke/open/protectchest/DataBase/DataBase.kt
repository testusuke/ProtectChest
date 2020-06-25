package net.testusuke.open.protectchest.DataBase

import net.testusuke.open.protectchest.Main.Companion.plugin
import java.io.File
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Created on 2020/06/25
 * Author testusuke
 */
class DataBase(private val db:String) {

    //  Connection url
    private val connectionUrl = "jdbc:sqlite"
    //  Connection DataBase
    init{

        //  Class loader
        loadClass()
        //  Create db file
        try {
            val directory = plugin.dataFolder
            if (!directory.exists())directory.mkdir()
            val file = File(directory,db)
            if(!file.exists()){
                file.createNewFile()
                println("create new file. ${file.path}")
            }
        }catch (e: IOException){
            println("Error: can not create new file. $db")
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
        }catch (e:Exception){
            println("Error: can not get connection.")
        }
        return connection
    }

    private fun loadClass():Boolean {
        return try{
            Class.forName("org.sqlite.JDBC")
            println("success load class")
            true
        }catch (e:Exception){
            println("Error: can not load class")
            false
        }
    }

    private fun testConnect():Boolean{
        val connection: Connection? = getConnection()
        return if(connection == null){
            println("Error: null connection.")
            false
        }else{
            println("success connect.")
            true
        }
    }

    private val TABLE_SQL = "CREATE TABLE IF NOT EXISTS chest_info (" +
            "x  INT   NOT NULL," +
            "y  INT   NOT NULL," +
            "z  INT   NOT NULL," +
            "name  TEXT NOT NULL," +
            "material  TEXT   NOT NULL," +
            "author  TEXT NOT NULL," +
            "date TEXT NOT NULL)"
    private fun createTable(){
        val connection= getConnection() ?: return
        try {
            val st = connection.createStatement()
            st.execute(TABLE_SQL)
            println("created table")
            st.close()
            connection.close()
        }catch (e: SQLException){
            e.printStackTrace()
            println("Error: can not create table")
        }
    }
}