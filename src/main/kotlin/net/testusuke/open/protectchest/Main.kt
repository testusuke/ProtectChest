package net.testusuke.open.protectchest

import net.testusuke.open.protectchest.Chest.ChestControl
import net.testusuke.open.protectchest.DataBase.DataBase
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.lang.NullPointerException

/**
 * Created on 2020/06/22
 * Author testusuke
 */
class Main : JavaPlugin() {

    companion object {
        lateinit var plugin: Main
        var enable: Boolean = false
        var prefix: String = "§e[§aProtect§6Chest§e]§f"
    }

    lateinit var db: DataBase

    //  Item
    val wandItem: ItemStack by lazy {
        val item = ItemStack(Material.STONE_AXE)
        val meta = item.itemMeta
        meta.setDisplayName("")
        val lore = mutableListOf<String>()
        lore.add("右クリック:保護")
        lore.add("左クリック:保護解除")
        meta.lore = lore
        item.itemMeta = meta
        item
    }

    //  Chest Material List
    val chestMaterialList: MutableList<Material> by lazy {
        val list = mutableListOf<Material>()
        list.add(Material.CHEST)
        list.add(Material.TRAPPED_CHEST)
        list.add(Material.BARREL)
        list.add(Material.SHULKER_BOX)
        list.add(Material.BLACK_SHULKER_BOX)
        list.add(Material.BLUE_SHULKER_BOX)
        list.add(Material.BROWN_SHULKER_BOX)
        list.add(Material.CYAN_SHULKER_BOX)
        list.add(Material.GRAY_SHULKER_BOX)
        list.add(Material.GREEN_SHULKER_BOX)
        list.add(Material.LIGHT_BLUE_SHULKER_BOX)
        list.add(Material.LIGHT_GRAY_SHULKER_BOX)
        list.add(Material.LIME_SHULKER_BOX)
        list.add(Material.MAGENTA_SHULKER_BOX)
        list.add(Material.ORANGE_SHULKER_BOX)
        list.add(Material.PINK_SHULKER_BOX)
        list.add(Material.PURPLE_SHULKER_BOX)
        list.add(Material.RED_SHULKER_BOX)
        list.add(Material.WHITE_SHULKER_BOX)
        list
    }

    override fun onEnable() {
        plugin = this
        //  Logger
        val msg = """
            ==========================================
            name: ProtectChest  author: testusuke
            github: http://github.com/testusuke
            ==========================================
        """.trimIndent()
        logger.info(msg)
        //  Command
        getCommand("pc")?.setExecutor(ChestCommand)
        //  Event
        server.pluginManager.registerEvents(EventListener, this)
        //  Config
        this.saveDefaultConfig()
        enable = try {
            config.getBoolean("mode")
        } catch (e: NullPointerException) {
            false
        }
        //  sqlite
        db = DataBase("sqlite.db")
        ChestControl.loadChestInformation()
    }

    override fun onDisable() {

        //  Config
        config.set("mode", enable)
        saveConfig()

    }

}