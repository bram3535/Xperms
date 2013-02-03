package com.github.sebc722.Xperms;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public class Xchat {
	
	private Xmain xm;
	
	public Xchat(Plugin plugin, Xmain main){
		xm = main;
	}
	
	public void setDisplayName(String playerName){		
		String prefix, suffix, displayName, playerGroup;
		prefix = null;
		suffix = null;
		
		displayName = playerName;
		
		playerGroup = xm.getXusers().getUserGroup(playerName);
		
		if(xm.getXperms().getConfig().isSet("groups." + playerGroup + ".prefix")){
			prefix = xm.getXperms().getConfig().getString("groups." + playerGroup + ".prefix");
			if(prefix != null){
				prefix = Parse(prefix);
			}
		}
		
		if(xm.getXperms().getConfig().isSet("groups." + playerGroup + ".suffix")){
			suffix = xm.getXperms().getConfig().getString("groups." + playerGroup + ".suffix");
			if(suffix != null){
				suffix = Parse(suffix);
			}
		}
		
		Boolean hasPrefix = CheckNulls(prefix);
		Boolean hasSuffix = CheckNulls(suffix);
		
		if(hasPrefix){
			displayName = prefix + playerName;
			if(hasSuffix){
				displayName += suffix;
			}
		}
		else{
			if(hasSuffix){
				displayName = playerName + suffix;
			}
		}
		
		xm.getServer().getPlayer(playerName).setDisplayName(displayName);
	}
	
	public void updateDisplayName(){
		
	}
	
	/*
	 * method for checking for colours in
	 * given prefix/suffix
	 */
	private String Parse(String displayModifier){
		String result = null;
		
		String key = "&";
		
		result = ChatColor.translateAlternateColorCodes(key.charAt(0), displayModifier);
		
		return result;
	}
	
	private boolean CheckNulls(String s){
		if(s == null){
			return false;
		}
		return true;
	}
}