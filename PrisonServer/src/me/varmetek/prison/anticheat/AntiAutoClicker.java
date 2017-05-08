package me.varmetek.prison.anticheat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.varmetek.prison.api.User;
import me.varmetek.prison.utils.Utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class AntiAutoClicker implements AntiCheat, Listener {
	private static final Map<UUID,Integer> clicks = new HashMap<UUID,Integer>();
	static{
		new BukkitRunnable(){
			public void run(){
				for(UUID id: clicks.keySet()){
						report(id);
						clicks.remove(id);
					
				}
			}
		}.runTaskTimer(Utils.PLUGIN, 0, 20L);
	}
	
	@EventHandler
	public void check(PlayerInteractEvent ev ){
		int i  = 0;
		Player pl = ev.getPlayer();
		UUID id = pl.getUniqueId();
		
		if(!pl.getInventory().getViewers().isEmpty() || !Utils.isLeftClicked(ev.getAction()))return;
		if(clicks.containsKey(id)){
			i = clicks.get(id);
			
		}
		clicks.put(id, i+1);
	}
	

	public static boolean report(UUID id) {
		Player pl = Utils.SERVER.getPlayer(id);
		if(pl== null)return false;
		User user = User.getUser(pl);
		int clickSpeed = clicks.get(id).intValue();
		if(clickSpeed>15){
			
			int pv = 0;
			pv = (int) user.getViolations().getInt("autoclick",0);
			Utils.report("&8[&6AC&8] &a"+pl.getName()+" &8might be auto-clicking &7(&3cps of " + clickSpeed+"&7)"+"&8[&7"+pv+"&8]");
		
			user.getViolations().put("autoclick", pv+1);
			return true;
		}else{
			return false;
		}
		
	}

}
