package me.varmetek.prison.anticheat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.varmetek.prison.api.User;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


public class AntiSpeed implements AntiCheat, Listener{
	private static final double sneakSpeed = 0.5;
	private static final double walkSpeed = 0.25;
	private static final double sprintSpeed = 0.35;
	private static final double sprintJumpSpeed = 0.45;
	private static final Map<UUID,Integer> failedCheck = new HashMap<UUID,Integer>();
	static{
		new BukkitRunnable(){
			public void run(){
				for(UUID id:failedCheck.keySet()){
						report(id);
						failedCheck.remove(id);
					
				}
			}
		}.runTaskTimer(Utils.PLUGIN, 0, 20L);
	}
	@EventHandler
	public void check(PlayerMoveEvent ev){
		Player pl = ev.getPlayer();
		 if(pl.getAllowFlight())return;
		World world = pl.getWorld();
		User user =User.getUser(pl);
		UUID id = pl.getUniqueId();
		 double velX = Math.abs(Math.abs(ev.getTo().getX())- Math.abs(ev.getFrom().getX()));
		 double velZ = Math.abs(Math.abs(ev.getTo().getZ())- Math.abs(ev.getFrom().getZ()));
		 double vel = velX+velZ;
		
		if(vel == 0)return;
		
		 Block ceil =	world.getBlockAt(pl.getEyeLocation().add(0d, 0.7, 0d));
		boolean  floored =	world.getBlockAt(pl.getLocation().add(0d,-0.7, 0d)).getType().isSolid();

		 if(ceil.getType() != Material.AIR)return;
	
	
		 int speedBonus = 0;
	
			 for(PotionEffect pf: pl.getActivePotionEffects()){
				 if(pf.getType() == PotionEffectType.SPEED){
					 speedBonus = pf.getAmplifier();
					 break;
				 }
			 }
		 
		 double bonus = 1d+(new Double(speedBonus).doubleValue())/10d;

		if(pl.isSneaking() && vel> sneakSpeed+bonus){
			int fc = getFailedChecks(id);	
			failedCheck.put(id, fc+1);
		}
		if(pl.isSprinting() && floored&& vel> sprintSpeed+bonus){
			int fc = getFailedChecks(id);	
			failedCheck.put(id, fc+1);	
		}
		if(pl.isSprinting() && !floored&& vel> sprintJumpSpeed+bonus){
			int fc = getFailedChecks(id);	
			failedCheck.put(id, fc+1);
		}
		if(!pl.isSprinting() && !pl.isSneaking()&& vel> walkSpeed+bonus){
			
			int fc = getFailedChecks(id);	
			failedCheck.put(id, fc+1);
		}
	}
	private static int getFailedChecks(UUID id){
		if(failedCheck.containsKey(id)){
			return failedCheck.get(id);
		}
		return 0;
	}
	public static boolean report(UUID id) {
		Player pl = Utils.SERVER.getPlayer(id);
		if(pl== null)return false;
		User user = User.getUser(pl);
		int fc = getFailedChecks(id);
		if(fc>1){
			
			int pv = 0;
			pv = (int) user.getViolations().getInt("speed",0);
			Utils.report("&8[&6AC&8] &a"+pl.getName()+" &8might be speed hacking &7(&3vps of " + (fc-1)+"&7)"+"&8[&7"+pv+"&8]");
		
			user.getViolations().put("speed", pv+1);
			return true;
		}else{
			return false;
		}
		
	}

}
