package net.testusuke.open.protectchest

import net.testusuke.open.protectchest.Main.Companion.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

/**
 * Created on 2020/06/22
 * Author testusuke
 */
object EventListener:Listener {

    @EventHandler
    fun onClickBlock(e:PlayerInteractEvent){
        val player = e.player
        //  左
        if(e.action == Action.LEFT_CLICK_BLOCK){
            //  Item
            val item = player.inventory.itemInMainHand
            if(item.isSimilar(plugin.wandItem)){

            }
        }
        //  右
        if(e.action == Action.RIGHT_CLICK_BLOCK){
            //  Item
            val item = player.inventory.itemInMainHand
            if(item.isSimilar(plugin.wandItem)){

            }
        }
    }
}