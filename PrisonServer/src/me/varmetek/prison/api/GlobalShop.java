package me.varmetek.prison.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.varmetek.prison.utils.Utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import com.earth2me.essentials.api.Economy;

public class GlobalShop {
	public static final Map<MaterialData,Double> PRICES =  new HashMap<MaterialData,Double>();
	private enum CompareMethod{
		PRICE_MIN,PRICE_MAX,ITEM_MAX,ITEM_MIN;
	}
	private static final Comparator<MaterialData> IM = new Comparator<MaterialData>(){

		public int compare(MaterialData b1, MaterialData b2) {
			return  compareM(CompareMethod.ITEM_MAX,b1,b2);
			
		}
	}; 
	private static final Comparator<MaterialData> IL = new Comparator<MaterialData>(){

		public int compare(MaterialData b1, MaterialData b2) {
			return  compareM(CompareMethod.ITEM_MIN,b1,b2);
			
		}
	}; 
	private static final Comparator<MaterialData> PM = new Comparator<MaterialData>(){

		public int compare(MaterialData b1, MaterialData b2) {
			return  compareM(CompareMethod.PRICE_MAX,b1,b2);
			
		}
	}; 
	private static final Comparator<MaterialData> PL = new Comparator<MaterialData>(){

		public int compare(MaterialData b1, MaterialData b2) {
			return  compareM(CompareMethod.PRICE_MIN,b1,b2);
			
		}
	}; 
	public static Inventory viewPrices(int page){
		int size = 54;
		page = Math.max(0, page);
		List<MaterialData> list = new ArrayList<MaterialData>(PRICES.keySet());
		Collections.sort(list, PL);
		Inventory inv = Utils.SERVER.createInventory(null, size,"Prices");
		
		for(int i = 0;i<size;i++){
			if(i + ((size-1)*page)<PRICES.size()){
				MaterialData md = list.get(i + ((size-1)*page));
				ItemStack it = md.toItemStack(1);
				ItemMeta im = it.getItemMeta();
				im.setDisplayName(Utils.colorCode("&2"+Utils.formatCur(PRICES.get(md))));
				it.setItemMeta(im);
				inv.addItem(it);
			}
		}
		return inv;
		
	}
	private static int compareM(CompareMethod e,MaterialData i1, MaterialData i2){
	
		if(!PRICES.containsKey(i1)){
			return -1;
		}else{
			if(!PRICES.containsKey(i2)){
				return 1;
			}
		}
		int s = 0;;
		switch(e){
		case ITEM_MAX:
			
			if(i1.getItemTypeId()== i2.getItemTypeId()){
				s = (int) Math.signum(i1.getData()- i2.getData());
			}else{
				s = (int) Math.signum(i1.getItemTypeId()- i2.getItemTypeId());
			}
			break;
		case ITEM_MIN:
			if(i2.getItemTypeId()== i1.getItemTypeId()){
				s = (int) Math.signum(i2.getData()- i1.getData());
			}else{
				s = (int) Math.signum(i2.getItemTypeId()- i1.getItemTypeId());
			}
			break;
		case PRICE_MAX:
			 s = (int) Math.signum(PRICES.get(i1)-PRICES.get(i2));
			break;
		case PRICE_MIN:
			s = (int) Math.signum(PRICES.get(i2)-PRICES.get(i1));
			break;
		default:
			break;
		
		}
		return s;
		
	}

}
