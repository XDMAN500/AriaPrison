package me.varmetek.prison.commands;

import java.util.Arrays;
import java.util.List;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.Rank;
import me.varmetek.prison.api.User;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class LockChatCommand implements CommandExecutor, TabCompleter {
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			Player player = (Player)sender;
			User playerUser = User.getUser(Bukkit.getOfflinePlayer(player.getName()));
			if(PermissionsEx.getUser(player).has(cmd.getPermission())){
				if(args.length >0){
					if(args[0].equalsIgnoreCase("on")){
						Utils.chatLocked = true;
					}else{
						if(args[0].equalsIgnoreCase("off")){
							Utils.chatLocked = false;
						}else{
							Messenger.send(player, "Invalid imput: must be off or on.");
							return false;
						}
					}
				}else{
					Utils.chatLocked = !Utils.chatLocked;
					
				}
			}else{
				Messenger.send(player, Main.NO_PERM_MSG);
			}
			if(Utils.chatLocked){
				
				Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been locked by "+ playerUser.getRankTitle()));
			}
			if(!Utils.chatLocked){
				
				Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been unlocked by "+  playerUser.getRankTitle()));
			}
		}else{
			if(args.length >0){
				if(args[0].equalsIgnoreCase("on")){
					Utils.chatLocked = true;
				}else{
					if(args[0].equalsIgnoreCase("off")){
						Utils.chatLocked = false;
					}else{
						return false;
					}
				}
			}else{
				Utils.chatLocked = !Utils.chatLocked;
				
			}
			if(Utils.chatLocked){
				
				Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been locked by &r&LCONSOLE"));
			}
			if(!Utils.chatLocked){
				
				Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been unlocked by &r&LCONSOLE"));
			}
				
			
			
		}
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		if(args.length == 1){
			return Arrays.asList(new String[] {"on","off"});
		}
		return null;
	}
}
