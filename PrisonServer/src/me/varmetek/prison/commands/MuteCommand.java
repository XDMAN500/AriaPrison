package me.varmetek.prison.commands;

import java.util.ArrayList;
import java.util.Calendar;
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

public class MuteCommand implements CommandExecutor,TabCompleter {

public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		/*if(sender instanceof Player){
			User user = User.getUser(Bukkit.getOfflinePlayer(((Player)sender).getName()) );
			if(!user.hasPermission(Rank.TrialMod)){
				Messenger.send(Main.NO_PERM_MSG, sender, Messenger.INFO);
				return false;
			}
		}*/
	if(cmd.getPermission() != null){
		if(!sender.hasPermission(cmd.getPermission())){
			Messenger.send(sender, Main.NO_PERM_MSG);
			return false;
		}
	}
		
		if(args.length == 2){
			int time = 0;
			try{
				time =Math.abs(Integer.parseInt(args[1]));
			}catch(NumberFormatException e){
				Messenger.send(sender, "The Argument requires a number.");
				return false;
			}
			Player toMute = Bukkit.getPlayer(args[0]);
			if(toMute == null){
				Messenger.send(sender, "Player is not online.");
				return false;
			}
			User user = User.getUser(Bukkit.getOfflinePlayer(toMute.getName()));
			
			user.addMuteExpire(time);
		
			 Calendar cd = Calendar.getInstance();
			Calendar bd = user.getMuteExpire();
			
			String message = Utils.getCalendarDiff(cd, bd);
			
			
			
			
			
			Bukkit.broadcastMessage(Utils.colorCode("&8&o"+toMute.getName()+" &8 has been muted for "+ message+"."));
		}else{
			if(args.length == 1){
				Player toMute = Bukkit.getPlayer(args[0]);
				if(toMute == null){
					Messenger.send(sender, "&cPlayer is not online.");
					return false;
				}
				User user = User.getUser(Bukkit.getOfflinePlayer(toMute.getName()));
				if(user.getMuteExpire() == null || user.getMuteExpire().after(Calendar.getInstance())){
					user.resetMuteExpire();
					Bukkit.broadcastMessage(Utils.colorCode("&8&o"+toMute.getName()+" &8 has been unmuted."));
					//Bukkit.broadcastMessage(Utils.colorCode("&6&l"+toMute.getName()+" &a has been unmuted."));
				}else{
					Bukkit.broadcastMessage(Utils.colorCode("&8&o"+toMute.getName()+" &8 has been permently muted."));
					user.setMuteExpire(null);
				}
			}else{
				Messenger.send(sender, "&C/mute <player> <time>");
			}
		}
		return false;
}
	@SuppressWarnings("deprecation")
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		if(args.length == 0){
			List<String> l = new ArrayList<String>();
			//BanList b = Bukkit.getBanList(BanList.Type.NAME);
			//Set<BanEntry> be = Bukkit.getBanList(BanList.Type.NAME).getBanEntries();
			//b.addBan(arg0, arg1, arg2, arg3)
			for(Player e:Bukkit.getOnlinePlayers()){
				l.add(e.getName());
			}
			
			
			return l;
		}
		return null;
	}

}
