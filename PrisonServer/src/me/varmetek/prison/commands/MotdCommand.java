package me.varmetek.prison.commands;

import java.util.HashMap;
import java.util.Map;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.DataManager;
import me.varmetek.prison.api.Rank;
import me.varmetek.prison.api.User;
import me.varmetek.prison.utils.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class MotdCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		int len = args.length;
		
		Map<String,String> argMap = new HashMap<String,String>();
		argMap.put("motd get","Retrieves the message of the day.");
		argMap.put("motd set <msg>","Sets the message of the day.");

		if(cmd.getPermission() != null){
			if(!sender.hasPermission(cmd.getPermission())){
				Messenger.send(sender, Main.NO_PERM_MSG);
				return false;
			}
		}
		
		if(len == 0){
			Messenger.send(sender, "&1-------&4MOTD&7-&aHelp&1-------");
			for(String arg: argMap.keySet()){
				String desc = argMap.get(arg);
				Messenger.send(sender,"&4/&6" + arg + " &8:&3 " + desc);
			}
		}else{
			if(len == 1){
				if(args[0].equalsIgnoreCase("get")){
					Messenger.send(sender, "&9MOTD&8: &r" + DataManager.getMotd());
				}else{
					if(args[0].equalsIgnoreCase("set")){
						Messenger.send(sender, "motd set <msg>");
					}else{
						Messenger.send(sender, "&cValid args are&8:&7 set, get.");
						return false;
					}
				}
			}
			if(len > 1){
				
					if(args[0].equalsIgnoreCase("set")){
						String msg = "";
						for(int i = 1; i< len; i++){
							msg += " " +args[i];
						}
						DataManager.setMotd(msg);
						//Main.config();
						Messenger.send(sender, "&9MOTD has been set to&8: &r" + DataManager.getMotd());
					}else{
						Messenger.send(sender, "&cValid args are&8:&7 set.");
						return false;
					}
				
			}
		}
		return false;
	}

}
