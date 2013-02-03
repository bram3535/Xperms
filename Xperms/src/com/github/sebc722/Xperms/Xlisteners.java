package com.github.sebc722.Xperms;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class Xlisteners implements Listener {
	
	Xmain xm;
	
	public Xlisteners(Plugin plugin, Xmain main){
		xm = main;
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event){
		if(xm.getXusers().getConfig().isSet("users." + event.getPlayer().getName())){
			xm.getXpermissions().setPermissions(event.getPlayer().getName(), event.getPlayer());
			return;
		}
		xm.getXusers().getConfig().set("users." + event.getPlayer().getName() + ".group", xm.getXperms().getDefaultGroup());
		xm.getXpermissions().setPermissions(event.getPlayer().getName(), event.getPlayer());
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerKicked(PlayerKickEvent event){
		xm.getXpermissions().resetAttachment(event.getPlayer().getName());
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerQuit(PlayerQuitEvent event){
		xm.getXpermissions().resetAttachment(event.getPlayer().getName());
	}
}