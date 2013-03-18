package com.github.sebc722.xperms.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.sebc722.xperms.core.Main;

public class Yaml {
	private Main xm;
	private File configFile;
	private FileConfiguration config;
	String fileName;
	
	public Yaml(Main main, String fileName){
		xm = main;
		this.fileName = fileName;
	}
	
	public void reload(){
		if(configFile == null){
			configFile = new File(xm.getDataFolder(), fileName);
		}
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = xm.getResource(fileName);
		if(defConfigStream != null){
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}
	
	public FileConfiguration get(){
		if(config == null){
			reload();
		}
		return config;
	}
	
	public void save(){
		try {
			get().save(configFile);
		} catch (IOException e) {
			xm.getLogger().warning("Failed to save file '" + fileName + "'");
			e.printStackTrace();
		}
	}
}
