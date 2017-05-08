package me.varmetek.prison.api;

public enum Area {
	
	SPAWN(false,false,true);
	
	
	private static boolean defaultCanSoup = true;
	private static boolean defaultCanPvP = true;
	private static boolean defaultCanWarp = true;
	
	boolean canSoup;
	boolean canWarp;
	boolean canPvP;
	
	private Area(boolean soup,boolean pvp, boolean warp){
		canSoup = soup;
		canPvP = pvp;
		canWarp = warp;
	}
	
	public static boolean allowSoup(Area a){
		if(a == null){
			return defaultCanSoup;
		}else{
			return a.canSoup;
		}
		
	}
	
	public static boolean allowPvP(Area a){
		if(a == null){
			return defaultCanPvP;
		}else{
			return a.canPvP;
		}
		
	}
	
	
	public static boolean allowWarp(Area a){
		if(a == null){
			return defaultCanWarp;
		}else{
			return a.canWarp;
		}
		
	}
	
}
