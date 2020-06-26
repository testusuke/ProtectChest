package net.testusuke.open.protectchest.Chest

import net.testusuke.open.protectchest.Main.Companion.plugin
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.sql.Connection

/**
 * Created on 2020/06/22
 * Author testusuke
 */
object ChestControl {


    //  Map
    var locationChestMap:MutableMap<Location, ChestInformation> = mutableMapOf()

    fun loadChestInformation(){

    }
    fun saveChestInformation(){
        //  DB
        val connection:Connection? = plugin.db.getConnection()
        if(connection == null){
            plugin.db.sendDBError("Can not get connection.method: saveChestInformation")
            return
        }
        //  AutoCommit
        connection.autoCommit = false

        /**
         * ProtectType.REGISTER_PROTECT ->  登録
         * ProtectType.UNREGISTER -> DBから削除
         */
        for(chestInfo in locationChestMap.values){
            val loc = "${chestInfo.location.world.name},${chestInfo.location.x},${chestInfo.location.y},${chestInfo.location.z}"
            when(chestInfo.protectType){
                ProtectType.UNPRORTECT -> {
                    val sql = "DELETE FROM chest_info WHERE location='${loc}' and name='${chestInfo.name}';"
                    val st = connection.createStatement()
                    st.executeUpdate(sql)
                }
                ProtectType.REGISTER_PROTECT -> {
                    val sql = "INSERT INTO chest_info VALUES ('${loc}','${chestInfo.name}','${chestInfo.material.name}','${chestInfo.author}','${chestInfo.date}',);"
                    val st = connection.createStatement()
                    st.executeUpdate(sql)
                }
                ProtectType.PROTECT -> {}
            }
        }
    }
    fun protectChest(location: Location,inventory: Inventory,material: Material,player: Player){
        val date
        val chestInfo = ChestInformation(location,,material,player.name,date,ProtectType.RESET_REGISTER)

    }

    fun unprotectChest(location: Location,player: Player){

    }

    fun isProtected(location: Location):Boolean{
        return locationChestMap.containsKey(location)
    }

    fun getInformation(location: Location):ChestInformation? {
        return locationChestMap[location]
    }

}