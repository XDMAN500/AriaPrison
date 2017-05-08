package me.varmetek.prison.commands;

import java.util.HashMap;
import java.util.Map;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.DataManager;
import me.varmetek.prison.api.User;
import me.varmetek.prison.mine.LocalBlock;
import me.varmetek.prison.mine.Mine;
import me.varmetek.prison.utils.InventoryUtil;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class MineSetCommand implements CommandExecutor {


	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		int len = args.length;
	

		Map<String,String> argMap = new HashMap<String,String>();
		argMap.put("mine <rank> ","Teleports you to a ranked mine");
		argMap.put("mine <name> info ","Provides info of the mine.");
		argMap.put("mine <name> create ","Creates a mine");
		argMap.put("mine <name> remove ","Remove a mine");
		argMap.put("mine <name> reset ","resets a mine");
		argMap.put("mine <name> setregion","Changes the region of the mine" );
		argMap.put("mine <name> setdelay <minutes> ","Sets the time in between resets");
		argMap.put("mine <name> setspawn ","Sets the spawn for a mine");
		
		argMap.put("mine <name> blocks add <id> <data> <percentage> ","adds a block to a mine");
		argMap.put("mine <name> blocks rem <id> <data>","deletes a block from a mine");
		argMap.put("mine <name> blocks setchance <pos> <chance>","Sets a block's chance of spawning");
		argMap.put("mine <name> blocks swap <item> <new item>","Swaps an item in the mine for a different one.");
		
		
		argMap.put("mine <name> rank reg ","Puts the mine on the rankup system");
		argMap.put("mine <name> rank remove","Puts the mine off the rankup system");
		argMap.put("mine <name> rank setpos <pos>","Puts the mine on a spot of the rankup system");
		
		
	
		if(cmd.getPermission() != null){
			if(!sender.hasPermission(cmd.getPermission())){
				Messenger.send(sender, Main.NO_PERM_MSG);
				return false;
			}
		}
		boolean admin = sender.hasPermission("ariaprison.commands.mine.admin");
	if(len == 0){

		if(admin){
			Messenger.send(sender, "&1-------&4Mine&7-&aHelp&1-------");
			for(String arg: argMap.keySet()){
			String desc = argMap.get(arg);
			Messenger.send(sender,"&4/&6" + arg + " &8:&3 " + desc);
			}
		}else{
			Messenger.send(sender,"&4/&6" + "mine <rank>" + " &8:&3 " + "Teleports you to a ranked mine");
		}
	}else{

		if(len>0)
		{
			Mine mine = Mine.getMine(args[0]);
			if(len ==1){
			if(args[0].equalsIgnoreCase("listall"))
			{
				if(Mine.getMines().isEmpty()){Messenger.send(sender, "&CNo mines");}else{
					int pos = 1;
					for(Mine min :Mine.getMines()){
						Messenger.send(sender, "&b"+pos+") &c"+min.getName());
						pos++;
					}
				}
			}else{
				if(args[0].equalsIgnoreCase("saveall"))
				{	if(sender instanceof Player){
						if(!admin){
							Messenger.send(sender, Main.NO_PERM_MSG+"so save all");
							return false;
						}
					}
					if(Mine.getMines().isEmpty()){
						Messenger.send(sender, "&cNothing to save");
					}
					
					DataManager.saveAllMines();
					Messenger.send(sender, "&aSaved all mines");
					
				}else{
					if(args[0].equalsIgnoreCase("loadall"))
					{
						
						if(sender instanceof Player){
							if(!admin){
								Messenger.send(sender, Main.NO_PERM_MSG+"so save all");
								return false;
							}
						}
						DataManager.loadAllMines();
						Messenger.send(sender, "&aLoadedMines");
						
					}else{ 
						
							if(mine == null)
							{
								Messenger.send(sender, "&cMine doesn't exist.");
								return false;
							}
								if(mine.isRanked())
							{
								if(sender instanceof Player)
								{
									User user = User.getUser(((Player)sender));
									if(user.getMinerRank()>= mine.getRankedPos()){
										((Player)sender).teleport(mine.getSpawn());
										Messenger.send(sender, mine.getIcon()+" &7Telepoting to mine.");
												
									}else{
										Messenger.send(sender, mine.getIcon()+" &cYour rank is not high enough to teleport");
									}
								}
							}else{
								Messenger.send(sender, "&cMine cannot be teleported to.");
										
							}
						
							
				
					
					}
				}
			}
		}else{
			;
			if(args[1].equalsIgnoreCase("info"))
			{
				if(mine == null)
				{
					Messenger.send(sender, "&cMine doesn't exist.");
					return false;
				}
				Messenger.send(sender, "&a&l====&4|&7"+ mine.getName() +"&4|&a&l====");
				Messenger.send(sender,"&eRanked:" + (mine.isRanked() ? "&aTrue- "+mine.getRankedPos(): "&cFalse"));
				Messenger.send(sender,"&eReset Delay: " +(mine.getDelay() > 0 ? mine.getDelay()/(60)+ " minutes" : "Doesn't reset"));
				Messenger.send(sender,"&eHasBlocks:" + (mine.getBlockList().isEmpty()? "&cNo": "&aYes"));
				if(!mine.getBlockList().isEmpty()){
					Messenger.send(sender, "&cBlockid &7&l: &4Block data &8&l-- &ePercentage");
					int pos = 1;
					for(LocalBlock bl: mine.getBlockList()){
						
					Messenger.send(sender, "&b"+pos+") "+"&c"+bl.getID()+" &7&l: &4"+bl.getData()+" &8&l-- &e"+bl.getChance());
					pos++;
					}
				}
				
			}else{
			
				if(args[1].equalsIgnoreCase("create"))
				{
					if(mine == null && sender instanceof Player){
						Player player = (Player)sender;
						Selection sel = Main.we.getSelection(player);
						if(sel == null){
							Messenger.send(player, "You must select the area first.");
							return false;
						}
						
						mine = Mine.create (args[0],
								new ProtectedCuboidRegion("MineMc_"+args[0],
										new BlockVector(sel.getNativeMaximumPoint()), new BlockVector(sel.getNativeMinimumPoint()))
								,player.getWorld(), 
								player.getLocation());
						
						Messenger.send(
								player
											, ( mine == null) ?
													"&cMine Already Exists" : "&aMine was created");
					}
					return true;
				}else{
				
					if(args[1].equalsIgnoreCase("remove"))
					{
						if(mine == null)
						{
							Messenger.send(sender, "&cMine doesn't exist.");
							return false;
						}
						mine.delete();
						Messenger.send(sender, mine.getIcon()+" &7Mine removed.");
						return true;
						
					}else{
						if(args[1].equalsIgnoreCase("reset"))
						{
							if(mine == null)
							{
								Messenger.send(sender, "&cMine doesn't exist.");
								return false;
							}
							mine.resetMine();;
							Messenger.send(sender, mine.getIcon()+" &7Mine reset.");
							return true;
						}else{
							if(args[1].equalsIgnoreCase("setspawn"))
							{
								if(mine == null)
								{
									Messenger.send(sender, "&cMine doesn't exist.");
									return false;
								}
								if(!(sender instanceof Player)){
									Messenger.send(sender, "&cYou may not set the mine spawn.");
									return false;
								}
								Player player = (Player)sender;
								mine.setSpawn( player.getLocation() );
								Messenger.send(sender,mine.getIcon()+" &7Mine spawn set.");
								return true;
							}else{
							
									if(args[1].equalsIgnoreCase("blocks"))
									{
										if(mine == null)
										{
											Messenger.send(sender, "&cMine doesn't exist.");
											return false;
										}	
										if(len == 2){
											Messenger.send(sender, "&c/mine <name> blocks add <item> <percentage> ");
											Messenger.send(sender, "&c/mine <name> blocks rem <item>");
											Messenger.send(sender, "&c/mine <name> blocks setchance <item> <chance>");
											Messenger.send(sender, "&c/mine <name> blocks swap <item> <new item>");
											return false;
										}
										if(args[2].equalsIgnoreCase("add")){
											if(len < 5){
												Messenger.send(sender, "&c/mine <name> blocks add <item> <percentage> ");
												return false;
											}
											LocalBlock b;
											MaterialData data;
											float per = 0;
											boolean result;
											
											if(!(Utils.isDouble(args[4]))){
												Messenger.send(sender, "&cNot a valid number.");
												return false;
												
											}
											data = InventoryUtil.parseData(args[3]);
											if(data == null){
												Messenger.send(sender, "&cNot a valid item.");
												return false;
												
											}
											per = Float.parseFloat(args[4]);
											
											b = new LocalBlock(data, per);
											result =mine.addLocalBlock(b);
											Messenger.send(sender, result? mine.getIcon() +"&7Block &c"+b.getID()+":"+b.getData() +" &7has been added with a chance of &c"+per+"&7.": 
												mine.getIcon() +" &cBlock "+b.getID()+":"+b.getData() +" already exist in this mine.");
										}else{
											if(args[2].equalsIgnoreCase("rem")){
												if(len<4){
													Messenger.send(sender, "&c/mine <name> blocks rem <item>");
													return false;
												}
												MaterialData data = InventoryUtil.parseData(args[3]); 
												boolean result;
												if(data == null){
													Messenger.send(sender, "&cNot a valid item.");
													return false;
													
												}
												if(mine.getBlock(data) == null){
													Messenger.send(sender, "&cThat item is not in this mine.");
													return false;
												}
											
												
												result = mine.removeLocalBlock(data);
												Messenger.send(sender, result? mine.getIcon()+" &7Block &c"+data.getItemTypeId()+":"+data.getData() +" &7has been removed. ": 
												mine.getIcon() +" &cBlock "+data.getItemTypeId()+":"+data.getData() +"doesn't exist in this mine.");
												return true;
											}else{
										
														if(args[2].equalsIgnoreCase("setchance")){
															if(len<5){
																Messenger.send(sender, "&c/mine <name> blocks setchance <item> <chance>");
																return false;
															}
															MaterialData data = InventoryUtil.parseData(args[3]);
															float ch = 0;
															boolean result;
															
															if(data == null){
														
																Messenger.send(sender, "&cNot a valid item.");
																return false;
															}
															if(mine.getBlock(data) == null){
																Messenger.send(sender, mine.getIcon() +" &cThat item is not in this mine.");
																return false;
															}
															if(Utils.isDouble(args[4])){
																
																ch = Float.parseFloat(args[4]);
															}else{
																Messenger.send(sender, "&cNot a valid number.");
																return false;
															}
														
															result = mine.setLocalBlockChance(data, ch);
															Messenger.send(sender, result?mine.getIcon() + " &7Chance  of &c" +data.getItemTypeId()+":"+data.getData()+ " &7has been set to &c"+ch+"%&7.": 
																mine.getIcon() +" &cNo block at that position.");
															return true;
														}else{
															if(args[2].equalsIgnoreCase("swap")){
																
																if(len<5){
																	Messenger.send(sender, "&c/mine <name> blocks swap <item> <new item>");
																	return false;
																}
															
																boolean result;
																MaterialData data = InventoryUtil.parseData(args[3]);
																MaterialData newItem = InventoryUtil.parseData(args[4]);
														
																if(newItem == null){
																	
																	Messenger.send(sender, "&cNot a valid item.");
																	return false;
																}
																
																if(data == null){
																	
																	Messenger.send(sender, "&cNot a valid item.");
																	return false;
																}
																if(mine.getBlock(data) == null){
																	Messenger.send(sender, mine.getIcon() +" &cThat item is not in this mine.");
																	return false;
																}
																if(mine.getBlock(newItem) != null){
																	Messenger.send(sender, mine.getIcon() +" &cCan't swap with an item that is already in the mine.");
																	return false;
																}
																result = mine.swapLocalBlock(data,newItem);
																Messenger.send(sender, result? mine.getIcon() + " &7Item &c"+data.getItemTypeId()+":"+data.getData()+" &7has been change to &c&c"+newItem.getItemTypeId()+":"+newItem.getData()+"&7.": 
																	mine.getIcon() +" &cNo block at that position.");
																return true;
															}else{
																Messenger.send(sender, "&cInvalid command "+"/"+label+" "+args[0]+" "+args[1]+" "+args[2]);
																Messenger.send(sender, "&c/mine <name> blocks add <item> <percentage> ");
																Messenger.send(sender, "&c/mine <name> blocks rem <item>");
																Messenger.send(sender, "&c/mine <name> blocks setchance <item> <chance>");
																Messenger.send(sender, "&c/mine <name> blocks swap <item> <new item>");
																
															}
										
												}
											}
										}
									
									}else{
									
											if(args[1].equalsIgnoreCase("setdelay"))
											{
												if(mine == null)
												{
													Messenger.send(sender, "&cMine doesn't exist.");
													return false;
												}	
												if(len<3){
													Messenger.send(sender, "&c/mine <name> setdelay <seconds>");
													return false;
												}
												int time = -1;
												if(Utils.isInt(args[2])){
													
													time = Integer.parseInt(args[2])*60;
												}else{
													Messenger.send(sender, "&cNot a valid number.");
													return false;
												}
												mine.setDelay(time);
												Messenger.send(sender, mine.getIcon() + " &7Delay has been set to &c"+args[2]+ " &7minutes.");
											}else{
												if(args[1].equalsIgnoreCase("rank"))
												{
													if(mine == null)
													{
														Messenger.send(sender, "&cMine doesn't exist.");
														return false;
													}
													if(len == 2){
														Messenger.send(sender, "&c/mine <name> rank reg  ");
														Messenger.send(sender, "&c/mine <name> rank setpos  ");
														Messenger.send(sender, "&c/mine <name> rank remove  ");
														return false;
													}
													if(args[2].equalsIgnoreCase("reg")){
														if(Mine.getRankedMines().contains(mine)){
															Messenger.send(sender, mine.getIcon() +" &cMine is already ranked at &e#"+(mine.getRankedPos()+1));
															return false;
														}
														if(Mine.getRankedMines().size() >= Main.maxRank){
															Messenger.send(sender, "&cThe maximum number of ranked mines has been reached, no more can be added.");
															return false;
														}
														Mine.getRankedMines().add(mine);
														Messenger.send(sender, mine.getIcon() + "&7Mine is now ranked at &c#"+(mine.getRankedPos()+1));
													}else{
														if(args[2].equalsIgnoreCase("setpos")){
															int id = -1;
															if(Utils.isInt(args[3])){
																
																id = Integer.parseInt(args[3]);
															}else{
																Messenger.send(sender, "&cNot a valid number.");
																return false;
															}
															if(id<1){
																Messenger.send(sender, "&cThe position must be larger than 1.");
																return false;
															}
															if(id>Mine.getRankedMines().size()){
																Messenger.send(sender, "&cThe position is too large, at max must be &e"+Mine.getRankedMines().size()+"&c.");
																return false;
															}
															Mine.getRankedMines().remove(mine);
															Mine.getRankedMines().add(id-1, mine);
															Messenger.send(sender, mine.getIcon() +" &7Mine is now ranked at &c#"+(mine.getRankedPos()+1));
														}else{
															if(args[2].equalsIgnoreCase("remove")){
																if(Mine.getRankedMines().contains(mine)){
																	Mine.getRankedMines().remove(mine);
																	Messenger.send(sender, mine.getIcon() +" &7Mine is no longer ranked.");
																	return false;
																}else{
																	Messenger.send(sender, mine.getIcon() +" &cMine was not ranked already.");
																	return false;
																}
															}
														}
													}
													
													
												}else{
													if(args[1].equalsIgnoreCase("setregion"))
													{
														if(mine == null)
														{
															Messenger.send(sender, "&cMine doesn't exist.");
															return false;
														}	
														if(!(sender instanceof Player)){
															Messenger.send(sender, "Only players may use this command");
															return false;
														}
														Player player = (Player)sender;
														Selection sel = Main.we.getSelection(player);
														if(sel == null){
															Messenger.send(sender, "&a You need to make a selection");
															return false;
														}
														mine.setRegion(new BlockVector(sel.getNativeMaximumPoint()), new BlockVector(sel.getNativeMinimumPoint()));
														Messenger.send(sender, mine.getIcon() +"&7 Region has been reset");
													}else{
														
															Messenger.send(sender, "&c No such Command");
														
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
	
	
		return true;
	}
	

}

