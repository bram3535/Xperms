package com.github.sebc722.Xperms;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

public class Xgroup {
	Plugin Xperms;
	Xmain xm;
	
	public Xgroup(Plugin plugin, Xmain main){
		Xperms = plugin;
		xm = main;
	}
	
	public boolean hasPermission(String group, String permission){
		String[] permissionNodes = xm.getXpermissions().getPermissionNodes(group);
		for(int i = 0; i < permissionNodes.length; i++){
			if(permissionNodes[i].equals(permission)){
				return true;
			}
		}
		return false;
	}
	
	public boolean addPermission(String group, String permission){
		List<String> permissions = new ArrayList<String>();
		permissions = xm.getXperms().getConfig().getStringList("groups." + group + ".permissions");
		String[] permissionNodes = permissions.toArray(new String[0]);
		
		for(int i = 0; i < permissionNodes.length; i++){
			if(permissionNodes[i].equals(permission)){
				return true;
			}
		}
		
		permissions.add(permission);
		xm.getXperms().getConfig().set("groups." + group + ".permissions", permissions);
		
		xm.getXpermissions().updatePermissions(group);
		return true;
	}
	
	public boolean removePermission(String group, String permission){
		List<String> permissions = new ArrayList<String>();
		permissions = xm.getXperms().getConfig().getStringList("groups." + group + ".permissions");
		String[] permissionNodes = permissions.toArray(new String[0]);
		
		for(int i = 0; i < permissionNodes.length; i++){
			if(permissionNodes[i].equals(permission)){
				permissions.remove(permission);
				xm.getXperms().getConfig().set("groups." + group + ".permissions", permissions);
			}
		}
		
		xm.getXpermissions().updatePermissions(group);
		return true;
	}
	
	public String[] getPlayers(String group){
		List<String> players = new ArrayList<String>();
		String[] playersInGroup = xm.getXusers().getUsers();
		
		for(int i = 0; i < playersInGroup.length; i++){
			if(xm.getXusers().getConfig().getString("users." + playersInGroup[i] + ".group").equals(group)){
				players.add(playersInGroup[i]);
			}
		}
		
		return playersInGroup;
	}
	
	public boolean isGroup(String groupName){
		if(xm.getXperms().getConfig().isSet("groups." + groupName)){
			return true;
		}
		return false;
	}
}
