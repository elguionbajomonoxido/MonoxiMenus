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
        if (sender instanceof Player) {
            new PlayerMenu(plugin.getConfig()).openPlayerMenu((Player) sender, 0, true);
            return true;
        }

        Player player = (Player) sender;
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

        new PlayerMenu(plugin.getConfig()).openPlayerMenu((Player) sender, 0, true);;
        return true;
    }
}
