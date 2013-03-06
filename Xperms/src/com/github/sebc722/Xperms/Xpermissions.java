package com.github.sebc722.Xperms;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

public class Xpermissions {
	
	private Xmain xm;
	private Plugin Xperms;
	
	public Xpermissions(Plugin plugin, Xmain main){
		xm = main;
		Xperms = plugin;
	}
	
	public void setPermissions(String playerName, Player player){
		if(player == null){
			return;
		}
		resetAttachment(playerName);
		PermissionAttachment attachment = player.addAttachment(Xperms);
		String playerGroup = xm.getXusers().getUserGroup(playerName);
		
		setPermissionNodes(getPermissionNodes(playerGroup), attachment, player);
		if(xm.getXperms().getConfig().isSet("groups." + playerGroup + ".inherit")){
			setInheritedPermissions(playerGroup, attachment, player);
		}
		
		addToMap(playerName, attachment);
		xm.getXchat().setDisplayName(playerName);
	}
	
	private void setInheritedPermissions(String playerGroup, PermissionAttachment attachment, Player player){
		List<String> inheritedGroup = xm.getXperms().getConfig().getStringList("groups." + playerGroup + ".inherit");
		String[] inherit = inheritedGroup.toArray(new String[0]);
		
		for(int i = 0; i < inherit.length; i++){
			setPermissionNodes(getPermissionNodes(inherit[i]), attachment, player);
			
			if(xm.getXperms().getConfig().isSet("groups." + inherit[i] + ".inherit")){
				setInheritedPermissions(inherit[i], attachment, player);
			}
		}
	}
	
	public void resetAttachment(String PlayerName){
		if(!xm.getPlayerPerms().containsKey(PlayerName)){
			return;
		}
		xm.getServer().getPlayer(PlayerName).setOp(false);
		PermissionAttachment attachment = xm.getPlayerPerms().get(PlayerName);
		Player player = xm.getServer().getPlayer(PlayerName);
		
		try{
			player.removeAttachment(attachment);
		} catch(IllegalArgumentException e){
			return;
		}
	}
	
	public String[] getPermissionNodes(String group){
		List<String> NodesList;
		String[] NodesArray;
		
		NodesList = xm.getXperms().getConfig().getStringList("groups." + group + ".permissions");
		NodesArray = NodesList.toArray(new String[0]);
		
		return NodesArray;
	}
	
	public void setPermissionNodes(String[] Nodes, PermissionAttachment attachment, Player player){
		for(int i = 0; i < Nodes.length; i++){
			if(Nodes[i].equals("*") || Nodes[i].equals("'*'")){
				player.setOp(true);
			}
			if(Nodes[i].startsWith("-")){
				attachment.setPermission(Nodes[i], false);
			}
			else{
				attachment.setPermission(Nodes[i], true);
			}
		}
	}
	
	public void addToMap(String playerName, PermissionAttachment attachment){
		xm.getPlayerPerms().put(playerName, attachment);
	}
	
	public void updatePermissions(String group){
		if(!xm.getXgroup().isGroup(group)){
			return;
		}
		
		if(group == "all"){
			updateAllPermissions();
		}
		
		String[] players = xm.getXgroup().getPlayers(group);
		for(int i = 0; i < players.length; i++){
			setPermissions(players[i], xm.getServer().getPlayer(players[i]));
		}
	}
	
	private void updateAllPermissions(){
		String[] groups = xm.getXperms().getGroups();
		for(int i = 0; i < groups.length; i++){
			updatePermissions(groups[i]);
		}
	}
}