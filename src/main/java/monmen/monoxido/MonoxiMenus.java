package monmen.monoxido;

import monmen.monoxido.commands.OpenMenuCommand;
import monmen.monoxido.listeners.MenuClickListener;
import monmen.monoxido.utils.SkinCache;
import org.bukkit.plugin.java.JavaPlugin;

public class MonoxiMenus extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig(); // Asegura que el archivo config.yml se genere si no existe

        // Cargar caché de skins al iniciar el plugin
        SkinCache.loadCache();

        // Registrar eventos del menú pasando la configuración
        getServer().getPluginManager().registerEvents(new MenuClickListener(getConfig()), this);

        // Registrar comando si está definido en plugin.yml
        if (getCommand("pmenu") != null) {
            getCommand("pmenu").setExecutor(new OpenMenuCommand(this));
        } else {
            getLogger().warning("El comando 'pmenu' no está definido en plugin.yml");
        }
    }

    @Override
    public void onDisable() {
        // Guardar caché de skins al deshabilitar el plugin
        SkinCache.saveCache();
    }
}
