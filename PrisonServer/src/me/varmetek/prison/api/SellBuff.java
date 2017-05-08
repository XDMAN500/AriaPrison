package me.varmetek.prison.api;

import me.varmetek.prison.utils.Utils;

public class SellBuff {
	private float amount;
	private long seconds;
	
	public SellBuff(float a,long s){
		amount = a;
		seconds = s;
	}
	public float getAmount(){
		return amount;
	}
	public long getTime(){
		return seconds;
	}
	public static SellBuff fromString(String s){
		if(!s.contains("_") ){
			return null;
		}
		String[] part = s.split("_");

		long sec = 0;
		float am = 0f;
		try{
			am = Float.parseFloat(part[0]);
			sec = Long.parseLong(part[1]);
		}catch(Exception e){return null;}
		return new SellBuff(Float.parseFloat(part[0]),sec);
	}
	public void decrimentTime(){
		seconds --;
	}
	@Override
	public String toString(){
		return amount+""+"_"+seconds+"";
		
	}
}
