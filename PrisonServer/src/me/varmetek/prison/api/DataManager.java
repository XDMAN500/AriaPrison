package me.varmetek.prison.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.varmetek.prison.Main;
import me.varmetek.prison.mine.LocalBlock;
import me.varmetek.prison.mine.Mine;
import me.varmetek.prison.utils.Utils;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
@SuppressWarnings("unchecked")
public class DataManager {
	//static FileConfiguration playerConfig = null;
	//static FileConfiguration warpConfig = null;
	
	private static Plugin plugin  = Main.getPluginInstance();
	private final static String WARP_DIR = "warps.yml";
	private final static String LOC_DIR = "loc.yml";
	private final static String CONFIG_DIR = "config.yml";
	public final static File WARP_LIST_FILE = new File(plugin.getDataFolder(), WARP_DIR);
	private final static File LOC_LIST_FILE = new File(plugin.getDataFolder(), LOC_DIR);
	private final static File CONFIG_FILE = new File(plugin.getDataFolder(), CONFIG_DIR);
	private final static File DATA_BASE_FILE= new File(plugin.getDataFolder(), "DataBase.yml");
	public final static File SERVER_FILE = new File(plugin.getDataFolder(),"config.yml");
	public final static File SHOPS_FILE = new File(plugin.getDataFolder(),"shops.yml");
	public final static File SHOPG_FILE = new File(plugin.getDataFolder(),"economy.yml");
	public final static File BANENRTY_FILE = new File(plugin.getDataFolder(),"ban.yml");
	private static  FileConfiguration configs = YamlConfiguration.loadConfiguration(SERVER_FILE);
	
	
	//private final static File BAN_ENTRY_FILE = new File(plugin.getDataFolder(), "BanEntries.yml");
	private final static File RANK_FILE = new File(plugin.getDataFolder(), "ranks.yml");

	private static final File[] FILES = new File[] {WARP_LIST_FILE,LOC_LIST_FILE,CONFIG_FILE};
	public static void createFiles(){
	
		
	for(File f : FILES){
		if (!f.exists()) {
	            try {
	                f.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	             //   return null;
	            }
	            
	        }
		}
	}
	public static void setMotd(String s){

		getConfig().set("motd", s);
		saveConfig();
		
	}
	public static String getMotd(){

		String s = getConfig().getString("motd");
		if(s == null){
			return "";
		}else{
			return s;
		}
		
	}

	

