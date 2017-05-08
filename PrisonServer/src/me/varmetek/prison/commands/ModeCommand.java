package me.varmetek.prison.commands;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.User;
import me.varmetek.prison.api.User.GameMode;
import me.varmetek.prison.utils.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModeCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(!(sender instanceof Player)){
			return false;
		}
		Player player = (Player)sender;
	
		if(cmd.getPermission() != null){
			if(!sender.hasPermission(cmd.getPermission())){
				Messenger.send(sender, Main.NO_PERM_MSG);
				return false;
			}
		}
		if(args.length  == 0){
			Messenger.send(sender, "/mode <player,spectator,builder>");
			return false;
		}
		GameMode mode = User.GameMode.getByName(args[0]);
		if(mode == null){
			Messenger.send(sender, "/mode <player,spectator,builder>");
			return false;
		}
		
		User user = null;
		
		if(args.length ==1){
			user = User.getUser(player);
			if(player.hasPermission("ariaprison.commands.admin.mode.*")|| player.hasPermission("ariaprison.commands.admin.mode."+mode.name().toLowerCase())){
				//Main.debug("ModeCommand: Giving Mode: "+ mode.name());
				user.setGameMode(mode);
			}else{
				Messenger.send(sender, "You don't have permission to change to mode " + mode.name().toLowerCase()+".");
				return false;
			}
			Messenger.send(sender, "Your Game mode has been set to "+ mode.name().toLowerCase()+".");
				return false;
		}else{
			if(args.length ==2)
			{
				User	cuser = User.getUser(player);
				if(!player.hasPermission("ariaprison.commands.admin.mode.*")){
					Messenger.send(sender, Main.NO_PERM_MSG);
					return false;
				}
					Player targ = Bukkit.getPlayer(args[1]);
					
					if(targ == null){
						Messenger.send(sender, "That player is not online.");
						return false;
					}
					user = User.getUser(Bukkit.getOfflinePlayer(targ.getName()));
					
					if(player.hasPermission("ariaprison.commands.admin.mode.*")|| player.hasPermission("ariaprison.commands.admin.mode."+mode.name().toLowerCase())){
		
						user.setGameMode(mode);
						Messenger.send(targ, "Your Game mode has been set to "+ mode.name().toLowerCase()+".");
				}else{
					Messenger.send(sender, "You don't have permission to change to mode " + mode.name().toLowerCase()+".");
					return false;
				}
				Messenger.send(sender, "Their Game mode has been set to "+ mode.name().toLowerCase()+".");
					return false;
			}
		}
		
	return false;
	}
	
}
