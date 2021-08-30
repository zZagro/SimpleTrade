package eu.zzagro.simpletrade.listeners;

import eu.zzagro.simpletrade.SimpleTrade;
import eu.zzagro.simpletrade.commands.TradeCmd;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class InventoryClickListener implements Listener {

    private final SimpleTrade plugin;

    private BukkitTask task;

    private Map<Player, Boolean> isPlayerReady = new HashMap<>();
    private Map<Player, Boolean> isTargetReady = new HashMap<>();

    public InventoryClickListener(SimpleTrade plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null) {
            if (e.getCurrentItem() != null) {
                if (e.getInventory().getTitle().equalsIgnoreCase("Trade Menu")) {
                    e.setCancelled(true);
                    Player target = Bukkit.getPlayerExact(TradeCmd.getTargetMap.get(player).getName());
                    if (target != null) {
                        if (TradeCmd.playerUuidMap.get(player).equals(TradeCmd.targetUuidMap.get(target))) {
                            isPlayerReady.put(player, false);

                            ItemStack confirmItem = plugin.metaManager.confirmItem;
                            ItemMeta confirmMeta = plugin.metaManager.getConfirmMeta();
                            ItemStack readyItem = plugin.metaManager.readyItem;
                            ItemMeta readyMeta = plugin.metaManager.getReadyMeta();
                            ItemStack waitingItem = plugin.metaManager.waitingItem;
                            ItemMeta waitingMeta = plugin.metaManager.getWaitingMeta();

                            if (e.getSlot() == 39 && e.getCurrentItem().isSimilar(confirmItem)) {
                                //player.sendMessage("Player: " + TradeCmd.playerNameMap.get(target).getName() + ", Target: " + target.getName());
                                isPlayerReady.put(player, true);
                                target.getInventory().setItem(41, readyItem);
                                player.getInventory().setItem(39, readyItem);
                            } else if (e.getSlot() == 39 && e.getCurrentItem().isSimilar(readyItem)) {
                                isPlayerReady.put(player, false);
                                target.getInventory().setItem(41, waitingItem);
                                player.getInventory().setItem(39, confirmItem);
                            }
                            if (isTargetReady.get(target) && isPlayerReady.get(player)) {
                                player.getOpenInventory().close();
                                target.getOpenInventory().close();
                            }

                            if (e.getClickedInventory() instanceof PlayerInventory) {

                            }
                        }
                    } else {
                        player.sendMessage("null");
                    }
                }
            }
        }
    }
}