	public static void saveAllShops(){
		  if (!SHOPS_FILE.exists()) {
	            try {
	            	SHOPS_FILE.createNewFile();
	                
	            } catch (IOException e) {
	                e.printStackTrace();
	                return;
	            }
		  }
		  FileConfiguration data = YamlConfiguration.loadConfiguration(SHOPS_FILE);
		  data.set("shops",null);
		  List<List<String>> save = new ArrayList<List<String>>();
		  	for(ChestShop cs: ChestShop.shops){
		  		save.add(cs.serialize());
		  	}
		  		data.set("shops", save);
		  	try {
				data.save(SHOPS_FILE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public static void loadAllShops(){
		  if (!SHOPS_FILE.exists()) {
	          return;
		  }
		  FileConfiguration data = YamlConfiguration.loadConfiguration(SHOPS_FILE);
		  if(!data.contains("shops"))return;

			 List<List<String>> save = (List<List<String>>) data.get("shops",new ArrayList<List<String>>());
			 if(save.isEmpty())return;
			 for(List<String> s : save ){
				 if(s == null)continue;
				 if(s.isEmpty())continue;
				try{
				 ChestShop.Deserialize(s);
				}catch(IllegalArgumentException e){}
			 }
		  
	}
	
	public static void addDataBaseName(UUID u , String name){
		
		
		  if (!DATA_BASE_FILE.exists()) {
	            try {
	            	DATA_BASE_FILE.createNewFile();
	                
	            } catch (IOException e) {
	                e.printStackTrace();
	                return;
	            }
		  }
		  FileConfiguration data = YamlConfiguration.loadConfiguration(DATA_BASE_FILE);
		 
		  String path = u.toString()+".names";
		
		Set<String> names = getDataBaseEntryNames(u);
		names.add(name);

		data.set(path, new ArrayList<String>(names));
		

		
		try {
			data.save(DATA_BASE_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		  
	}
	public static void addDataBaseIP(final UUID u , String ip){
		
		
		  if (!DATA_BASE_FILE.exists()) {
	            try {
	            	DATA_BASE_FILE.createNewFile();
	                
	            } catch (IOException e) {
	                e.printStackTrace();
	                return;
	            }
		  }
		  FileConfiguration data = YamlConfiguration.loadConfiguration(DATA_BASE_FILE);
		
		
		String path = u.toString()+".ips";
		Set<String> ips = getDataBaseEntryIps(u);
		ips.add(ip);

		data.set(path,new ArrayList<String>(ips));
		
		try {
			data.save(DATA_BASE_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		  
	}
	public static Set<String> getDataBaseEntryIps(UUID u){
	
		  if (!DATA_BASE_FILE.exists()) {
	         return new HashSet<String>();
		  }
		  FileConfiguration data = YamlConfiguration.loadConfiguration(DATA_BASE_FILE);
			
			 Set<String> ips =new HashSet<String>();
			String path = u.toString()+".ips";
			
			if(data.contains(path)){
				List<String> array = data.getStringList(path);
				if(array != null){
					for(String s : array){
						if(s != null){
							ips.add(s);
						}
					}	
				}
			}
		  return ips;
	}
	public static Set<String> getDataBaseEntryNames(UUID u){
		
		  if (!DATA_BASE_FILE.exists()) {
	         return new HashSet<String>();
		  }
		  FileConfiguration data = YamlConfiguration.loadConfiguration(DATA_BASE_FILE);
		  Set<String> names =new HashSet<String>();
			
				

				
				String path = u.toString()+".names";
					
					if(data.contains(path)){
						List<String> array = data.getStringList(path);
						if(array != null){
							for(String s : array){
								if(s != null){
									names.add(s);
								}
							}	
						}
					}
		  return names;
	}
	
	public  static void checkForMatches(UUID u){

		  if (!DATA_BASE_FILE.exists()) {
		         return;
			  }
			  FileConfiguration data = YamlConfiguration.loadConfiguration(DATA_BASE_FILE);
			 Set<String> entrys = data.getKeys(false);
			if(entrys.isEmpty())return;
			
			for(String f : entrys){
				UUID ud = UUID.fromString(f);
				
				for(String ip : getDataBaseEntryIps(ud)){
					if(getDataBaseEntryIps(u).contains(ip)){
					
						for(String toAdd : getDataBaseEntryIps(ud)){
							addDataBaseIP(u,toAdd);
						}
						for(String toAdd : getDataBaseEntryNames(ud)){
							addDataBaseName(u,toAdd);
						}
						break;
					}
				}
			}
	}
	
	public static void nukeUser(User user){
		
		if(user == null){return;}
	//	saveUserRank(user,user.getRank());
		String name = user.getUserID();
		OfflinePlayer pl =  user.getPlayer();
		
		String filename = user.getPlayerUUID().toString();
		//	Main.debug("Saving User:"+playerUser.getPlayer().getName());
			File myFile = new File(plugin.getDataFolder(), "/Players/"+filename+".yml");
		 
        if (myFile.exists()) {
         	myFile.delete();
         	
        }
        user.remove();
       user = User.getUser(pl);
        //loadUserRank(user);
        
        
	}
	

 	public static void saveLocation(File file,String path,Location loc){
		 Validate.notNull(file);
		 Validate.notNull(loc, "Spawn location is null");
		 if (!file.exists()) {
			 Bukkit.broadcastMessage("File is null");
			 return;
		 
		 }
		
		 FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		  config.set(path+".x", loc.getX());
		  config.set(path+".y", loc.getY());
		  config.set(path+".z", loc.getZ());
		  config.set(path+".pitch",String.valueOf(loc.getPitch()));
		  config.set(path+".yaw",String.valueOf(loc.getYaw()));
		  config.set(path+".world", loc.getWorld().getName());
			try {
				config.save(file);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	 }
	public static Location loadLocation(File file,String path){
		 
		 if (!file.exists()) {
			 return null;
		 
		 }
		 
		 FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			if(config.isConfigurationSection(path)){
				return new Location(
					Bukkit.getWorld(config.getString(path+".world")), 			
						
					config.getDouble(path+".x"),
					config.getDouble(path+".y"),
					config.getDouble(path+".z"), 
					Float.parseFloat(config.getString(path+".yaw")),
					Float.parseFloat(config.getString(path+".pitch"))
								   
						);
						
					
		}
			return null;
	 }
	 
/*
	public static void loadArenas(){
		 File file = new File(plugin.getDataFolder(), "arenas.yml");
		 FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		 if(config.getKeys(false).isEmpty())return;
		 for(String arena : config.getKeys(false)){
			 	loadArena(Arena.getArea(arena, true));
		 }
	 }
	public static void saveArenas(){
		 if(Arena.getArenas().isEmpty())return;
		 
		 for(Arena a: Arena.getArenas()){
			 saveArena(a);
		 }
		
	 }
*/

	//Bans
	public static void addBan(String value, boolean ip ){
		String path =""; 
		if(value.isEmpty() || value == null)return;
		File myFile = new File(plugin.getDataFolder(), "banlist.yml");
		  if (!myFile.exists()) {
	            try {
	                myFile.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	                return;
	            }
		  }
		  FileConfiguration banList = YamlConfiguration.loadConfiguration(myFile);
		  if(ip){
			  path = "ips";
		  }else{
			  path = "players";
		  }
			  List<String> l = (List<String>) banList.get(path);
			
			  if(l == null){
				  l = new ArrayList<String>();
			  }
			  l.add(value);
			  banList.set(path, l);  
		 
		
		 
		  try {
			banList.save(myFile);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		  
	}
	public static void removeBan(String value, boolean ip ){
		String path =""; 
		if(value.isEmpty() || value == null)return;
		File myFile = new File(plugin.getDataFolder(), "banlist.yml");
		  if (!myFile.exists()) {
	            try {
	                myFile.createNewFile();
	                return;
	            } catch (IOException e) {
	                e.printStackTrace();
	                return;
	            }
		  }
		  FileConfiguration banList = YamlConfiguration.loadConfiguration(myFile);
		  if(ip){
			  path = "ips";
		  }else{
			  path = "players";
		  }
			
		  
		  List<String> l = (List<String>) banList.get(path);
			  if(l == null){
				 return;
			  }
			 if(!l.contains(value)){
				 return;
			 }
			
			  l.remove(l.indexOf(value));
			  
			  
			  banList.set(path, l);  
		  
		
		 
		  try {
			banList.save(myFile);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		  
	}
	public static List<String> getBanList( boolean ip ){
		String path =""; 
		
		File myFile = new File(plugin.getDataFolder(), "banlist.yml");
		  if (!myFile.exists()) {
	            try {
	                myFile.createNewFile();
	                return null;
	            } catch (IOException e) {
	                e.printStackTrace();
	                return null;
	            }
		  }
		  FileConfiguration banList = YamlConfiguration.loadConfiguration(myFile);
		  if(ip){
			  path = "ips";
		  }else{
			  path = "players";
		  }
			
		 
		  
			  if((List<String>) banList.get(path) == null){
				 return null;
			  }
			  return (List<String>) banList.get(path);

	}
	public static boolean isBanned(String value ,boolean ip ){
		if(value.isEmpty() || value == null)return false;
		List<String> l = getBanList(ip);
		if(l == null)
			return false;
		
		if(l.contains(value)){
			return true;
		}
		return false;
	}
	public static boolean isBanListExist(boolean ip){
		File myFile = new File(plugin.getDataFolder(), "banlist.yml");
		  if (!myFile.exists()) {
	           return false;
		  }
		  String path = "";
		  if(ip){
			  path = "ips";
		  }else{
			  path = "players";
		  }
		  FileConfiguration banList = YamlConfiguration.loadConfiguration(myFile);
		  return banList.contains(path);
	}
	//Warps

	public static void loadUserRank(User user){
	//	File myFile = new File(plugin.getDataFolder(), "/Players/" + user.getPlayerUUID() + ".yml");
		 FileConfiguration config;
		 Rank rank;
		 if (RANK_FILE.exists()) {
			 config = YamlConfiguration.loadConfiguration(RANK_FILE);
			 
	         	if(config.contains(user.getPlayerUUID().toString()));{
	         		rank = Rank.getRankByName(config.getString(user.getPlayerUUID().toString()));
	         		if(rank ==  null){
	         			
	         			saveUserRank(user,rank);
	         			rank = Rank.Default;
	         		}
	         	
	         	}
	        }else{
	        	rank = Rank.Default;
	        	/*if(myFile.exists()){
	        		config = YamlConfiguration.loadConfiguration(myFile);
	        		  rank = Rank.getRankByName(config.getString("rank"));
	        		 if(rank ==  null){
		         			rank =Rank.Default;
		         		}
	        			
	        	}else{
	        		rank =Rank.Default;

	        	}*/
	        }
		// user.setRank(rank);
	///	 return Rank.Default;
	}
	public static void saveUserRank(User user, Rank rank){
		//File myFile = new File(plugin.getDataFolder(), "/Players/" + user.getPlayerUUID() + ".yml");
		//if(rank == Rank.Default){}
		 FileConfiguration config;
		 if (RANK_FILE.exists()) {
			 config = YamlConfiguration.loadConfiguration(RANK_FILE);
			 if(rank == null){
				 config.set(user.getPlayerUUID().toString(), rank);
			 }else{
				 if(rank == Rank.Default){
					 
					 config.set(user.getPlayerUUID().toString(), null);
				 }else{
					 config.set(user.getPlayerUUID().toString(), rank.name());
				 }
			 }
			 
			 try {
				config.save(RANK_FILE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
	       }else{
	    	   try {
	    		   RANK_FILE.createNewFile();
	    	   } catch (IOException e1) {
				// TODO Auto-generated catch block
	    		   e1.printStackTrace();
	    	   }
	        	/*if(myFile.exists()){
	        		config = YamlConfiguration.loadConfiguration(myFile);
	        		config.set("rank", rank.name());
	        		try {
						config.save(myFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}*/
	        }
		 
	}
	
	
	//User
	public static  boolean loadUser(User user){
	try{
	
		File myFile = new File(plugin.getDataFolder(), "/Players/" + user.getPlayerUUID() + ".yml");
		//Utils.debug("LoadingUser:"+user.getPlayer().getName());
	        if (!myFile.exists()) {
	           return false;
	        }
	        FileConfiguration config = YamlConfiguration.loadConfiguration(myFile);
	
	
	     
	   
	      //loadUserRank(user);
	       
	   if(config.get( "sellbuffs")!= null){
		
		   List<String> l = config.getStringList( "sellbuffs");
		   for(String st: l){
			   SellBuff sb = SellBuff.fromString(st);
			   if(sb!=null){
				 user.getBuffs().add(sb);
			   }
		   }
	   }
	
	     user.setMinerRank(config.getInt("minerrank", 0));
	      user.setBanMessage(config.getString("bannedmessage"));
	  
	   //   user.setProdLvL(config.getString("pLvL") == null? ProducerLevel.NONE : ProducerLevel.valueOf(config.getString("pLvL")));
	      //valueOf()
	      {
	    	  String j = config.get("bannedexpire")+"";
	      
	    	  if(j.isEmpty()){
	    		  user.setBanExpire(Calendar.getInstance() );
	    	  	config.set("bannedexpire", "0l");
	    	  }else{
	    	 
	    		  if(j.equalsIgnoreCase("E")){
	    			  user.setBanExpire(null);
	    		  }else{
	    			  Long time = 0l;
	    			  try{
	    				  time = Long.parseLong(j);
	    			  }catch(NumberFormatException e){
	    				  user.setBanExpire(Calendar.getInstance());
	    				  config.set("bannedexpire", "0l");
	    			  
	    		  		}	
	    	  
	    		  		Calendar c = Calendar.getInstance();
	    		  	c.setTimeInMillis(time );
	      		
	    		  	user.setBanExpire(c );
	    		  }
	    	  }
	      }
	      
	      {
	    	  String j = config.get("mutetime")+"";
	      
	    	  if(j.isEmpty()){
	    		  Bukkit.broadcastMessage("EMPTY");
	    		  user.setMuteExpire(Calendar.getInstance() );
	    	  	config.set("mutetime", "0l");
	    	  }else{
	    	 // Calendar c; //c.
	    		  if(j.equalsIgnoreCase("E")){
	    			  user.setMuteExpire(null);
	    		  }else{
	    		  Long time = 0l;
	    		  try{
	    			  time = Long.parseLong(j);
	    		  }catch(NumberFormatException e){
	    			  Bukkit.broadcastMessage("Error");
	    			  user.setMuteExpire(Calendar.getInstance());
	    			  config.set("mutetime", "0l");
	    			  
	    		  	}
	    	  
	    		  Calendar c = Calendar.getInstance();
	    		  c.setTimeInMillis(time );
	      		
	    		  user.setMuteExpire(c );
	    		  }
	    	  }
	      }
	      
	     /* String s = config.getString("ownedkits");
	      for(Kit k : Kit.getAllKits()){
	    	  
	    		  if(s.contains(k.name())){
	    			  user.addOwnedKit(k);
	    		  }
	    	  
	      }*/
	    	  
	      
	     // Set<?> l= (Sets.newHashSet(config.getList("ownedkits")));
	      //user.setOwnedKits( e);//config.getInt("kills"));config.set("ownedkits", new ArrayList<Kit>(user.getOwnedKits(false)));
	     
	      return true;
	}	catch(Exception e){e.printStackTrace();return false;}
	}
	public static void savePlayer(OfflinePlayer p){
		savePlayerUser(User.getUser(Bukkit.getOfflinePlayer(p.getName())));
	}	
	public static void savePlayerUser(User playerUser){
	try{
		String filename = playerUser.getPlayerUUID().toString();
	//	Main.debug("Saving User:"+playerUser.getPlayer().getName());
		File myFile = new File(plugin.getDataFolder(), "/Players/"+filename+".yml");
		 
	        if (!myFile.exists()) {
	            try {
	                myFile.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(myFile);
	      
	    ;
	    List<String> el = new ArrayList<String>();
	    for(SellBuff sb: playerUser.getBuffs()){
	    	el.add(sb.toString());
	    }
	      
	 
	     
	      playerConfig.set("sellbuffs", el);
	   //   saveUserRank(playerUser,playerUser.getRank());
	     // playerConfig.set("ownedkits", new ArrayList<Kit>(playerUser.getBoughtKits()).toString());
	      playerConfig.set("bannedmessage", playerUser.getBanMessage());
	     playerConfig.set("minerrank", playerUser.getMinerRank());
	      playerConfig.set("pLvL", playerUser.getProdLvL().name());
	      if(playerUser.getBanExpire() == null){
	    	  playerConfig.set("bannedexpire", "E");
	      }else{
	    	  playerConfig.set("bannedexpire",playerUser.getBanExpire().getTime().getTime()+"");
	      }
	      playerConfig.set("mutetime", playerUser.getMuteExpire().getTimeInMillis()+"");
	      try {
			playerConfig.save(myFile);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	     
	}	catch(Exception e){e.printStackTrace();}
	}
	public static void saveAllUsers(){
		
		for(User user: User.users.values()){
			DataManager.savePlayerUser(user);
		
		
		}
		
	}

	public  static void saveAll(){
		for(Player p :Bukkit.getOnlinePlayers()){
			savePlayer(p);
		}
	}
	
	public  static void loadAll(){
		for(Player p :Bukkit.getOnlinePlayers()){
			loadUser(User.getUser(Bukkit.getOfflinePlayer(p.getName())));
		}
	}

	public static void reloadUsers(){
		for(Player p :Bukkit.getOnlinePlayers()){
			savePlayer(p);
			loadUser(User.getUser(Bukkit.getOfflinePlayer(p.getName())));
		}
	}

	public static Location getLoc(String warp){
	
	/*File myFile = new File(plugin.getDataFolder(), LOC_DIR);
	if (myFile.exists()) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);
		
		if(warpConfig.getKeys(false).contains(warp))
			return new Location( 
					
					Bukkit.getWorld((String)warpConfig.get(warp+".world")), 			
					
					(double)warpConfig.get(warp+".x"),
					(double)warpConfig.get(warp+".y"),
				    (double)warpConfig.get(warp+".z"), 
					Float.parseFloat((String) warpConfig.get(warp+".yaw")),
					Float.parseFloat((String)warpConfig.get(warp+".pitch"))
							   
								);
	}
	return null;*/
		return loadLocation(LOC_LIST_FILE,warp);
}
	public static Set<String> getLocList(){
	
	File myFile = new File(plugin.getDataFolder(), LOC_DIR);
	if (myFile.exists()) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);
		if(warpConfig.getConfigurationSection("").getKeys(false)== null){Utils.debug("No list");return null;}
		if(warpConfig.getConfigurationSection("").getKeys(false).size() == 0) {Utils.debug("No locs");return null;}
			
		
		return warpConfig.getConfigurationSection("").getKeys(false);
			 
	}
	return null;
}
	public static boolean doesLocExist(String warp){
	return getLocList().contains(warp);
}
	public static boolean setLoc(String warp,Location loc){
	
	File myFile = new File(plugin.getDataFolder(), LOC_DIR);
	  if (!myFile.exists()) {
            try {
                myFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            
        }
	  FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);

	  warpConfig.set(warp+".x", loc.getX());
	  warpConfig.set(warp+".y", loc.getY()+1);
	  warpConfig.set(warp+".z", loc.getZ());
	  warpConfig.set(warp+".pitch",loc.getPitch()+"f");
	  warpConfig.set(warp+".yaw",loc.getYaw()+"f");
	  warpConfig.set(warp+".world", loc.getWorld().getName());
	 
	  try {
		warpConfig.save(myFile);
	} catch (IOException e) {
		
		e.printStackTrace();
		return false;
	}
	 // Bukkit.broadcastMessage("WARP CREATED");
	  return true;
}
	public static boolean removeLoc(String loc){
	File myFile = new File(plugin.getDataFolder(), "loc.yml");
	  if (myFile.exists()) {
		  FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);
		  warpConfig.set(loc, null);
		  try {
			warpConfig.save(myFile);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		  return true;
            
        }
	 return false;
	//User
}
	public static void setSpawn(Location loc){
	try{
		saveLocation(CONFIG_FILE,"spawn",loc);
		/*String path = "spawn" ;
		if(loc == null)return;
		getConfig().set(path+".x", loc.getX());
		getConfig().set(path+".y", loc.getY());
		getConfig().set(path+".z", loc.getZ());
		getConfig().set(path+".pitch",loc.getPitch()+"f");
		getConfig().set(path+".yaw",loc.getYaw()+"f");
		getConfig().set(path+".world", loc.getWorld().getName());*/
		saveConfig();
	}catch(Exception e){}
	
	
	}
	public static Location getSpawn(){
		return loadLocation(CONFIG_FILE,"spawn");
		/*String path = "spawn" ;
		if(!getConfig().contains(path))return null;
		return new Location(
				Bukkit.getWorld(getConfig().getString(path+".world")), 			
					
				getConfig().getDouble(path+".x"),
				getConfig().getDouble(path+".y"),
				getConfig().getDouble(path+".z"), 
				Float.parseFloat((String)getConfig().get(path+".yaw")),
				Float.parseFloat((String)getConfig().get(path+".pitch"))
							   
					);*/
	}
	public static void saveGlobalShop(){
		   if (!SHOPG_FILE.exists()) {
	            try {
	            	SHOPG_FILE.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		   FileConfiguration config = YamlConfiguration.loadConfiguration(SHOPG_FILE);
		   for(MaterialData md : GlobalShop.PRICES.keySet()){
			   config.set(md.getItemTypeId()+":"+md.getData(), GlobalShop.PRICES.get(md));
		   }
		   try {
			config.save(SHOPG_FILE);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}
	public static void loadGlobalShop(){
		   if (!SHOPG_FILE.exists()) {
	           return;
	        }
		   FileConfiguration config = YamlConfiguration.loadConfiguration(SHOPG_FILE);
		   for(String s:config.getKeys(false)){
			  if(!(s.contains(":")))continue;
			  String[] p = s.split(":", 2);
			  try{
				  int id = Integer.parseInt(p[0]);
				  byte data = Byte.parseByte(p[1]);
				  MaterialData d = new MaterialData(id,data);
				  double price = config.getDouble(s);
				  GlobalShop.PRICES.put(d, price);
			  }catch(Exception e){
				  
			  }
			 
		   }
		  
	
	}
	

	
	public static void saveMine(Mine mine){
	
		String filename = mine.getName();
		try{
		File myFile = new File(plugin.getDataFolder()+"/Mines/"+filename+".yml");
		 
        if (!myFile.exists()) {
            try {
                myFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        List<String> blz = new ArrayList<String>();
        FileConfiguration config = YamlConfiguration.loadConfiguration(myFile);
      
        Location loc = mine.getSpawn();
        try{
        String path = "spawn";
  	  config.set(path+".x", loc.getX());
	  config.set(path+".y", loc.getY());
	  config.set(path+".z", loc.getZ());
	  config.set(path+".pitch",loc.getPitch()+"f");
	  config.set(path+".yaw",loc.getYaw()+"f");
	  config.set(path+".world", loc.getWorld().getName());
        }catch(Exception e){}
        
	  config.set("delay", mine.getDelay());
        config.set("world", mine.getWorld().getName());
       // config.set("rank",  mine.getRankedPos());
        config.set("max.x", mine.getRegion().getMaximumPoint().getBlockX());
        config.set("max.y", mine.getRegion().getMaximumPoint().getBlockY());
        config.set("max.z", mine.getRegion().getMaximumPoint().getBlockZ());
        
        config.set("min.x", mine.getRegion().getMinimumPoint().getBlockX());
        config.set("min.y", mine.getRegion().getMinimumPoint().getBlockY());
        config.set("min.z", mine.getRegion().getMinimumPoint().getBlockZ());
        for(LocalBlock lb :mine.getBlockList()){
        	blz.add(lb.toString());
        }
        config.set("blocks", blz);
      
        
			config.save(myFile);
	
        Utils.sendConsoleMessage("&8[&bAria&8]&7"+"Saved mine &a"+ filename);
		}catch(Exception e){
			Utils.sendConsoleMessage("&4[Error]&c "+"Couldn't save mine "+ filename);
			e.printStackTrace();
			//Bukkit.broadcastMessage("Couldn't load mine "+ name);
		
		}
	}
	
	public static Mine loadMine(String name){
	
		try{
		File myFile = new File(plugin.getDataFolder()+"/Mines/"+name+".yml");
		
        if (!myFile.exists()) {
        	return null;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(myFile);
        String path = "spawn";
        World wov = Utils.SERVER.getWorld(config.getString("world"));
        if(wov == null)return null;
		Mine mine = Mine.create(
				
				name, 
				new ProtectedCuboidRegion
						(
						"MineMc_"+name,
						new BlockVector(config.getInt("max.x"), config.getInt("max.y"), config.getInt("max.z")), 
						new BlockVector(config.getInt("min.x"), config.getInt("min.y"), config.getInt("min.z"))
						),
				wov,
				new Location(
						wov, 			
							
						config.getDouble(path+".x"),
						config.getDouble(path+".y"),
						config.getDouble(path+".z"), 
						Float.parseFloat((String)config.get(path+".yaw")),
						Float.parseFloat((String)config.get(path+".pitch"))
									   
							)
							
				);
		if(mine == null)mine = Mine.getMine(name);
		try{
		int time  =config.getInt("delay");
	
		
		mine.setDelay(time);
		}catch(Exception e){
			mine.setDelay(Mine.DEFAULT_DELAY);
		}

		List<String> blz = config.getStringList("blocks");
		for(String s : blz){
			mine.addLocalBlock( new LocalBlock(
						Integer.parseInt(s.split(":")[0]),
						Byte.parseByte(s.split(":")[1]),
						Float.parseFloat(s.split(":")[2] )
						));
		}
		Utils.sendConsoleMessage("&8[&bAria&8]&7"+"Loaded mine &a"+ mine.getName());
		return mine;
		}catch(Exception e){
			Utils.sendConsoleMessage("&4[Error]&c "+"Couldn't load mine "+ name);
			e.printStackTrace();
			//Bukkit.broadcastMessage("Couldn't load mine "+ name);
			return null;
		}
		
	}
	public static void deleteMine(Mine mine){
		File myFile = new File(plugin.getDataFolder()+ "/Mines/"+mine.getName()+".yml");
		 
        if (myFile.exists()) {
            myFile.delete();
        }
	}
	public static void loadAllMines(){
	
		File group =  new File(plugin.getDataFolder()+ "/Mines/");
	
		for(File f: group.listFiles()){
			
			String name = f.getName().replace(".yml", "");
		
		
			loadMine(name);
		}
		Mine.loadRanked();
	}
	public static void saveAllMines(){
		if(Mine.getMines()== null)return;
		for(Mine mine: Mine.getMines()){
			saveMine(mine);
		}
		Mine.saveRanked();
	}
	
	public static  FileConfiguration getConfig(){
		if(!SERVER_FILE.exists()){
			try {
				SERVER_FILE.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			configs = YamlConfiguration.loadConfiguration(SERVER_FILE);
		}
		return configs;
		
	}
	public static void saveConfig(){
		try {
			configs.save(SERVER_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/*
 * 	public static Location getWarpLoc(String warp){
		
		return loadLocation(WARP_LIST_FILE,warp);
	}
	public static Set<String> getWarpList(){
		
		File myFile = new File(plugin.getDataFolder(), WARP_DIR);
		if (myFile.exists()) {
			FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);
			if(warpConfig.getConfigurationSection("").getKeys(false)== null){return null;}
			if(warpConfig.getConfigurationSection("").getKeys(false).size() == 0){return null;}
				
			
			return warpConfig.getConfigurationSection("").getKeys(false);
				 
		}
		return null;
	}
	
	public static Set<Warp> getShopWarps(){
		Set<Warp> set = new HashSet<Warp>();
		for(String s: getWarpList()){
			Warp warp = getWarp(s);
			if(warp.getLocation() != null && warp.getIcon() != null && warp.getIcon() != Material.AIR){
				set.add(warp);
				
			}
			
		}
		return set;
	}
	public static boolean doesWarpExist(String warp){
		return getWarpList().contains(warp);
	}
	public static boolean setWarp(String warp,Location loc){

		  createFiles();
		  saveLocation(WARP_LIST_FILE,warp,loc);
		  return true;
	}


 	public static boolean removeWarp(String warp){
		File myFile = new File(plugin.getDataFolder(), WARP_DIR);
		  if (myFile.exists()) {
			  FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);
			  warpConfig.set(warp, null);
			  try {
				warpConfig.save(myFile);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			  return true;
	            
	        }
		 return false;
		//User
	}
	public static Material getWarpIcon(String warp){
		Material mat = null;
		if (!WARP_LIST_FILE.exists()) {
	           return mat;
		  }
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(WARP_LIST_FILE);
		if(!warpConfig.contains(warp+".icon"))return mat;
		
		try{
		mat = Material.valueOf(warpConfig.getString(warp+".icon"));	
		}catch(NumberFormatException e){
			 warpConfig.set(warp+".icon", mat);
		}
			
			return mat;
	}
	
	public static void setWarpIcon(String warp, Material mat){

		  if (!WARP_LIST_FILE.exists()) {
	           return ;
		  }
		
		 FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(WARP_LIST_FILE);
		 if(mat == null){
			 warpConfig.set(warp+".icon", mat);
		 }else{
			 warpConfig.set(warp+".icon", mat.name());
		 }
		 
		 try {
			warpConfig.save(WARP_LIST_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
 * 	public static Warp getWarp(String s){
		if (!WARP_LIST_FILE.exists()) {
	        return null;
	       }
		return new Warp(s, getWarpIcon(s));
	}
	
	public static void addBanEntry(BanEntry eb){
		if(!BAN_ENTRY_FILE.exists()){
			try {
				BAN_ENTRY_FILE.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileConfiguration config =  YamlConfiguration.loadConfiguration(BAN_ENTRY_FILE); 
		List<String> list = config.getStringList("bans");
		if(list == null){
			list = new ArrayList<String>();
			list.add(eb.toString());
			return;
		}
		list.add(eb.toString());
		config.set("bans", Utils.cleanseList(list));
		try {
			config.save(BAN_ENTRY_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static List<String> getRawBanList(){
		List<String> list;
		
		
		list = new ArrayList<String>();
		if(!BAN_ENTRY_FILE.exists()){
			return list;
		}
		
		FileConfiguration config =  YamlConfiguration.loadConfiguration(BAN_ENTRY_FILE); 
		list = config.getStringList("bans");
		if(list == null){
			list = new ArrayList<String>();
			
		}
		return Utils.cleanseList(list);
		
		
	}
	public static Set<BanEntry> getBanEntryList(){
		Set<BanEntry> set = new HashSet<BanEntry>();
		if(getRawBanList().isEmpty())return set;
		
		for(String s : getRawBanList()){
			String[] parts = s.split("|");
			if(parts.length == 3){
				set.add(new BanEntry(UUID.fromString(parts[0]),Date.valueOf(parts[1]),parts[2]  ) );
			}
		}
		return set;
		
	}
	public synchronized static List<String> getBanTab(){
		final Set<String> set = new HashSet<String>();
		new Thread(){
			public void run(){
				for(BanEntry eb : getBanEntryList()){
					set.add(Bukkit.getOfflinePlayer(eb.getUUID()).getName());
				}
				
			}
		};
		return new ArrayList<String>(set);
	}
	*/
}
