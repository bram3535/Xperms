package com.github.sebc722.xperms.permissions;

import java.util.ArrayList;
import java.util.Set;

import com.github.sebc722.xperms.core.Main;

public class Xgroup {
	private Main xm;
	
	public Xgroup(Main main){
		xm = main;
	}
	
	public boolean isGroup(String groupName){
		String[] groups = getGroups();
		if(groups == null){
			return false;
		}
		
		for(int i = 0; i < groups.length; i++){
			if(groups[i].equals(groupName)){
				return true;
			}
		}
		
		return false;
	}
	
	public String[] getGroups(){
		Set<String> groupsSet = xm.getPermissions().get().getConfigurationSection("groups").getKeys(false);
		
		return groupsSet.toArray(new String[0]);
	}
	
	public String getDefault(String world){
		String defaultGroup = getDefaultGroup(world);
		if(defaultGroup == null){
			defaultGroup = getDefaultGroup("true");
		}
		
		return defaultGroup;
	}
	
	private String getDefaultGroup(String world){
		String[] groups = getGroups();
		
		if(world == null){
			world = "true";
		}
		
		for(int i = 0; i < groups.length; i++){
			if(xm.getPermissions().get().isSet("groups." + groups[i] + ".default")){
				if(xm.getPermissions().get().getString("groups." + groups[i] + ".default").equalsIgnoreCase(world)){
					return groups[i];
				}
				if(world.equalsIgnoreCase("true")){
					if(xm.getPermissions().get().getBoolean("groups." + groups[i] + ".default")){
						return groups[i];
					}
				}
			}
		}
		
		return null;
	}
	
	public boolean isSupportedByWorld(String group, String world){
		if(xm.getPermissions().get().isSet("groups." + group + ".worlds")){
			String[] worldsSupported = xm.getPermissions().get().getStringList("groups." + group + ".worlds").toArray(new String[0]);
			
			for(int i = 0; i < worldsSupported.length; i++){
				if(worldsSupported[i].equalsIgnoreCase(world)){			
					return true;
				}
				if(worldsSupported[i].equalsIgnoreCase("-" + world)){
					return false;
				}
			}
			
			return false;
		}
		else{
			return true;
		}
	}
	
	public String[] getWorlds(String groupName){
		if(xm.getPermissions().get().isSet("groups." + groupName + ".worlds")){
			return xm.getPermissions().get().getStringList("groups." + groupName + ".worlds").toArray(new String[0]);
		}
		return null;
	}
	
	public String[] getSupportedWorlds(String group){
		ArrayList<String> supported = new ArrayList<String>();
		String[] worlds = getWorlds(group);
		if(worlds == null){
			worlds = xm.getXworld().getAllWorlds();
		}
		
		if(isDefault(group)){
			String[] disallowed = getDefaultClaimedWorlds(group);
			if(disallowed == null){
				for(int i = 0; i < worlds.length; i++){
					if(!worlds[i].startsWith("-")){
						supported.add(worlds[i]);
					}
				}
			}
			else{
				for(int i = 0; i < worlds.length; i++){
					boolean allow = true;
					for(int o = 0; o < disallowed.length; o++){
						if(worlds[i].equalsIgnoreCase(disallowed[o])){
							allow = false;
							break;
						}
					}
					
					if(allow){
						if(!worlds[i].startsWith("-")){
							supported.add(worlds[i]);
						}
					}
				}
			}
		}
		else{
			for(int i = 0; i < worlds.length; i++){
				if(!worlds[i].startsWith("-")){
					supported.add(worlds[i]);
				}
			}
		}
		
		return supported.toArray(new String[0]);
	}
	
	private String[] getDefaultClaimedWorlds(String neglectedGroup){
		ArrayList<String> worlds = new ArrayList<String>();
		String[] defaults = getDefaults();
		
		for(int i = 0; i < defaults.length; i++){
			if(!defaults[i].equals(neglectedGroup)){
				String world = xm.getPermissions().get().getString("groups." + defaults[i] + ".default");
				if(!world.equalsIgnoreCase("true") || !world.equalsIgnoreCase("false")){
					worlds.add(world);
				}
				if(world.equalsIgnoreCase("true")){
					String[] unclaimed = getUnclaimed();
					for(int o = 0; o < unclaimed.length; o++){
						worlds.add(unclaimed[o]);
					}
				}
			}
		}
		
		return worlds.toArray(new String[0]);
	}
	
	private String[] getUnclaimed(){
		ArrayList<String> unclaimed = new ArrayList<String>();
		ArrayList<String> worlds = new ArrayList<String>();
		String[] defaults = getDefaults();
		
		for(int i = 0; i < defaults.length; i++){
			String world = xm.getPermissions().get().getString("groups." + defaults[i] + ".default");
			if(!world.equalsIgnoreCase("true") || !world.equalsIgnoreCase("false")){
				worlds.add(world);
			}
		}
		
		String[] allWorlds = xm.getXworld().getAllWorlds();
		String[] claimed = worlds.toArray(new String[0]);
		for(int i = 0; i < allWorlds.length; i++){
			boolean isClaimed = false;
			for(int o = 0; o < claimed.length; o++){
				if(allWorlds[i].equalsIgnoreCase(claimed[o])){
					isClaimed = true;
					break;
				}
			}
			
			if(!isClaimed){
				unclaimed.add(allWorlds[i]);
			}
		}
		
		return unclaimed.toArray(new String[0]);
	}
	
	public String[] getDefaults(){
		String[] allGroups = getGroups();
		ArrayList<String> defaults = new ArrayList<String>();
		
		for(int i = 0; i < allGroups.length; i++){
			if(xm.getPermissions().get().isSet("groups." + allGroups[i] + ".default")){
				defaults.add(allGroups[i]);
			}
		}
		
		return defaults.toArray(new String[0]);
	}
	
	private boolean isDefault(String group){
		if(xm.getPermissions().get().isSet("groups." + group + ".default")){
			if(xm.getPermissions().get().getBoolean("groups." + group + ".default") || !xm.getPermissions().get().getString("groups." + group + ".default").equalsIgnoreCase("false")){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPerm(String group, String node){
		if(group == null || node == null){
			return false;
		}
		
		if(xm.getXpermissions().containsString(xm.getXpermissions().getNodes(group), node)){
			return true;
		}
		return false;
	}
	
	public boolean addNode(String group, String node){
		if(group == null || node == null){
			return false;
		}
		
		xm.getXpermissions().addNode(group, node);
		return true;
	}
	
	public boolean removeNode(String group, String node){
		if(group == null || node == null){
			return false;
		}
		
		xm.getXpermissions().removeNode(group, node);
		return true;
	}
}
