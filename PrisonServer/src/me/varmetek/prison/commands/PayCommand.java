package me.varmetek.prison.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.varmetek.prison.Main;
import me.varmetek.prison.enums.ParseMethod;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;
import net.ess3.api.Economy;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

public class PayCommand implements CommandExecutor,TabCompleter{


	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getPermission() != null){
			if(!sender.hasPermission(cmd.getPermission())){
				Messenger.send(sender, Main.NO_PERM_MSG);
				return false;
			}
		}
		if(sender instanceof Player){
			Player giver = (Player) sender;
			
				
				if(args.length == 2){
					OfflinePlayer receipent  = Utils.getPlayer(args[0]);
					double value = ParseMethod.parse(args[1]);
					if(Double.isNaN(value)){
						Messenger.send(sender, "&cInvalid input.");
						return false;
					}

						try {
							if(Economy.hasEnough(giver.getName(),value)){
								Economy.subtract(giver.getName(), value);
								Economy.add(receipent.getName(), value);
								
								Messenger.send(giver, "&7You payed "+ receipent.getName()+" &c"+Utils.formatCur(value)+".");
								if(receipent.isOnline()){
									Messenger.send((Player)receipent, "&7"+giver.getName()+" has payed you &c"+Utils.formatCur(value)+".");
								}
							}else{
								Messenger.send(giver, "&cYou need &e"+Utils.formatCur(value - Economy.getMoney(giver.getName())) +" &cmore money in order to complete the transaction.");
							}
						} catch (NoLoanPermittedException
								| UserDoesNotExistException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		
					
				}else{
					Messenger.send(giver, "&c/pay <name> <dinero>");
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

