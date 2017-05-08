package me.varmetek.prison.commands;

import me.varmetek.prison.api.GlobalShop;
import me.varmetek.prison.api.User;
import me.varmetek.prison.utils.InventoryUtil;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

public class SellCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender s,
			Command c, String l,
			String[] args) {
		if(!(s instanceof  Player))return false;
		final Player pl = (Player)s;
		final User user = User.getUser(pl);
		Inventory inv = pl.getInventory();
		double total = 0d;
		for(ItemStack item : inv){
			if(item == null)continue;
			if(!GlobalShop.PRICES.containsKey(item.getData()))continue;
			total += GlobalShop.PRICES.get(item.getData())* InventoryUtil.getAmount(inv, item);
			for(ItemStack d:InventoryUtil.getall(inv, item)){
				inv.remove(item);
				
			}
				
			
		}
		total*= user.getTotalBuff();
	
		Messenger.send(s, "&7+ "+Utils.formatCur(total) + (user.getTotalBuff()!= 1?" &8(&6&lx"+user.getTotalBuff() +"&8)":""));
		
		try {
			Economy.add(pl.getName(), total);
		} catch (NoLoanPermittedException | UserDoesNotExistException e) {
			
			e.printStackTrace();
		}
		return false;
	}
	

}
