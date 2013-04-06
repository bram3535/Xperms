package com.github.sebc722.xperms.permissions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.github.sebc722.xperms.chat.Xchat;
import com.github.sebc722.xperms.core.Main;

public class Xpermissions {
	private Main xm;
	private Xchat xc;
	
	public Xpermissions(Main main){
		xm = main;
		xc = new Xchat(xm);
	}
	
	public void setPermissions(Player player, String playerGroup){
		if(haveAttachment(player)){
			reset(player);
		}
		
		PermissionAttachment attachment = player.addAttachment(xm);
		if(inherits(playerGroup)){
			setInheritedPermissions(player, attachment, playerGroup);
		}
		
		setGroupPermissions(player, playerGroup, attachment);
		
		if(playerHasPermissions(player.getName())){
			setPlayerPermissions(player, attachment);
		}
		
		storeAttachment(player, attachment, playerGroup);
		xc.setDisplayName(player, playerGroup);
	}
	
	public void setGroupPermissions(Player player, String playerGroup, PermissionAttachment attachment){
		String[] Nodes = getNodes(playerGroup);
		setNodes(Nodes, attachment, player);
	}
	
	public void setInheritedPermissions(Player player, PermissionAttachment attachment, String playerGroup){
		String[] inherited = getInherited(playerGroup);
		
		for(int i = 0; i < inherited.length; i++){
			String[] Nodes = getNodes(inherited[i]);
			setNodes(Nodes, attachment, player);
			
			if(xm.getPermissions().get().isSet("groups." + inherited[i] + ".inherit")){
				setInheritedPermissions(player, attachment, inherited[i]);
			}
		}
	}
	
	public void setPlayerPermissions(Player player, PermissionAttachment attachment){
		String[] Nodes = xm.getXplayer().getNodes(player.getName());
		setNodes(Nodes, attachment, player);
	}
	
	public void updatePermissions(Player player){
		String playerGroup = xm.getXplayer().getCurrentGroup(player);
		
		setPermissions(player, playerGroup);
	}
	
	public void updatePermissions(String groupName){
		Player[] playersToUpdate = xm.getXplayer().playersInGroup(groupName);
		
		for(int i = 0; i < playersToUpdate.length; i++){
			setPermissions(playersToUpdate[i], groupName);
		}
	}
	
	public void updatePermissions(){
		Player[] players = xm.getServer().getOnlinePlayers();
		String playerGroup;
		
		for(int i = 0; i < players.length; i++){
			playerGroup = xm.getXplayer().getCurrentGroup(players[i]);
			setPermissions(players[i], playerGroup);
		}
	}
	
	public String[] getNodes(String groupName){
		String[] Nodes = null;
		Nodes = xm.getPermissions().get().getStringList("groups." + groupName + ".permissions").toArray(new String[0]);
		return Nodes;
	}
	
	private void setNodes(String[] Nodes, PermissionAttachment attachment, Player player){
		for(int i = 0; i < Nodes.length; i++){
			if(Nodes[i].equals("*") || Nodes[i].equals("'*'")){
				player.setOp(true);
			}
			if(Nodes[i].startsWith("-")){
				String Node = Nodes[i].replaceFirst("-", "");
				attachment.setPermission(Node, false);
			}
			else{
				attachment.setPermission(Nodes[i], true);
			}
		}
	}
	
	public void addNode(String group, String Node){
		String[] Nodes = getNodes(group);
		
		if(containsString(Nodes, Node)){
			return;
		}
		else{
			List<String> nodes;
			nodes = xm.getPermissions().get().getStringList("groups." + group + ".permissions");
			nodes.add(Node);
			xm.getPermissions().get().set("groups." + group + ".permissions", nodes);
			xm.getPermissions().save();
		}
	}
	
	public boolean containsString(String[] list, String item){
		for(int i = 0; i < list.length; i++){
			if(list[i].equalsIgnoreCase(item)){
				return true;
			}
		}
		return false;
	}
	
	public void removeNode(String group, String Node){
		String[] Nodes = getNodes(group);
		ArrayList<String> NodeList = new ArrayList<String>();
		
		if(containsString(Nodes, Node)){
			for(int i = 0; i < Nodes.length; i++){
				if(!Nodes[i].equalsIgnoreCase(Node)){
					NodeList.add(Nodes[i]);
				}
			}
		}
		
		xm.getPermissions().get().set("groups." + group + ".permissions", NodeList);
		xm.getPermissions().save();
	}
	
	private void storeAttachment(Player player, PermissionAttachment attachment, String playerGroup){
		xm.getPermissionsMap().put(player, attachment);
		xm.getXplayer().setCurrentGroup(player, playerGroup);
	}
	
	private boolean haveAttachment(Player player){
		if(xm.getPermissionsMap().containsKey(player)){
			return true;
		}
		return false;
	}
	
	public void reset(Player player){
		player.removeAttachment(xm.getPermissionsMap().get(player));
		player.removeMetadata(player.getName() + "CurrentPlayerGroup", xm);
		
		if(haveAttachment(player)){
			xm.getPermissionsMap().remove(player);
		}
		
		if(player.isOp()){
			player.setOp(false);
		}
	}
	
	private boolean inherits(String groupName){
		if(xm.getPermissions().get().isSet("groups." + groupName + ".inherit")){
			return true;
		}
		return false;
	}
	
	private boolean playerHasPermissions(String playerName){
		if(xm.getUsers().get().isSet("users." + playerName + ".permissions")){
			return true;
		}
		return false;
	}
	
	private String[] getInherited(String groupName){
		String[] groupsInherited;
		groupsInherited = xm.getPermissions().get().getStringList("groups." + groupName + ".inherit").toArray(new String[0]);
		return groupsInherited;
	}
	
	public String[] getLadder(String ladderName){
		String[] groupsOnLadder = xm.getConfig().getStringList("ladders." + ladderName).toArray(new String[0]);
		return groupsOnLadder;
	}
}
