package com.ejrp.spigotutils.commandUtils

/**
 * This is an annotation to use the in conjunction with the PluginCommand class.
 * It will provide three values to the PluginCommand class.
 * The name and the permission of the command and if the command requires a player.
 * The permission default value is set to "operator" so that if you forget it will not do any damage.
 * Also, huge thanks to Jordan Osterberg that made this code.
 * Here is a link to his channel: https://www.youtube.com/channel/UCiZQVaud0pAPfzA8busnRBA
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandInfo(val name: String, val permission: String = "operator", val requiresPlayer: Boolean)