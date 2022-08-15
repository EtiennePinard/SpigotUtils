package com.ejrp.spigotutils.commandUtils

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*

/**
 * This is a tab completer that should cover all basic use case of using
 * a tab completer. There will be different completions based on what was
 * the previous arguments.
 */
class TabCompleteUtil
/**
 * Creates a new TabComplete instance with the specified arguments.
 * @param argumentMap The key of the map representing the arguments that need to precede
 * the actual arguments, which is the key. So this means that you can have
 * different arguments based on what precede them.
 */(private val argumentMap: Map<Array<String>, List<String>>) : TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>? {
        val result = ArrayList<String>()
        val previousArguments = arrayOfNulls<String>(args.size - 1)
        System.arraycopy(args, 0, previousArguments, 0, args.size - 1)
        val optionalStrings = argumentMap.keys.stream()
            .filter { strings: Array<String>? -> Arrays.equals(strings, previousArguments) }
            .findFirst()
        if (!optionalStrings.isPresent) return null
        for (arg in argumentMap[optionalStrings.get()]!!)
            if (arg.lowercase().startsWith(args[args.size - 1].lowercase()))
                result.add(arg)
        return result
    }
}