package monmen.monoxido.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerActionsMenu {
    private static final int MENU_SIZE = 27; // Inventario de 3 filas
    private final FileConfiguration config;

    public PlayerActionsMenu(FileConfiguration config) {
        this.config = config;
    }

    /**
     * Abre el menú de acciones para un jugador específico
     * @param player El jugador que abre el menú
     * @param targetPlayer El jugador objetivo sobre el que se realizarán las acciones
     */
    public void openActionsMenu(Player player, Player targetPlayer) {
        // Obtener el título del menú desde la configuración
        String menuTitle = config.getString("actions_menu.title", "Acciones para %player%")
                .replace("%player%", targetPlayer.getName());

        // Crear el inventario
        Inventory menu = Bukkit.createInventory(player, MENU_SIZE, menuTitle);

        // Añadir los botones de acciones
        addActionButtons(menu, targetPlayer);
        
        // Abrir el inventario para el jugador
        player.openInventory(menu);
    }

    /**
     * Añade los botones de acciones al menú
     * @param menu El inventario del menú
     * @param targetPlayer El jugador objetivo
     */
    private void addActionButtons(Inventory menu, Player targetPlayer) {
        // Botón Kill
        String killText = config.getString("actions_menu.buttons.kill", "§cMatar a %player%")
                .replace("%player%", targetPlayer.getName());
        List<String> killLore = new ArrayList<>();
        killLore.add(config.getString("actions_menu.descriptions.kill", "§7Mata instantáneamente al jugador"));
        menu.setItem(10, createMenuItem(Material.DIAMOND_SWORD, killText, killLore));

        // Botón TP
        String tpText = config.getString("actions_menu.buttons.teleport", "§aTeletransportarse a %player%")
                .replace("%player%", targetPlayer.getName());
        List<String> tpLore = new ArrayList<>();
        tpLore.add(config.getString("actions_menu.descriptions.teleport", "§7Te teletransporta a la ubicación del jugador"));
        menu.setItem(13, createMenuItem(Material.ENDER_PEARL, tpText, tpLore));

        // Botón Kick
        String kickText = config.getString("actions_menu.buttons.kick", "§eExpulsar a %player%")
                .replace("%player%", targetPlayer.getName());
        List<String> kickLore = new ArrayList<>();
        kickLore.add(config.getString("actions_menu.descriptions.kick", "§7Expulsa al jugador del servidor"));
        menu.setItem(16, createMenuItem(Material.OAK_DOOR, kickText, kickLore));

        // Botón Cerrar (en el centro abajo)
        String closeText = config.getString("menu.buttons.close", "§cCerrar");
        menu.setItem(22, createMenuItem(Material.BARRIER, closeText));
        
        // Espacio para futuras acciones:
        // Posiciones 11, 12, 14, 15 disponibles en la fila superior
        // Posiciones 19, 20, 21, 23, 24, 25 disponibles en la fila inferior
    }

    /**
     * Crea un ítem para el menú con nombre y descripción
     * @param material El material del ítem
     * @param name El nombre del ítem
     * @param lore La descripción del ítem (opcional)
     * @return El ítem creado
     */
    private ItemStack createMenuItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore != null && !lore.isEmpty()) {
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Sobrecarga del método createMenuItem sin lore
     */
    private ItemStack createMenuItem(Material material, String name) {
        return createMenuItem(material, name, null);
    }
}