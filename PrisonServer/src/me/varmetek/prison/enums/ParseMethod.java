package me.varmetek.prison.enums;

import java.util.HashMap;
import java.util.Map;

import me.varmetek.prison.utils.Utils;

public enum ParseMethod {

	NORMAL,SCIENTIFIC,ABREVIATION,ABV_MULTI;
	private static Map<String,Integer> abv = new HashMap<String,Integer>();
	private static boolean setup  = false;
	private static void setupAbv(){
		setup = true;
		abv.put("k", 3);
		abv.put("K", 3);
		abv.put("M", 6);
		abv.put("m", 6);
		abv.put("b", 9);
		abv.put("B", 9);
		abv.put("T", 12);
		abv.put("t", 12);
	}
	
/*	private double parseE(String e)throws IllegalArgumentException {
		if(!e.contains("E")){
			throw new IllegalArgumentException("Cannot compile: "+ e);
		}
		String[] p  = e.split("E",2);
			if(!Utils.isDouble(p[0])){
				throw new IllegalArgumentException("Cannot compile: "+ p[0]);
			}
			if(!Utils.isDouble(p[1])){
				throw new IllegalArgumentException("Cannot compile: "+ p[1]);
			}
			return Double.parseDouble(p[0])* Math.pow(10d, Double.parseDouble(p[1]));
	}
	*/
	public static  double parse(ParseMethod m,String e) throws IllegalArgumentException {
		if(canParse(m,e)){
			
			switch(m){
			case ABREVIATION:
				if(!setup){
					setupAbv();
				}
			
				if(!e.contains(":")){
					throw new IllegalArgumentException("Cannot compile: "+ e);	
				}
				String[] p1 = e.split(":",2);
			

				if(!Utils.isDouble(p1[0]) ){
					throw new IllegalArgumentException("Cannot compile: "+ p1[0]);	
				}
			
				if(!abv.containsKey(p1[1])){
					throw new IllegalArgumentException("Cannot compile: "+ p1[1]);	
				}
			
				return Double.parseDouble(p1[0])*Math.pow(10,abv.get(p1[1]));
			case SCIENTIFIC:
				if(!e.contains("E")){
					throw new IllegalArgumentException("Cannot compile: "+ e);
				}
				String[] p  = e.split("E",2);
					if(!Utils.isDouble(p[0])){
						throw new IllegalArgumentException("Cannot compile: "+ p[0]);
					}
					if(!Utils.isDouble(p[1])){
						throw new IllegalArgumentException("Cannot compile: "+ p[1]);
					}
					return Double.parseDouble(p[0])* Math.pow(10d, Double.parseDouble(p[1]));
			case ABV_MULTI:
				double value = 0d;
				String[] split = e.split(",");
				for(String f:split){
					
						if(! canParse(ABREVIATION ,f)){
							throw new IllegalArgumentException("Cannot compile: "+ f);
						}else{
							value += parse(ABREVIATION,f);
						}
					
				}
				return value;
			case NORMAL:
				if(!Utils.isDouble(e))throw new IllegalArgumentException("Cannot parse \""+e+"\".");
				
				return Double.parseDouble(e);
			
			}
		}else{
			throw new IllegalArgumentException("Cannot compile: "+ e);
			
		}
		return Double.NaN;
		
	}
	public static double parse(String e){
		for(ParseMethod m: values()){
			if(canParse(m,e)){
				return parse(m,e);
			}
		}
		return Double.NaN;
	}
	public static boolean canParse(String e){
		for(ParseMethod m: values()){
			if(!canParse(m,e)){
				return false;
			}
		}
		return true;
	}
	public static boolean canParse(ParseMethod m,String e){
		switch(m){
			case ABREVIATION:
			
		if(!setup){
			setupAbv();
		}
	
		if(!e.contains(":")){
			return false;
		}
		String[] p1 = e.split(":",2);
	

		if(!Utils.isDouble(p1[0]) ){
			return false;	
		}
	
		if(!abv.containsKey(p1[1])){
			return false;	
		}
		break;
		case SCIENTIFIC:
			if(!e.contains("E")){
				return false;
			}
			String[] p2 = e.split("E",2);
			if(!Utils.isDouble(p2[0])){
				return false;
			}
			if(!Utils.isDouble(p2[1])){
				return false;
			}
			break;
		case ABV_MULTI:
			if(e.contains(",") && !e.contains(":"))return false;
			String[] split = e.split(",");
			for(String f:split){
				
					if(! canParse(ABREVIATION ,f)){
						return false;
					}
				
			}
		case NORMAL:
			if(!Utils.isDouble(e))return false;
			
			break;
		
		
		}
		return true;
			
				
	}
	
}
