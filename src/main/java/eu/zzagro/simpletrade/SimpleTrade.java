package eu.zzagro.simpletrade;

import eu.zzagro.simpletrade.commands.AcceptCmd;
import eu.zzagro.simpletrade.commands.TradeCmd;
import eu.zzagro.simpletrade.listeners.InventoryClickListener;
import eu.zzagro.simpletrade.listeners.PlayerInteractListener;
import eu.zzagro.simpletrade.utils.MetaManager;
import eu.zzagro.simpletrade.utils.TradeInv;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleTrade extends JavaPlugin {

    //Players can trade items
    //CODE KOMMENTIEREN NICHT VERGESSEN!

    public static String prefix = ChatColor.translateAlternateColorCodes('&', "&bSimpleTrade &7>> ");

    FileConfiguration config = this.getConfig();

    public TradeInv tradeInv = new TradeInv(this);
    public MetaManager metaManager = new MetaManager();

    @Override
    public void onEnable() {
        getCommand("trade").setExecutor(new TradeCmd(this));
        getCommand("accept").setExecutor(new AcceptCmd(this));

        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);

        loadConfig();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public void loadConfig() {
        config.options().copyDefaults(true);
        saveConfig();
    }
}
