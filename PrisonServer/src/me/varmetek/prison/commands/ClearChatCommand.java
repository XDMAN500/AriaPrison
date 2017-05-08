package me.varmetek.prison.commands;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.Rank;
import me.varmetek.prison.api.User;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ClearChatCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			Player player = (Player)sender;
			User playerUser = User.getUser(player);
			
		
			if(PermissionsEx.getUser(player).has(cmd.getPermission())){
				for(int i = 0;i < 201; i++){
					Bukkit.broadcastMessage("         ");
				}
				Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been cleared by "+playerUser.getRankTitle()));
			}else
				Messenger.send(player, Main.NO_PERM_MSG);
		}else{
			for(int i = 0;i<101;i++){
				Bukkit.broadcastMessage("         ");
			}
			
			Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been cleared by "+"&r&lCONSOLE"));
			
		}
	
		return false;
	}
	
}
