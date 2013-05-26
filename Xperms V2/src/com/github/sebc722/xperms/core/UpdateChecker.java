package com.github.sebc722.xperms.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateChecker {
	private Main xm;
	
	private Double masterVersion;
	private Double currentVersion;
	
	public UpdateChecker(Main main, Double CV){
		xm = main;
		
		currentVersion = CV;
	}
	
	public boolean Check(){
		getVersion();
		return compareVersions();
	}
	
	public boolean compareVersions(){
		if(currentVersion < masterVersion){
			return true;
		}
		return false;
	}
	
	public void getVersion(){
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
			line = in.readLine();
			String[] sublist = line.split("=");
			if(sublist[0].equalsIgnoreCase("version")){
				masterVersion = Double.parseDouble(sublist[1]);
			}
			
		} catch (IOException e) {
			xm.getLogger().info("Failed to read master version file, this is probably nothing to worry about");
		}
	}
}
