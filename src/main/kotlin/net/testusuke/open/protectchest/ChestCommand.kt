package net.testusuke.open.protectchest

import net.testusuke.open.protectchest.Main.Companion.enable
import net.testusuke.open.protectchest.Main.Companion.plugin
import net.testusuke.open.protectchest.Main.Companion.prefix
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created on 2020/06/22
 * Author testusuke
 */
object ChestCommand:CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player || !sender.hasPermission(Permission.GENERAL)){
            sender.sendMessage("you do not have permission.")
            return false
        }

        if(args.isEmpty()){
            sendHelp(sender)
            return true
        }

        when(args[0]){
            "wand" -> {
                if(!enable){
                    sendDisable(sender)
                    return false
                }
                if(sender.hasPermission(Permission.ADMIN)){
                    sender.sendMessage("${prefix}§aWandを付与します。")
                    sender.inventory.addItem(plugin.wandItem)
                }else{sender.sendMessage("${prefix}§cあなたには権限がありません。")}
            }
            "help" -> {sendHelp(sender)}
            "on" -> {changeEnable(sender,true)}
            "off" -> {changeEnable(sender,false)}
        }

        return false
    }

    private fun changeEnable(sender: Player, mode: Boolean) {
        if(!sender.hasPermission(Permission.ADMIN)){
            sender.sendMessage("${prefix}§cあなたには権限がありません。")
            return
        }
        if(enable == mode){
            sender.sendMessage("${prefix}§cすでに§e${mode}§cになっています。")
        }else{
            enable = true
            sender.sendMessage("${prefix}§aプラグインが§e${mode}§aになりました。")
        }
    }

    private fun sendHelp(player: Player){
        val msg = """
            §e===================================
            §e/pc [help] <- Helpの表示
            §e/pc wand <- 編集用アイテムの取得
            §e/pc on/off <- プラグインの有効/無効
            §d§lCreated by testusuke
            §e===================================
        """.trimIndent()
        player.sendMessage(msg)
    }

    private fun sendDisable(player: Player){
        player.sendMessage("${prefix}§c現在利用できません")
    }
}