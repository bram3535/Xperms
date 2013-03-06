package com.github.sebc722.Xperms;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class Xcommands {
	
	private Xmain xm;
	
	/*
	 * Xcommands constructor
	 */
	public Xcommands(Plugin plugin, Xmain main){
		xm = main;
	}
	
	/*
	 * checks what action to take based on
	 * first argument given
	 */
	public void mainExecutor(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length < 2){
			displayOptions(sender);
			return;
		}
		
		String First = args[0].toLowerCase();
		
		switch (First){
		case "add":
			if(args.length < 3){
				sender.sendMessage(ChatColor.RED + "insufficient arguments!");
				displayOptions(sender);
				break;
			}
			addCommand(sender, cmd, args);
			break;
		case "remove":
			if(args.length < 3){
				sender.sendMessage(ChatColor.RED + "insufficient arguments!");
				displayOptions(sender);
				break;
			}
			removeCommand(sender, cmd, args);
			break;
		case "create":
			if(args.length < 2){
				sender.sendMessage(ChatColor.RED + "insufficient arguments!");
				displayOptions(sender);
				break;
			}
			createCommand(sender, cmd, args);
			break;
		case "delete":
			if(args.length < 2){
				sender.sendMessage(ChatColor.RED + "insufficient arguments!");
				displayOptions(sender);
				break;
			}
			deleteCommand(sender, cmd, args);
			break;
		case "promote":
			if(args.length < 3){
				sender.sendMessage(ChatColor.RED + "insufficient arguments!");
				displayOptions(sender);
				break;
			}
			promoteCommand(sender, cmd, args);
			break;
		case "demote":
			if(args.length < 3){
				sender.sendMessage(ChatColor.RED + "insufficient arguments!");
				displayOptions(sender);
				break;
			}
			demoteCommand(sender, cmd, args);
			break;
		case "reload":
			xm.reload();
			sender.sendMessage(ChatColor.DARK_PURPLE + "Xperms reloaded!");
			break;
		case "reset":
			xm.resetConfigs();
			sender.sendMessage(ChatColor.DARK_PURPLE + "Configuration files reset!");
			break;
		case "help":
			displayOptions(sender);
			break;
		case "?":
			displayOptions(sender);
			break;
		default:
			if(args[1].equalsIgnoreCase("inherits")){
				if(args.length < 4){
					sender.sendMessage(ChatColor.RED + "insufficient arguments!");
					displayOptions(sender);
					break;
				}
				inheritCommand(sender, cmd, args);
			}
			else{
				displayOptions(sender);
				break;
			}
		}
	}
	
	private void addCommand(CommandSender sender, Command cmd, String[] args){
		String Second = args[1];
		String Third = args[2];
		String Fourth;
		
		switch(Second.toLowerCase()){
		case "prefix":
			Fourth = args[3];
			if(xm.getXperms().getConfig().isSet("groups." + Fourth)){
				xm.getXperms().getConfig().set("groups." + Fourth + ".prefix", Third);
				xm.getXperms().saveConfig();
				xm.getXpermissions().updatePermissions(Fourth);
				sender.sendMessage(ChatColor.DARK_PURPLE + "The prefix " + Third + " has been added to " + Fourth);
			}
			else{
				sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + Fourth + " does not appear to exist!");
			}
			break;
		case "suffix":
			Fourth = args[3];
			if(xm.getXperms().getConfig().isSet("groups." + Fourth)){
				xm.getXperms().getConfig().set("groups." + Fourth + ".suffix", Third);
				xm.getXperms().saveConfig();
				xm.getXpermissions().updatePermissions(Fourth);
				sender.sendMessage(ChatColor.DARK_PURPLE + "The suffix " + Third + " has been added to " + Fourth);
			}
			else{
				sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + Fourth + " does not appear to exist!");
			}
			break;
		case "permission":
			Third = args[2];
			Fourth = args[3];
			xm.getXgroup().addPermission(Fourth, Third);
			sender.sendMessage(ChatColor.DARK_PURPLE + "Permission node " + Third + " was added to " + Fourth);
			break;
		default:
			if(xm.getServer().getPlayer(Second) != null || xm.getServer().getOfflinePlayer(Second) != null){
				if(xm.getXperms().getConfig().isSet("groups." + Third)){
						xm.getXusers().getConfig().set("users." + Second + ".group", Third);
						xm.getXusers().saveConfig();
						xm.getXpermissions().setPermissions(Second,  xm.getServer().getPlayer(Second));
						sender.sendMessage(ChatColor.DARK_PURPLE + "The player " + Second + " now belongs to " + Third);
				}
				else{
					sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + Third + " does not appear to exist!");
				}
			}
			else{
				displayOptions(sender);
			}
			break;
		}
	}
	
	private void removeCommand(CommandSender sender, Command cmd, String[] args){
		String Second, Third, Fourth;
		Second = args[1];
		switch(Second.toLowerCase()){
		case "prefix":
			Fourth = args[3];
			if(xm.getXperms().getConfig().isSet("groups." + Fourth + ".prefix")){
				xm.getXperms().getConfig().set("groups." + Fourth + ".prefix", null);
				xm.getXperms().saveConfig();
				xm.getXpermissions().updatePermissions(Fourth);
				sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + Fourth + " no longer has a prefix");
			}
			else{
				sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + Fourth  + " does not have a defined prefix!");
			}
			break;
		case "suffix":
			Fourth = args[3];
			if(xm.getXperms().getConfig().isSet("groups." + Fourth + ".suffix")){
				xm.getXperms().getConfig().set("groups." + Fourth + ".suffix", null);
				xm.getXperms().saveConfig();
				xm.getXpermissions().updatePermissions(Fourth);
				sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + Fourth + " no longer has a suffix");
			}
			else{
				sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + Fourth + " does not have a defined suffix!");
			}
			break;
		case "permission":
			Third = args[2];
			Fourth = args[3];
			xm.getXgroup().removePermission(Fourth, Third);
			sender.sendMessage(ChatColor.DARK_PURPLE + "Permission node " + Third + " was added to " + Fourth);
			break;
		default:
			if(xm.getServer().getPlayer(Second) != null || xm.getServer().getOfflinePlayer(Second) != null){
				xm.getXusers().getConfig().set("users." + Second + ".group", xm.getXperms().getDefaultGroup());
				xm.getXusers().saveConfig();
				xm.getXpermissions().setPermissions(Second,  xm.getServer().getPlayer(Second));
				sender.sendMessage(ChatColor.DARK_PURPLE + "The player " + Second + " now belongs to " + xm.getXperms().getDefaultGroup());
			}
			else{
				displayOptions(sender);
			}
			break;
		}
	}
	
	private void createCommand(CommandSender sender, Command cmd, String[] args){
		String Second = args[1];
		if(xm.getXperms().getConfig().isSet("groups." + Second)){
			sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + Second + " already exists!");
		}
		else{
			xm.getXperms().getConfig().set("groups." + Second + ".permissions", Arrays.asList("sample.permission"));
			xm.getXperms().saveConfig();
			sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + Second + " now exists!");
		}
	}
	
	private void deleteCommand(CommandSender sender, Command cmd, String[] args){
		String Second = args[1];
		if(xm.getXperms().getConfig().isSet("groups." + Second)){
			xm.getXperms().getConfig().set("groups." + Second, null);
			xm.getXperms().saveConfig();
			sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + Second + " no longer exists");
		}
		else{
			sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + Second + " does not exist!");
		}
	}
	
	private void inheritCommand(CommandSender sender, Command cmd, String[] args){
		String First, Third, Fourth;
		
		First = args[0];
		Third = args[2];
		Fourth = args[3];
		
		if(xm.getXperms().getConfig().isSet("groups." + First)){
			if(xm.getXperms().getConfig().isSet("groups." + Third)){
				if(Fourth.equalsIgnoreCase("true")){
					xm.getXperms().getConfig().set("groups." + First + ".inherit", Third);
					xm.getXperms().saveConfig();
					sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + First + " now inherits " + Third);
					return;
				}
				if(Fourth.equalsIgnoreCase("false")){
					xm.getXperms().getConfig().set("groups." + First + ".inherit", null);
					xm.getXperms().saveConfig();
					sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + First + " no longer inherits " + Third);
				}
				else{
					sender.sendMessage(ChatColor.RED + "Strange argument encountered: " + Fourth);
				}
			}
			else{
				sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + Third + " does not appear to exist!");
			}
		}
		else{
			sender.sendMessage(ChatColor.DARK_PURPLE + "The group " + First + " does not appear to exist!");
		}
	}
	
	private void promoteCommand(CommandSender sender, Command cmd, String[] args){
		String Second, Third;
		Second = args[1];
		Third = args[2];
		
		if(xm.getConfig().isSet("ladders." + Third)){
				List<String> ladderGroups = xm.getConfig().getStringList("ladders." + Third);
				String[] groups = ladderGroups.toArray(new String[0]);
				
				for(int i = 0; i < groups.length; i++){
					if(groups[i].equals(xm.getXusers().getUserGroup(Second))){
						if(i + 2 > groups.length){
							sender.sendMessage(ChatColor.DARK_PURPLE + "The player " + Second + " has already reached the top of this ladder");
							return;
						}
						xm.getXusers().getConfig().set("users." + Second + ".group", groups[i + 1]);
						xm.getXusers().saveConfig();
						xm.getXpermissions().setPermissions(Second,  xm.getServer().getPlayer(Second));
						sender.sendMessage(ChatColor.DARK_PURPLE + "Player " + Second + " was succesfully promoted to " + groups[i + 1]);
						return;
					}
				}
				
				sender.sendMessage(ChatColor.DARK_PURPLE + "The player " + Second + "'s group could not be found in the ladder " + Third);
		}
		else{
			sender.sendMessage(ChatColor.DARK_PURPLE + "The ladder " + Third + " does not exist!");
		}
	}
	
	private void demoteCommand(CommandSender sender, Command cmd, String[] args){
		String Second, Third;
		Second = args[1];
		Third = args[2];
		
		if(xm.getConfig().isSet("ladders." + Third)){
				List<String> ladderGroups = xm.getConfig().getStringList("ladders." + Third);
				String[] groups = ladderGroups.toArray(new String[0]);
				
				for(int i = 0; i < groups.length; i++){
					if(groups[i].equals(xm.getXusers().getUserGroup(Second))){
						if(i - 1 < 0){
							sender.sendMessage(ChatColor.DARK_PURPLE + "The player " + Second + " has already reached the bottom of this ladder!");
							return;
						}
						xm.getXusers().getConfig().set("users." + Second + ".group", groups[i - 1]);
						xm.getXusers().saveConfig();
						xm.getXpermissions().setPermissions(Second, xm.getServer().getPlayer(Second));
						sender.sendMessage(ChatColor.DARK_PURPLE + "Player " + Second + " was succesfully demoted to " + groups[i - 1]);
						return;
					}
				}
				
				sender.sendMessage(ChatColor.DARK_PURPLE + "The player " + Second + "'s group could not be found in the ladder " + Third);
		}
		else{
			sender.sendMessage(ChatColor.DARK_PURPLE + "The ladder " + Third + " does not exist!");
		}
	}
	
	private void displayOptions(CommandSender sender){
		String[] options = new String[19];
		options[0] = "Try one of these:";
		options[1] = ChatColor.DARK_PURPLE + "/xperms [add|remove] <user> <group>";
		options[2] = ChatColor.RED + "- Add or Remove <user> to/from <group>";
		options[3] = ChatColor.DARK_PURPLE + "/xperms [add|remove] prefix <prefix> <group>";
		options[4] = ChatColor.RED + "- Add or Remove <prefix> to/from <group>";
		options[5] = ChatColor.DARK_PURPLE + "/xperms [add|remove] suffix <suffix> <group>";
		options[6] = ChatColor.RED + "- Add or Remove <suffix> to/from <group>";
		options[7] = ChatColor.DARK_PURPLE + "/xperms [create|delete] <group>";
		options[8] = ChatColor.RED + "- Create or Delete <group>";
		options[9] = ChatColor.DARK_PURPLE + "/xperms [promote|demote] <user> <ladder>";
		options[10] = ChatColor.RED + "- Promote or Demote <user> along <ladder>";
		options[11] = ChatColor.DARK_PURPLE + "/xperms <group> inherits <group> [true|false]";
		options[12] = ChatColor.RED + "- set <group> to or not to inherit <group>";
		options[13] = ChatColor.DARK_PURPLE + "/xperms [add|remove] permission <permission node> <group>";
		options[14] = ChatColor.RED + "- Add or Remove <permission node> to/from <group>";
		options[15] = ChatColor.DARK_PURPLE + "/xperms reload";
		options[16] = ChatColor.RED + "- reloads Xperms";
		options[17] = ChatColor.DARK_PURPLE + "/xperms reset";
		options[18] = ChatColor.RED + "- " + ChatColor.BOLD + "Warning: " + ChatColor.RESET + "This resets ALL config files";
		
		sender.sendMessage(options);
	}
}