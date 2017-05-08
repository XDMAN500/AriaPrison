package me.varmetek.prison.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.varmetek.prison.enums.ParseMethod;
import me.varmetek.prison.utils.InventoryUtil;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;


public class ChestShop{
	public static final Set<ChestShop> shops =  new HashSet<ChestShop>();
	public static final Set<UUID> SHOPEDITORS  = new HashSet<UUID>();
	private UUID owner;
	private BlockLocation coords;
	private boolean isPost;
	private BlockFace facing;
	private String price;
	private Method method;
	private int amount;
	private MaterialData item;

	public enum Method{
		BUY("B"),SELL("S");
		String i;
		private Method(String icon){
			i = icon;
		}
		public String getIcon(){
			return i;
		}
		public static Method parse(String e){
			for(Method m : values()){
				if(m.i.equals(e)){
					return m;
				}
			}
			return null;
			
		}
	}
	public ChestShop(UUID creater, BlockLocation bl, BlockFace face,Method meth,String cost,int num, MaterialData data, boolean isPost){
		owner = creater;
		coords = bl;
		facing = face;
		price = cost;
		method = meth;
		amount = num;
		item = data;
		this.isPost = isPost;
		shops.add(this);

	}

	@SuppressWarnings("deprecation")
	public static ChestShop Deserialize(List<String> input){

		if(input.size()<8)return null;
		UUID owner = UUID.fromString(input.get(0));
		BlockLocation  coords = new BlockLocation(input.get(1));
		BlockFace  facing = BlockFace.valueOf(input.get(2));
		Method method = Method.valueOf(input.get(3));
		String price = input.get(4);
		int amount =  Integer.parseInt(input.get(5));
		String[] p = input.get(6).split(":",2);
		MaterialData item = new MaterialData(Integer.parseInt(p[0]), Byte.parseByte(p[1]));
		boolean isPost = Boolean.parseBoolean(input.get(7));
		
		return new ChestShop(owner,coords,facing,method,price,amount,item,isPost);
		
	}
	public UUID getOwner(){return owner;}
	public BlockLocation getLocation(){return coords;}
	public BlockFace getFace(){return facing;}
	public Method getMethod(){return method;}
	public String getPrice(){return price;}
	public int getAmount(){return amount;}
	public MaterialData getData(){return item;}
	public static boolean isContainer(Block bl){
		return bl.getState() instanceof InventoryHolder;
	}
	public static boolean isSign(Block bl){
		return bl.getType() == Material.WALL_SIGN || bl.getType() == Material.SIGN_POST;
	}
	public Block getBlock(){
		return coords.getBlock();
	}
	public boolean hasAccess(Player pl){
		return pl.getUniqueId().equals(owner) || pl.hasPermission("ariaprison.chestshop.admin");
	}
	public Block getAttached(){
		if(!(isSign(getBlock())))return null;
		//check();
		Attachable s = (Attachable) getBlock().getState().getData();

			return getBlock().getRelative(s.getAttachedFace());
		
	
	}
	public Inventory getInv(){
		Block at = getAttached();
		//check();
		if(at == null)return null;
		if(isContainer(at)){
			return ((InventoryHolder)at.getState()).getInventory();
		}else{
				return null;
			}
		
	}
	public void PlaceSign(){
		Block at = getBlock();
		at.setType(isPost? Material.SIGN_POST : Material.WALL_SIGN);
		org.bukkit.block.Sign  sign =  (org.bukkit.block.Sign)at.getState();
		sign.setLine(0, Utils.colorCode("&4"+Utils.getPlayer(owner).getName()));
		//sign.setLine(1, new Integer(amount).toString());
		//sign.setLine(2,method.getIcon()+" "+price);
		/*Material mat = item.getItemType();
		String toadd = "";
		toadd += (mat== null)? item.getItemTypeId():  mat.name().replace("_", "").toLowerCase();
		if(item.getData() != 0){
			toadd+=":"+item.getData();
		}
		sign.setLine(3, toadd);*/
	}
	public List<String> serialize(){
		List<String> output = new ArrayList<String>();
		output.add(owner.toString());
		output.add(coords.toString());
		output.add(facing.name());
		output.add(method.name());
		output.add(new Double(price).toString());
		output.add(new Integer(amount).toString());
		output.add(item.getItemTypeId()+":"+item.getData());
		output.add(new Boolean(isPost).toString());

		
		return output;
	}
	public boolean isPost(){
		return isPost;
	}
	public void destroy(){
		shops.remove(this);
		if(isSign(getBlock()))
			getBlock().breakNaturally();
	}
	/*public boolean check(){
		Block at = getAttached();
		if(at == null){
			destroy();
			return false;
		}
		if(!(at instanceof InventoryHolder)|| at.getType() == Material.AIR){
			
			destroy();
			return false;
		}
		return true;
	}*/
	public static void verifySign(String[] l) throws IllegalArgumentException{
		
		//Utils.SERVER.broadcastMessage(new ArrayList<String>(Arrays.asList(l)).toString());
		if(!Utils.isInt(l[1])){throw new IllegalArgumentException("Line 2 must be a valid number");}
		if(!l[2].contains(" ")){throw new IllegalArgumentException("Line 3 must be in the valid format \"<B,S> <Number>\".");}
		String[] split = l[2].split(" ",2);
		if(split.length != 2){throw new IllegalArgumentException("Line 3 must be in the valid format <B,S> \"Number\".");}
		if(Method.parse(split[0]) == null){throw new IllegalArgumentException("Line 3 on the left side must contain \"B\" or \"S\".");}
		if(ParseMethod.canParse(split[1])){throw new IllegalArgumentException("Line 3 on the right side is not valid.");}
		split = l[3].split(":",2);
		if(InventoryUtil.parseMaterial(split[0]) == null){
			if(!Utils.isInt(split[0])){
				throw new IllegalArgumentException("Line 4 does not contian a valid item");
			}
		if(split.length>1){
			
			if(!Utils.isInt(split[1])){throw new IllegalArgumentException("Line 4 does not contain valid data");}
			
			
		}
		
		
		
		
		
		}
	}
	/*public static boolean verifySign(org.bukkit.block.Sign s) throws IllegalArgumentException{
	if(s == null){return false;}
	String[] l = s.getLines();
	if(!Utils.isInt(l[1])){return false;}
	if(!l[2].contains(" ")){return false;}
	String[] split = l[2].split(" ",2);
	if(split.length != 2){return false;}
	if(Method.parse(split[0]) == null){return false;}
	if(ParseMethod.canParse(split[2])){return false;}
	split = l[3].split(":",2);
	if(Utils.parseMaterial(split[0]) == null){return false;}
	if(split.length>1){
		
		if(!Utils.isInt(split[1])){return false;}
		
		
	}
	
	
	
	return true;
	

}*/

	
}
