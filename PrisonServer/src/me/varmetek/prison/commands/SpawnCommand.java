package me.varmetek.prison.commands;

import me.varmetek.prison.Main;
import me.varmetek.prison.utils.Messenger;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(!(sender instanceof Player))return false;
			
		Player send = (Player)sender;
		
			if(args.length == 0){
				
					try{
						send.teleport(Main.spawn);
						send.playSound(send.getLocation(),Sound.ENDERDRAGON_WINGS, 1f, 1f);	
					}catch(Exception e){
						send.teleport(send.getWorld().getSpawnLocation());
						
						send.playSound(send.getLocation(),Sound.ENDERDRAGON_WINGS, 1f, 1f);
						Messenger.send(sender, "&cSpawn is not set, teleporting you to the world spawn.");
					}
					
				
			}else{
			if(!sender.hasPermission("ariaprison.command.spawn.admin")){
				Messenger.send(send, "&cNo permission.");
				return false;
			}
			if(args.length ==1){
				if(args[0].equalsIgnoreCase("set")){
					Main.spawn = send.getLocation();
				}else{
					if(args[0].equalsIgnoreCase("del")){
						Main.spawn = null;
					}else{
						Messenger.send(sender, "&cNot a valid sub command");
					}
				}
			}
			}
		
	return false;
	}
}
