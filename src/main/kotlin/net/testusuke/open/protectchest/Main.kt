package net.testusuke.open.protectchest

import net.testusuke.open.protectchest.Chest.ChestInformation
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.lang.NullPointerException

/**
 * Created on 2020/06/22
 * Author testusuke
 */
class Main: JavaPlugin() {

    companion object{
        lateinit var plugin:Main
        var enable:Boolean = false
        var prefix:String = "§e[§aProtect§6Chest§e]§f"
    }

    //  Map
    var locationChestMap:MutableMap<Location, ChestInformation> = mutableMapOf()

    //  Item
    val wandItem:ItemStack by lazy {
        val item = ItemStack(Material.STONE_AXE)
        val meta = item.itemMeta
        meta.setDisplayName("")
        val lore = mutableListOf<String>()
        lore.add("左クリック:保護")
        lore.add("右クリック:保護解除")
        meta.lore = lore
        item.itemMeta = meta
        item
    }

    override fun onEnable() {
        plugin = this

        //  Command
        getCommand("pc")?.setExecutor(ChestCommand)
        //  Event
        server.pluginManager.registerEvents(EventListener,this)
        //  Config
        this.saveDefaultConfig()
        enable = try{
            config.getBoolean("mode")
        }catch (e:NullPointerException){
            false
        }


    }

    override fun onDisable() {


    }



}