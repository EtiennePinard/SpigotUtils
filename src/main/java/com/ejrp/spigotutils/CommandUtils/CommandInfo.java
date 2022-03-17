package com.ejrp.spigotutils.CommandUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is an annotation to use the in conjunction with the PluginCommand class.
 * It will provide three values to the PluginCommand class.
 * The name and the permission of the command and if the command requires a player.
 * The permission default value is set to "operator" so that if you forget it will not do any damage.
 * Also, huge thanks to Jordan Osterberg that made this code.
 * Here is a link to his channel: https://www.youtube.com/channel/UCiZQVaud0pAPfzA8busnRBA
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    String name();
    String permission() default "operator";
    boolean requiresPlayer();
}
