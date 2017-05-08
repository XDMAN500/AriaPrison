package me.varmetek.prison.mine;

import org.bukkit.material.MaterialData;

public class LocalBlock implements Comparable<LocalBlock>
{

	private MaterialData data;
	private float chance;
	
	public LocalBlock(int itemID, byte itemData,float itemChance){
		
		data = new MaterialData(itemID,itemData);
		chance = itemChance;
		
	}
	public LocalBlock(MaterialData itemData,float itemChance){
		data = itemData;
		chance = itemChance;
		
	}
	
	public int getID(){return data.getItemTypeId();}
	public byte getData(){return data.getData();}
	public MaterialData getMaterialData(){return data;}
	public void setMData(MaterialData d){
		if(d == null)return;
		data = d;
	}
	public float getChance(){return chance;}
	public void setId(int itemId){data = new MaterialData(itemId,data.getData());}
	public void setData(byte itemData){data.setData(itemData);	}
	public void setChance(float ch){chance = ch;}
	public String toString(){return getID()+":"+getData()+":"+chance;}
	public boolean isSimilar(LocalBlock bl){
		return this.data.equals(bl.data);
	}
	public boolean isSimilar(int id,byte data){
		return this.data.getItemTypeId() == id && this.data.getData() == data;
	}
	


	@Override
	public int compareTo(LocalBlock o) {
		if(!(o instanceof LocalBlock)){
			return 0;
		}
		
		return (int) Math.signum(
				 ((LocalBlock)o).getChance()-chance 
				);
	
	}
}
