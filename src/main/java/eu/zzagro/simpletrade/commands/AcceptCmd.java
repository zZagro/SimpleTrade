package eu.zzagro.simpletrade.commands;

import eu.zzagro.simpletrade.SimpleTrade;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AcceptCmd implements CommandExecutor {

    private final SimpleTrade plugin;

    public AcceptCmd(SimpleTrade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player target = TradeCmd.getTargetMap.get(player);
            if (target == null) return false;
            if (args.length == 1) {
                if (TradeCmd.targetUuidMap.containsKey(player)) {
                    UUID uuid = TradeCmd.targetUuidMap.get(player);
                    if (args[0].equalsIgnoreCase(uuid.toString())) {
                        Bukkit.getScheduler().cancelTask(TradeCmd.task.getTaskId());
                        plugin.tradeInv.openTargetInv(player, target);
                        plugin.tradeInv.openPlayerInv(target, player);
                    } else {
                        player.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&cThat trade doesn't exist"));
                    }
                } else {
                    player.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&cYou don't have an outgoing trade request!"));
                }
            } else {
                player.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&cUsage: /accept <tradeUUID>"));
            }
        }
        return false;
    }
}
