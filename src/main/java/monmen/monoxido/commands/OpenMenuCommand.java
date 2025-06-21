package monmen.monoxido.commands;

import monmen.monoxido.MonoxiMenus;
import monmen.monoxido.menus.PlayerMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenMenuCommand implements CommandExecutor {

    private final MonoxiMenus plugin;

    public OpenMenuCommand(MonoxiMenus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando solo puede ser usado por jugadores.");
            return true;
        }

        Player player = (Player) sender;

        // Verificar si el jugador tiene permiso para usar el comando
        if (!player.hasPermission("monoximenus.use")) {
            String message = plugin.getConfig().getString("messages.no_permission", "§cNo tienes permiso para usar este comando.");
            player.sendMessage(message);
            return true;
        }
        boolean alphabeticalOrder = true;
        int page = 0;

        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
                if (args.length > 1) {
                    alphabeticalOrder = Boolean.parseBoolean(args[1]);
                }
            } catch (NumberFormatException e) {
                player.sendMessage("§cUso incorrecto. Usa: /pmenu [página] [ordenAlfabético]");
                return true;
            }
        }

        new PlayerMenu(plugin.getConfig()).openPlayerMenu(player, page, alphabeticalOrder);
        return true;
    }
}
