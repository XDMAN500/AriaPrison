package me.varmetek.prison.mine;

import me.varmetek.prison.Main;
import me.varmetek.prison.utils.RandomMaterial;
import me.varmetek.prison.utils.Utils;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class MineGenerator {
	Mine mine;
	public MineGenerator(Mine mine){
		this.mine  = mine;
	}
	
	
	@SuppressWarnings("deprecation")
	public void run(){
		try{
		World world = mine.getWorld();
		
		if(world == null)return;
		ProtectedRegion pr =  mine.getRegion();
		RandomMaterial Rmat =  new RandomMaterial(mine.getBlockList());
   	 	
		int minX = pr.getMinimumPoint().getBlockX();
	      int minY = pr.getMinimumPoint().getBlockY();
	      int minZ = pr.getMinimumPoint().getBlockZ();
	      int maxX = pr.getMaximumPoint().getBlockX();
	      int maxY = pr.getMaximumPoint().getBlockY();
	      int maxZ = pr.getMaximumPoint().getBlockZ();
	      new BukkitRunnable(){
	    	  int y = minY;
	    	  public void run(){
	    		  if(y>maxY){
	    			  this.cancel();
	    			  return;
	    		  }
	    		     for (int z = minZ; z <= maxZ; z++) {
	    		          for (int x = minX; x <= maxX; x++) {
	    		
	    		        	  LocalBlock chosen  = Rmat.nextBlock(Main.resetSamples);
	    		        	  
	    		        	   world.getBlockAt(x, y, z).
	    		        	   setTypeIdAndData(chosen.getID(), chosen.getData(), false);
	    		        	
	    		          }
	    		        }
	    		     y++;
	    	  }
	      }.runTaskTimer(Utils.PLUGIN, 5L, 0L);
	 /*     for (int y = minY; y <= maxY; y++) {
	        for (int z = minZ; z <= maxZ; z++) {
	          for (int x = minX; x <= maxX; x++) {
	
	        	  LocalBlock chosen  = Rmat.nextBlock(Main.resetSamples);
	        	  
	        	   world.getBlockAt(x, y, z).
	        	   setTypeIdAndData(chosen.getID(), chosen.getData(), false);
	        	
	          }
	        }
	        
	      }*/
		}
	      catch(NullPointerException e){
	    	  Utils.debug("&78&a&l"+mine.getName()+"&8]&7 "+"couldn't reset properly");
	    	  e.printStackTrace();
	      }
		
	}

}
