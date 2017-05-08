package me.varmetek.prison.commands;

import me.varmetek.prison.api.User;
import me.varmetek.prison.api.User.AutoAction;
import me.varmetek.prison.utils.Messenger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AutoActionCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender s, Command c, String l,String[] args) {
		if(!(s instanceof Player))return false;
		Player player = (Player)s;
		User user = User.getUser(player);
		int len = args.length;
		if(len ==0){
			for(AutoAction a: AutoAction.values()){
				Messenger.send(s, "&7/"+l+" "+a.name().toLowerCase()+" <on,off>");
			}
		}else{
			if(len>0){
				
				AutoAction aa;
				try{
				aa= AutoAction.valueOf(args[0].toUpperCase());
				}catch(Exception e){
					Messenger.send(s, "&cInvalid Argument");
					return false;
				}
		
				if(!s.hasPermission("ariaprison.autoaction.*")){
					if(!s.hasPermission("ariaprison.autoaction."+aa.name().toLowerCase()) ){
						Messenger.send(s, "&cYou don't have permission to change the auto action &e"+aa.name()+"&c.");
						return false;
					
					}
				}
				if(len == 1){
					user.toggleAutoAction(aa);
					
				}else{
					if(len == 2){
						user.setAutoAction(aa,args[1].equalsIgnoreCase("on"));
					}
				}
				Messenger.send(player,"&7AutoAction &a"+aa.name() +" &7has been turned "+ (user.getAutoAction(aa)? "&a&lON":"&c&lOFF"));
			}
		}
		return false;
	}
	

}
