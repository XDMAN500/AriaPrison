package me.varmetek.prison.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.BlockLocation;
import me.varmetek.prison.api.ChestShop;
import me.varmetek.prison.api.GlobalShop;
import me.varmetek.prison.api.ChestShop.Method;
import me.varmetek.prison.api.User;
import me.varmetek.prison.api.User.AutoAction;
import me.varmetek.prison.api.User.GameMode;
import me.varmetek.prison.enums.ParseMethod;
import me.varmetek.prison.mine.Mine;
import me.varmetek.prison.utils.InventoryUtil;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Attachable;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

public class MineEvents implements Listener{
	 
	private static final Set<Enchantment> combineableEnch   = new HashSet<Enchantment>();
	static{
		for(Enchantment ench: Enchantment.values()){
			if(ench.getItemTarget() != EnchantmentTarget.ARMOR &&
					ench.getItemTarget() != EnchantmentTarget.WEAPON &&
					ench.getItemTarget() != EnchantmentTarget.BOW &&
					ench.getItemTarget() != EnchantmentTarget.ARMOR_HEAD&&
					ench.getItemTarget() != EnchantmentTarget.ARMOR_LEGS &&
					ench.getItemTarget() != EnchantmentTarget.ARMOR_TORSO &&
					ench.getItemTarget() != EnchantmentTarget.ARMOR_FEET){
				combineableEnch.add(ench);
				
			}
			
		}
		
	}
	@EventHandler(priority = EventPriority.HIGH,ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent ev){
		final Player player = ev.getPlayer();
		final User user = User.getUser(player);
		if(user.getGameMode()  ==GameMode.BUILDER )return;
		final Block bl = ev.getBlock();
		boolean isIgnored = Main.ignoredWorlds.contains(bl.getWorld().getName());
		if(Mine.getMines().isEmpty()){return;}
		Mine selected = Mine.getMine(bl.getX(), bl.getY(), bl.getZ(),bl.getWorld());
		
		
		if( selected == null){
			
			ev.setCancelled(!isIgnored);
			return;
		}
		if(user.getAutoAction(AutoAction.AURA)){
			ev.setCancelled(true);
			return;
		}
	
			if(selected.isRanked()){
				
				if(user.getMinerRank() < selected.getRankedPos()){
					return;
				}
			}
	
	}
	@EventHandler
	public void onDamageBlock(final BlockDamageEvent ev){
		Player player = ev.getPlayer();
		User user = User.getUser(player);
		Block cblock = ev.getBlock();
		if(user.getGameMode() == User.GameMode.BUILDER )return;
		if(cblock == null)return;
		if(Mine.getMine(cblock.getX(), cblock.getY(), cblock.getZ(), cblock.getWorld())== null)return;

		if(player.getInventory().firstEmpty()== -1){
			//	ActionBarAPI.sendActionBar(player, Utils.colorCode("&c&lINVENTORY IS FULL"));
			//new TMPlayer(player).sendPacket(new ActionbarTitlePacket(Utils.colorCode("&c&lINVENTORY IS FULL")));
				//	Utils.sendActionBar(player, "&c&lINVENTORY IS FULL");
			
				Messenger.send(player,"&c&lINVENTORY IS FULL");
				ev.setCancelled(true);
				return;
			}
		ev.setInstaBreak(true);

		new BukkitRunnable(){
			public void run(){
				if(user.getAutoAction(AutoAction.SMELT)){
					
					for(ItemStack i: player.getInventory()){
						ItemStack result = InventoryUtil.getSmeltResult(i);
						if(result != null){
							int amount  = i.getAmount();
							byte data = i.getData().getData();
							player.getInventory().remove(i);
							player.getInventory().addItem(new ItemStack(result.getType(),amount,data));
						}
					}
					
				}
				if(user.getAutoAction(AutoAction.PACK)){
					
					packItems(player.getInventory());
					}
				if(user.getAutoAction(AutoAction.SELL)){
					sellInventory(player.getInventory(),user);
				}
				
				
				
			}
			
			}.runTaskLater(Utils.PLUGIN,1L);
		//BlockBreakEvent be = new BlockBreakEvent(cblock, player);
		//Utils.PLUGIN.getServer().getPluginManager().callEvent(be);
	}
	@EventHandler(priority = EventPriority.HIGH,ignoreCancelled = true)
	public void onBreak(final BlockBreakEvent ev){
	
		final Player player = ev.getPlayer();
		final User user = User.getUser(player);
		if(user.getGameMode()  ==GameMode.BUILDER )return;
		final Block bl = ev.getBlock();
		if(bl == null)return;
		final ItemStack hand = player.getItemInHand();
		final Collection<ItemStack> drops = bl.getDrops();
		int expBonus = 0;
		if(GlobalShop.PRICES.containsKey(bl.getState().getData())){
			expBonus = (int) Math.floor(GlobalShop.PRICES.get(bl.getState().getData())/10);
		}
		final int exp = (int) (ev.getExpToDrop() +expBonus );
		boolean isIgnored = Main.ignoredWorlds.contains(bl.getWorld().getName());
	
		if(Mine.getMines().isEmpty()){return;}
		Mine selected = Mine.getMine(bl.getX(), bl.getY(), bl.getZ(),bl.getWorld());
	
		
		if((selected == null)){
			ev.setCancelled(!isIgnored);
			return;
		}
	
			ev.setCancelled(true);
			if(selected.isRanked()){
				
				if(user.getMinerRank() < selected.getRankedPos()){
					return;
				}
			}
			new BukkitRunnable(){
				public void run(){
				int xp = exp;
				int lootBonus = 1;
				if(hand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)){
					lootBonus = new Random().nextInt(hand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS))+1;
				}
					if(hand.containsEnchantment(Enchantment.SILK_TOUCH)){
						ItemStack item = new ItemStack(bl.getType(),1,bl.getData());
						item.setAmount(lootBonus);
					
						player.getInventory().addItem(item);
					}else{
						for(ItemStack i: drops){
							
							
							i.setAmount(i.getAmount()*lootBonus);
							
							player.getInventory().addItem(i);
						}
						
					}
					
				
					//xp*= lootBonus;
					
					player.giveExp(xp);
					}
			}.runTask(Utils.PLUGIN);
		
			bl.setType(Material.AIR);



	}
	

	@EventHandler(priority = EventPriority.HIGH,ignoreCancelled = true)	
	public void preCraft(PrepareItemCraftEvent ev){
	
		List<ItemStack> stack = new ArrayList<ItemStack>();
		for(ItemStack i: ev.getInventory().getMatrix()){
			if(i!= null){
				if(i.getType() != Material.AIR)
					stack.add(i);
			}
		}
		
		if(stack.size() == 2)
		{
			
			ItemStack item1 = stack.get(0);
			ItemStack item2 = stack.get(1);
			Map<Enchantment,Integer> toadd = new HashMap<Enchantment,Integer>(item1.getEnchantments());
			Map<Enchantment,Integer> map= item2.getEnchantments();

			
			for(Enchantment ench: map.keySet()){
				
				
				if(toadd.containsKey(ench)){
			
					if(toadd.get(ench).intValue() == map.get(ench).intValue()){
						int lvl = map.get(ench).intValue();
						
						toadd.put(ench, lvl+1);
					}else{
						if(toadd.get(ench).intValue() < map.get(ench).intValue()){
							int lvl = map.get(ench).intValue();
							toadd.put(ench, lvl);
						}
					}
				}else{
					int lvl = map.get(ench).intValue();
					toadd.put(ench, lvl);
				}
				
			}
			for(Enchantment ench: toadd.keySet()){
				if(!combineableEnch.contains(ench))continue;
				ev.getInventory().getResult().addUnsafeEnchantment(ench,toadd.get(ench).intValue());
			}
	
		}
	}
	public void sellInventory(Inventory inv,User user){
		double total = 0.0;
		for(ItemStack item : inv){
			if(item == null)continue;
			if(!GlobalShop.PRICES.containsKey(item.getData()))continue;
			total += GlobalShop.PRICES.get(item.getData())* InventoryUtil.getAmount(inv, item);
			for(ItemStack d:InventoryUtil.getall(inv, item)){
				inv.remove(item);
				
			}
				
			
		}
		total*= user.getTotalBuff();
		try {
			Economy.add(user.getPlayer().getName(), total);
		} catch (NoLoanPermittedException | UserDoesNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@EventHandler(ignoreCancelled = true)
 	public void ClickOnBlock(PlayerInteractEvent ev){
		
		Player player = ev.getPlayer();
		User user = User.getUser(player);
		Block cblock = ev.getClickedBlock();
		if(user.getGameMode() == User.GameMode.BUILDER)return;
		if(cblock == null)return;
		if(!user.getAutoAction(AutoAction.AURA) )return;
		Mine mine = Mine.getMine(cblock.getX(), cblock.getY(), cblock.getZ(), cblock.getWorld());
		if(mine == null)return;
		if(player.getInventory().firstEmpty()== -1 ){
			Messenger.send(player,"&c&lINVENTORY IS FULL");
			return;
		}
		if(!Utils.isRightClicked(ev.getAction()))return;
			
			
					int radius  = 1;
		
					Location loc = cblock.getRelative(ev.getBlockFace().getOppositeFace()	).getLocation();
			
					int minX = loc.getBlockX()-radius;
					int minY = loc.getBlockY()-radius;
					int minZ = loc.getBlockZ()-radius;
					int maxX = loc.getBlockX()+radius;
					int maxY = loc.getBlockY()+radius;
					int maxZ = loc.getBlockZ()+radius;
					for (int x = minX; x <= maxX; x++) {
						for (int z = minZ; z <= maxZ; z++) {
							for (int y = minY; y <= maxY; y++) {
			        		
								if(InventoryUtil.isFull(player.getInventory()))break;
			        			if(!mine.getRegion().contains(x, y, z))continue;
			        			Block bl = player.getWorld().getBlockAt(x, y, z);	
			        			if(bl.getType()==  Material.AIR)continue;
			        					
					        			BlockBreakEvent be = new BlockBreakEvent(bl, player);
					        			Utils.PLUGIN.getServer().getPluginManager().callEvent(be);
			        					bl.getWorld().playEffect(bl.getLocation(), Effect.MOBSPAWNER_FLAMES, 2);
			        				
			        			
							}
						}
					}
					new BukkitRunnable(){
						public void run(){
							if(user.getAutoAction(AutoAction.SMELT)){
								
								for(ItemStack i: player.getInventory()){
									ItemStack result = InventoryUtil.getSmeltResult(i);
									if(result != null){
										int amount  = i.getAmount();
										byte data = i.getData().getData();
										player.getInventory().remove(i);
										player.getInventory().addItem(new ItemStack(result.getType(),amount,data));
									}
								}
								
							}
							if(user.getAutoAction(AutoAction.PACK)){
								
								packItems(player.getInventory());
								}
							if(user.getAutoAction(AutoAction.SELL)){
								sellInventory(player.getInventory(),user);
							}
							
							
							
						}
						
						}.runTaskLater(Utils.PLUGIN,1L);
				
			
		
			
	}
	

	private void packItems(Inventory inv){
		////("Running AP");
		Set<ItemStack> convert = new  HashSet<ItemStack>();
		convert.add(new ItemStack(Material.EMERALD_BLOCK));
		convert.add(new ItemStack(Material.DIAMOND_BLOCK));
		convert.add(new ItemStack(Material.IRON_BLOCK));
		convert.add(new ItemStack(Material.LAPIS_BLOCK));
		convert.add(new ItemStack(Material.REDSTONE_BLOCK));
		convert.add(new ItemStack(Material.COAL_BLOCK));
		convert.add(new ItemStack(Material.SNOW_BLOCK));
		convert.add(new ItemStack(Material.QUARTZ_BLOCK));
		convert.add(new ItemStack(Material.BRICK));
		convert.add(new ItemStack(Material.GLOWSTONE));
		convert.add(new  ItemStack(Material.GOLD_BLOCK));
		convert.add(new  ItemStack(Material.NETHER_BRICK));

	
		for(ItemStack im: convert){
			Recipe r = Bukkit.getRecipesFor(im).get(0);
			
				if((r instanceof FurnaceRecipe))continue;
				
				//Set<ItemStack> itemz = new HashSet<ItemStack>();
				ItemStack needed  = null;
				
				if(r instanceof ShapedRecipe)
				{
					needed =  new ArrayList<ItemStack>( ((ShapedRecipe)r).getIngredientMap().values() ).get(0);
				
				}else{
					if(r instanceof ShapelessRecipe)
					{
						needed =  new ArrayList<ItemStack>( ((ShapelessRecipe)r).getIngredientList()).get(0);
							
					}
				}
			
					int amount = InventoryUtil.getAmount(inv,needed);
					if(amount<9)continue;
					
					int blockNum = (int) Math.floor(amount/9);
					int itemNum = amount% 9;
			
				
					for(int i = 0;i<amount-itemNum;i++){
						inv.removeItem(needed);
					
					}
					for(int i = 0;i<blockNum;i++){
						inv.addItem(im);
					}
			
				
			
		}
		
	}
	@EventHandler(ignoreCancelled = true)
	public void createShop(SignChangeEvent ev){
	
		String[] lines = ev.getLines();
		if(lines[0].equalsIgnoreCase("[chestshop]")|| lines[0].equalsIgnoreCase("[cs]")){
		try{
			
			Sign sign = (Sign)ev.getBlock().getState();
			Block attached = ev.getBlock().getRelative(((Attachable)sign.getData()).getAttachedFace());

			if(!( attached.getState() instanceof InventoryHolder)){Messenger.send(ev.getPlayer(),"&cChestShop must be placed on a container.");return;}
			ChestShop.verifySign(ev.getLines());
			ev.setLine(0, Utils.colorCode("&4"+ev.getPlayer().getName()));
			
			int amount = Integer.parseInt(lines[1]);
			String[] split = lines[2].split(" ",2);
			Method meth = Method.parse(split[0]);
			String  price =split[1];
			split = lines[3].split(":",2);
			Material mat = InventoryUtil.parseMaterial(split[0]);
			int id = 0;
			if(mat == null){
				id = Integer.parseInt(split[0]);
			}
			byte data = 0;
			 if(split.length>1){
					data = Byte.parseByte(split[1]);
			}
			 BlockLocation bl = new BlockLocation(ev.getBlock().getLocation());
			
			 ChestShop cs =new ChestShop(ev.getPlayer().getUniqueId(),bl,((Directional)sign.getData()).getFacing(),meth,price,amount,new MaterialData(mat== null? id:mat.getId(),data),!((org.bukkit.material.Sign)sign.getData()).isWallSign() );
			 	//cs.check();
				Messenger.send(ev.getPlayer(),"&aChestShop succesfuly created!");
			 
		}catch(Exception e){
			e.printStackTrace();
			Messenger.send(ev.getPlayer(),"&c"+e.getMessage());
			return;
		}
	}
	}
	@EventHandler
	public void editShopInv(InventoryClickEvent ev){
	
		Player pl = (Player) ev.getWhoClicked();
		if(!ChestShop.SHOPEDITORS.contains(pl.getUniqueId()))return;
		ev.setCancelled(true);
			
		
		
	}
	@EventHandler
	public void exitShop(InventoryCloseEvent ev){
		ChestShop.SHOPEDITORS.remove(ev.getPlayer().getUniqueId());
	}
	@EventHandler(ignoreCancelled = true)
	public void breakshop(BlockBreakEvent ev){
		Block block = ev.getBlock();
		Player pl = ev.getPlayer();
		ChestShop clicked = null;
		if(ChestShop.isSign(block  )){
			for(ChestShop cs: ChestShop.shops){
				if(cs.getLocation().isSimular(new BlockLocation(block.getLocation()))){
						clicked = cs;
						break;
				}		
			}
		}else{
			if(ChestShop.isContainer(block)){
				for(ChestShop cs: ChestShop.shops){
					if(cs.getAttached()!= null){
						if( new BlockLocation(cs.getAttached().getLocation()).isSimular(new BlockLocation(block.getLocation()) )){
				
						clicked = cs;
						break;
					}
				}
				}
				/*if(clicked != null){
					ev.setCancelled(true);
					return;
				}*/
			}
		}
		if(clicked != null){
			if(clicked.hasAccess(pl)){
				if(!pl.isSneaking()){
					ev.setCancelled(true);
					Messenger.send(pl, "You must be sneaking to break a chestshop");
					return;
				}
				clicked.destroy();
			}else{
				ev.setCancelled(true);
				Messenger.send(pl, "You don't have access to this sho.");
				return;
			}
		}
	}
	@EventHandler
	public void clickOnShop(PlayerInteractEvent ev){
		
	
		if(!ev.hasBlock())return;
		Block block = ev.getClickedBlock();
		Location loc = block.getLocation();
		if(loc == null)return;
		
		Player pl = ev.getPlayer();
		ChestShop clicked = null;
	
		if(ChestShop.isSign(ev.getClickedBlock()  )){
		//	Utils.SERVER.broadcastMessage("Running "+ev.getEventName());
			
			for(ChestShop cs: ChestShop.shops){
				if(cs.getLocation().isSimular(new BlockLocation(loc))){
						clicked = cs;
						break;
				}		
			}
			if(clicked == null){return;}
			
			if(ev.getAction() == Action.LEFT_CLICK_BLOCK){
				//Utils.SERVER.broadcastMessage("LeftCLick");
					if(!pl.isSneaking()){
					pl.openInventory(clicked.getInv());
					if(!clicked.hasAccess(pl)){
						ChestShop.SHOPEDITORS.add(ev.getPlayer().getUniqueId());
						
					}
					Messenger.send(ev.getPlayer(), "&7Viewing the Inventory of this shop.");
					}
			}else{
				if(Utils.isRightClicked(ev.getAction())){
					//Utils.SERVER.broadcastMessage("RightCLick");
					
					try {
					switch(clicked.getMethod()){
						case BUY: 
						if(InventoryUtil.isEmpty(clicked.getInv())){
								Messenger.send(pl, "&cThe shop is empty, try to buy another time.");
								return;
							}
						
						if(InventoryUtil.getall(clicked.getInv(),clicked.getData().toItemStack()).isEmpty()){
							Messenger.send(pl, "&cThe shop is out of stock, try to buy another time.");
							return;
						}
						if(InventoryUtil.isFull(pl.getInventory())){
							Messenger.send(pl, "&cYour inventory is full.");
							return;
						}
						List<ItemStack> itemz = InventoryUtil.getall(clicked.getInv(),clicked.getData().toItemStack());
							//ItemStack item = InventoryUtil.getall(clicked.getInv(),clicked.getData().toItemStack()).get(0).clone();
							//item.setAmount(1);
							int amount = InventoryUtil.getAmount(clicked.getInv(), 
									InventoryUtil.getall(clicked.getInv(),clicked.getData().toItemStack()).get(0));
							double percent = clicked.getAmount() / Math.min(clicked.getAmount(),amount);
							double debt = ParseMethod.parse(clicked.getPrice())*percent;
							if(!Economy.hasEnough(pl.getName(), debt)){
								Messenger.send(pl, "&cYou need &e"+(debt - Economy.getMoney(pl.getName()))+" &cmore money to complete the transaction." );
								return;
							}
							String namez = Utils.getPlayer(clicked.getOwner()).getName();
							Economy.subtract(pl.getName(), debt);
							Economy.add(namez, debt);
							Messenger.send(pl, "&7You bought &a"+clicked.getAmount()+"&7of &a"+InventoryUtil.format(clicked.getData().toItemStack())+"&7 for &a"+Economy.format(debt)+"&7." );
							
							for(int i =0 ; i<clicked.getAmount();i++){
								try{
								ItemStack item = itemz.get(i).clone();
								item.setAmount(clicked.getAmount());
								pl.getInventory().addItem(item);
								clicked.getInv().removeItem(item);
							}catch(IndexOutOfBoundsException e){
								
							}
								
							}
						
						break;
						case SELL:
						if(InventoryUtil.isFull(clicked.getInv())){
							Messenger.send(pl, "&cThe shop is full, try to sell another time.");
							return;
						}
						if(InventoryUtil.getall(pl.getInventory(),clicked.getData().toItemStack()).isEmpty()){
							Messenger.send(pl, "&cNo items to sell");
						return;
						}
						if(InventoryUtil.isEmpty(pl.getInventory())){
						Messenger.send(pl, "&cYour inventory is empty.");
							return;
						}
					List<ItemStack> itemzz = InventoryUtil.getall(pl.getInventory(),clicked.getData().toItemStack());
						//ItemStack item = InventoryUtil.getall(clicked.getInv(),clicked.getData().toItemStack()).get(0).clone();
						//item.setAmount(1);
						int amountz = InventoryUtil.getAmount(pl.getInventory(), 
								itemzz.get(0));
						double percentz = clicked.getAmount() / Math.min(clicked.getAmount(),amountz);
						double debtz = ParseMethod.parse(clicked.getPrice())*percentz;
						String name = Utils.getPlayer(clicked.getOwner()).getName();
						if(!Economy.hasEnough(name, debtz)){
							Messenger.send(pl, "&cThe shop owner does not have enough money to complete the transaction." );
							return;
						}
						Economy.subtract(name, debtz);
						Economy.add(pl.getName(), debtz);
						Messenger.send(pl, "&7You sold &a"+clicked.getAmount()+"&7 of &a"+InventoryUtil.format(clicked.getData().toItemStack())+"&7 for &a"+Economy.format(debtz)+"&7." );
						
					
						for(int i =0 ; i<clicked.getAmount();i++){
							try{
							ItemStack item = itemzz.get(i).clone();
							item.setAmount(clicked.getAmount());
							pl.getInventory().removeItem(item);
							clicked.getInv().addItem(item);
							}catch(IndexOutOfBoundsException e){
								
							}
							
						}
						break;
					}
					pl.updateInventory();
					} catch (NoLoanPermittedException
							| UserDoesNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else{
			if(ChestShop.isContainer(block)){
				for(ChestShop cs: ChestShop.shops){
					if(cs.getAttached()!= null){
						if( new BlockLocation(cs.getAttached().getLocation()).isSimular(new BlockLocation(cs.getAttached().getLocation()) )){
				
						clicked = cs;
						break;
					}
				}
				}
				if(clicked != null){
				if(!clicked.hasAccess(pl)){
					ev.setCancelled(true);
				}
				}
				return;
			}
		}

		
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void enableSignEdit(SignChangeEvent ev){
		ev.setCancelled(false);
	}
	@EventHandler(ignoreCancelled= true)
	public void upgradePick(InventoryClickEvent ev){
		try{
		Player pl =(Player)ev.getWhoClicked();
		ItemStack pick = ev.getCurrentItem();
	
		ItemStack star = ev.getCursor();
		ItemStack master = Main.itemRegistry.get("FUP");
		if(star == null)return;
		if(star.getType() == Material.AIR)return;
		if(pick == null)return;
		if(pick.getType() == Material.AIR)return;

		if(!pick.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS))return;
		if(!star.hasItemMeta())return;
		if(!star.getItemMeta().hasDisplayName())return;

	
		if(InventoryUtil.compareItems(star,master ) && star.getItemMeta().getDisplayName().equals(master.getItemMeta().getDisplayName())){
		
			ev.setCancelled(true);
			int lvl =pick.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)+star.getAmount();
			ItemMeta im = pick.getItemMeta();
			im.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, lvl,true);
			pick.setItemMeta(im);
	
			ev.setCursor(null);
		
			Messenger.send(pl, "&7Pickaxe was upgraded to Fortune "+lvl);
			pl.updateInventory();

		}
		
		}catch(Exception e){
		
		}
	}

}
