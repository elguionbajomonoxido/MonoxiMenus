package monmen.monoxido.commands;

import monmen.monoxido.MonoxiMenus;
import monmen.monoxido.utils.SkinCache;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearSkinCacheCommand implements CommandExecutor {

    private final MonoxiMenus plugin;

    public ClearSkinCacheCommand(MonoxiMenus plugin) {
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

        // Limpiar la caché de skins
        SkinCache.clearCache();

        // Obtener mensaje de la configuración
        String message = plugin.getConfig().getString("messages.cache_cleared", "§aLa caché de skins ha sido borrada correctamente.");

        if (sender instanceof Player) {
            sender.sendMessage(message);
        } else {
            // Si es la consola, enviar mensaje sin colores
            sender.sendMessage(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message)));
        }

        return true;
    }
}
