package com.ejrp.spigotutils.CommandUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteUtil implements TabCompleter {

    @NotNull private final List<List<String>> listsOfArguments;

    public TabCompleteUtil(@NotNull List<List<String>> listsOfArguments) {
        this.listsOfArguments = listsOfArguments;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, @NotNull String[] args) {
        List<String> result = new ArrayList<>();
        // There is no arguments in the list of arguments so return
        if (listsOfArguments.size() > args.length) return null;

        for (String arg : listsOfArguments.get(args.length - 1)) {
            if (arg.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                result.add(arg);
            return result;
        }
        return null;
    }
}
