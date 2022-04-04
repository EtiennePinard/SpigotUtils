package com.ejrp.spigotutils.CommandUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * This is a tab completer that should cover all basic use case of using
 * a tab completer. There will be different completions based on what was
 * the previous arguments.
 */
public class TabCompleteUtil implements TabCompleter {

    @NotNull private final Map<String[],List<String>> argumentMap;

    /**
     * Creates a new TabComplete instance with the specified arguments.
     * @param argumentMap The key of the map representing the arguments that need to precede
     *                    the actual arguments, which is the key. So this means that you can have
     *                    different arguments based on what precede them.
     */
    public TabCompleteUtil(@NotNull Map<String[],List<String>> argumentMap) { this.argumentMap = argumentMap; }

    @Nullable
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, @NotNull String[] args) {
        List<String> result = new ArrayList<>();

        String[] previousArguments = new String[args.length - 1];
        System.arraycopy(args,0,previousArguments,0,args.length - 1);

        Optional<String[]> optionalStrings =
                argumentMap.keySet().stream()
                        .filter(strings -> Arrays.equals(strings,previousArguments))
                        .findFirst();
        if (!optionalStrings.isPresent()) return null;

        for (String arg : argumentMap.get(optionalStrings.get())) {
            if (arg.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                result.add(arg);
        }
        return result;
    }
}
