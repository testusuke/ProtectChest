package net.testusuke.open.protectchest.Chest

import net.testusuke.open.protectchest.Main.Companion.plugin
import net.testusuke.open.protectchest.Main.Companion.prefix
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.scheduler.BukkitRunnable
import java.sql.Connection
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * Created on 2020/06/22
 * Author testusuke
 */
object ChestControl {


    //  Map
    var locationChestMap = ConcurrentHashMap<Location,ChestInformation>()

    fun loadChestInformation(){
        object : BukkitRunnable(){
            override fun run() {
                //  Logger
                plugin.logger.info("start load chest information...")
                //  map clear
                locationChestMap.clear()
                //  DB
                val connection = plugin.db.getConnection()
                if(connection == null){
                    plugin.db.sendDBError("can not access db. method: ChestControl/loadChestInformation()")
                    return
                }
                try {
                    val sql = "SELECT * FROM chest_info"
                    val statement = connection.createStatement()
                    val resultSet = statement.executeQuery(sql)
                    if(!resultSet.next()){
                        plugin.logger.info("no data.")
                        resultSet.close()
                        statement.close()
                        connection.close()
                        return
                    }
                    //  loop
                    var count = 0
                    while(resultSet.next()){
                        val loc = resultSet.getString("location").toString().split(",")
                        val materialName = resultSet.getString("material").toString()
                        val author = resultSet.getString("author").toString()
                        val date = resultSet.getString("date").toString()
                        //  string to any object
                        val location = Location(Bukkit.getServer().getWorld(loc[0]),loc[1].toInt().toDouble(),loc[2].toInt().toDouble(),loc[3].toInt().toDouble())
                        val material = Material.getMaterial(materialName) ?: continue
                        //  class
                        val chestInformation = ChestInformation(location,material,author,date)
                        locationChestMap[location] = chestInformation
                        count++
                    }
                    plugin.logger.info("loaded $count chests information.")

                    //  DB
                    resultSet.close()
                    statement.close()
                    connection.close()
                }catch (e:SQLException){
                    e.printStackTrace()
                    plugin.db.sendDBError("can not load chest information. method: ChestControl/loadChestInformation()")
                }
            }
        }.runTask(plugin)

    }

    fun protectChest(location: Location,material: Material,player: Player){
        val simpleDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val date = simpleDate.format(Date())
        val chestInfo = ChestInformation(location,material,player.name,date)
        object : BukkitRunnable(){
            override fun run() {
                //  DB
                val connection = plugin.db.getConnection()
                if(connection == null){
                    plugin.db.sendDBError("can not access db. method: ChestControl/protectChest")
                    cancel()
                    return
                }
                try {
                    val loc = "${location.world.uid},${location.x.toInt()},${location.y.toInt()},${location.z.toInt()}"
                    val sql = "INSERT INTO chest_info VALUES ('${loc}','${material.name}','${player.name}','${date}');"
                    val statement = connection.createStatement()
                    if(statement.execute(sql)){
                        //  Map
                        locationChestMap[location] = chestInfo
                        plugin.logger.info("loaded chest information.")
                    }else{
                        plugin.logger.info("could not load chest information.")
                    }
                    statement.close()
                    connection.close()

                }catch (e:SQLException){
                    e.printStackTrace()
                    plugin.logger.info("sql exception. method: ChestControl/protectChest")
                    player.sendMessage("${prefix}§cエラーが発生しました。")
                }
            }
        }.runTask(plugin)
    }

    fun unprotectChest(location: Location,player: Player){
        if(!locationChestMap.containsKey(location)){
            player.sendMessage("${prefix}§c保護情報が見つかりませんでした。")
            return
        }
        // task
        object : BukkitRunnable(){
            override fun run() {
                val connection = plugin.db.getConnection()
                if(connection == null){
                    plugin.db.sendDBError("can not access db. method: ChestControl/unprotectChest")
                    cancel()
                    return
                }
                try {
                    val loc = "${location.world.uid},${location.x.toInt()},${location.y.toInt()},${location.z.toInt()}"
                    val sql = "DELETE FROM chest_info WHERE location='$loc';"
                    val statement = connection.createStatement()
                    statement.executeUpdate(sql)
                    statement.close()
                    connection.close()
                    //  Map
                    locationChestMap.remove(location)
                    player.sendMessage("${prefix}§a解除しました。")
                }catch (e:SQLException){
                    e.printStackTrace()
                    plugin.db.sendDBError("sql exception. method: ChestControl/unprotectChest")
                    player.sendMessage("${prefix}§cエラーが発生しました。")
                }
            }
        }.runTask(plugin)

    }

    fun isProtected(location: Location):Boolean{
        return locationChestMap.containsKey(location)
    }

    fun getInformation(location: Location):ChestInformation? {
        return locationChestMap[location]
    }

}