package net.testusuke.open.protectchest.Chest

import net.testusuke.open.protectchest.Main.Companion.plugin
import org.bukkit.Location
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

/**
 * Created on 2020/06/22
 * Author testusuke
 */
object ChestControl {

    //  Map
    var locationChestMap:MutableMap<Location, ChestInformation> = mutableMapOf()

    fun protectChest(location: Location,inventory: Inventory,player: Player){

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