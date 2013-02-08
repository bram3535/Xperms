package com.github.sebc722.Xperms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Xyaml {
	private Xmain xm;
	private FileConfiguration config;
	private File configFile;
	private String FileName;
	
	/*
	 * Class constructor
	 */
	public Xyaml(Plugin plugin, Xmain main, String fileName){
		xm = main;
		FileName = fileName;
	}
	
	/*
	 * reloads custom config
	 */
	public void reloadConfig(){
		if(configFile == null){
			configFile = new File(xm.getDataFolder(), FileName);
		}
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = xm.getResource(FileName);
		if(defConfigStream != null){
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}
	
	/*
	 * returns config object
	 */
	public FileConfiguration getConfig(){
		if(config == null){
			reloadConfig();
		}
		return config;
	}
	
	/*
	 * Saves config
	 */
	public void saveConfig(){
		if(config == null || configFile == null){
			return;
		}
		try{
			getConfig().save(configFile);
		} catch(IOException e) {
			xm.getLogger().log(Level.WARNING, "Could not save config file " + FileName, e);
		}
	}
	
	/*
	 * Save default config from resources
	 */
	public void saveDefaultConfig(){
		xm.saveResource(FileName, false);
	}

	/*
	 * returns default group in permissions.yml
	 */
	public String getDefaultGroup(){
		if(FileName != "permissions.yml"){
			return null;
		}
		
		String defaultGroup = null;
		String[] groups = getGroups();
		
		if(groups == null){
			return null;
		}
		
		for(int i = 0; i < groups.length; i++){
			if(getConfig().isSet("groups." + groups[i] + ".default")){
				if(getConfig().getString("groups." + groups[i] + ".default").equalsIgnoreCase("true")){
					defaultGroup = groups[i];
					break;
				}
				if(getConfig().getBoolean("groups." + groups[i] + ".default")){
					defaultGroup = groups[i];
					break;
				}
			}
		}
		
		return defaultGroup;
	}
	
	/*
	 * returns an array of all groups
	 * defined in permissions.yml
	 */
	public String[] getGroups(){
		String[] groups = null;
		List<String> listOfGroups = new ArrayList<String>();
		ConfigurationSection section = getConfig().getConfigurationSection("groups");
		if(section == null){
			return null;
		}
		
		for(String Node : section.getKeys(false)){
			listOfGroups.add(Node.toString());
		}
		groups = listOfGroups.toArray(new String[0]);
		
		return groups;
	}
	
	public String[] getUsers(){
		String[] Users;
		List<String> listOfUsers = new ArrayList<String>();
		ConfigurationSection section = getConfig().getConfigurationSection("users");
		if(section == null){
			return null;
		}
		
		for(String Node : section.getKeys(false)){
			listOfUsers.add(Node.toString());
		}
		Users = listOfUsers.toArray(new String[0]);
		
		return Users;
	}
	
	/*
	 * if user is in users.yml, this returns
	 * the user's group
	 */
	public String getUserGroup(String playerName){
		String userGroup = null;
		
		if(getConfig().isSet("users." + playerName)){
			userGroup = getConfig().getString("users." + playerName + ".group");
		}
		else{
			userGroup = getDefaultGroup();
		}
		return userGroup;
	}
}