package monmen.monoxido.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderUtil {
    /**
     * Aplica los placeholders de PlaceholderAPI a una lista de líneas de texto.
     * @param player Jugador al que se aplican los placeholders.
     * @param lines Líneas de texto con placeholders a reemplazar.
     * @return Lista con los placeholders reemplazados.
     */
    public static List<String> applyPlaceholders(Player player, String... lines) {
        List<String> result = new ArrayList<>();
        for (String line : lines) {
            result.add(PlaceholderAPI.setPlaceholders(player, line));
        }
        return result;
    }
}

