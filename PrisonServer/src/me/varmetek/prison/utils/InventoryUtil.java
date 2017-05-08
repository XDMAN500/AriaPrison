package me.varmetek.prison.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class InventoryUtil {
	public static ItemStack getSmeltResult(ItemStack input){
		
		
		if(input == null) return null;
	
		for(FurnaceRecipe e: Utils.FurnaceRs)	  
			  if (e.getInput().getType() == input.getType()){
				  return  e.getResult();
			  }
		  
		
		return null;
	}
	
	public static boolean containsAll(Inventory inv,List<ItemStack> col){
		////(col.toString());
		List<ItemStack> iz = new ArrayList<ItemStack>();
		int yesCount = 0;
		for(ItemStack item: col){
			
		
			for(ItemStack i: inv){
			 if(i == null)continue;
			 if(compareItems(i,item)){
				 
				 if(i.getAmount()>0){
					 iz.add(i);
					 ItemStack toadd = new ItemStack(i);
					 toadd.setAmount(i.getAmount()-1);
					 inv.remove(i);
					 inv.addItem(toadd);
					// //(toadd.getAmount()+"");
			
					
					 yesCount++;
				 }else{
					 inv.remove(i);
				 }
		
				
				 
			 }
			}
		}
		
		boolean result = yesCount == col.size();
		if(!result){
			for(ItemStack e:  iz){
				inv.addItem(new ItemStack(e.getType(),1,e.getData().getData()));
				///inv.addItem(e);
			}
		}
		return  result;
	}
	public static  List<ItemStack> getall(Inventory inv, ItemStack item){
		List<ItemStack> list = new ArrayList<ItemStack>();
		for(ItemStack i:inv){
			if(i== null) continue;
			if(compareItems(i, item)){
				list.add(i);
			}
		}
		return list;
	}
	public static  int getAmount(Inventory inv, ItemStack item){
		int amount = 0;
	
		for(ItemStack i: getall(inv,item)){
			amount += i.getAmount();
		}
		return amount;
		
	}
	   public static void clearInv(Player pl){
	    	pl.getInventory().setContents(null);
	    	pl.getInventory().clear();
	    
	    }
	    
	    
	    public static void repairInv(PlayerInventory pi){
	    	for(ItemStack i: pi){
	    		if(i!= null){
	    			i.setDurability((short)0);
	    			}
	    	
	    	}
	    }
		public static boolean compareItems(ItemStack item1,ItemStack item2){
			if(item1 == null || item2 == null){
				return false;
			}
			return (item1.getTypeId() == item2.getTypeId() && item1.getData().getData() == item2.getData().getData());
			
		}
		public static boolean isFull(Inventory inv){
			return inv.firstEmpty()== -1;
		}
		public static boolean isEmpty(Inventory inv){
			for(ItemStack i: inv){
				if(i != null){
					if(i.getType() != Material.AIR){
						return false;
					}
				}else{
					return false;
				}
			}
			return true;
			
		}
		public static String format(ItemStack item){
			Material mat = item.getType();
			String output;
			if(mat!= null){
				output = mat.name().toLowerCase().replace("_", " ");
			}else{
				output =  item.getTypeId()+"";
			}
			if(item.getData().getData()!= 0){
				output+=":"+item.getData().getData();
			}
			return output;
			
		}
		public static Material parseMaterial(String e){
			e = e.toUpperCase();
			e= e.replaceAll(" ", "_");
		
			try{
				return Material.valueOf(e);
			}catch(Exception ex){
				return null;
			}
		}
	public static MaterialData parseData(String e){
			int id = 0;
			byte data = 0;
			
			String[] part = e.split(":",2);
			if(Utils.isInt(part[0])){
				id  = Integer.parseInt(part[0]);
				
			}else{
				Material mat = parseMaterial(part[0]);
				if(mat == null){
					return null;
					
				}else{
					id = mat.getId();
				}
			}
			if(part.length>1){
				if(Utils.isInt(part[1])){
					data = Byte.parseByte(part[1]);
				}
			}
			return new MaterialData(id,data);
			
		}
	public static List<String> getLore(ItemMeta im){
	List<String> lore = im.getLore();
	if(lore == null){
		lore = new ArrayList<String>();
	}
	return lore;
	}
		
}
