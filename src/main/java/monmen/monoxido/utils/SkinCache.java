package monmen.monoxido.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinCache {
    private static final Map<UUID, ItemStack> cachedSkins = new HashMap<>();
    private static final File file = new File("plugins/MonoxiMenus/skins_cache.yml");
    private static FileConfiguration config;

    private static final String PREVIOUS_PAGE_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY0Zjc3OWE4ZTNmZmEyMzExNDNmYTY5Yjk2YjE0ZWUzNWMxNmQ2NjllMTljNzVmZDFhN2RhNGJmMzA2YyJ9fX0=";
    private static final String NEXT_PAGE_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDllY2NjNWMxYzc5YWE3ODI2YTE1YTdmNWYxMmZiNDAzMjgxNTdjNTI0MjE2NGJhMmFlZjQ3ZTVkZTlhNWNmYyJ9fX0=";

    public static void loadCache() {
        if (!file.exists()) return;
        config = YamlConfiguration.loadConfiguration(file);
        for (String uuidString : config.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            ItemStack head = createPlayerHead(player);
            cachedSkins.put(uuid, head);
        }
    }

    public static void saveCache() {
        config = new YamlConfiguration();
        for (UUID uuid : cachedSkins.keySet()) {
            config.set(uuid.toString(), true);
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ItemStack getPlayerHead(OfflinePlayer player) {
        return cachedSkins.computeIfAbsent(player.getUniqueId(), uuid -> {
            ItemStack head = createPlayerHead(player);
            saveCache();
            return head;
        });
    }

    public static ItemStack getCustomHead(boolean isNextPage) {
        String texture = isNextPage ? NEXT_PAGE_TEXTURE : PREVIOUS_PAGE_TEXTURE;
        return createCustomSkull(texture);
    }

    private static ItemStack createPlayerHead(OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            head.setItemMeta(meta);
        }
        return head;
    }

    private static ItemStack createCustomSkull(String textureValue) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            applyCustomTexture(meta, textureValue);
            skull.setItemMeta(meta);
        }
        return skull;
    }

    private static void applyCustomTexture(SkullMeta meta, String textureValue) {
        try {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", textureValue));

            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void clearCache() {
        cachedSkins.clear();
        file.delete();
    }
}
