package monmen.monoxido.commands;

import monmen.monoxido.MonoxiMenus;
import monmen.monoxido.utils.SkinCache;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando para recargar la configuración del plugin
 */
public class ReloadCommand implements CommandExecutor {

    private final MonoxiMenus plugin;

    public ReloadCommand(MonoxiMenus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verificar si el remitente tiene permiso para usar el comando
        if (sender instanceof Player && !sender.hasPermission("monoximenus.admin")) {
            String message = plugin.getConfig().getString("messages.no_permission", "§cNo tienes permiso para usar este comando.");
            sender.sendMessage(message);
            return true;
        }

        // Recargar la configuración
        plugin.reloadConfig();

        // Recargar la caché de skins
        SkinCache.clearCache();
        SkinCache.loadCache();

        // Obtener mensaje de la configuración
        String message = plugin.getConfig().getString("messages.config_reloaded", "§aLa configuración ha sido recargada correctamente.");

        if (sender instanceof Player) {
            sender.sendMessage(message);
        } else {
            // Si es la consola, enviar mensaje sin colores
            sender.sendMessage(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message)));
        }

        return true;
    }
}
