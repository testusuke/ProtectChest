package net.testusuke.open.protectchest

import net.testusuke.open.protectchest.Chest.ChestControl
import net.testusuke.open.protectchest.Main.Companion.enable
import net.testusuke.open.protectchest.Main.Companion.plugin
import net.testusuke.open.protectchest.Main.Companion.prefix
import org.bukkit.Material
import org.bukkit.block.Barrel
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.block.ShulkerBox
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory

/**
 * Created on 2020/06/22
 * Author testusuke
 */
object EventListener : Listener {

    @EventHandler
    fun onClickBlock(e: PlayerInteractEvent) {
        if (!enable) return
        val player = e.player
        if (!player.hasPermission(Permission.ADMIN)) return
        //  左
        if (e.action == Action.LEFT_CLICK_BLOCK) {
            //  Item
            val item = player.inventory.itemInMainHand
            if (item.isSimilar(plugin.wandItem)) {
                //  Block
                val block = e.clickedBlock ?: return
                if (plugin.chestMaterialList.contains(block.type)) {
                    if (ChestControl.isProtected(block.location)) {
                        val msg = """
                            ${prefix}§cすでに保護されているブロックです。
                            §a<information>
                            §eauthor: ${ChestControl.getInformation(block.location)?.author}
                            §edate: ${ChestControl.getInformation(block.location)?.date}
                        """.trimIndent()
                        player.sendMessage(msg)
                        return
                    }
                    val material = block.type
                    ChestControl.protectChest(block.location, material, player)
                }
            }
        }
        //  右
        if (e.action == Action.RIGHT_CLICK_BLOCK) {
            //  Item
            val item = player.inventory.itemInMainHand
            if (item.isSimilar(plugin.wandItem)) {
                //  Block
                val block = e.clickedBlock ?: return
                if (plugin.chestMaterialList.contains(block.type)) {
                    if (!ChestControl.isProtected(block.location)) {
                        player.sendMessage("${prefix}§c保護されていません")
                        return
                    }
                    ChestControl.unprotectChest(block.location, player)
                }
            }
        }
    }

    /*
    private fun getInventoryFromBlock(block: Block): Inventory {
        return when (block.type) {
            Material.CHEST -> {
                val chest = block.state as Chest
                chest.inventory
            }
            Material.TRAPPED_CHEST -> {
                val chest = block.state as Chest
                chest.inventory
            }
            Material.BARREL -> {
                val chest = block.state as Barrel
                chest.inventory
            }
            else -> {
                val shulker = block.state as ShulkerBox
                shulker.inventory
            }
        }
    }*/


    //  Break
    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        if (!enable) return
        val player = e.player
        val block = e.block
        //  Block
        if (plugin.chestMaterialList.contains(block.type)) {
            val location = block.location
            if (ChestControl.isProtected(location)) {
                if (player.hasPermission(Permission.ADMIN)) {
                    ChestControl.unprotectChest(location, player)
                    player.sendMessage("${prefix}§aチェストを壊しました。")
                } else {
                    e.isCancelled = true
                    player.sendMessage("${prefix}§cあなたには壊せません。")
                }
            }
        }
    }

    //  InventoryClick
    @EventHandler
    fun onClickEvent(e: InventoryClickEvent) {
        if (!enable) return
        val player = e.whoClicked
        val inventory = e.inventory
        val location = inventory.location ?: return
        if (ChestControl.isProtected(location)) {
            if (player.hasPermission(Permission.ADMIN)) return
            e.isCancelled = true
        }
    }
}
