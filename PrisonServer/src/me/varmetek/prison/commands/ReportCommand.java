package me.varmetek.prison.commands;

import me.varmetek.prison.api.User.NotifyMode;
import me.varmetek.prison.utils.Messenger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReportCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String l,
			String[] args) {
		int len = args.length;
		if(len == 0){
			Messenger.send(s, "&c/report [reason]");
			return false;
		}
		StringBuilder sb = new StringBuilder(args[0]);
		if(len>1){
			for(int i = 1; i< len;i++){
				sb.append(args[0]);
			}
		}
		Messenger.send(s, "&aYour report has been sent and shall be handled soon.");
		Messenger.sendNotify(NotifyMode.STAFF, "&4[RP]&7"+sb.toString());
		return true;
	}


}
