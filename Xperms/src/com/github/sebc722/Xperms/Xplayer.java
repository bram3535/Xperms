package com.github.sebc722.Xperms;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.plugin.Plugin;

public class Xplayer {
	Xmain xm;
	Plugin Xperms;
	
	public Xplayer(Plugin plugin, Xmain main){
		Xperms = plugin;
		xm = main;
	}
	
	public boolean hasPermission(String playerName, String Node){
		String playerGroup = xm.getXusers().getUserGroup(playerName);
		String[] permissionNodes;

		permissionNodes = xm.getXpermissions().getPermissionNodes(playerGroup);
		for(int i = 0; i < permissionNodes.length; i++){
			if(permissionNodes[i].equals(Node)){
				return true;
			}
		}
		return false;
	}
	
	public boolean addPermission(String player, String permission){
		String playerGroup = xm.getXusers().getUserGroup(player);
		List<String> permissions = new ArrayList<String>();
		permissions = xm.getXperms().getConfig().getStringList("groups." + playerGroup + ".permissions");
		String[] permissionNodes = permissions.toArray(new String[0]);
		
		for(int i = 0; i < permissionNodes.length; i++){
			if(permissionNodes[i].equals(permission)){
				return true;
			}
		}
		
		permissions.add(permission);
		xm.getXperms().getConfig().set("groups." + playerGroup + ".permissions", permissions);
		
		xm.getXpermissions().updatePermissions(playerGroup);
		return true;
	}
	
	public boolean removePermission(String player, String permission){
		String playerGroup = xm.getXusers().getUserGroup(player);		
		List<String> permissions = new ArrayList<String>();
		permissions = xm.getXperms().getConfig().getStringList("groups." + playerGroup + ".permissions");
		String[] permissionNodes = permissions.toArray(new String[0]);
		
		for(int i = 0; i < permissionNodes.length; i++){
			if(permissionNodes[i].equals(permission)){
				permissions.remove(permission);
				xm.getXperms().getConfig().set("groups." + playerGroup + ".permissions", permissions);
			}
		}
		
		xm.getXpermissions().updatePermissions(playerGroup);
		return true;
	}
	
	public boolean setGroup(String playerName, String groupName){
		if(xm.getXperms().getConfig().isSet("groups." + groupName)){
			xm.getXusers().getConfig().set("users." + playerName + ".group", groupName);
			xm.getXusers().saveConfig();
			xm.getXpermissions().setPermissions(playerName,  xm.getServer().getPlayer(playerName));
			return true;
		}
		return false;
	}
}
