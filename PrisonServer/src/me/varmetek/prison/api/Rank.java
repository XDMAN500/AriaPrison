package me.varmetek.prison.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.varmetek.prison.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum Rank {
	Default("default player def reg regular", null,"&8","","&7"),
	Donator("donor", Default,"&a","","&7"),
	Supporter("sup", Donator,"&e","","&7"),
	Contributer("con trib", Supporter,"&5","","&b"),
	
 
    TrialMod("tmoderator tmod tm", Contributer,"&b","","&7&o"),
	Mod("moderator mod m", TrialMod,"&3","","&7&o"),
	ModPlus("moderatorp modp mp mod+ m+", Rank.Mod,"&1","","&7&o"),
	Admin("administrator admin", ModPlus,"&c","","&7&o"),
	Owner("own", Admin,"&4","","&c&o");
	/*
	 * 	Default("default player def reg regular", null,"&8","","&7"),
	Donator("donor", Default,"&a","","&7"),
	Supporter("sup", Donator,"&e","","&7"),
	//VIP("vip", Supporter,"&9","","&7"),
	Ultimate("ul", VIP,"&d","","&7"),
	Royalty("royal roy", Ultimate,"&5&o","","&7"),
   //Youtuber("youtube yt tuber yout tube ytube", Supporter,"&6","","&a"),
    TrialMod("tmoderator tmod tm", Royalty,"&b","","&7&o"),
	Mod("moderator mod m", TrialMod,"&3","","&7&o"),
	ModPlus("moderatorp modp mp mod+ m+", Rank.Mod,"&1","","&7&o"),
	Admin("administrator admin", ModPlus,"&c","","&7&o"),
	Owner("own", Admin,"&4","","&c&o");*/
	
	String alias;
	Rank inh;
	String prefix;
	String suffix;
	String chatColor; 

	private Rank(String alias, Rank inh, String prefix,String suffix, String chatcolor ){
		this.alias = alias;
		this.inh = inh;
		this.suffix =suffix;
		this.prefix= prefix;
		this.chatColor = chatcolor;
		
	}
	
	public String getPrefix(){
		return prefix;
	}
	
	public String getSuffix(){
		return suffix;
	}
	public String getChatColor(){
		return chatColor;
	}
	
	public Rank getDemote(){
		return inh;
	}
	
	public String getFullName(){
		return Utils.colorCode(prefix + name()+suffix);
	}
	
	public Rank getPromote(){
		if(this  == Owner){
			return null;
		}
		
		for(Rank r : Rank.getRanks()){
			if( r.getDemote() == this.getRank() ){
				return  r;
			}
		}
	return null;
	}
	
	public Rank getRank(){
		return Rank.valueOf(this.name());
	}
	
	
	public boolean doesInherient(Rank checkRank){
		Rank tempRank  = this;
		
		
		while (tempRank != checkRank) {
			tempRank = tempRank.inh;
			if( tempRank == null){
				return false;
			}
		}
		
		if(tempRank == checkRank){
			return true;
		}
		
		return false;
	}
	
	public Set<String> getNames(){
		Set<String> aliases = new HashSet<String>();
		aliases.add(this.name().toLowerCase());
		if(!alias.equals("")){
			for(String val: alias.split(" ")){
			
			aliases.add(val.toLowerCase());					
			}
		}
		
		return aliases;
		}
	
	public static Rank getRankByName(String rankName){
		if(rankName == null){return Rank.Default;}
		for(Rank temp : Rank.getRanks()){
			if(temp.getNames().contains(rankName.toLowerCase())){
				return temp;
			}
		}
		return null;
	}
	

/*	public static List<String> getUsersWithPemission(Rank rank){
		List<String> l = new ArrayList<String>();
		
		for(Player p : Bukkit.getOnlinePlayers()){
			
			if(User.getUser(Bukkit.getOfflinePlayer(p.getName())).hasPermission(rank)){
				l.add(p.getName());
			}
		}
		
		return l;
	}*/
	
	
	/*public static List<Player> getPlayersWithPemission(Rank rank){
		List<Player> l = new ArrayList<Player>();
		
		for(Player p : Bukkit.getOnlinePlayers()){
			
			if(User.getUser(Bukkit.getOfflinePlayer(p.getName())).hasPermission(rank)){
				l.add(p);
			}
		}
		
		return l;
	}*/
	
	public static Rank[] getRanks(){
		return values();
	}
}
