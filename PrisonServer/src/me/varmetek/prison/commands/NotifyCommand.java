package me.varmetek.prison.commands;

import java.util.ArrayList;
import java.util.List;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.User;
import me.varmetek.prison.api.User.NotifyMode;
import me.varmetek.prison.utils.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class NotifyCommand implements CommandExecutor,TabCompleter{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		
		if( !(sender instanceof Player))return false;
		int len = args.length;
		Player pl = (Player)sender;
		if(cmd.getPermission() != null){
			if(!sender.hasPermission(cmd.getPermission())){
				Messenger.send(sender, Main.NO_PERM_MSG);
				return false;
			}
		}
		if(len == 0){
			Messenger.send(pl,"/notify <normal,staff,debug>");
			return false;
		}
		
		if(len > 0){
			User.NotifyMode nMode;
			try{
				nMode = User.NotifyMode.valueOf(args[0].toUpperCase());
				if(!sender.hasPermission("ariaprison.notify."+nMode.name().toLowerCase())){
					Messenger.send(sender, Main.NO_PERM_MSG);
					return false;
				}
				User.getUser(Bukkit.getOfflinePlayer(pl.getName())).setNotifyMode(nMode);
				Messenger.send(pl,"You will now get notifications at level "+ nMode.name().toLowerCase());
			}catch(IllegalArgumentException e){
				Messenger.send(pl,"/notify <normal,staff,debug>");
				return false;
			}

			
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] args) {
		int len = args.length;
		if(len ==1){
			List<String> val = new ArrayList<String>();
			for(NotifyMode mn: NotifyMode.values()){
				val.add(mn.name());
			}
			return val;
		}
		return null;
	}

}
