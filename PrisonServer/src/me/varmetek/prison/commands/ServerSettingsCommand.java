package me.varmetek.prison.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.DataManager;
import me.varmetek.prison.api.SellBuff;
import me.varmetek.prison.api.User;
import me.varmetek.prison.api.User.ProducerLevel;
import me.varmetek.prison.mine.Mine;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ServerSettingsCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		int len = args.length;
		Map<String,String> argMap = new HashMap<String,String>();
		argMap.put(label+" setsamples <num> ","sets the max samples for a mine");
		argMap.put(label+" getsamples","sets the max samples for a mine");
		argMap.put(label+" ignoreworld <name> ","ignore block-breaking restictions on that world");
		argMap.put(label+" unignoreworld <name> ","unignore block-breaking restictions on that world");
		argMap.put(label+" setmaxrank <number>","Sets the maximum rank limit");
		argMap.put(label+" getmaxrank","returns the maximum rank limit");
		argMap.put(label+" setrankcost <rank> <money>","sets the expense to rankup to this level");
		argMap.put(label+" getrankcost <rank>","gets the expense to rankup to this level");
		argMap.put(label+" rankups","gets the expense to rankup to this level");
		argMap.put(label+" setrank <player> <rankid> ","gets the expense to rankup to this level");
	
	


	if(len == 0){
		if(cmd.getPermission() != null){
			if(!sender.hasPermission(cmd.getPermission())){
				Messenger.send(sender, Main.NO_PERM_MSG);
				return false;
			}
		}
	
		Messenger.send(sender, "&1-------&4Prison&7-&aHelp&1-------");
		for(String arg: argMap.keySet()){
			String desc = argMap.get(arg);
			Messenger.send(sender,"&4/&6" + arg + " &8:&3 " + desc);
		}
	}else{
			if(len>0)
			{
				
				if(args[0].equalsIgnoreCase("setsamples"))
				{
					if(len == 1){
						Messenger.send(sender, "&c/serset setsamples <num> ");
					}else{
						if(len == 2){
							int toset;
							if(Utils.isInt(args[1])){
								toset = Integer.parseInt(args[1]);
								Messenger.send(sender,"&7Samples has been set to &c"+ toset);
								Main.resetSamples = toset;
							}else{
								Messenger.send(sender,"&cArgument is not a valid number");
							}
						}else{
							Messenger.send(sender, "&c/serset setsamples <num> ");				
						}
					}
				}else{
					
					if(args[0].equalsIgnoreCase("ignoreworld"))
					{
						if(len == 1)
						{
							
							Messenger.send(sender, "&c/serset ignoreworld <name> ");
						}else{
							if(len ==2){
								Main.ignoredWorlds.add(args[1]);
								Messenger.send(sender ,"&7World &c"+ args[1]+"&7 is now ignored.");
							}else{
								Messenger.send(sender, "&c/serset ignoreworld <name> ");
							}
						}
					}else{
						if(args[0].equalsIgnoreCase("unignoreworld"))
						{
							if(len == 1)
							{
								Messenger.send(sender, "&c/serset unignoreworld <name> ");
							}else{
								if(len ==2){
									Main.ignoredWorlds.remove(args[1]);
									Messenger.send(sender ,"&7World &c"+ args[1]+"&7 is no longer ignored.");
								}else{
									Messenger.send(sender, "&c/serset unignoreworld <name> ");
								}
							}
						}else{
							if(args[0].equalsIgnoreCase("ignorelist"))
							{	
									List<String> ign = new ArrayList<String>(Main.ignoredWorlds);
									if(ign.isEmpty()){
										Messenger.send(sender, "&c NO ignored worlds");
									}else{
										Messenger.send(sender, "&cIgnored Worlds:");
										for(int i = 0; i< ign.size();i++){
											Messenger.send(sender, "&b"+(i+1)+") &a"+ign.get(i));
										}
									}
							}else{
								if(args[0].equalsIgnoreCase("getsamples"))
								{
									Messenger.send(sender, "&7The maximum samples are &c"+ Main.resetSamples+"&7.");
								}else{
									if(args[0].equalsIgnoreCase("getmaxrank"))
									{
									
										Messenger.send(sender, "&7The maximum rank is &c"+ Main.maxRank+"&7.");
									}else{
										if(args[0].equalsIgnoreCase("setmaxrank"))
										{
											if(len ==1){
												Messenger.send(sender, "&c/serset setmaxrank <number> ");
											}else{
												if(len ==2){
													int toset;
													if(Utils.isInt(args[1])){
														toset = Integer.parseInt(args[1]);
														Messenger.send(sender,"&7The max rank has been set to has been set to &c"+ toset+"&7.");
														Main.maxRank = toset;
													
													
													}else{
														Messenger.send(sender,"&cArgument is not a valid number");
													}
												}else{
													Messenger.send(sender, "&c/serset setmaxrank <number> ");
												}
											}
										}else{
											if(args[0].equalsIgnoreCase("setrankcost"))
											{
												if(len == 1){
													Messenger.send(sender, "&c/serset setrankcost <rank> <money>");
													
												}else{
													if(len == 2){
														Messenger.send(sender, "&c/serset setrankcost <rank> <money>");
													}else{
														if(len ==3){
															int pos = 0;
															double money = 0;
															if(Utils.isInt(args[1])){
																pos = Integer.parseInt(args[1]);
																if(pos<1){
																	Messenger.send(sender, "&cThe position must be larger than 1.");
																	return false;
																}
															}
															if(Utils.isDouble(args[2])){
																money = Double.parseDouble(args[2]);
																if(Math.signum(money) == -1){
																	Messenger.send(sender, "&cThe money cannot be negative	");
																	return false;
																}
															}
															Main.updateRankupCosts();
															
															if(pos>Main.maxRank){
																Messenger.send(sender, "&cThat rank id is larger than the max rank set!");
																return false;
															}
															Main.rankupcosts.set(pos-1, money);
															Messenger.send(sender, "&7Position &c"+(pos)+" &7 now has an expence of &c"+Utils.formatCur(money)+"&7.");
														}
														
													}
												}
											}else{
												if(args[0].equalsIgnoreCase("getrankcost"))
												{
													if(len == 1){
														Messenger.send(sender, "&c/serset setrankcost <rank> <money>");
														
													}else{
														if(len ==2){
															int pos = 0;
															if(Utils.isInt(args[1])){
																pos = Integer.parseInt(args[1]);
																if(pos<1){
																	Messenger.send(sender, "&cThe position must be larger than 1.");
																	return false;
																}
															}
															if(pos>Main.maxRank){
																Messenger.send(sender, "&cThat rank id is larger than the max rank set!");
																return false;
															}
															Messenger.send(sender, "&7Position &a"+(pos)+" &7 has an expence of &c"+Utils.formatCur(Main.rankupcosts.get(pos-1))+"&7.");
														}
													}
												}else{
													if(args[0].equalsIgnoreCase("rankups"))
													{
														Main.updateRankupCosts();
														for(int i = 0; i<Main.maxRank;i++){
															if(i<Mine.getRankedMines().size()){
																Mine mine =Mine.getRankedMines().get(i); 
																if(mine != null){
																	Messenger.send(sender, "&c"+(i+1)+"&7:("+"&a"+mine.getName()+"&7) - &c"+Utils.formatCur(Main.rankupcosts.get(i)));
																}else{
																	Messenger.send(sender, "&c"+(i+1)+"&7 - &c"+Utils.formatCur(Main.rankupcosts.get(i)));
																}
																
															}else{
																
																Messenger.send(sender, "&c"+(i+1)+"&7-&c"+Utils.formatCur(Main.rankupcosts.get(i)));
															}
														}
													}else{
														if(args[0].equalsIgnoreCase("player")){
															if(len ==1){
																Messenger.send(sender, "&c/serset player <player>");
																return false;
															}else{
																if(Utils.getPlayer(args[1])== null){
																	Messenger.send(sender, "&c Player is not online.");
																	return false;
																}
																OfflinePlayer pl =Utils.getPlayer(args[1]);
																User user = User.getUser(pl);
																if(!pl.isOnline()){
																	DataManager.loadUser(user);
																}
																if(len ==2){
																
																	Messenger.send(sender, "&c/"+label+" player "+pl.getName()+" setrank <rankid>");
																	Messenger.send(sender, "&c/"+label+" player "+pl.getName()+" addbuff <amount> <time>");
																	return false;
																}
																else{
																	if(args[2].equalsIgnoreCase("setrank")){
													
																		if(len <4){
																			Messenger.send(sender, "&c/"+label+" player "+pl.getName()+" setrank <rankid>");
																			return false;
																		}
																		int pos = 0;
																		if(!Utils.isInt(args[3])){
																			Messenger.send(sender, "&cThe number is invalid.");
																			return false;
																		}
																		pos = Integer.parseInt(args[3]);
																		if(pos<1){
																			Messenger.send(sender, "&cThe position must be larger than 1.");
																			return false;
																		}
																		if(pos>Main.maxRank){
																			Messenger.send(sender, "&cThat rank id is larger than the max rank set!");
																			return false;
																		}
																	
																		user.setMinerRank(pos-1);
																		Messenger.send(sender, "&7The user &c"+user.getUserID()+ " &7's rank has been set to &c"+pos+"&7.");
																	}else{
																		if(args[2].equalsIgnoreCase("addbuff")){
																			if(len <5){
																				Messenger.send(sender, "&c/"+label+" player "+pl.getName()+" addbuff <amount> <time>");
																				return false;
																			}
																			if(!Utils.isDouble(args[3])){	
																				Messenger.send(sender, "&cThe number is invalid.");
																				return false;
																			}
																			if(!Utils.isInt(args[4])){	
																				Messenger.send(sender, "&cThe number is invalid.");
																				return false;
																			}
																			
																			float a = Float.parseFloat(args[3]);
																			long s = Long.parseLong(args[4]);
																			user.addBuff(new SellBuff(a,s));
																			Messenger.send(sender, "&7 A sell buff of &c"+a+" &7 has been givin to player &c"+pl.getName()+" &7for &c"+s+" &7seconds.");
																		}else{
																			if(args[2].equalsIgnoreCase("resetprofile")){
																			
																				DataManager.nukeUser(user);
																				Messenger.send(sender, "&7The user data has been reset.");
																			}else{
																				if(args[2].equalsIgnoreCase("randomPick")){
																					if(!pl.isOnline()){
																						Messenger.send(sender, "&c That Player is not online.");
																						return false;
																					}
																					int sel = 9;
																					if(len >3){
																						if(!Utils.isInt(args[3])){
																							Messenger.send(sender, "&c"+args[3]+" is nto a valid number.") ;
																							return false;
																						}
																						sel = Integer.parseInt(args[3]);
																					}
																					ItemStack pick = randomPick(sel);
																					boolean won = pick!= null;
																					if(won){
																						((Player)pl).getInventory().addItem(pick);
																					}
																					Messenger.send(sender, won? "&7 A pickaxe was awarded to &c"+pl.getName()+"&7.":"&7No pickaxe was awarded.");
																					Messenger.send((Player)pl, (won? "&7 A pickaxe was awarded to you.":"&7No pickaxe was awarded."));
																					
																				}else{
																					if(args[2].equalsIgnoreCase("giveItem")){
																						if(len ==3){
																							Messenger.send(sender, "&c/"+label+" "+args[0]+" "+args[1]+" "+args[2]+" <item>");
																							return false;
																						}
																						if(!pl.isOnline()){
																							Messenger.send(sender, "&c That Player is not online.");
																							return false;
																						}
																						ItemStack item = Main.itemRegistry.get(args[3]);
																					//	Bukkit.broadcastMessage(Main.itemRegistry.keySet().toString());
																						if(item == null){
																							Messenger.send(sender, "&cInvalid item.");
																							return false;
																						}
																						((Player)pl).getInventory().addItem(item);
																						Messenger.send(sender, "&7Item added.");
																					}else{
																						if(args[2].equalsIgnoreCase("clearbuffs")){
																							user.clearBuff();
																						}else{
																							if(args[2].equalsIgnoreCase("producer")){
																								if(len ==3){
																									Messenger.send(sender, "&c/"+label+" "+args[0]+" "+args[1]+" "+args[2]+" <arg>");
																									return false;
																								}
																								if(!pl.isOnline()){
																									Messenger.send(sender, "&c That Player is not online.");
																									return false;
																								}
																								try{
																								ProducerLevel pll = ProducerLevel.valueOf(args[3].toUpperCase());
																								user.setProdLvL(pll);
																								Messenger.send((Player)pl, "&c"+pl.getName()+" &7now has a producer rank of &5"+pll.name());
																								}catch(Exception e){
																									Messenger.send((Player)pl, "&4Not a producer rank.");
																									return false;
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
	public static ItemStack randomPick(int sel){
		double chance = 0d;
		Random ran = new Random();
		int level = 0;
		int max = 10;
		double diff =  (10d-sel)/10d;
		for(double i =max;i>0;i--){ 
			chance = -(Math.pow(i/11d, diff))+1d;
			
			if(ran.nextDouble()<= chance ){
					level = (int) i;
					ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE);
					ItemMeta im = pick.getItemMeta();
					im.addEnchant(Enchantment.DIG_SPEED, ran.nextInt(level)+1, true);
					im.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, level, true);
					im.addEnchant(Enchantment.DURABILITY,  ran.nextInt(level)+1, true);
					im.setDisplayName(Utils.colorCode("&6"+level+" &5Pickaxe"));
					pick.setItemMeta(im);
					
					return pick;
					
				
			}
		
		
		}
		return null;
	}
}
