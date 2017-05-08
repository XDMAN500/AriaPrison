package me.varmetek.prison.anticheat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.varmetek.prison.api.User;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class AntiFly implements Listener,AntiCheat
{
	private static final Map<UUID,Integer> failedCheck = new HashMap<UUID,Integer>();
	private static final Map<UUID,Double> pastVel = new HashMap<UUID,Double>();

	private static final Map<UUID,Long> lastLand = new HashMap<UUID,Long>();
	
	private static final Set<UUID> tagged =  new HashSet<UUID>();
	
	static{
		new BukkitRunnable(){
			public void run(){
				Set<UUID> checks = failedCheck.keySet();
				for(UUID id:checks){
						report(id);
						failedCheck.remove(id);
					
				}
			}
		}.runTaskTimer(Utils.PLUGIN, 0, 20L);
	}
	@EventHandler
	public void check(PlayerMoveEvent ev){
	//	if(ev.getFrom().getX() == ev.getTo().getX() && ev.getFrom().getY() == ev.getTo().getY() &&ev.getFrom().getZ() == ev.getTo().getZ())return;
		Player pl = ev.getPlayer();
		 if(pl.getAllowFlight())return;
		 if(pl.getLocation().getBlockY() <0)return;
		User user =User.getUser(pl);
		UUID id = pl.getUniqueId();
		 double fY = ev.getFrom().getY();
			double tY = ev.getTo().getY();
		 double pVel = getPastVel(id);
		 double vel  = tY-fY;
		 long lLand = getLastLand(id);
		 World world = pl.getWorld();
		 Block ground = pl.getWorld().getBlockAt(pl.getLocation().add(0, -0.7, 0));
		
		 Block eye = world.getBlockAt(pl.getEyeLocation());
		 Block legs = world.getBlockAt(pl.getEyeLocation());
		if(legs.isLiquid())return;
		if(eye.isLiquid())return;
		if(ground.getType() == Material.LADDER)return;
		if(legs.getType() == Material.LADDER)return;
		if(eye.getType() == Material.LADDER)return;
		if(ground.getType() == Material.VINE)return;
		if(legs.getType() == Material.VINE)return;
		if(eye.getType() == Material.VINE)return;

	
			pastVel.put(id, vel);
			

			if(ground.getType().isSolid()){
				lastLand.put(id, System.currentTimeMillis());
				//pastVel.put(id, vel);
				return;
			}
			if(System.currentTimeMillis()-lLand> 70&& vel>= pVel){
				failedCheck.put(id,getFailedChecks(id)+1);
	
			}
			
	}
	private static int getFailedChecks(UUID id){
		if(failedCheck.containsKey(id)){
			return failedCheck.get(id);
		}
		return 0;
	}
	private double getPastVel(UUID id){
		if(pastVel.containsKey(id)){
			return pastVel.get(id);
		}
		return 0.0;
	}

	private long getLastLand(UUID id){
		if(lastLand.containsKey(id)){
			return lastLand.get(id);
		}
		return 0L;
	}
	private int height(Player e){
		Block start = e.getWorld().getBlockAt(e.getLocation());
		for(int h = 1; h<e.getLocation().getBlockY();h++){
			Block bl = start.getRelative(BlockFace.DOWN, h);
			if(bl.getType().isSolid()){
		
				return h-1;
			}
		}
		return e.getLocation().getBlockY();
	}
	public static boolean  report(UUID id){
		Player pl = Utils.SERVER.getPlayer(id);
		if(pl== null)return false;
		User user = User.getUser(pl);
	
		
		if(!failedCheck.containsKey(id))return false;
		

		
		if( getFailedChecks(id) >=20 ){
				
		//	failedCheck.remove(id);
		
			int pv = 0;
			pv = (int) user.getViolations().getInt("fly",0);
			user.getViolations().put("fly", pv+1);
			Utils.report("&8[&6AC&8] &a"+pl.getName()+" &8might be flying "+"&8[&7"+pv+"&8]");
			return true;
		}

		return false;
			
	}




}
