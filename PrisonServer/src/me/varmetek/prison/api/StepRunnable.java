package me.varmetek.prison.api;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import me.varmetek.prison.mine.Mine;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

public class StepRunnable {
	

	public class CleanUserSet implements Runnable
	{

		@Override
		public void run() {
			Collection<? extends User> values = User.users.values();
			
			for(User user:values){
				if(!user.getPlayer().isOnline()){
					user.remove();
				}
			}
			
		}
		

	}
	
	public class UpdateAllScoreboards implements Runnable
	{
		Collection<? extends User> values = User.users.values();
		@Override
		public void run() {
			for(User user:values){
				if(user.getPlayer().isOnline()){
					user.updateScoreBoard();
				}
			}
			
		}
		

	}
	public class AutoResetMines implements Runnable
	{
		public void run() {
			for(Mine mine:Mine.getMines()){
				if(mine.getDelay() <0)continue;
			//	Utils.debug(mine.getName()+" - "+ mine.getNextReset());
				mine.decrimentNext();
				if(mine.getNextReset()<0){
					mine.resetMine();
				}
			}
			
		}
	}
	
	public class ClearInfractions implements Runnable {

		@Override
		public void run() {
			Collection<? extends User> values = User.users.values();
		
			for(User user:values){
				user.getViolations().clear();
			}
			
		}
		
	}
	public class CheckCombatTags implements Runnable {

		@Override
		public void run() {
			Collection<? extends User> values = User.users.values();
		
			for(User user:values){
				
			
					if(user.getCombatLog() == 0)continue;
				
						if(user.getCombatLog()>0){
							user.setCombatLog(user.getCombatLog()-1);
							if(user.getCombatLog() == 0){
								if(user.getPlayer().isOnline()){
									Messenger.send((Player) user.getPlayer(), "&7 You are no longer tagged.");
								}
							}
						}
				}
				
			}
			
		}
	
	public class CheckSellBuffs implements Runnable {

		@Override
		public void run() {
			Collection<? extends User> values = User.users.values();
		
			for(User user:values){
				
			
					if(user.getBuffs().isEmpty())continue;
					List<SellBuff> e = user.getBuffs();
						//long[] amount = new long[e.size()];
						
						//int index = 0;
						for(SellBuff sb: e){
						//	index++;
							//amount[index]= sb.getTime();
							//Messenger.send(((Player)user.getPlayer()), sb.toString());
								sb.decrimentTime();
								if(sb.getTime() < 1){
								float f = sb.getAmount();
								if(user.getPlayer().isOnline()){
									Messenger.send(	((Player)user.getPlayer()),"&7Your sell buff of &c"+f+" &7 has expired.");
									
								}
								
									user.removeBuff(sb);
								}
						}
					
				}
				
			}
			
		}
		
	public class CheckCharging implements Runnable{
		MaterialData data = new MaterialData(0);
		public void run(){
			for(Player pl:Utils.SERVER.getOnlinePlayers()){
				if(pl.isSneaking() && !pl.isFlying()){
				World w= pl.getWorld();
				
					//w.playEffect(pl.getEyeLocation(), Effect.POTION_BREAK, new Potion(PotionType.INSTANT_HEAL));
					//w.playEffect(pl.getLocation(), Effect.MOBSPAWNER_FLAMES,0,100);
				//MaterialData bl = w.getBlockAt(pl.getLocation()).getState().getData();
				Random ran = new Random();	
				//w.playEffect(pl.getLocation(), Effect.EXPLOSION_LARGE,1);
				w.playEffect(pl.getLocation(), Effect.ENDER_SIGNAL,1);
				//w.playEffect(pl.getLocation(), Effect.POTION_SWIRL_TRANSPARENT,1);
				w.playEffect(pl.getLocation(), Effect.PORTAL,1);
				/*if(ran.nextInt(5) == 0){
						
						w.strikeLightningEffect(pl.getLocation().add(ran.nextInt(3)-1, 0, ran.nextInt(3)-1));
					}*/
					for(int i = 0 ;i<5;i++){
						//.add(ran.nextInt(3)-1, 0, ran.nextInt(3)-1)
						w.playEffect(pl.getLocation(), Effect.TILE_BREAK,data,100);
						w.playEffect(pl.getEyeLocation(), Effect.TILE_BREAK,data,100);
					}
					for(int i = 0 ;i<20;i++){
					w.playEffect(pl.getLocation(), Effect.PORTAL,1);
					w.playEffect(pl.getEyeLocation(), Effect.PORTAL,1);
					}
					
				
						
				}
			}
		}
	}
	

}
