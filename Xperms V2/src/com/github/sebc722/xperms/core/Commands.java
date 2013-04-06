package com.github.sebc722.xperms.core;

/*
 * Commands Added:
 * - Add/remove prefix/suffix to/from player
 * - Add/remove node to/from player
 */

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {
	private Main xm;
	
	private ChatColor Red = ChatColor.RED;
	private ChatColor DarkGrey = ChatColor.DARK_GRAY;
	private ChatColor Green = ChatColor.GREEN;
	private ChatColor Purple = ChatColor.DARK_PURPLE;
	private ChatColor DarkGreen = ChatColor.DARK_GREEN;
	private ChatColor DarkBlue = ChatColor.DARK_BLUE;
	
	private String sectionDiv;
	private String itemDiv;
	private String WebsiteURL = "http://dev.bukkit.org/server-mods/xperms";
	
	public Commands(Main main){
		xm = main;
		
		sectionDiv = DarkGreen + "=================================================";
		itemDiv = DarkBlue + "-------------------------------------------------";
	}

	public void run(CommandSender sender, Command cmd, String label, String[] args) {
		String key = args[0];
		if(key.equalsIgnoreCase("add")){
			add(sender, args);
			return;
		}
		if(key.equalsIgnoreCase("remove")){
			remove(sender, args);
			return;
		}
		if(key.equalsIgnoreCase("create")){
			create(sender, args);
			return;
		}
		if(key.equalsIgnoreCase("delete")){
			delete(sender, args);
			return;
		}
		if(key.equalsIgnoreCase("promote")){
			promote(sender, args);
			return;
		}
		if(key.equalsIgnoreCase("demote")){
			demote(sender, args);
			return;
		}
		if(xm.getXgroup().isGroup(key)){
			inherit(sender, args);
			return;
		}
		if(key.equalsIgnoreCase("reload")){
			xm.reloadAll();
			sender.sendMessage(Green + "Xperms has been reloaded");
			return;
		}
		if(key.equalsIgnoreCase("reset")){
			xm.resetAll();
			sender.sendMessage(Green + "Xperms files have been reset");
			return;
		}
		if(key.equalsIgnoreCase("check")){
			boolean update = new UpdateChecker(xm).Check();
			if(update){
				sender.sendMessage(DarkGrey + "An update is available for Xperms! download it here: " + ChatColor.GREEN + WebsiteURL);
			}
			sender.sendMessage(DarkGrey + "No update is available at this time");
			return;
		}
		if(key.equalsIgnoreCase("help")){
			sendHelp(sender);
			return;
		}
		if(key.equals("?")){
			sendHelp(sender);
			return;
		}
		sender.sendMessage(DarkGrey + "Strange argument provided: " + Red + key);
		sendHelp(sender);
	}
	
	public void add(CommandSender sender, String[] args){
		String Second;
		try{
			Second = args[1];
		} catch(ArrayIndexOutOfBoundsException e){
			sender.sendMessage(Red + "Too few arguments!");
			sendHelp(sender);
			return;
		}
		
		if(xm.playerExists(Second)){
			String Third = null, Fourth = null, playerWorld = null;
			
			try{
				playerWorld = xm.getServer().getPlayer(Second).getWorld().getName();
			} catch(NullPointerException e){
				playerWorld = null;
			}
			
			boolean worldSpecified = false;
			try{
				Third = args[2];
			} catch(ArrayIndexOutOfBoundsException e){
				sender.sendMessage(Red + "Please Specify a group!");
				return;
			}
			try{
				Fourth = args[3];
				worldSpecified = true;
			} catch(ArrayIndexOutOfBoundsException e){
				worldSpecified = false;
			}
			
			
			if(xm.getXgroup().isGroup(Third)){
				if(worldSpecified){
					if(xm.getXworld().isWorld(Fourth)){
						xm.getUsers().get().set("users." + Second + "." + Fourth, Third);
						xm.getUsers().save();
						if(playerWorld != null){
							if(xm.getXgroup().isSupportedByWorld(Third, playerWorld)){
								xm.getXpermissions().setPermissions(xm.getServer().getPlayer(Second), Third);
							}
						}
						sender.sendMessage(Green + Second + DarkGrey + " now belongs to " + Green + Third + DarkGrey + " when in " + Green + Fourth);
						return;
					}
					else{
						sender.sendMessage(DarkGrey + "Invalid world: " + Red + Fourth);
					}
				}
				else{
					String[] supported = xm.getXgroup().getSupportedWorlds(Third);
					ArrayList<String> messages = new ArrayList<String>();
					messages.add(Green + Second + DarkGrey + " now belongs to " + Green + Third + DarkGrey + " in the following worlds: ");
					messages.add(sectionDiv);
					
					for(int i = 0; i < supported.length; i++){
						xm.getUsers().get().set("users." + Second + "." + supported[i], Third);
						messages.add(supported[i]);
					}
					xm.getUsers().save();
					
					if(playerWorld != null){
						if(xm.getXgroup().isSupportedByWorld(Third, playerWorld)){
							xm.getXpermissions().setPermissions(xm.getServer().getPlayer(Second), Third);
						}
					}
					
					messages.add(sectionDiv);
					sender.sendMessage(messages.toArray(new String[0]));
				}
			}
			else{
				sender.sendMessage(DarkGrey + "Group not found: " + Green + Third);
			}
			
		}
		if(Second.equalsIgnoreCase("prefix")){
			if(args.length < 4){
				sender.sendMessage("Too few arguments!");
				sendHelp(sender);
			}
			else{
				String Third, Fourth;
				Third = args[2];
				Fourth = args[3];
				if(xm.getXgroup().isGroup(Fourth)){
					xm.getPermissions().get().set("groups." + Fourth + ".prefix", Third);
					xm.getPermissions().save();
					xm.getXpermissions().updatePermissions(Fourth);
					
					sender.sendMessage(Green + Third + DarkGrey + " is now " + Green + Fourth + DarkGrey + "'s prefix");
				}
				else{
					if(xm.playerExists(Fourth)){
						xm.getUsers().get().set("users." + Fourth + ".prefix", Third);
						xm.getUsers().save();
						Player player;
						try{
							player = xm.getServer().getPlayer(Fourth);
						} catch(NullPointerException e){
							sender.sendMessage(Green + Third + DarkGrey + " is now " + Green + Fourth + DarkGrey + "'s prefix");
							return;
						}
						
						xm.getXpermissions().updatePermissions(player);
						sender.sendMessage(Green + Third + DarkGrey + " is now " + Green + Fourth + DarkGrey + "'s prefix");
					}
					else{
						sender.sendMessage(Red + Fourth + DarkGrey + " does not appear to exist");
					}
				}
			}
		}
		if(Second.equalsIgnoreCase("suffix")){
			if(args.length < 4){
				sender.sendMessage("Too few arguments!");
				sendHelp(sender);
			}
			else{
				String Third, Fourth;
				Third = args[2];
				Fourth = args[3];
				if(xm.getXgroup().isGroup(Fourth)){
					xm.getPermissions().get().set("groups." + Fourth + ".suffix", Third);
					xm.getPermissions().save();
					xm.getXpermissions().updatePermissions(Fourth);
					sender.sendMessage(Green + Third + DarkGrey + " is now " + Green + Fourth + DarkGrey + "'s suffix");
				}
				else{
					if(xm.playerExists(Fourth)){
						xm.getUsers().get().set("users." + Fourth + ".suffix", Third);
						xm.getUsers().save();
						Player player;
						try{
							player = xm.getServer().getPlayer(Fourth);
						} catch(NullPointerException e){
							sender.sendMessage(Green + Third + DarkGrey + " is now " + Green + Fourth + DarkGrey + "'s suffix");
							return;
						}
						
						xm.getXpermissions().updatePermissions(player);
						sender.sendMessage(Green + Third + DarkGrey + " is now " + Green + Fourth + DarkGrey + "'s suffix");
					}
					else{
						sender.sendMessage(Red + Fourth + DarkGrey + " does not appear to exist");
					}
				}
			}
		}
		if(Second.equalsIgnoreCase("permission")){
			if(args.length < 4){
				sender.sendMessage("Too few arguments!");
				sendHelp(sender);
			}
			else{
				String Third, Fourth;
				Third = args[2];
				Fourth = args[3];
				if(xm.getXgroup().isGroup(Fourth)){
					xm.getXpermissions().addNode(Fourth, Third);
					xm.getXpermissions().updatePermissions(Fourth);
					sender.sendMessage(Green + Third + DarkGrey + " has been added to " + Green + Fourth);
				}
				else{
					if(xm.playerExists(Fourth)){
						xm.getXplayer().addNode("", Fourth, Third);
						Player player;
						try{
							player = xm.getServer().getPlayer(Fourth);
						} catch(NullPointerException e){
							sender.sendMessage(Green + Third + DarkGrey + " has been given to " + Green + Fourth);
							return;
						}
						sender.sendMessage(Green + Third + DarkGrey + " has been given to " + Green + Fourth);
						xm.getXpermissions().updatePermissions(player);
					}
					else{
						sender.sendMessage(Red + Fourth + DarkGrey + " does not appear to exist");
					}
				}
			}
		}
	}
	
	public void remove(CommandSender sender, String[] args){
		String Second;
		try{
			Second = args[1];
		} catch(ArrayIndexOutOfBoundsException e){
			sender.sendMessage(Red + "Too few arguments!");
			sendHelp(sender);
			return;
		}
		
		if(xm.playerExists(Second)){
			String Third, Fourth = null, playerWorld, defaultGroup;
			boolean isOnline = true;
			
			try{
				playerWorld = xm.getServer().getPlayer(Second).getWorld().getName();
			} catch(NullPointerException e){
				playerWorld = "true";
				isOnline = false;
			}
			
			defaultGroup = xm.getXgroup().getDefault(playerWorld);
			boolean worldSpecified = false;
			try{
				Third = args[2];
			} catch(ArrayIndexOutOfBoundsException e){
				sender.sendMessage(Red + "Please specify a group!");
				return;
			}
			try{
				Fourth = args[3];
				worldSpecified = true;
			} catch(ArrayIndexOutOfBoundsException e){
				worldSpecified = false;
			}
			
			if(worldSpecified){
				if(!xm.getXworld().isWorld(Fourth)){
					sender.sendMessage(Red + Fourth + DarkGrey + " is not a valid world");
					return;
				}
				
				defaultGroup = xm.getXgroup().getDefault(Fourth);
				xm.getUsers().get().set("users." + Second + "." + Fourth, defaultGroup);
				xm.getUsers().save();
				
				if(playerWorld != null){
					if(xm.getXgroup().isSupportedByWorld(defaultGroup, playerWorld)){
						xm.getXpermissions().setPermissions(xm.getServer().getPlayer(Second), defaultGroup);
					}
				}
				
				sender.sendMessage(Green + Second + DarkGrey + " no longer belongs to " + Green + Third + DarkGrey + " when in " + Green + Fourth);
			}
			else{
				ArrayList<String> messages = new ArrayList<String>();
				
				String[] worlds = xm.getXworld().getAllWorlds();
				ArrayList<String> worldsToChange = new ArrayList<String>();
				for(int i = 0; i < worlds.length; i++){
					if(xm.getUsers().get().isSet("users." + Second + "." + worlds[i])){
						String current = xm.getUsers().get().getString("users." + Second + "." + worlds[i]);
						if(current.equals(Third)){
							worldsToChange.add(worlds[i]);
						}
					}
				}
				String[] list = worldsToChange.toArray(new String[0]);
				if(worldsToChange.isEmpty()){
					sender.sendMessage(list);
					sender.sendMessage(Green + Third + DarkGrey + " Does not seem to be " + Green + Second + DarkGrey + "'s group");
					return;
				}
				
				messages.add(Green + Second + DarkGrey + " no longer belongs to " + Green + Third + DarkGrey + " in the following worlds: ");
				messages.add(sectionDiv);
				
				for(int i = 0; i < list.length; i++){
					defaultGroup = xm.getXgroup().getDefault(list[i]);
					xm.getUsers().get().set("users." + Second + "." + list[i], defaultGroup);
					messages.add(list[i] + DarkGrey + " - is now: " + Green + defaultGroup);
				}
				xm.getUsers().save();
				
				if(isOnline){
					defaultGroup = xm.getXplayer().getGroup(Second, playerWorld);
					if(defaultGroup == null){
						defaultGroup = xm.getXgroup().getDefault(playerWorld);
					}
					if(xm.getXgroup().isSupportedByWorld(defaultGroup, playerWorld)){
						xm.getXpermissions().setPermissions(xm.getServer().getPlayer(Second), defaultGroup);
					}
				}
				
				messages.add(sectionDiv);
				sender.sendMessage(messages.toArray(new String[0]));
			}
		}
		if(Second.equalsIgnoreCase("prefix")){
			if(args.length < 4){
				sender.sendMessage("Too few arguments!");
				sendHelp(sender);
			}
			else{
				String Fourth;
				Fourth = args[3];
				if(xm.getXgroup().isGroup(Fourth)){
					xm.getPermissions().get().set("groups." + Fourth + ".prefix", null);
					xm.getPermissions().save();
					xm.getXpermissions().updatePermissions(Fourth);
					sender.sendMessage(Green + Fourth + DarkGrey + " no longer has a prefix");
				}
				else{
					if(xm.playerExists(Fourth)){
						xm.getUsers().get().set("users." + Fourth + ".prefix", null);
						xm.getUsers().save();
						Player player;
						try{
							player = xm.getServer().getPlayer(Fourth);
						} catch(NullPointerException e){
							sender.sendMessage(Green + Fourth + DarkGrey + " no longer has a prefix");
							return;
						}
						
						xm.getXpermissions().updatePermissions(player);
						sender.sendMessage(Green + Fourth + DarkGrey + " no longer has a prefix");
					}
					else{
						sender.sendMessage(Red + Fourth + DarkGrey + " does not appear to exist");
					}
				}
			}
		}
		if(Second.equalsIgnoreCase("suffix")){
			if(args.length < 4){
				sender.sendMessage("Too few arguments!");
				sendHelp(sender);
			}
			else{
				String Fourth;
				Fourth = args[3];
				if(xm.getXgroup().isGroup(Fourth)){
					xm.getPermissions().get().set("groups." + Fourth + ".suffix", null);
					xm.getPermissions().save();
					xm.getXpermissions().updatePermissions(Fourth);
					sender.sendMessage(Green + Fourth + DarkGrey + " no longer has a suffix");
				}
				else{
					if(xm.playerExists(Fourth)){
						xm.getUsers().get().set("users." + Fourth + ".suffix", null);
						xm.getUsers().save();
						Player player;
						try{
							player = xm.getServer().getPlayer(Fourth);
						} catch(NullPointerException e){
							sender.sendMessage(Green + Fourth + DarkGrey + " no longer has a suffix");
							return;
						}
						
						xm.getXpermissions().updatePermissions(player);
						sender.sendMessage(Green + Fourth + DarkGrey + " no longer has a suffix");
					}
					else{
						sender.sendMessage(Red + Fourth + DarkGrey + " does not appear to exist");
					}
				}
			}
		}
		if(Second.equalsIgnoreCase("permission")){
			if(args.length < 4){
				sender.sendMessage("Too few arguments!");
				sendHelp(sender);
			}
			else{
				String Third, Fourth;
				Third = args[2];
				Fourth = args[3];
				if(xm.getXgroup().isGroup(Fourth)){
					xm.getXpermissions().removeNode(Fourth, Third);
					xm.getXpermissions().updatePermissions(Fourth);
					sender.sendMessage(Green + Fourth + DarkGrey + " no longer has the node " + Green + Third);
				}
				else{
					if(xm.playerExists(Fourth)){
						xm.getXplayer().removeNode("", Fourth, Third);
						Player player;
						try{
							player = xm.getServer().getPlayer(Fourth);
						} catch(NullPointerException e){
							sender.sendMessage(Green + Third + DarkGrey + " has been removed from " + Green + Fourth);
							return;
						}
						sender.sendMessage(Green + Third + DarkGrey + " has been removed from " + Green + Fourth);
						xm.getXpermissions().updatePermissions(player);
					}
					else{
						sender.sendMessage(Red + Fourth + DarkGrey + " does not appear to exist");
					}
				}
			}
		}
	}
	
	public void create(CommandSender sender, String[] args){
		String groupName = args[1];
		
		if(!xm.getXgroup().isGroup(groupName)){
			ArrayList<String> SampleNodes = new ArrayList<String>();
			SampleNodes.add("sample.permission");
			SampleNodes.add("other.sample.permission");
			xm.getPermissions().get().set("groups." + groupName + ".permissions", SampleNodes);
			xm.getPermissions().save();
			
			sender.sendMessage(Green + groupName + DarkGrey + " now exists");
		}
		else{
			sender.sendMessage(Green + groupName + DarkGrey + " already exists");
		}
	}
	
	public void delete(CommandSender sender, String[] args){
		String groupName = args[1];
		
		if(xm.getXgroup().isGroup(groupName)){
			xm.getPermissions().get().set("groups." + groupName, null);
			xm.getPermissions().save();
			
			sender.sendMessage(Green + groupName + DarkGrey + " no longer exists");
		}
		else{
			sender.sendMessage(Green + groupName + DarkGrey + " doesn't exist");
		}
	}
	
	public void promote(CommandSender sender, String[] args){
		String ladder, player, playerGroup, playerWorld;
		
		try{
			player = args[1];
		} catch(ArrayIndexOutOfBoundsException e){
			sender.sendMessage(Red + "Please specify a player!");
			return;
		}
		if(!xm.playerExists(player)){
			sender.sendMessage(Red + player + DarkGrey + " could not be found");
			return;
		}
		
		try{
			ladder = args[2];
		} catch(ArrayIndexOutOfBoundsException e){
			sender.sendMessage(Red + "Please specify a ladder to use!");
			return;
		}
		try{
			playerWorld = xm.getServer().getPlayer(player).getWorld().getName();
			playerGroup = xm.getXplayer().getCurrentGroup(xm.getServer().getPlayer(player));
		} catch(NullPointerException e){
			sender.sendMessage("Sorry, offline players cannot be promoted at this time");
			return;
		}
		
		
		String[] groups = xm.getXpermissions().getLadder(ladder);
		if(groups == null){
			sender.sendMessage("The ladder " + ladder + " could not be found");
		}
		
		for(int i = 0; i < groups.length; i++){
			if(groups[i].equalsIgnoreCase(playerGroup)){
				String nextGroup;
				try{
					nextGroup = groups[i + 1];
				} catch(ArrayIndexOutOfBoundsException e){
					sender.sendMessage(player + " has already reached the top of this ladder");
					return;
				}
				String[] supportedWorlds = xm.getXgroup().getWorlds(nextGroup);
				for(int o = 0; o < supportedWorlds.length; o++){
					xm.getUsers().get().set("users." + player + "." + supportedWorlds[i], nextGroup);
				}
				xm.getUsers().save();
				if(xm.getXgroup().isSupportedByWorld(nextGroup, playerWorld)){
					xm.getXpermissions().setPermissions(xm.getServer().getPlayer(player), nextGroup);
				}
				sender.sendMessage(player + " has been promoted to " + nextGroup + " for supported worlds");
				return;
			}
		}
	}
	
	public void demote(CommandSender sender, String[] args){
		String ladder, player, playerGroup, playerWorld;
		
		try{
			player = args[1];
		} catch(ArrayIndexOutOfBoundsException e){
			sender.sendMessage(Red + "Please specify a player!");
			return;
		}
		if(!xm.playerExists(player)){
			sender.sendMessage(Red + player + DarkGrey + " could not be found");
			return;
		}
		
		try{
			ladder = args[2];
		} catch(ArrayIndexOutOfBoundsException e){
			sender.sendMessage(Red + "Please specify a ladder to use!");
			return;
		}
		try{
			playerWorld = xm.getServer().getPlayer(player).getWorld().getName();
			playerGroup = xm.getXplayer().getCurrentGroup(xm.getServer().getPlayer(player));
		} catch(NullPointerException e){
			sender.sendMessage("Sorry, offline players cannot be demoted at this time");
			return;
		}
		
		String[] groups = xm.getXpermissions().getLadder(ladder);
		if(groups == null){
			sender.sendMessage("The ladder " + ladder + " could not be found");
		}
		
		for(int i = 0; i < groups.length; i++){
			if(groups[i].equalsIgnoreCase(playerGroup)){
				String nextGroup;
				if(i == 0){
					sender.sendMessage(player + " has already reached the bottom of this ladder");
					return;
				}
				else{
					nextGroup = groups[i - 1];
				}
				String[] supportedWorlds = xm.getXgroup().getWorlds(nextGroup);
				for(int o = 0; o < supportedWorlds.length; o++){
					xm.getUsers().get().set("users." + player + "." + supportedWorlds[i], nextGroup);
				}
				xm.getUsers().save();
				if(xm.getXgroup().isSupportedByWorld(nextGroup, playerWorld)){
					xm.getXpermissions().setPermissions(xm.getServer().getPlayer(player), nextGroup);
				}
				sender.sendMessage(player + " has been demoted to " + nextGroup + " for supported worlds");
				return;
			}
		}
	}
	
	public void inherit(CommandSender sender, String[] args){
		String inheritingGroup, inheritedGroup, inherit;
		inheritingGroup = args[0];
		try{
			inheritedGroup = args[2];
		} catch(ArrayIndexOutOfBoundsException e){
			sender.sendMessage(Red + "Group to inherit not specified!");
			return;
		}
		try{
			inherit = args[3];
		} catch(ArrayIndexOutOfBoundsException e){
			sender.sendMessage(Red + "true/false value not specified!");
			return;
		}
		
		ArrayList<String> list = new ArrayList<String>();
		if(xm.getXgroup().isGroup(inheritingGroup)){
			if(xm.getXgroup().isGroup(inheritedGroup)){
				if(inherit.equals("false")){
					if(xm.getPermissions().get().isSet("groups." + inheritingGroup + ".inherit")){
						String[] inherited = xm.getPermissions().get().getStringList("groups." + inheritingGroup + ".inherit").toArray(new String[0]);
						for(int i = 0; i < inherited.length; i++){
							if(!inheritedGroup.equals(inherited[i])){
								list.add(inherited[i]);
							}
						}
						xm.getPermissions().get().set("groups." + inheritingGroup + ".inherit", inherited);
					}
					xm.getPermissions().save();
					sender.sendMessage(Green + inheritingGroup + DarkGrey + " no longer inherits " + Green + inheritedGroup);
					return;
				}
				if(!inherit.equals("true")){
					return;
				}
				if(!xm.getPermissions().get().isSet("groups." + inheritingGroup + ".inherit")){
					ArrayList<String> groups = new ArrayList<String>();
					groups.add(inheritedGroup);
					xm.getPermissions().get().set("groups." + inheritingGroup + ".inherit", groups);
				}
				else{
					List<String> groups;
					groups = xm.getPermissions().get().getStringList("groups." + inheritingGroup + ".inherit");
					groups.add(inheritedGroup);
					xm.getPermissions().get().set("groups." + inheritingGroup + ".inherit", groups);
				}
				xm.getPermissions().save();
				sender.sendMessage(Green + inheritingGroup + DarkGrey + " now inherits " + Green + inheritedGroup);
				return;
			}
			else{
				sender.sendMessage(Red + inheritedGroup + DarkGrey + " could not be found");
			}
		}
		else{
			sender.sendMessage(Red + inheritingGroup + DarkGrey + " could not be found");
			return;
		}
	}
	
	public void sendHelp(CommandSender sender){
		ArrayList<String> help = new ArrayList<String>();
		
		help.add(sectionDiv);
		
		help.add(Purple + "/xperms [add|remove] <Player Name> <Group Name>");
		help.add(DarkGrey + " - add or remove a player to/from a group");
		
		help.add(itemDiv);
		
		help.add(Purple + "/xperms [add|remove] [prefix|suffix] <Prefix Text|Suffix Text> <Group/Player Name>");
		help.add(DarkGrey + " - add or remove a prefix or suffix to/from a group");
		
		help.add(itemDiv);
		
		help.add(Purple + "/xperms [add|remove] permission <Permission Node> <Group/Player Name>");
		help.add(DarkGrey + " - add or remove a permission node to/from a group");
		
		help.add(itemDiv);
		
		help.add(Purple + "/xperms [create|delete] <Group Name>");
		help.add(DarkGrey + " - create or delete a group");
		
		help.add(itemDiv);
		
		help.add(Purple + "/xperms [promote|demote] <Player Name> <Ladder Name>");
		help.add(DarkGrey + " - promote or demote a user along ladder specified");
		
		help.add(itemDiv);
		
		help.add(Purple + "/xperms <Group Name> inherits <Group Name> [true|false]");
		help.add(DarkGrey + " - set a group to (or not to) inherit another group");
		
		help.add(itemDiv);
		
		help.add(Purple + "/xperms reload");
		help.add(DarkGrey + " - reload all of Xperms");
		
		help.add(itemDiv);
		
		help.add(Purple + "/xperms reset");
		help.add(DarkGrey + " - reset all config files (Warning: this returns all files generated by Xperms to a default state)");
		
		help.add(sectionDiv);
		
		
		sender.sendMessage(DarkGrey + "Try one of the following: ");
		sender.sendMessage(help.toArray(new String[0]));
	}
}
