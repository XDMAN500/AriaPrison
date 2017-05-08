package me.varmetek.prison.utils;

import java.util.Collection;
import java.util.UUID;

import me.varmetek.prison.api.User;
import me.varmetek.prison.api.User.NotifyMode;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messenger {

	
	public String colorCode;
	


	
	private String pluginPrefix = Utils.colorCode( "&7[&5Prison&7]&r");
	

	public String getPluginPrefix(){
		return pluginPrefix;
	}

	public static void send( CommandSender sender,String message ){

		if(sender == null)return;
		sender.sendMessage( Utils.colorCode(message));		
		
	}
	
	
	

	
	public static void sendGroup(CommandSender[] senderList,String message  ){
		for(CommandSender sender: senderList){
			send(sender,message);
		}
	}
	
	public static void sendGroup(Collection<? extends CommandSender> senderList,String message  ){
		for(CommandSender sender: senderList){
			send(sender,message);
		}
	}
	public static void sendGroupU(String message , Collection<? extends UUID> senderList){
		for(UUID id : senderList){
			Player p = Bukkit.getServer().getPlayer(id);
			if(p== null)continue;
			send(p,message);
		}
	}
	public static void sendNotify(NotifyMode nm,String message){
		for(User u : User.getUsersWithNotify(nm)){
			if(u.getPlayer().isOnline()){
				Messenger.send((Player)u.getPlayer(),message);
			}
		}
	}
	public static void sendAll(String message){
		sendGroup(Utils.getOnlinePlayers(),message);
	}
}
	
	