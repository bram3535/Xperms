package com.github.sebc722.xperms.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateChecker {
	private Main xm;
	
	private String masterVersion;
	private String currentVersion;
	
	public UpdateChecker(Main main){
		xm = main;
		currentVersion = xm.getDescription().getVersion();
	}
	
	public boolean Check(){
		checkUpdate();
		return compareVersions();
	}
	
	public boolean compareVersions(){
		Double version, master;
		version = Double.parseDouble(currentVersion);
		master = Double.parseDouble(masterVersion);
		if(version < master){
			return true;
		}
		return false;
	}
	
	public void checkUpdate(){
		URL versionURL;
		try {
			versionURL = new URL("https://dl.dropbox.com/u/63465675/Bukkit/Xperms/XpermsCurrentVersion.txt");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return;
		}
		
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(versionURL.openStream()));
			String line;
			while((line = in.readLine()) != null){
				String[] sublist = line.split("=");
				if(sublist[0].equalsIgnoreCase("version")){
					masterVersion = sublist[1];
				}
			}
		} catch (IOException e) {
			xm.getLogger().info("Failed to read master version file, this is probably nothing to worry about");
		}
	}
}
