package me.varmetek.prison.commands;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.User;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if(!(sender instanceof Player)) return false;
		if(cmd.getPermission() != null){
			if(!sender.hasPermission(cmd.getPermission())){
				Messenger.send(sender, Main.NO_PERM_MSG);
				return false;
			}
		}
		int len = args.length;
		Player pl = (Player)sender;
		User user = User.getUser(pl);
		Player toReply = user.getLastMsgReciever();
		if( toReply == null){
			Messenger.send(pl, "No one to reply to.");
			return false;
		}
		String msg = "";
		for(int i = 0; i< len; i++){
			msg += " " + Utils.colorCode(args[i]);
		}
		pl.performCommand("msg " + toReply.getName()+ " "+ msg);
		return false;
	}

}
