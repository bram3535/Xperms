package com.github.sebc722.xperms.core;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener{
	private Main xm;
	
	private String WebsiteURL = "http://dev.bukkit.org/server-mods/xperms";
	
	public Listeners(Main main){
		xm = main;
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event){
		String playerName, playerGroup, playerWorld;
		Player player;
		player = event.getPlayer();
		playerName = player.getName();
		playerWorld = player.getWorld().getName();
		
		if(xm.getXplayer().hasGroupForWorld(playerName, playerWorld)){
			playerGroup = xm.getXplayer().getGroupForWorld(playerName, playerWorld);
			xm.getXpermissions().setPermissions(player, playerGroup);
		}
		else{
			playerGroup = xm.getXgroup().getDefault(playerWorld);
			if(saveUsersOnJoin()){
				String[] supported = xm.getXgroup().getSupportedWorlds(playerGroup);
				for(int i = 0; i < supported.length; i++){
					xm.getUsers().get().set("users." + playerName + "." + supported[i], playerGroup);
				}
				xm.getUsers().save();
			}
			xm.getXpermissions().setPermissions(player, playerGroup);
		}
		
		if(player.hasPermission("xperms.use")){
			if(!xm.allowedChecking("OnJoin")){
				return;
			}
			boolean update = new UpdateChecker(xm, xm.getVersion()).Check();
			if(update){
				player.sendMessage(ChatColor.DARK_GRAY + "An update is available for Xperms! download it here: " + ChatColor.GREEN + WebsiteURL);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerQuit(PlayerQuitEvent event){
		xm.getXpermissions().reset(event.getPlayer());
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerKicked(PlayerKickEvent event){
		xm.getXpermissions().reset(event.getPlayer());
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event){
		String playerName, playerGroup, playerWorld;
		Player player;
		player = event.getPlayer();
		playerName = player.getName();
		playerGroup = xm.getXplayer().getCurrentGroup(player);
		playerWorld = player.getWorld().getName();
		
		if(xm.getXplayer().hasGroupForWorld(playerName, playerWorld)){
			playerGroup = xm.getXplayer().getGroupForWorld(playerName, playerWorld);
			xm.getXpermissions().setPermissions(player, playerGroup);
			return;
		}
		else{
			if(xm.getXgroup().isSupportedByWorld(xm.getXplayer().getCurrentGroup(player), playerWorld)){
				playerGroup = xm.getXplayer().getCurrentGroup(player);
				xm.getXpermissions().setPermissions(player, playerGroup);
				return;
			}
			
			playerGroup = xm.getXgroup().getDefault(playerWorld);
			xm.getXpermissions().setPermissions(player, playerGroup);
			return;
		}
	}
	
	public boolean hasGroupForWorld(String playerName, String worldName){
		if(xm.getUsers().get().isSet("users." + playerName + "." + worldName)){
			return true;
		}
		return false;
	}
	
	public boolean hasGroup(String playerName){
		if(xm.getUsers().get().isSet("groups." + playerName + ".global")){
			return true;
		}
		return false;
	}
	
	public void setDefault(Player player){
		String playerGroup;
		String playerWorld;
		
		playerWorld = player.getWorld().getName();
		playerGroup = xm.getXgroup().getDefault(playerWorld);
		
		xm.getXpermissions().setPermissions(player, playerGroup);
	}
	
	private boolean saveUsersOnJoin(){
		if(xm.getConfig().getBoolean("SaveUsersOnJoin") || xm.getConfig().getString("SaveUsersOnJoin").equalsIgnoreCase("true")){
			return true;
		}
		return false;
	}
}
