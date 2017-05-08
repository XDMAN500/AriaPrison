package me.varmetek.prison.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.DataManager;
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

public class LocCommand implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			Player pl = (Player)sender;
			User user = User.getUser(Bukkit.getOfflinePlayer(pl.getName()));
			
			if(PermissionsEx.getUser(pl).has(cmd.getPermission())){
				Messenger.send(pl,Main.NO_PERM_MSG);
				return false;
			}
			int len = args.length;
			
			List<String> argList = new ArrayList<String>(Arrays.asList(new String[] {"set","del","tp"}));
			if(len == 0){
				Messenger.send(pl, "/loc <set,del,tp>");
			}
			if(len > 0){
				if(!argList.contains(args[0].toLowerCase())){
					Messenger.send(pl, "/loc <set,del,tp>");
					return false;
				}
				
				if(args[0].equalsIgnoreCase("set")){
					if(len<2){
						Messenger.send(pl, "/loc set <name>");
						return false;
					}
					DataManager.setLoc(args[1], pl.getLocation());
					Messenger.send(pl, args[1] + " has been set.");
				}
				if(args[0].equalsIgnoreCase("del")){
					if(len<2){
						Messenger.send(pl, "/loc del <name>");
						return false;
					}
					DataManager.removeLoc(args[1]);
					Messenger.send(pl, args[1] + " has been removed.");
				}
				if(args[0].equalsIgnoreCase("tp")){
					if(len<2){
						Messenger.send(pl, "/loc tp <name>");
						return false;
					}
					pl.teleport(DataManager.getLoc(args[1]));
					Messenger.send(pl, args[1] + " has been teleported to.");
				}
				if(args[0].equalsIgnoreCase("list")){
					
					
					Messenger.send(pl, "&aLocs&8: "+Utils.listToString(new ArrayList<String>(DataManager.getLocList())));
				}
			}
		}
		return false;
	}
}
