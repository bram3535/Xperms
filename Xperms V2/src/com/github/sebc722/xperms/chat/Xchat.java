package com.github.sebc722.xperms.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.sebc722.xperms.core.Main;

public class Xchat {
	private Main xm;
	
	public Xchat(Main main){
		xm = main;
	}
	
	public void setDisplayName(Player player, String playerGroup){
		String Prefix, Suffix, DisplayName, Key;
		
		Prefix = xm.getPermissions().get().getString("groups." + playerGroup + ".prefix");
		Suffix = xm.getPermissions().get().getString("groups." + playerGroup + ".suffix");
		Key = "&";
		
		if(notNull(Prefix)){
			Prefix = ChatColor.translateAlternateColorCodes(Key.charAt(0), Prefix);
			if(notNull(Suffix)){
				Suffix = ChatColor.translateAlternateColorCodes(Key.charAt(0), Suffix);
				DisplayName = Prefix + player.getName() + Suffix;
				player.setDisplayName(DisplayName);
				return;
			}
			DisplayName = Prefix + player.getName();
			player.setDisplayName(DisplayName);
			return;
		}
		else{
			if(notNull(Suffix)){
				Suffix = ChatColor.translateAlternateColorCodes(Key.charAt(0), Suffix);
				DisplayName = player.getName() + Suffix;
				player.setDisplayName(DisplayName);
				return;
			}
			return;
		}
	}
	
	public boolean notNull(String item){
		if(item != null){
			return true;
		}
		return false;
	}
}
