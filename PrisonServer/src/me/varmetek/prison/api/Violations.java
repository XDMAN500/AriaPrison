package me.varmetek.prison.api;

import java.util.HashMap;

public class Violations extends HashMap<String, Object>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8050990018700257005L;
	public Violations getInf(){
		return this;
	}
	public int getInt(String s, int def){
		if(containsKey(s)){
			if(get(s) instanceof Integer){
			return ((Integer) get(s)).intValue();
			}else return def;
		}else{
			return def;
		}
	}
	public byte getByte(String s, byte def){
		if(containsKey(s)){
			if(get(s) instanceof Byte){
				return ((Byte) get(s)).byteValue();
				}else return def;
		}else{
			return def;
		}
	}

	public short getShort(String s, short def){
		if(containsKey(s)){
			if(get(s) instanceof Short){
				return ((Short) get(s)).shortValue();
				}else return def;
		}else{
			return def;
		}
	}
	public long getLong(String s, long def){
		if(containsKey(s)){
			if(get(s) instanceof Long){
				return ((Long) get(s)).longValue();
				}else return def;
		}else{
			return def;
		}
	}
	public double getDouble(String s, double def){
		if(containsKey(s)){
			if(get(s) instanceof Double){
				return ((Double) get(s)).doubleValue();
				}else return def;
		}else{
			return def;
		}
	}

	public float getFloat(String s,float def){
		if(containsKey(s)){
			if(get(s) instanceof Float){
				return ((Float) get(s)).floatValue();
				}else return def;
		}else{
			return def;
		}
	}

	public String getString(String s, String def){
		if(containsKey(s)){
			if(get(s) instanceof String){
				return ((String) get(s));
				}else return def;
		}else{
			return def;
		}
	}
	

}
