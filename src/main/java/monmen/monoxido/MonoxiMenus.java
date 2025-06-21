package monmen.monoxido;

import monmen.monoxido.commands.ClearSkinCacheCommand;
import monmen.monoxido.commands.OpenMenuCommand;
import monmen.monoxido.commands.ReloadCommand;
import monmen.monoxido.listeners.ActionsMenuListener;
import monmen.monoxido.listeners.MenuClickListener;
import monmen.monoxido.utils.SkinCache;
import org.bukkit.plugin.java.JavaPlugin;

public class MonoxiMenus extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig(); // Asegura que el archivo config.yml se genere si no existe

        // Inicializar y cargar caché de skins al iniciar el plugin
        SkinCache.init(this);
        SkinCache.loadCache();

        // Registrar eventos del menú pasando la configuración
        getServer().getPluginManager().registerEvents(new MenuClickListener(getConfig()), this);

        // Registrar eventos del menú de acciones
        getServer().getPluginManager().registerEvents(new ActionsMenuListener(getConfig()), this);

        // Registrar comando pmenu si está definido en plugin.yml
        if (getCommand("pmenu") != null) {
            getCommand("pmenu").setExecutor(new OpenMenuCommand(this));
        } else {
            getLogger().warning("El comando 'pmenu' no está definido en plugin.yml");
        }

        // Registrar comando clearskincache si está definido en plugin.yml
        if (getCommand("clearskincache") != null) {
            getCommand("clearskincache").setExecutor(new ClearSkinCacheCommand(this));
        } else {
            getLogger().warning("El comando 'clearskincache' no está definido en plugin.yml");
        }

        // Registrar comando monoxireload si está definido en plugin.yml
        if (getCommand("pmenureload") != null) {
            getCommand("pmenureload").setExecutor(new ReloadCommand(this));
        } else {
            getLogger().warning("El comando 'pmenureload' no está definido en plugin.yml");
        }
    }

    @Override
    public void onDisable() {
        // Guardar caché de skins al deshabilitar el plugin
        SkinCache.saveCache();
    }
}
