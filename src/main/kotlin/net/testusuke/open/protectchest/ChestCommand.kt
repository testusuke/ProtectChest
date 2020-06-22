package net.testusuke.open.protectchest

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

            "help" -> {sendHelp(sender)}
            "on" -> {changeEnable(sender,true)}
            "off" -> {changeEnable(sender,false)}
        }

        return false
    }

    private fun changeEnable(sender: Player, mode: Boolean) {


    }

    private fun sendHelp(player: Player){


    }

}