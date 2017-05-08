package me.varmetek.prison.commands;

import java.util.ArrayList;
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

public class MsgCommand implements CommandExecutor, TabCompleter{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			if(cmd.getPermission() != null){
				if(!sender.hasPermission(cmd.getPermission())){
					Messenger.send(sender, Main.NO_PERM_MSG);
					return false;
				}
			}
			int len = args.length;
			Player pl = (Player)sender;
			if(len <2){
				Messenger.send(pl, "&c/msg <player> <message>");
				return false;
			}
			Player plTarg = Bukkit.getPlayer(args[0]);
			if(plTarg == null){
				Messenger.send(pl, "&cThat player is not online.");
				return false;
			}
			String msg = "";
			for(int i = 1; i< len; i++){
				msg += " " + Utils.colorCode(args[i]);
			}
			Messenger.send(plTarg , "&9(&6" + pl.getName() + " &8»&7 me&9)&3:&r" +  msg);
			Messenger.send(pl , "&9(&7me &8»&6 " + plTarg.getName()+ "&9)&3:&r" +  msg);
			List<User> l = new ArrayList<User>( User.users.values());
			
			User msgSender = User.getUser(pl);
			User msgReciever = User.getUser(plTarg);
			l.remove(msgSender);
			l.remove(msgReciever);
			msgSender.setLastMsgReciever(plTarg);
			msgReciever.setLastMsgReciever(pl);
			
			for(User u : l){
				if(u.getPlayer() instanceof Player){
					if(u.getNotifyMode() == User.NotifyMode.STAFF){
						Player y0 = (Player)u.getPlayer();
						Messenger.send(y0 , "&8[&7" + pl.getName() + " &8>&7 " + plTarg.getName()+ "&8]&3:&r" +  msg);
					}
				}
				
			}
					
		}
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
		
		if(args.length == 1){
			return Utils.matchOnlinePlayers(args[0]);
		}
		return null;
	}
	
	
	
	
	
}
