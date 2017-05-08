package me.varmetek.prison.mine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.DataManager;
import me.varmetek.prison.utils.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;


public class Mine{
	private static final Set<Mine> MINES= new HashSet<Mine>();
	//public static Map<String,Mine> getMines() {return MINES;}
	
	private static final List<Mine> RankedMines = new ArrayList<Mine>();	
	public static final int DEFAULT_DELAY = 600;
	
	 private Mine(String name,ProtectedRegion pr,World world,Location spawn){
		 
		 this.pr = pr;
		 this.name = name;
		 this.world = world;
		 spawnLoc = spawn;
		 pr.setFlag(DefaultFlag.BLOCK_BREAK, State.ALLOW);
		 pr.setFlag(DefaultFlag.BLOCK_PLACE, State.ALLOW);
		 pr.setFlag(DefaultFlag.BUILD, State.ALLOW);
		 pr.setFlag(DefaultFlag.EXP_DROPS,State.DENY);
		 pr.setFlag(DefaultFlag.PASSTHROUGH,State.ALLOW);
		 pr.setFlag(DefaultFlag.FALL_DAMAGE,State.DENY);
		 pr.setFlag(DefaultFlag.MOB_SPAWNING,State.DENY);

		 
	 }
	
	private final MineGenerator mg =  new MineGenerator(this);
	private int nextReset = 0;
	private List<LocalBlock> blocks =new ArrayList<LocalBlock>();;
	private int resetDelay = DEFAULT_DELAY;//10 minutes
	private ProtectedRegion pr;
	private String name;
	private World world;
	private Location spawnLoc; 
	private Comparator<LocalBlock> cp = new Comparator<LocalBlock>(){

		public int compare(LocalBlock b1, LocalBlock b2) {
			return b1.compareTo(b2);
			
		}
	}; 
	
	
	public static Set<Mine> getMines(){
		return MINES;
	}
	public static List<Mine> getRankedMines(){
		return RankedMines;
	}
	public void decrimentNext(){
		nextReset--;
	}
	public void shuffleBlockList(){
		Collections.shuffle(blocks);
	}
	 public List<LocalBlock> getBlockList(){
	//S	 Collections.shuffle(blocks);
		 return blocks;
		 }
	 public int getNextReset(){ return nextReset;}
	 public ProtectedRegion getRegion(){ return pr;}
	 public World getWorld(){return world;}
	
	 public String getName(){return name;}
	 public Location getSpawn(){return spawnLoc;}
	 public void setSpawn(Location loc){spawnLoc = loc;}
	 
	 public void setDelay(int time){
		 
		 resetDelay = time;
		 nextReset = time;
	 }
	 public long getDelay(){return resetDelay;}
	 
	 public void delete(){
		 removeRanked();
		 MINES.remove(this);
		 DataManager.deleteMine(this);
	 }
	 public LocalBlock getBlock(int id, byte data){
		 //if(blocks.isEmpty()){return null;}
		 for( LocalBlock b: blocks){
			 if(b.getID() == id && b.getData()  == data){
				 return b;
			 }
		 }
		 return null;
		
	 }
	 public LocalBlock getBlock(MaterialData d){
		 //if(blocks.isEmpty()){return null;}
		 for( LocalBlock b: blocks){
			 if(b.getMaterialData().equals(d)){
				 return b;
			 }
		 }
		 return null;
		
	 }
	 public void sortBlocks(){
		 Collections.sort(blocks, cp);
	 }
	 public boolean addLocalBlock(LocalBlock bl){
		
		 boolean change  = getBlock(bl.getMaterialData()) == null;
	
		 if(change)
		 {
			 blocks.add(bl);
			 sortBlocks();
			 return true;
		}else{
			return false;
			}
		 
	 }
	 public LocalBlock getLocalBlock(int id,byte data){
		 for(LocalBlock b: blocks ){
			 if(b.isSimilar(id, data)){
				 return b;
			 }
		 }
		 return null;
	 }
	 public boolean setLocalBlockChance(MaterialData d,float ch){
		 LocalBlock block = getBlock(d);
		 if(block == null){
			 return false;
		 }
		 block.setChance(ch);
		 return true;
		 
		 
	 }
	public boolean swapLocalBlock(MaterialData oldI,MaterialData newI){
		LocalBlock oldBlock = getBlock(oldI);
		LocalBlock newBlock = getBlock(newI);
		 if(oldBlock == null || newBlock != null){
			 return false;
		 }
		 
		 oldBlock.setMData(newI);
		 return true;
	}
	 public boolean setLocalBlockId(MaterialData d,int id){
		 LocalBlock block = getBlock(d);
		 if(block == null){
			 return false;
		 }
		 block.setId(id);
		 return true;
		 
		 
	 }
	 public boolean setLocalBlockData(MaterialData d,int data){
		 LocalBlock block = getBlock(d);
		 if(block == null){
			 return false;
		 }
		 block.setData((byte) data);
		 return true;
		 
		 
	 }

