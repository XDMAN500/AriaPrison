package me.varmetek.prison.commands;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.DataManager;
import me.varmetek.prison.api.GlobalShop;
import me.varmetek.prison.api.GuiApi;
import me.varmetek.prison.api.User;
import me.varmetek.prison.enums.ParseMethod;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WorthCommand implements CommandExecutor,Listener
{

	@Override
	public boolean onCommand(CommandSender s,
			Command c, String l,
			String[] args) {
		if(s instanceof Player){
			if(c.getPermission() != null){
				if(!s.hasPermission(c.getPermission())){
					Messenger.send(s, Main.NO_PERM_MSG);
					return false;
				}
			}
		}
		if(!(s instanceof Player))return false;

		Player pl = (Player)s;
		User user = User.getUser(pl);
		
		int len = args.length;
		if(len == 0){
			ItemStack item = pl.getItemInHand();
			if(item == null){
				Messenger.send(s,"&cCan't get the worth of nothing :/");
				return false;
			}
			if(item.getType() == Material.AIR){
				Messenger.send(s,"&cCan't get the worth of nothing :/");
				return false;
			}
			if(!GlobalShop.PRICES.containsKey(item.getData())){
				Messenger.send(s, "&cThis item is not avaliable in the shop.");
				return false;
			}
			Messenger.send(s, "&7The worth of &c"+item.getTypeId()+":"+item.getData().getData()+" is &c" +Utils.formatCur(GlobalShop.PRICES.get(item.getData())));
			
		}else{
			if(args[0].equalsIgnoreCase("set")){
				if(!s.hasPermission("ariaprison.commands.worth.set")){
					Messenger.send(s, Main.NO_PERM_MSG);
					return false;
				}
				if(len ==1){
					Messenger.send(s, "&c/worth set <value>");
					return false;
				}
				double val = ParseMethod.parse(args[1]);
				if(Double.isNaN(val)){
					Messenger.send(s,"&cNot a valid number");
					return false;
				}
				ItemStack item = pl.getItemInHand();
				if(item == null){
					Messenger.send(s,"&cCan't set a value to air.");
					return false;
				}
				if(item.getType() == Material.AIR){
					Messenger.send(s,"&cCan't set a value to air.");
					return false;
				}
			
				if(val< 0){
					Messenger.send(s,"&7The item has been removed fom the economy.");
					GlobalShop.PRICES.remove(item.getData());
					return false;
				}
				GlobalShop.PRICES.put(item.getData(), val);
				Messenger.send(s,"&7The item &c"+item.getTypeId()+":"+item.getData().getData()+" &7is now worth "+ Utils.formatCur(val));
				
			}else{
				if(args[0].equalsIgnoreCase("all")){
					if(len ==1){
					
						
						user.setGui(GuiApi.WORTH);
						pl.openInventory(GlobalShop.viewPrices(0));
					}else{
						int page = 0;
						if(!Utils.isInt(args[1])){
							Messenger.send(s,"&cNot a valid number");
							return false;
						}
						page = Integer.parseInt(args[1])-1;
						if(page< 0){
							Messenger.send(s,"&cThe page must not be less than 1.");
							return false;
						}
						
						user.setGui(GuiApi.WORTH);
						pl.openInventory(GlobalShop.viewPrices(page));
					}
				}else{
					if(args[0].equalsIgnoreCase("reload")){
						if(!s.hasPermission("ariaprison.commands.worth.set")){
							Messenger.send(s, Main.NO_PERM_MSG);
							return false;
						}
						DataManager.loadGlobalShop();
						Messenger.send(s, "&7Prices reloaded.");
						
					}else{
						
						Messenger.send(s, "&cUnknown sub-command.");
					}
				}
					
				
			}
		}
		return false;
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onClick(InventoryClickEvent e){
		Player pl = (Player) e.getWhoClicked();
		User user = User.getUser(pl);
		if(user.getGui() == GuiApi.WORTH){
			e.setCancelled(true);
		}
	}

}
