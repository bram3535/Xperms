package com.github.sebc722.Xperms;

import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

public class Xpermissions {
	
	private Xmain xm;
	private Plugin Xperms;
	
	/*
	 * Xpermissions constructor
	 */
	public Xpermissions(Plugin plugin, Xmain main){
		xm = main;
		Xperms = plugin;
	}
	
	/*
	 * sets permissions for specified user
	 */
	public void setPermissions(String playerName, Player player){
		resetAttachment(playerName);
		
		if(player == null){
			return;
		}
		
		PermissionAttachment attachment = player.addAttachment(Xperms);
		
		String playerGroup = xm.getXusers().getUserGroup(playerName);
		
		List<String> Nodes = xm.getXperms().getConfig().getStringList("groups." + playerGroup + ".permissions");
		String[] PermissionNodes = Nodes.toArray(new String[0]);
		
		for(int i = 0; i < PermissionNodes.length; i++){
			if(PermissionNodes[i].equals("'*'") || PermissionNodes[i].equals("*")){
				player.setOp(true);
			}
			if(PermissionNodes[i].startsWith("-")){
				attachment.setPermission(PermissionNodes[i], false);
			}
			else{
				attachment.setPermission(PermissionNodes[i], true);
			}
		}
		
		if(xm.getXperms().getConfig().isSet("groups." + playerGroup + ".inherit")){
			setInheritedPermissions(playerName, attachment);
		}
		
		addToMap(playerName, attachment);
		xm.getXchat().setDisplayName(playerName);
	}
	
	/*
	 * sets permissions for specified user
	 * based on inheritance for user's group
	 */
	private void setInheritedPermissions(String playerName, PermissionAttachment attachment){
		String playerGroup = xm.getXperms().getConfig().getString("users." + playerName + ".group");
		List<String> inheritedGroup = xm.getXperms().getConfig().getStringList("groups." + playerGroup + ".inherit");
		String[] inherit = inheritedGroup.toArray(new String[0]);
		
		for(int i = 0; i < inherit.length; i++){
			String group = inherit[0];
			List<String> groupNodes = xm.getXperms().getConfig().getStringList("groups." + group + ".permissions");
			String[] Nodes = groupNodes.toArray(new String[0]);
			
			for(int l = 0; l < Nodes.length; l++){
				if(Nodes[l].startsWith("-")){
					attachment.setPermission(Nodes[i], false);
				}
				else{
					attachment.setPermission(Nodes[i], true);
				}
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
	
	/*
	 * adds player and associated
	 * PermissionAttachment to PlayerPerms
	 * HashMap in Xmain
	 */
	public void addToMap(String playerName, PermissionAttachment attachment){
		xm.getPlayerPerms().put(playerName, attachment);
	}
	
	/*
	 * removes player and associated
	 * PermissionAttachment from PlayerPerms
	 * HashMap in Xmain
	 */
	public void removeFromMap(String playerName){
		xm.getServer().getPlayer(playerName).removeAttachment(xm.getPlayerPerms().get(playerName));
		xm.getPlayerPerms().remove(playerName);
	}
	
	/*
	 * called on shut down to empty the
	 * PlayerPerms HashMap
	 */
	public void clearMap(){
		Set<String> remainingAttachments = xm.getPlayerPerms().keySet();
		String[] toRemove = remainingAttachments.toArray(new String[0]);
		
		for(int i = 0; i < toRemove.length; i++){
			if(xm.getPlayerPerms().isEmpty()){
				break;
			}
			xm.getServer().getPlayer(toRemove[i]).removeAttachment(xm.getPlayerPerms().get(toRemove[i]));
			xm.getPlayerPerms().remove(toRemove[i]);
		}
	}
	
	/*
	 * Checks map for player specified
	 */
	public boolean checkMap(String playerName){
		boolean isKey = xm.getPlayerPerms().containsKey(playerName);
		return isKey;
	}
}