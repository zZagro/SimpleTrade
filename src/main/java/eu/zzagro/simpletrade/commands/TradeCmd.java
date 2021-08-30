package eu.zzagro.simpletrade.commands;

import eu.zzagro.simpletrade.SimpleTrade;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradeCmd implements CommandExecutor {

    public static Map<Player, Player> getPlayerMap = new HashMap<>();
    public static Map<Player, Player> getTargetMap = new HashMap<>();
    public static Map<Player, UUID> targetUuidMap = new HashMap<>();
    public static Map<Player, UUID> playerUuidMap = new HashMap<>();
    public UUID uuid;

    public static BukkitTask task;

    private final SimpleTrade plugin;

    public TradeCmd(SimpleTrade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("simpletrade.trade")) {
                if (command.getName().equalsIgnoreCase("trade")) {
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayerExact(args[0]);
                        if (target == null) {
                            player.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&6" + args[0] + " &cisn't online or doesn't exist!"));
                            return false;
                        }
                        if (target == player) {
                            player.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&cYou can't trade with yourself!"));
                            return false;
                        }
                        if (!(getTargetMap.containsKey(player))) {
                            target.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&6" + player.getName() + " &ahas sent you a trade request!"));

                            uuid = UUID.randomUUID();
                            targetUuidMap.put(target, uuid);
                            playerUuidMap.put(player, uuid);

                            getPlayerMap.put(target, player);
                            getTargetMap.put(player, target);

                            TextComponent accept = new TextComponent();
                            accept.setText("[ACCEPT] ");
                            accept.setColor(ChatColor.GREEN);
                            accept.setBold(true);
                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + uuid));
                            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to accept").create()));

                            TextComponent deny = new TextComponent();
                            deny.setText("[DENY]");
                            deny.setColor(ChatColor.RED);
                            deny.setBold(true);
                            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deny " + uuid));
                            deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to deny").create()));

                            TextComponent text = new TextComponent();
                            text.setText(SimpleTrade.color(SimpleTrade.prefix + "&6Do you want to accept? "));
                            text.addExtra(accept);
                            accept.addExtra(deny);

                            target.spigot().sendMessage(text);
                            player.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&aYou've sent a trade request to &6" + target.getName()));

                            Bukkit.getOnlinePlayers().forEach(p -> {
                                p.sendMessage("Target: " + getTargetMap.get(player).getName());
                                p.sendMessage("Player: " + getPlayerMap.get(target).getName());
                            });

                            task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                                targetUuidMap.remove(target);
                                getPlayerMap.remove(target);
                                getTargetMap.remove(player);
                                player.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&cYour trade request to &6" + target.getName() + " &cexpired!"));
                                target.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&cThe trade request from &6" + player.getName() + " &cexpired!"));
                            }, 20*15);
                        } else if (!(getTargetMap.get(player).getName().equalsIgnoreCase(target.getDisplayName()) && getPlayerMap.get(target).getName().equalsIgnoreCase(player.getDisplayName()))) {
                            target.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&6" + player.getName() + " &ahas sent you a trade request!"));

                            uuid = UUID.randomUUID();
                            targetUuidMap.put(target, uuid);
                            playerUuidMap.put(player, uuid);

                            getPlayerMap.put(target, player);
                            getTargetMap.put(player, target);

                            TextComponent accept = new TextComponent();
                            accept.setText("[ACCEPT] ");
                            accept.setColor(ChatColor.GREEN);
                            accept.setBold(true);
                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + uuid));
                            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to accept").create()));

                            TextComponent deny = new TextComponent();
                            deny.setText("[DENY]");
                            deny.setColor(ChatColor.RED);
                            deny.setBold(true);
                            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deny " + uuid));
                            deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to deny").create()));

                            TextComponent text = new TextComponent();
                            text.setText(SimpleTrade.color(SimpleTrade.prefix + "&6Do you want to accept? "));
                            text.addExtra(accept);
                            accept.addExtra(deny);

                            target.spigot().sendMessage(text);
                            player.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&aYou've sent a trade request to &6" + target.getName()));

                            task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                                targetUuidMap.remove(target);
                                getPlayerMap.remove(target);
                                getTargetMap.remove(player);
                                player.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&cYour trade request to &6" + target.getName() + " &cexpired!"));
                                target.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&cThe trade request from &6" + player.getName() + " &cexpired!"));
                            }, 20*15);
                        } else {
                            player.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&cYou already have an outgoing trade request to this player!"));
                        }
                    } else {
                        player.sendMessage(SimpleTrade.prefix + SimpleTrade.color("&cUsage: /trade <player>"));
                    }
                }
            }
        }
        return false;
    }
}
