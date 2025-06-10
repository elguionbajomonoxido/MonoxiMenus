package monmen.monoxido.listeners;

import monmen.monoxido.menus.PlayerMenu;
import monmen.monoxido.utils.SkinCache;
import org.bukkit.Material;
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

        Inventory inventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        // Evita que los jugadores muevan objetos en el inventario
        event.setCancelled(true);

        if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
            String displayName = clickedItem.getItemMeta().getDisplayName();

            if (displayName.contains("Página Anterior")) {
                int currentPage = getCurrentPage(player.getOpenInventory().getTitle());
                int previousPage = Math.max(0, currentPage - 1);
                new PlayerMenu(config).openPlayerMenu(player, previousPage, true);
            } else if (displayName.contains("Página Siguiente")) {
                int currentPage = getCurrentPage(player.getOpenInventory().getTitle());
                int nextPage = currentPage + 1;
                new PlayerMenu(config).openPlayerMenu(player, nextPage, true);
            } else if (clickedItem.getType() == Material.PLAYER_HEAD) {
                player.sendMessage("§aSeleccionaste un jugador: " + displayName);
            } else if (clickedItem.getType() == Material.CLOCK) {
                player.sendMessage("§eRefrescando placeholders...");
                Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("MonoxiMenus"), () -> {
                    new PlayerMenu(config).openPlayerMenu(player, getCurrentPage(player.getOpenInventory().getTitle()), true);
                }, 20L); // 1 segundo de espera antes de refrescar
            } else if (clickedItem.getType() == Material.BOOK) {
                boolean currentFilter = player.getOpenInventory().getTitle().contains("A-Z");
                new PlayerMenu(config).openPlayerMenu(player, 0, !currentFilter);
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
