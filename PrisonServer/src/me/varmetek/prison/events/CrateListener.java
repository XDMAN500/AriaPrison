package me.varmetek.prison.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.SellBuff;
import me.varmetek.prison.api.User;
import me.varmetek.prison.commands.ServerSettingsCommand;
import me.varmetek.prison.utils.InventoryUtil;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

public class CrateListener implements Listener{
	private static final List<ItemStack> keyTeir =  new ArrayList<ItemStack>();
	private static final Set<Enchantment> armorEnch = new HashSet<Enchantment>();
	private static final String PREFIX = "&9Crate> ";
	static{
		for(Enchantment e: Enchantment.values()){
			if(e.getItemTarget()  == EnchantmentTarget.ARMOR){
				armorEnch.add(e);
			}
		}
		keyTeir.addAll(Arrays.asList(new ItemStack[]{ Main.itemRegistry.get("BVK"), Main.itemRegistry.get("DVK"),Main.itemRegistry.get("UVK"),Main.itemRegistry.get("LVK")}));
	}
	@EventHandler
	public void onCrateInteract(PlayerInteractEvent ev){
		Player pl = ev.getPlayer();
		if(!ev.hasBlock())return;
		
		ItemStack inHand =new ItemStack( ev.getPlayer().getItemInHand());
		if(!inHand.hasItemMeta())return;
		if(!inHand.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS))return;
		if(!inHand.getItemMeta().hasDisplayName())return;
		inHand.setAmount(1);
		int slot = -1;

		for(ItemStack i : keyTeir){
			//Bukkit.broadcastMessage("1:"+i.toString());
		//	Bukkit.broadcastMessage("2:"+inHand.toString());
			if(i.getItemMeta().getDisplayName().equals(inHand.getItemMeta().getDisplayName()) && InventoryUtil.compareItems(i, inHand)){
			
				slot = keyTeir.indexOf(i);
				break;
			}
		}
		if(ev.getClickedBlock().getType() == Material.SLIME_BLOCK && slot != -1){
			//Bukkit.broadcastMessage("slot: "+slot);

			
			Messenger.send(pl, "&7You used a "+inHand.getItemMeta().getDisplayName());
			pl.getInventory().removeItem(inHand);
			pl.playEffect(ev.getClickedBlock().getLocation(), Effect.MOBSPAWNER_FLAMES,1);
			try{
			spendKey(ev.getPlayer(),slot);
		}catch(Exception e){
			Utils.debug("&4[ERROR] cant use crate key "+(e));
			e.printStackTrace();
		}
		}
	}
	public void spendKey(Player player,int teir){
	try{
		int points =0;

		Random ran = new Random();
		for(int i = 10; i>0;i-- ){
			double chance = (-Math.pow((double)i/(10d+(double)teir+1) ,((double)(teir+1) )/4d)+1d);
			double sel = ran.nextDouble();
			if(chance >= sel){
				points += i;
			//Bukkit.broadcastMessage(Utils.formatNum(chance*100d)+":"+Utils.formatNum(sel*100d));
				
			}
		}
	
		if(points < 1){Messenger.send(player, "&cYou won nothing :>");return;}
		//Bukkit.broadcastMessage("Points:"+points+"");
		double money = 0d;
		float multi = 0f;
		int mp = points;
		while(points>0){
			double chance = ran.nextDouble()*100d;
			ran.setSeed(ran.nextLong());
			//Bukkit.broadcastMessage(points+"");
			if(chance< 9.0 &&points>=2){
				Material[] types = new Material[]{Material.DIAMOND_HELMET,Material.DIAMOND_CHESTPLATE,Material.DIAMOND_LEGGINGS,Material.DIAMOND_BOOTS};
				points -=2;
				ItemStack armor = new ItemStack(types[ran.nextInt(types.length)]);
				armor.addEnchantment(Enchantment.DURABILITY, 3);
				for(Enchantment e :armorEnch){
					int lvl = ran.nextInt(4);
					if(lvl!= 0){
						armor.addUnsafeEnchantment(e, lvl);
					}
				}
				if(InventoryUtil.isFull(player.getInventory())){
					player.getWorld().dropItemNaturally(player.getLocation(),armor);
				}else{
				player.getInventory().addItem(armor);
				}
				Messenger.send(player, PREFIX +"&7You won some armor.");
			}
			if(chance< 15.0 && points>=4){
				points -=4;
				ItemStack pick = ServerSettingsCommand.randomPick( 9-(teir*2));
				boolean won = pick!= null;
				if(won){
					if(InventoryUtil.isFull(player.getInventory())){
						player.getWorld().dropItemNaturally(player.getLocation(), pick);
					}else{
					player.getInventory().addItem(pick);
					}
					Messenger.send(player, PREFIX +"&7You won a new Pickaxe.");
				}
			}
			if(chance< 5.0){
				for(int i = keyTeir.size(); i>0 ;i--){
					if(ran.nextInt(i*2)!= 0 ){
						int am = (i*2)+3;;
						if(points< am)continue;
						points -=  am;
						ItemStack it = keyTeir.get(i-1);
						if(InventoryUtil.isFull(player.getInventory())){
							player.getWorld().dropItemNaturally(player.getLocation(), it);
						}else{
						player.getInventory().addItem(it);
						}
						Messenger.send(player, PREFIX +"&7You won a new key");
							
						
					}
				}
			}
			if(chance< 15.0 && points>= 2){
				points -=2;
				ItemStack it = Main.itemRegistry.get("FUP");
				if(InventoryUtil.isFull(player.getInventory())){
					player.getWorld().dropItemNaturally(player.getLocation(), it);
				}else{
				player.getInventory().addItem(it);
				}
				Messenger.send(player, PREFIX +"&7You won a fortune upgrade.");
			}
		 if(chance< 13.0 && points>0){
			 
			  multi+= points/5f;
		
			  points--;
		 }
			if(points>0){
				int toPay =ran.nextInt(points)+1;
				if(points< toPay)continue;
				double amount= ((ran.nextInt(toPay+1)+1)* Math.pow(toPay+points, ((ran.nextInt(teir+1)+1))))+1000d;
				points -= toPay;
				money+= amount;
			//	Bukkit.broadcastMessage(amount+"");
		
			}
			
		}
			if(money>0d){
				try {
					Economy.add(player.getName(),money);
					Messenger.send(player, PREFIX +"&7You won &c"+Utils.formatCur(money)+"&7.");
				} catch (NoLoanPermittedException | UserDoesNotExistException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				}
			}
			if(multi>0f){
				User user = User.getUser(player);
			int time = ran.nextInt(	mp*10*teir) ;
				int sec = 	(int)( time% 60f); 
				int min = (int)((time/60f)%60f);
				int hr = 	(int)((time/60f)/60f);
				
				if(time>0){
				
				user.addBuff(new SellBuff(multi,time));
			
				StringBuilder  sb = new StringBuilder(PREFIX +"&7You have been given a sell buff of& c" +multi+" &7for");
				if(hr>0){
					sb.append(" &c"+hr+" &7hours");
					if(min>0 || sec>0){
						sb.append(",");
					}
				}
				if(min>0){
					sb.append(" &c"+min+" &7minutes");
					if(sec>0){
						sb.append(",");
					}
				}
				if(sec>0){
					sb.append(" &c"+sec+" &7seconds");
				}
				sb.append("&7.");
			
				Messenger.send(player, sb.toString());
				}
			}
			new BukkitRunnable(){
				public void run(){
					player.updateInventory();
				}
			}.runTask(Utils.PLUGIN);
		
	}catch(Exception e){
		e.printStackTrace();
	}
	}
}
