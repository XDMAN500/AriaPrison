package me.varmetek.prison.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.DataManager;
import me.varmetek.prison.api.Rank;
import me.varmetek.prison.api.User;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class LookUpCommand implements CommandExecutor,TabCompleter {

	

	public boolean onCommand(final CommandSender sender, Command cmd, String label,String[] args) {
		int len = args.length;
		if(len == 0){
			Messenger.send(sender, "/lookup <player>");
			return false;
		}
		if(sender instanceof Player){
			if(cmd.getPermission() != null){
				if(!sender.hasPermission(cmd.getPermission())){
					Messenger.send(sender, Main.NO_PERM_MSG);
					return false;
				}
			}
		}
		final OfflinePlayer pl = Utils.getPlayer(args[0]);
		new BukkitRunnable(){
			public void run(){
				UUID id = pl.getUniqueId();
				DataManager.checkForMatches(id);
				Messenger.send(sender, "&5====&9LookUp&5======");
				if(DataManager.isBanListExist(false)){
					String name = "";
					name = "&a"+pl.getName();
					/*if(Main.playerBanList.contains(pl.getName())){
						name = "&4"+pl.getName();
					}else{
						name = "&a"+pl.getName();
					}*/
						Messenger.send(sender, "&3Player&8: "+name);
				}else{
					Messenger.send(sender, "&3Player&8:&a "+pl.getName());
				}
				List<String> list;
				list =new ArrayList<String>(DataManager.getDataBaseEntryNames(id));
				list.remove(pl.getName());
				if(DataManager.isBanListExist(false)){
					for(int i = 0 ;i< list.size(); i++){
						String s = list.get(i);
						list.set(i, "&4"+s+"&a");
						/*if(Main.playerBanList.contains(s)){
							list.set(i, "&4"+s+"&a");
						}else{
							list.set(i, "&a"+s+"&a");
						}*/
					}
				}
				Messenger.send(sender, "&3Alts&8:&a "+list);
				Messenger.send(sender, "&3Ips&8:&a "+DataManager.getDataBaseEntryIps(id));
				if(pl.isOnline()){
					Messenger.send(sender, "&3Address&8:&a" + ((Player)pl).getAddress().getAddress().getHostAddress());
				}else{
					Messenger.send(sender, "&3Address:&8&a " + "Not online");
				}
				Messenger.send(sender, "&5====================");
		}
	}.runTaskAsynchronously(Main.getPluginInstance());
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		if(args.length == 1){
		
			return Utils.matchOfflinePlayers(args[0]);
			
		}
		return null;
		
	}

}
