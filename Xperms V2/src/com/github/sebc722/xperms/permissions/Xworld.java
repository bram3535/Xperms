package com.github.sebc722.xperms.permissions;

import java.util.ArrayList;

import org.bukkit.World;

import com.github.sebc722.xperms.core.Main;

public class Xworld {
	private Main xm;
	
	public Xworld(Main main){
		xm = main;
	}
	
	public boolean isWorld(String world){
		String[] worlds = getAllWorlds();
		
		for(int i = 0; i < worlds.length; i++){
			if(worlds[i].equalsIgnoreCase(world)){
				return true;			
			}
		}
		
		return false;
	}
	
	public String[] getAllWorlds(){
		ArrayList<String> worlds = new ArrayList<String>();
		World[] worldList;
		worldList = xm.getServer().getWorlds().toArray(new World[0]);
		
		for(int i = 0; i < worldList.length; i++){
			worlds.add(worldList[i].getName());
		}
		
		return worlds.toArray(new String[0]);
	}
}
