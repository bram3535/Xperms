package com.github.sebc722.xperms.core;

/*
 * ChangeLog v2.4:
 *  - Added dynamic world handling
 *    - when a player joins a new world not encountered before
 *      Xperms will now check to see if the player's current
 *      group is compatible in the new world, if so, it will
 *      carry over
 */

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sebc722.xperms.config.*;
import com.github.sebc722.xperms.permissions.*;
import com.github.sebc722.xperms.metrics.*;

public class Main extends JavaPlugin {
	private String permissionsFileName = "permissions.yml";
	private String usersFileName = "users.yml";
	private String WebsiteURL = "http://dev.bukkit.org/server-mods/xperms";
	
	private Double currentVersion = 2.4;
	
	private Yaml permissionsFile = new Yaml(this, permissionsFileName);
	private Yaml usersFile = new Yaml(this, usersFileName);
	private ConfigChecker cc = new ConfigChecker(this, currentVersion);
	
	private Xgroup groupMethods = new Xgroup(this);
	private Xplayer playerMethods = new Xplayer(this);
	private Xworld worldMethods = new Xworld(this);
	
	private Xpermissions permissions = new Xpermissions(this);
	
	private HashMap<Player, PermissionAttachment> permissionsMap = new HashMap<Player, PermissionAttachment>();
	private HashMap<Player, String> groupsMap = new HashMap<Player, String>();
	
	public void onEnable(){
		registerListeners();
		saveResource(permissionsFileName, false);
		saveResource(usersFileName, false);
		saveDefaultConfig();
		reloadAll();
		cc.checkConfigFiles();
		
		if(statsAllowed()){
			Metrics metrics;
			try {
				metrics = new Metrics(this);
				metrics.start();
				getLogger().info("Stats collection started succesfully");
			} catch (IOException e) {
				getLogger().warning("Stats collection start up has failed");
			}
		}
		
		if(!haveDefault()){
			getLogger().warning("A global default group has not been defined! Please define one in permissions.yml");
		}
		
		if(allowedChecking("OnStart")){
			checkForUpdate();
		}
	}
	
	public void onDisable(){
		
	}
	
	@SuppressWarnings("unused")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("xperms") || cmd.getName().equals("xp")){
			if(!sender.hasPermission("xperms.use")){
				sender.sendMessage("You do not have permission to do that");
				return true;
			}
			
			try{
				String testForArguments = args[0];
			} catch(ArrayIndexOutOfBoundsException e){
				sender.sendMessage("Too few arguments!");
				new Commands(this).sendHelp(sender);
				return true;
			}
			reloadAll();
			new Commands(this).run(sender, cmd, label, args);
			return true;
		}
		return false;
	}
	
	private void registerListeners(){
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
	}
	
	private void checkForUpdate(){
		if(new UpdateChecker(this, currentVersion).Check()){
			getServer().getConsoleSender().sendMessage("[Xperms] " + ChatColor.DARK_GRAY + "An update is available for Xperms! download it here: " + ChatColor.GREEN + WebsiteURL);
		}
	}
	
	
	private boolean haveDefault(){
		if(getXgroup().getDefault("true") == null){
			return false;
		}
		return true;
	}
	
	// For test purposes
	/*private void printGroups(){
		String[] groups = getXgroup().getGroups();
		getLogger().info("Defined groups are: ");
		for(int i = 0; i < groups.length; i++){
			getLogger().info(groups[i]);
		}
	}*/
	
	public boolean playerExists(String playerName){
		if(getServer().getPlayerExact(playerName) == null){
			if(!getServer().getOfflinePlayer(playerName).hasPlayedBefore()){
				return false;
			}
		}
		return true;
	}
	
	public void reloadAll(){
		reloadConfig();
		permissionsFile.reload();
		usersFile.reload();
		getXpermissions().updatePermissions();
	}
	
	public void resetAll(){
		String[] files = {"config.yml", "permissions.yml", "users.yml"};
		for(int i = 0; i < files.length; i++){
			saveResource(files[i], true);
		}
	}
	
	public boolean statsAllowed(){
		if(getConfig().isSet("CollectStats")){
			if(getConfig().getString("CollectStats").equalsIgnoreCase("true")){
				return true;
			}
		}
		return false;
	}
	
	public boolean allowedChecking(String eventName){
		if(getConfig().getString("CheckUpdate." + eventName).equals("true")){
			return true;
		}
		
		return false;
	}
	
	public Yaml getPermissions(){
		return permissionsFile;
	}
	
	public Yaml getUsers(){
		return usersFile;
	}
	
	public Xgroup getXgroup(){
		return groupMethods;
	}
	
	public Xplayer getXplayer(){
		return playerMethods;
	}
	
	public Xworld getXworld(){
		return worldMethods;
	}
	
	public Xpermissions getXpermissions(){
		return permissions;
	}
	
	public HashMap<Player, PermissionAttachment> getPermissionsMap(){
		return permissionsMap;
	}
	
	public HashMap<Player, String> getGroupMap(){
		return groupsMap;
	}
	
	public Double getVersion(){
		return currentVersion;
	}
}
