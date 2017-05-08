package me.varmetek.prison.api;

import me.varmetek.prison.utils.Utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockLocation {
	private int x,y,z;
	private World world;
	
	public BlockLocation(int x,int y,int z,World w){
		this.x = x;
		this.y = y;
		this.z = z;
		world = w;
	}
	public BlockLocation(Location loc){
		x = loc.getBlockX();
		y = loc.getBlockY();
		z = loc.getBlockZ();
		world = loc.getWorld();
	}
	public BlockLocation(String s )throws IllegalArgumentException,NumberFormatException,NullPointerException{
		String[] p =s.split(":", 4);
		if(p.length != 4){
			throw new IllegalArgumentException();
		}
		
		world = Utils.SERVER.getWorld(p[0]);
		if(world == null){throw new IllegalArgumentException("World cannot be null");}
		x = Integer.parseInt(p[1]);
		y = Integer.parseInt(p[2]);
		z = Integer.parseInt(p[3]);
		

		
	}
	public boolean isSimular(BlockLocation bl){
		return(x == bl.x && y == bl.y && z == bl.z &&world.getUID().equals(world.getUID()));
		
	}
	public int getX(){return x;}
	public int getY(){return y;}
	public int getZ(){return z;}
	public World getWorld(){return world;}
	public Block getBlock(){return world.getBlockAt(x, y, z);}
	public Location toLocation(){
		return new Location(world, x, x, x);
	}
	@Override
	public String toString(){
		return world.getName()+":"+x+":"+y+":"+z;
	}
}
