package me.varmetek.prison.commands;

import java.text.DecimalFormat;
import java.util.List;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.DataManager;
import me.varmetek.prison.api.User;
import me.varmetek.prison.mine.Mine;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;

public class StatsCommand implements CommandExecutor,TabCompleter
{

	@Override
	public boolean onCommand(CommandSender s, Command c, String l,
			String[] args) {
		int len = args.length;
		OfflinePlayer targPl = null;
		if(len == 0){
			if( !(s instanceof Player))return false;
			targPl = (Player)s;
		}else{
			targPl  = Utils.getPlayer(args[0]);
		}
			
			
		
		if(!targPl.hasPlayedBefore() && !targPl.isOnline()){
			Messenger.send(s, "&c That Player has never played before");
			return false;
		}
		
		User user = User.getUser(targPl);
		if(!targPl.isOnline()){
			DataManager.loadUser(user);
		}
		Messenger.send(s, " &7&oStats of \"&5"+targPl.getName()+"&7&o\".");
		Mine mine  =  null;
		DecimalFormat df = new DecimalFormat("0.##%");
		if(user.getMinerRank()>=0){
			if(user.getMinerRank() < Mine.getRankedMines().size()){
				mine = Mine.getRankedMines().get(user.getMinerRank());
			}
		}
		Messenger.send(s,"&7Rank: &c"+ ( mine != null ? mine.getIcon():"None" ));
		Messenger.send(s,"&7Sell Multiplier: &cx"+ Utils.formatNum(user.getTotalBuff()));
		try {
			Messenger.send(s, "&7Money: &c"+Utils.formatCur(Economy.getMoney(targPl.getName())));
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		}
		if(mine != null){
			try {
				Messenger.send(s,"&7RankUp Completion: &c"+ df.format(Math.min(1.0,Economy.getMoney(targPl.getName())/Main.rankupcosts.get(user.getMinerRank()) )));
			} catch (UserDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!targPl.isOnline()){
		user.remove();
		}
		return false;
	}
	@Override
	public List<String> onTabComplete(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] args) {
		int len = args.length;
		if(len == 1){
			return Utils.matchOnlinePlayers(args[0]);
		}
		return null;
	}
	
	

}
