package me.varmetek.prison.commands;

import java.util.ArrayList;
import java.util.List;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.User;
import me.varmetek.prison.mine.Mine;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

public class RankupCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
			if(!(sender instanceof Player)){
				Messenger.send(sender, "&cSorry, only players :)");
				return false;
			}
			if(cmd.getPermission() != null){
				if(!sender.hasPermission(cmd.getPermission())){
					Messenger.send(sender, Main.NO_PERM_MSG);
					return false;
				}
			}
			User user = User.getUser((Player)sender);
			if(user.getMinerRank()>=Main.maxRank-1){
				Messenger.send(sender, "&cSorry, there is else nothing to rank up too.");
				return false;
			}
			
				try {
					if(Economy.hasEnough(sender.getName(),Main.rankupcosts.get(user.getMinerRank()))){
						Economy.subtract(sender.getName(),  Main.rankupcosts.get(user.getMinerRank()) );
						user.setMinerRank(user.getMinerRank()+1);
						Messenger.send(sender,"&7You have ranked up to &a"+ Mine.getRankSymbol(user.getMinerRank())+"&7.");
						List<Player> plz =  new ArrayList<Player>(Utils.SERVER.getOnlinePlayers());
						plz.remove(sender);
						Messenger.sendGroup(plz,"&c"+sender.getName()+" &7 has ranked up to "+ Mine.getRankedIcon(user.getMinerRank()) );
					}else{
						double needed = Main.rankupcosts.get(user.getMinerRank())-Economy.getMoney(sender.getName());
						Messenger.send(sender,"&cYou need &e$"+Utils.formatNum(needed)+" &cmore in order to rankup to &e"+Mine.getRankSymbol(user.getMinerRank()+1));
					}
				} catch (NoLoanPermittedException | UserDoesNotExistException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		return false;
	}
	

}
