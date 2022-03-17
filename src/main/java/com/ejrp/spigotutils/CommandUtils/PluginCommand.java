package com.ejrp.spigotutils.CommandUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This represents a command sent by a plugin.
 * It is a quick way to set up a command without having to deal with permissions, if it requires a player and if the command name matches.
 * All commands that wants to use this class needs to extend this class and use the CommandInfo annotation.
 * Also, huge thanks to Jordan Osterberg that made this code.
 * Here is a link to his channel: https://www.youtube.com/channel/UCiZQVaud0pAPfzA8busnRBA
 */
public abstract class PluginCommand implements CommandExecutor {

    private final CommandInfo commandInfo;

    /**
     * Gets the CommandInfo annotation from the child class.
     */
    public PluginCommand() {
        commandInfo = getClass().getDeclaredAnnotation(CommandInfo.class);
        Objects.requireNonNull(commandInfo,"Command must have CommandInfo annotations");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase(commandInfo.name())) {
            if (!commandInfo.permission().isEmpty()) {
                if (!sender.hasPermission(commandInfo.permission())) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }
            }

            if (commandInfo.requiresPlayer()) {
                if (!(sender instanceof Player))
                    sender.sendMessage(ChatColor.RED + "You must be a player to execute this command!");
                else
                    execute((Player) sender, args);
            } else
                execute(sender, args);
            return true;
        }
        return false;
    }

    /**
     * The method that will get executed after all the checks are made in the PluginCommand class.
     * Override this method if your command requires a player.
     * @param player The player that sent this command
     * @param args The arguments passed by the player
     */
    public void execute(Player player, String[] args) {}

    /**
     * The method that will get executed after all the checks are made in the PluginCommand class.
     * Override this method if your does not command requires a player.
     * @param sender The sender of the command
     * @param args The arguments passed by the sender
     */
    public void execute(CommandSender sender, String[] args) {}

}
