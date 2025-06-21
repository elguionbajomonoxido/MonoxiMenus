package monmen.monoxido.listeners;

import monmen.monoxido.menus.PlayerMenu;
import monmen.monoxido.menus.PlayerActionsMenu;
import monmen.monoxido.utils.SkinCache;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class MenuClickListener implements Listener {
    private final FileConfiguration config;

    public MenuClickListener(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getCurrentItem() == null) return;

        // Verificar si el inventario es parte de nuestro plugin
        String title = event.getView().getTitle();
        if (!title.contains("Jugadores")) return;

        Inventory inventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        // Evita que los jugadores muevan objetos en el inventario
        event.setCancelled(true);

        if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
            String displayName = clickedItem.getItemMeta().getDisplayName();
            boolean isAZ = title.contains(config.getString("menu.filter_az", "A-Z"));

            // Botón de página anterior
            if (displayName.contains("Página Anterior") || 
                (clickedItem.getType() == Material.PLAYER_HEAD && event.getSlot() == 48)) {
                int currentPage = getCurrentPage(title);
                int previousPage = Math.max(0, currentPage - 1);
                new PlayerMenu(config).openPlayerMenu(player, previousPage, isAZ);
            } 
            // Botón de página siguiente
            else if (displayName.contains("Página Siguiente") || 
                    (clickedItem.getType() == Material.PLAYER_HEAD && event.getSlot() == 50)) {
                int currentPage = getCurrentPage(title);
                int nextPage = currentPage + 1;
                new PlayerMenu(config).openPlayerMenu(player, nextPage, isAZ);
            } 
            // Cabeza de jugador
            else if (clickedItem.getType() == Material.PLAYER_HEAD && event.getSlot() < 45) {
                String playerName = ChatColor.stripColor(displayName);
                Player targetPlayer = Bukkit.getPlayerExact(playerName);

                if (targetPlayer != null && targetPlayer.isOnline()) {
                    // Abrir el menú de acciones para el jugador seleccionado
                    new PlayerActionsMenu(config).openActionsMenu(player, targetPlayer);
                } else {
                    String message = config.getString("messages.player_offline", "§cEl jugador %player% no está en línea.")
                            .replace("%player%", playerName);
                    player.sendMessage(message);
                }
            }
            // Botón de refrescar
            else if (clickedItem.getType() == Material.CLOCK) {
                String message = config.getString("messages.refreshing", "§eRefrescando el menú...");
                player.sendMessage(message);
                Bukkit.getScheduler().runTaskLater(
                    Bukkit.getPluginManager().getPlugin("MonoxiMenus"), 
                    () -> new PlayerMenu(config).openPlayerMenu(player, getCurrentPage(title), isAZ), 
                    20L // 1 segundo de espera antes de refrescar
                );
            } 
            // Botón de filtro
            else if (clickedItem.getType() == Material.BOOK) {
                new PlayerMenu(config).openPlayerMenu(player, 0, !isAZ);
            }
            // Botón de cerrar
            else if (clickedItem.getType() == Material.BARRIER) {
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Posible implementación futura para limpiar datos temporales
    }

    private int getCurrentPage(String title) {
        String[] parts = title.split(" - Página ");
        if (parts.length > 1) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
