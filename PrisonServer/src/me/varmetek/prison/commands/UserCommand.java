package me.varmetek.prison.commands;

import me.varmetek.prison.Main;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

public class UserCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
	int len  = args.length;
	if(len == 0){
		
	}else{
		if(len>0)
		{
			if(len == 1)
			{
				if(args[0].equalsIgnoreCase("edit")){
					Messenger.send(sender, "&c/user edit <name> [stat] [modifier] [value]");
				}
			}else{
				if(len>1)
				{
					
					if(len == 2)
					{
						if(args[0].equalsIgnoreCase("edit")){
							Messenger.send(sender, "&c/user edit <name> [stat] [modifier] [value]");
						}
					}else{
						if(len >2)
						{
							if(len == 3)
							{
								if(args[0].equalsIgnoreCase("edit")){
									Messenger.send(sender, "&c/user edit <name> [stat] [modifier] [value]");
								}
							}else{
								if(len>3)
								{
									if(len ==4)
									{
										if(args[0].equalsIgnoreCase("edit")){
											Messenger.send(sender, "&c/user edit <name> [stat] [modifier] [value]");
										}
									}else{
										if(len>4)
										{
											if(len ==5)
											{
												if(args[0].equalsIgnoreCase("edit")){
													OfflinePlayer pl = Utils.getPlayer(args[1]);
													if(args[2].equalsIgnoreCase("set")){
										
														double amount = 0;
														try{
															amount = Double.parseDouble(args[3]);
														}catch(Exception e){
															Messenger.send(sender, "Not a valid amount.");
															return false;
														}
														if(Double.isInfinite( amount)|| Double.isNaN(amount)){
															Messenger.send(sender, "Not a valid amount.");
															return false;
														}
													
															try {
																Economy.setMoney(pl.getName(), amount);
															} catch (
																	NoLoanPermittedException
																	| UserDoesNotExistException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}
													
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
		return false;
	}

}
