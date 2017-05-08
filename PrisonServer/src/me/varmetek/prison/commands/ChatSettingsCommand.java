package me.varmetek.prison.commands;

import java.util.ArrayList;
import java.util.Arrays;

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

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ChatSettingsCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		int len = args.length;
		boolean chatMode = Utils.chatLocked;
		PermissionUser puser;
		if(len>0  ){

			if(cmd.getPermission() != null){
				if(!sender.hasPermission(cmd.getPermission())){
					Messenger.send(sender, Main.NO_PERM_MSG);
					return false;
				}
			}
			
			if(!(new ArrayList<String>( Arrays.asList( new String[] {"lock","unlock","delay","clear"} ) ).contains( args[0].toLowerCase() )) ){
				Messenger.send(sender, "lock,unlock,delay");
				return false;
			}
			if(args[0].equalsIgnoreCase("clear")){
				for(int i = 0;i < 201; i++){
					Bukkit.broadcastMessage("         ");
				}
				if(sender instanceof Player){
					User user = User.getUser(Bukkit.getOfflinePlayer(((Player)sender).getName()));
					Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been cleared by "+user.getRankTitle()));

				}else{
					Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been cleared by "+"&r&lCONSOLE"));
				}
			}
			
			if(args[0].equalsIgnoreCase("lock")){
				if(len >1){
					if(args[1].equalsIgnoreCase("on")){
						Utils.chatLocked = true;
					}else{
						if(args[1].equalsIgnoreCase("off")){
							Utils.chatLocked = false;
						}else{
							Messenger.send(sender, "Invalid imput: must be off or on.");
							return false;
						}
					}
				}else{
					Utils.chatLocked = !Utils.chatLocked;
					}	
			}
			
			
			if(args[0].equalsIgnoreCase("unlock")){
				Utils.chatLocked = false;
			}
			
			if(args[0].equalsIgnoreCase("delay")){
				if(len >1){
					int amount;
					try{
						amount= Integer.parseInt(args[1]);
					}catch(NumberFormatException e){
						Messenger.send(sender, "Invalid imput: Must be a whole number.");
						return false;
					}
					amount = (int) Math.abs(amount);
					Utils.chatDelay = amount;
					Messenger.send(sender , "Chat delay has been set to "+(amount)+" miliseconds.");
				}else{
					Messenger.send(sender , "/chatsettings delay <number>");
				}
			}
			if(!args[0].equalsIgnoreCase("delay")){
				if(chatMode == Utils.chatLocked){
				
					Messenger.send(sender , "Chat is already set to that mode.");
					return false;
				}else{
					if(sender  instanceof Player){
						User playerUser = User.getUser( Bukkit.getOfflinePlayer(((Player)sender).getName() ));
						if(Utils.chatLocked)
							Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been locked by "+ playerUser.getRankTitle()));
						else
							Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been unlocked by "+ playerUser.getRankTitle()));
						
							
					}else{
						if(Utils.chatLocked)
							Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been locked by &r&LCONSOLE"));
						else
							Bukkit.broadcastMessage(Utils.colorCode("&a&oChat has been unlocked by &r&LCONSOLE"));
						
						
					}
				}
			}
		}else{
			Messenger.send(sender, "lock,unlock,delay");
		}
	
		
	
		
		return false;
	}
}
