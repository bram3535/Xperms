package com.github.sebc722.Xperms;

/*
 * not reading groups properly
 */

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
	
	private HashMap<String, PermissionAttachment> playerPerms = new HashMap<String, PermissionAttachment>();
	
	@Override
	public void onEnable(){
		registerListeners();
		xperms.saveDefaultConfig();
		xusers.saveDefaultConfig();
		saveDefaultConfig();
		InitialCheck();
	}
	
	@Override
	public void onDisable(){
		xp.clearMap();
		xperms.saveConfig();
		xusers.saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("xperms") || cmd.getName().equalsIgnoreCase("xp")){
			if(!sender.hasPermission("xperms.use")){
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this.");
				return true;
			}
			xc.mainExecutor(sender, cmd, label, args);
			return true;
		}
		return false;
	}
	
	private void InitialCheck(){
		if(xperms.getDefaultGroup() == null){
			getLogger().severe("No default group is defined! Xperms will NOT function properly!");
		}
	}
	
	private void registerListeners(){
		getServer().getPluginManager().registerEvents(new Xlisteners(this, this), this);
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