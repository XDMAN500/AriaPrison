package me.varmetek.prison.commands;

import java.util.List;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.User;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class UnIgnoreCommand implements CommandExecutor,TabCompleter
{

	@Override
	public boolean onCommand(CommandSender s, Command c, String l,
			String[] args) {
		if(!(s instanceof Player))return false;
		
			if(c.getPermission() != null){
				if(!s.hasPermission(c.getPermission())){
					Messenger.send(s, Main.NO_PERM_MSG);
					return false;
				}
			}
		
		int len = args.length;
		Player pl = (Player)s;
		User user =User.getUser(pl);
		
		if(len == 0){
			Messenger.send(s, "&c/unignore <player>");
			return false;
		}else{
			OfflinePlayer op = Utils.getPlayer(args[0]);
			if(!op.isOnline()){
				Messenger.send(s, "&cThat player is not online.");
				return false;
			}
			if(!user.getIgnoredPlayers().contains(op.getUniqueId())){
				Messenger.send(s, "&7Player &c"+op.getName()+" &7is not ignored.");
			}
				
			user.getIgnoredPlayers().remove(op.getUniqueId());
			Messenger.send(s, "&7Player &c"+op.getName()+" &7is no longer ignored.");
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] args) {
		int len = args.length;
		if(len == 1){
			return Utils.matchOnlinePlayers(args[0]);
		}
		return null;
	}

}
