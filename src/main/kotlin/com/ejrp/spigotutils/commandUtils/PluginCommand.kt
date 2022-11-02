package com.ejrp.spigotutils.commandUtils

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

/**
 * This represents a command sent by a plugin.
 * It is a quick way to set up a command without having to deal with permissions, if it requires a player and if the command name matches.
 * All commands that wants to use this class needs to extend this class and use the CommandInfo annotation.
 * Also, huge thanks to Jordan Osterberg that made this code.
 * Here is a link to his channel: https://www.youtube.com/channel/UCiZQVaud0pAPfzA8busnRBA
 */
abstract class PluginCommand : CommandExecutor {
    private val commandInfo: CommandInfo = javaClass.getDeclaredAnnotation(CommandInfo::class.java)

    init { Objects.requireNonNull(commandInfo, "Command must have CommandInfo annotations") }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (label.equals(commandInfo.name, ignoreCase = true)) {
            if (commandInfo.permission.isNotEmpty()) {
                if (!sender.hasPermission(commandInfo.permission)) {
                    sender.sendMessage(ChatColor.RED.toString() + "You don't have permission to use this command!")
                    return true
                }
            }
            if (commandInfo.requiresPlayer) {
                if (sender !is Player) sender.sendMessage(ChatColor.RED.toString() + "You must be a player to execute this command!")
                else execute(sender, args)
            } else execute(sender, args)
            return true
        }
        return false
    }

    /**
     * The method that will get executed after all the checks are made in the PluginCommand class.
     * Override this method if your command requires a player.
     * @param player The player that sent this command
     * @param args The arguments passed by the player
     */
    open fun execute(player: Player, args: Array<String>) {}

    /**
     * The method that will get executed after all the checks are made in the PluginCommand class.
     * Override this method if your does not command requires a player.
     * @param sender The sender of the command
     * @param args The arguments passed by the sender
     */
    open fun execute(sender: CommandSender, args: Array<String>) {}
}