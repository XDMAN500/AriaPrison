package me.varmetek.prison.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;

import me.varmetek.prison.mine.LocalBlock;

public class RandomMaterial {
	private List<LocalBlock> materialSet;
	public RandomMaterial( List<LocalBlock> mSet){
		materialSet = mSet;
		
	}
	
	public LocalBlock nextBlock(int maxSamples){
		
		float[] percentages = new float[materialSet.size()];
		 LocalBlock block;
		 LocalBlock chosen = null;
		Random ran = new Random();
	
		// ran.setSeed(ran.nextLong());
		
		 
		for(int i = 0; i< percentages.length ; i++){
			block = materialSet.get(i);
   		  	percentages[i] = block.getChance();
		 }
	
		 for(int s =0; s<=maxSamples;s++){
		
			if(chosen != null){
				break;//Stop it from running through all of the samples if it has found one.
			}
			
			
   	  		for(int i = 0; i<  percentages .length ; i++){
   	  		ran.setSeed(ran.nextLong());
   	  		float chance = (ran.nextFloat()*100f);
   	  			if( chance <=  percentages [i] ){
   	  			///Bukkit.broadcastMessage(chance+"");
   	  			//	Bukkit.broadcastMessage(percentages [i]+"" );
   	  				chosen =materialSet.get(i);
   	  				break;
   	  			
   	  			}
   	  		}
   	  		
		 }
		
   	  	if(chosen == null ){//if not block is chosen then choose a random one;
   	  //	
   		  chosen = materialSet.get(ran.nextInt(materialSet.size()));

   	 	}
   	  	return chosen;
	}	
	
	
	/* private float[] dot(float[] input){
		 float magnitude = 0;
		 float[] output = new float[input.length];
		
		 for(int i = 0;i<input.length;i++){
			 magnitude += Math.pow(input[i], 2);
		 }
		
		 magnitude = (float) Math.sqrt(magnitude);
		 
	
		 
		 for(int i = 0;i<input.length;i++){
			 output[i] = magnitude / input[i];
		 }
		 return output;
	 }*/
}