	 public boolean removeLocalBlock(MaterialData d){
		 
		 LocalBlock block = getBlock(d);
		
		 if(block == null)
		 {
			
			 return false;
		}else{
			 blocks.remove(block);
			 sortBlocks();
			 return true;
			}
	 }
	 public boolean isRanked(){return RankedMines.contains(this);}
	 public static Mine create(String name,ProtectedRegion pr,World world,Location spawn){
		 	if(getMine(name) == null){
		 		Mine m = new Mine(name,pr,world,spawn);
		 		MINES.add(m);
		 		return m;
		 	}
		 	return null;
	 }
	
	 public static Mine getMine(String name){
		for(Mine e : MINES){
			if(e.name.equalsIgnoreCase(name)){
				return e;
			}
			
		}
		return null;
	}

	 public int getRankedPos(){
		
			 return RankedMines.indexOf(this);
	
	 }
	 
	 public void setRanked(int pos){
		 if(isRanked())return;
		 int size = RankedMines.size();
		 if(pos<0)return;
		 if(pos< size){
			 RankedMines.set(pos, this);
		 }else{
			 RankedMines.add(this);
		 }
		 Bukkit.broadcastMessage(RankedMines.indexOf(this)+"");
	 }
	 public void removeRanked(){
		 if(!isRanked())return;
		 RankedMines.remove(getRankedPos());
	 }
	 
	 public void teleportPlayers(){
		 if(world == null)return;
		List<Player> players = world.getPlayers();
	
	
	
		for(Player player : players){
			Location loc = player.getLocation();
			if(pr.contains(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ())){
				 Messenger.send(player, "&7Mine &c"+name+" &7has reset.");
				player.teleport(getSpawn());
			
			}
			
		}
	 }
	 public void resetMine(){
		nextReset = resetDelay;
		teleportPlayers();
		 final List<LocalBlock> LLB = new ArrayList<LocalBlock>(blocks);
		 if(pr == null || LLB.isEmpty()) return;
		 mg.run();
		
		
		 
		 
	 }
	 public String getIcon(){
		 return "&8[&c&l"+getName()+"&8]";
	 }
	 public static String getRankedIcon(int i){
		 return "&8[&c&l"+getRankSymbol(i)+"&8]";
	 }
	 public void setRegion(BlockVector max, BlockVector min){
		
		pr = new ProtectedCuboidRegion("MineMc_"+name,
					max, min);
		 
	 }

	 public static Mine getMine(int x,int y,int z){
		 Mine output = null;
		 if(!Mine.MINES.isEmpty()){
			 for(Mine mine: Mine.MINES){
				 if(mine.getRegion().contains(x, y, z)){
					output = mine;
					break;
				}
			 }
		 }
		 return output;
	 }
	 public static Mine getMine(int x,int y,int z,World world){
		
		 if(!Mine.MINES.isEmpty()){
			 for(Mine mine: Mine.MINES){
				 if(mine.getRegion().contains(x, y, z) && mine.getWorld().equals(world)){
					return mine;
					
				}
			 }
		 }
		 return null;
	 }

	 public static String getRankSymbol(int r){
		
	 		if(r >=Mine.RankedMines.size()){
	 			return r+"";
	 		}
	 		return Mine.RankedMines.get(r).getName();
	 		
	 	}	
	 public static void loadRanked(){
		 List<String> names = DataManager.getConfig().getStringList("ranked");
		 if(names == null)
		 {	  
			return;
			
		 }else{
			 for(int i = 0; i< names.size();i++){
				 Mine m = getMine(names.get(i));
				 if(!RankedMines.contains(m)){
					 RankedMines.add(m);
				 }
			 	}
			
		 }
		 
	}
	 public static void saveRanked()
	 {
		 List<String> names = new ArrayList<String>();
		 for(int i = 0; i< RankedMines.size();i++){
			 Mine m = RankedMines.get(i);
			 if(m == null)continue;
			 names.add(m.name);
		 }
		 DataManager.getConfig().set("ranked",names);
		 DataManager.saveConfig();
		
	 }	 

	
	

}
