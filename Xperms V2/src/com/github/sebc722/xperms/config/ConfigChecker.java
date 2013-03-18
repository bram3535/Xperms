package com.github.sebc722.xperms.config;

import com.github.sebc722.xperms.core.Main;

public class ConfigChecker {
	private Main xm;
	private Double currentVersion;
	
	public ConfigChecker(Main main){
		xm = main;
		currentVersion = 2.0;
	}
	
	public void checkConfigFiles(){
		if(!isConfigUpToDate()){
			updateConfig();
		}
		if(!isUsersUpToDate()){
			updateUsers();
		}
	}
	
	private boolean isConfigUpToDate(){
		if(xm.getConfig().getDouble("ConfigVersion") < currentVersion){
			return false;
		}
		return true;
	}
	
	private boolean isUsersUpToDate(){
		if(xm.getUsers().get().isSet("FileVersion")){
			if(xm.getUsers().get().getDouble("FileVersion") < currentVersion){
				return false;
			}
			else{
				return true;
			}
		}
		return false;
	}
	
	private void updateConfig(){
		if(!xm.getConfig().isSet("SaveUsersOnJoin")){
			xm.getConfig().set("SaveUsersOnJoin", "true");
		}
		if(!xm.getConfig().isSet("ConfigVersion")){
			xm.getConfig().set("ConfigVersion", currentVersion);
		}
	}
	
	private void updateUsers(){
		xm.getUsers().get().set("FileVersion", currentVersion);
		
		String group;
		String[] worlds;
		String[] allUsers = getAllUsersOnFile();
		for(int i = 0; i < allUsers.length; i++){
			if(xm.getUsers().get().isSet("users." + allUsers[i] + ".group")){
				group = xm.getUsers().get().getString("users." + allUsers[i] + ".group");
				worlds = xm.getXgroup().getSupportedWorlds(group);
				for(int o = 0; o < worlds.length; o++){
					xm.getUsers().get().set("users." + allUsers[i] + "." + worlds[o], group);
				}
				
				xm.getUsers().get().set("users." + allUsers[i] + ".group", null);
			}
		}
		xm.getUsers().save();
	}
	
	private String[] getAllUsersOnFile(){
		String[] allUsers;
		allUsers = xm.getUsers().get().getConfigurationSection("users").getKeys(false).toArray(new String[0]);
		
		return allUsers;
	}
}
