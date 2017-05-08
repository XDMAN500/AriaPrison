package me.varmetek.prison.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.varmetek.prison.utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class SideBar {
	private Scoreboard sb = Utils.SERVER.getScoreboardManager().getNewScoreboard();
	private Objective side;
	public SideBar(String name){
		side = sb.registerNewObjective(ChatColor.stripColor(Utils.colorCode(name)), "dummy");
		side.setDisplaySlot(DisplaySlot.SIDEBAR);
		side.setDisplayName(Utils.colorCode(name));
	}
	public void setDisplayName(String name){
		side.setDisplayName(Utils.colorCode(name));
	}
	public Objective getObj(){
		return side;
	}
	public String getEntry(int id){
		List<String> e = new ArrayList<String>(sb.getEntries());
		if(id>=0 && id<e.size()){
			return e.get(id);
		}
		return null;
	}
	public void clearSlot(int i){
		String e = getEntry(i);
		if(e!= null){
			sb.resetScores(e);
		}

	}
	public void reset(){
		Set<String> e = sb.getEntries();
		for(String s: e)sb.resetScores(s);
		
	/*	String name = side.getName();
		String display = side.getDisplayName();
		side.unregister();
		side = sb.registerNewObjective(name, "dummy");
		side.setDisplaySlot(DisplaySlot.SIDEBAR);
		side.setDisplayName(display);*/
	}
	public Scoreboard getBoard(){
		return sb;
	}
	public void setSlot(int id,String name){
		//clearSlot(id);
		side.getScore(Utils.colorCode(name)).setScore(id);
	}
}
