package com.github.sebc722.Xperms;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Xmain extends JavaPlugin {
	
	private Xlisteners xl = new Xlisteners(this, this);
	private Xcommands xc = new Xcommands(this, this);
	private Xyaml xperms = new Xyaml(this, this, "permissions.yml");
	private Xyaml xusers = new Xyaml(this, this, "users.yml");
	private Xpermissions xp = new Xpermissions(this, this);
	private Xchat xch = new Xchat(this, this);
	private Xplayer xpl = new Xplayer(this, this);
	private Xgroup xgr = new Xgroup(this, this);
	
	private HashMap<String, PermissionAttachment> playerPerms = new HashMap<String, PermissionAttachment>();
	
	@Override
	public void onEnable(){
		doEnable();
	}
	
	public void doEnable(){
		registerListeners();
		xperms.saveDefaultConfig(false);
		xusers.saveDefaultConfig(false);
		saveDefaultConfig();
		InitialCheck();
		if(getConfigVersion() < 1.5){
			reloadConfig();
			getConfig().set("SaveUsersOnJoin", true);
			getConfig().set("ConfigVersion", 1.5);
			saveConfig();
			getLogger().info("Config updated to 1.5!");
		}
	}
	
	@Override
	public void onDisable(){
		doDisable();
	}
	
	public void doDisable(){
		playerPerms.clear();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("xperms") || cmd.getName().equalsIgnoreCase("xp")){
			if(!sender.hasPermission("xperms.use")){
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this.");
				return true;
			}
			if(args[0].equalsIgnoreCase("reload")){
				reload();
				sender.sendMessage(ChatColor.DARK_PURPLE + "Xperms reloaded!");
				return true;
			}
			if(args[0].equalsIgnoreCase("reset")){
				resetConfigs();
				sender.sendMessage(ChatColor.DARK_PURPLE + "All config files are reset!");
				return true;
			}
			reloadAll();
			xc.mainExecutor(sender, cmd, label, args);
			return true;
		}
		return false;
	}
	
	public void reloadAll(){
		getXperms().reloadConfig();
		getXusers().reloadConfig();
		reloadConfig();
	}
	
	private void InitialCheck(){
		if(xperms.getDefaultGroup() == null){
			getLogger().severe("No default group is defined! Xperms will NOT function properly!");
		}
		if(checkForPlayers()){
			getLogger().info("Reload detected, updating all user permissions!");
			getXpermissions().updatePermissions("all");
		}
	}
	
	private boolean checkForPlayers(){
		if(getServer().getOnlinePlayers() == null){
			return false;
		}
		return true;
	}
	
	private void registerListeners(){
		getServer().getPluginManager().registerEvents(new Xlisteners(this, this), this);
	}
	
	private double getConfigVersion(){
		Double version;
		
		if(!getConfig().isSet("ConfigVersion")){
			return 0;
		}
		
		version = getConfig().getDouble("ConfigVersion");
		
		return version;
	}
	
	public void resetConfigs(){
		getXusers().saveDefaultConfig(true);
		getXperms().saveDefaultConfig(true);
		saveResource("config.yml", true);
	}
	
	public void reload(){
		reloadAll();
		getXpermissions().updatePermissions("all");
		getLogger().info("Xperms reloaded");
	}
	
	/*
	 * returns current Xlisteners instance
	 */
	public Xlisteners getXlisteners(){
		return xl;
	}
	
	/*
	 * returns current Xcommands instance
	 */
	public Xcommands getXcommands(){
		return xc;
	}
	
	/*
	 * returns current xperms instance
	 */
	public Xyaml getXperms(){
		return xperms;
	}
	
	/*
	 * returns current xusers instance
	 */
	public Xyaml getXusers(){
		return xusers;
	}
	
	/*
	 * returns current Xpermissions instance
	 */
	public Xpermissions getXpermissions(){
		return xp;
	}
	
	public Xplayer getXplayer(){
		return xpl;
	}
	
	public Xgroup getXgroup(){
		return xgr;
	}
	
	/*
	 * returns current Xchat instance
	 */
	public Xchat getXchat(){
		return xch;
	}
	
	/*
	 * returns current playerPerms instance
	 */
	public HashMap<String, PermissionAttachment> getPlayerPerms(){
		return playerPerms;
	}
	
	public Plugin getInstance(){
		return this;
	}
}