package monmen.monoxido.menus;

import monmen.monoxido.utils.SkinCache;
import monmen.monoxido.utils.PlaceholderUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerMenu {
    private static final int MENU_SIZE = 54;
    private static final int MAX_PLAYERS_PER_PAGE = 45;
    private final FileConfiguration config;

    public PlayerMenu(FileConfiguration config) {
        this.config = config;
    }

    public void openPlayerMenu(Player player, int page, boolean alphabeticalOrder) {
        List<Player> onlinePlayers = Bukkit.getOnlinePlayers().stream()
                .filter(p -> !p.isOp())
                .collect(Collectors.toList());

        if (onlinePlayers.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No hay jugadores disponibles.");
            return;
        }

        if (alphabeticalOrder) {
            onlinePlayers.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
        } else {
            onlinePlayers.sort((p1, p2) -> Long.compare(p2.getFirstPlayed(), p1.getFirstPlayed()));
        }

        // Primero calculamos el total de páginas
        final int totalPages = (int) Math.ceil((double) onlinePlayers.size() / MAX_PLAYERS_PER_PAGE);

        // Luego ajustamos `page` y lo hacemos final
        final int fixedPage = Math.max(0, Math.min(page, totalPages - 1));

        // Crear el inventario
        Inventory menu = Bukkit.createInventory(player, MENU_SIZE, "Jugadores - " + (alphabeticalOrder ? "A-Z" : "Tiempo") + " - Página " + (fixedPage + 1));

        int startIndex = fixedPage * MAX_PLAYERS_PER_PAGE;
        int endIndex = Math.min(startIndex + MAX_PLAYERS_PER_PAGE, onlinePlayers.size());

        for (int i = startIndex; i < endIndex; i++) {
            Player target = onlinePlayers.get(i);
            ItemStack head = getPlayerHead(target);
            menu.setItem(i - startIndex, head);
        }

        addMenuButtons(menu, fixedPage, totalPages, alphabeticalOrder);
        player.openInventory(menu);
    }




    private ItemStack getPlayerHead(Player player) {
        ItemStack head = SkinCache.getPlayerHead(player);
        if (head == null || head.getType() != Material.PLAYER_HEAD) {
            head = new ItemStack(Material.PLAYER_HEAD);
        }

        ItemMeta meta = head.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + player.getName());
            List<String> loreConfig = config.getStringList("player_head.lore");
            List<String> lore = PlaceholderUtil.applyPlaceholders(player, loreConfig.toArray(new String[0]))
                    .stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .collect(Collectors.toList());
            meta.setLore(lore);
            head.setItemMeta(meta);
        }
        return head;
    }

    private void addMenuButtons(Inventory menu, int page, int totalPages, boolean alphabeticalOrder) {
        menu.setItem(45, createMenuItem(Material.CLOCK, ChatColor.YELLOW + "Refrescar"));
        if (page > 0) {
            menu.setItem(48, SkinCache.getCustomHead(false)); // Página Anterior
        }
        menu.setItem(49, createMenuItem(Material.BARRIER, ChatColor.RED + "Cerrar"));
        if (page < totalPages - 1) {
            menu.setItem(50, SkinCache.getCustomHead(true)); // Página Siguiente
        }
        menu.setItem(53, createMenuItem(Material.BOOK, ChatColor.AQUA + "Filtros: " + (alphabeticalOrder ? "Tiempo" : "A-Z")));
    }

    private ItemStack createMenuItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }
}
