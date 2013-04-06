package com.github.sebc722.xperms.permissions;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.github.sebc722.xperms.core.Main;

public class Xplayer {
	private Main xm;
	
	public Xplayer(Main main){
		xm = main;
	}
	
	public String getGroup(String playerName, String world){
		String playerGroup = null;
		
		if(xm.getUsers().get().isSet("users." + playerName + "." + world)){
			playerGroup = xm.getUsers().get().getString("users." + playerName + "." + world);
		}
		else{
			playerGroup = xm.getXgroup().getDefault(world);
		}
		
		return playerGroup;
	}
	
	public String getCurrentGroup(Player player){
		String currentGroup = null;
		
		if(haveCurrentGroup(player)){
			currentGroup = xm.getGroupMap().get(player);
		}
		
		return currentGroup;
	}
	
	public boolean haveCurrentGroup(Player player){
		if(xm.getGroupMap().containsKey(player)){
			return true;
		}
		return false;
	}
	
	public void setCurrentGroup(Player player, String playerGroup){
		if(haveCurrentGroup(player)){
			xm.getGroupMap().remove(player);
		}
		xm.getGroupMap().put(player, playerGroup);
	}
	
	public Player[] playersInGroup(String groupName){
		ArrayList<Player> players = new ArrayList<Player>();
		Player[] onlinePlayers = xm.getServer().getOnlinePlayers();
		
		for(int i = 0; i < onlinePlayers.length; i++){
			if(getCurrentGroup(onlinePlayers[i]).equals(groupName)){
				players.add(onlinePlayers[i]);
			}
		}
		
		return players.toArray(new Player[0]);
	}
	
	public boolean hasGroupForWorld(String player, String world){
		if(xm.getUsers().get().isSet("users." + player + "." + world)){
			return true;
		}
		return false;
	}
	
	public String getGroupForWorld(String player, String world){
		String group = null;
		group = xm.getUsers().get().getString("users." + player + "." + world);
		return group;
	}
	
	public boolean isOfflinePlayer(String playerName){
		if(xm.playerExists(playerName)){
			if(!xm.getServer().getPlayer(playerName).isOnline()){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPerm(String world, String playerName, String node){
		if(playerName == null || node == null){
			return false;
		}
		
		if(xm.getXpermissions().containsString(getNodes(playerName), node)){
			return true;
		}
		
		return false;
	}
	
	public boolean addNode(String world, String playerName, String node){
		if(playerName == null || node == null){
			return false;
		}
		ArrayList<String> list = new ArrayList<String>();
		
		if(!xm.getUsers().get().isSet("users." + playerName + ".permissions")){
			list.add(node);
			xm.getUsers().get().set("users." + playerName + ".permissions", list);
		}
		else{
			if(!xm.getXpermissions().containsString(getNodes(playerName), node)){
				String[] Nodes = getNodes(playerName);
				
				for(int i = 0; i < Nodes.length; i++){
					list.add(node);
				}
				list.add(node);
				xm.getUsers().get().set("users." + playerName + ".permissions", list);
			}
		}
		xm.getUsers().save();
		return true;
	}
	
	public boolean removeNode(String world, String playerName, String node){
		if(playerName == null || node == null){
			return false;
		}
		ArrayList<String> list = new ArrayList<String>();
		
		if(!xm.getUsers().get().isSet("users." + playerName + ".permissions")){
			return true;
		}
		else{
			if(xm.getXpermissions().containsString(getNodes(playerName), node)){
				String[] Nodes = getNodes(playerName);
				
				for(int i = 0; i < Nodes.length; i++){
					if(!Nodes[i].equalsIgnoreCase(node)){
						list.add(Nodes[i]);
					}
				}
				
				xm.getUsers().get().set("users." + playerName + ".permissions", list);
			}
		}
		xm.getUsers().save();
		return true;
	}
	
	public String[] getNodes(String playerName){
		if(!xm.getUsers().get().isSet("users." + playerName + ".permissions")){
			return null;
		}
		
		String[] Nodes;
		Nodes = xm.getUsers().get().getStringList("users." + playerName + ".permissions").toArray(new String[0]);
		return Nodes;
	}
	
	public String[] getPlayerGroups(String playerName){
		ArrayList<String> groups = new ArrayList<String>();
		String[] worlds = xm.getXworld().getAllWorlds();
		for(int i = 0; i < worlds.length; i++){
			if(xm.getUsers().get().isSet("users." + playerName + "." + worlds[i])){
				groups.add(xm.getUsers().get().getString("users." + playerName + "." + worlds[i]));
			}
		}
		
		return groups.toArray(new String[0]);
	}
	
	public boolean setPlayerGroup(String world, String playerName, String group){
		if(world == null || playerName == null || group == null){
			return false;
		}
		if(!xm.getXworld().isWorld(world) || !xm.playerExists(playerName) || !xm.getXgroup().isGroup(group)){
			return false;
		}
		
		xm.getUsers().get().set("users." + playerName + "." + world, group);
		xm.getUsers().save();
		try{
			xm.getXpermissions().setPermissions(xm.getServer().getPlayer(playerName), group);
		} catch(NullPointerException e){
			// Player is offline, do nothing
		}
		
		return true;
	}
	
	public boolean setPlayerDefault(String world, String playerName){
		if(world == null || playerName == null){
			return false;
		}
		if(!xm.getXworld().isWorld(world) || !xm.playerExists(playerName)){
			return false;
		}
		
		String group = xm.getXgroup().getDefault(world);
		xm.getUsers().get().set("users." + playerName + "." + world, group);
		xm.getUsers().save();
		try{
			xm.getXpermissions().setPermissions(xm.getServer().getPlayer(playerName), group);
		} catch(NullPointerException e){
			// Player is offline, do nothing
		}
		
		
		return true;
	}
	
	public boolean hasPrefix(String playerName){
		if(xm.getUsers().get().isSet("users." + playerName + ".prefix")){
			return true;
		}
		return false;
	}
	
	public String getPrefix(String playerName){
		if(!hasPrefix(playerName)){
			return null;
		}
		String prefix;
		prefix = xm.getUsers().get().getString("users." + playerName + ".prefix");
		return prefix;
	}
	
	public boolean hasSuffix(String playerName){
		if(xm.getUsers().get().isSet("users." + playerName + ".suffix")){
			return true;
		}
		return false;
	}
	
	public String getSuffix(String playerName){
		if(!hasPrefix(playerName)){
			return null;
		}
		String suffix;
		suffix = xm.getUsers().get().getString("users." + playerName + ".suffix");
		return suffix;
	}
}
