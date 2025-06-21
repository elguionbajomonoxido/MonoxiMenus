package monmen.monoxido.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.FileConfiguration;

public class ActionsMenuListener implements Listener {
    private final FileConfiguration config;

    public ActionsMenuListener(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getCurrentItem() == null) return;

        // Verificar si el inventario es parte de nuestro plugin (menú de acciones)
        String title = event.getView().getTitle();
        if (!title.contains("Acciones para")) return;

        // Evitar que los jugadores muevan objetos en el inventario
        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
            String displayName = clickedItem.getItemMeta().getDisplayName();
            
            // Extraer el nombre del jugador objetivo del título del menú
            String targetPlayerName = title.replace("Acciones para ", "");
            Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
            
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                String message = config.getString("messages.player_offline", "§cEl jugador %player% no está en línea.")
                        .replace("%player%", targetPlayerName);
                player.sendMessage(message);
                player.closeInventory();
                return;
            }

            // Manejar las diferentes acciones
            if (displayName.contains("Matar")) {
                // Acción Kill
                targetPlayer.setHealth(0);
                String message = config.getString("actions_menu.messages.kill_success", "§aHas matado a %player%.")
                        .replace("%player%", targetPlayer.getName());
                player.sendMessage(message);
                player.closeInventory();
            } 
            else if (displayName.contains("Teletransportarse")) {
                // Acción TP
                player.teleport(targetPlayer.getLocation());
                String message = config.getString("actions_menu.messages.teleport_success", "§aTe has teletransportado a %player%.")
                        .replace("%player%", targetPlayer.getName());
                player.sendMessage(message);
                player.closeInventory();
            } 
            else if (displayName.contains("Expulsar")) {
                // Acción Kick
                String kickReason = config.getString("actions_menu.kick_reason", "Has sido expulsado por un administrador");
                targetPlayer.kickPlayer(kickReason);
                String message = config.getString("actions_menu.messages.kick_success", "§aHas expulsado a %player%.")
                        .replace("%player%", targetPlayer.getName());
                player.sendMessage(message);
                player.closeInventory();
            } 
            else if (displayName.contains("Cerrar")) {
                // Cerrar el menú
                player.closeInventory();
            }
            
            // Aquí se pueden agregar más acciones en el futuro
            // Ejemplo:
            /*
            else if (displayName.contains("Banear")) {
                // Implementar lógica para banear al jugador
                // Bukkit.getBanList(BanList.Type.NAME).addBan(targetPlayer.getName(), "Razón del ban", null, player.getName());
                // targetPlayer.kickPlayer("Has sido baneado");
            }
            else if (displayName.contains("Silenciar")) {
                // Implementar lógica para silenciar al jugador
                // Requiere un sistema de mute personalizado o un plugin de permisos
            }
            */
        }
    }
}