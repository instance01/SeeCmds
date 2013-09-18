package com.comze_instancelabs.seepn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin implements Listener {
	
	//TODO:
	// [Medium] 1. show user rank (Vault)
	
	//TODO: Bugs
	// -/-
	
	@Override
    public void onEnable(){
		getLogger().info("Initializing comzeseepn");
		getServer().getPluginManager().registerEvents(this, this);
		
		
		getConfig().addDefault("config.showip", true);
		getConfig().addDefault("config.auto_updating", true);
		getConfig().addDefault("strings.playernotonline1", "§3[SeeCmds] §4");
		getConfig().addDefault("strings.playernotonline2", " is not online!");
		getConfig().addDefault("strings.nowreceivingcmds1_spy", "§3[SeeCmds] §2Now receiving ");
		getConfig().addDefault("strings.nowreceivingcmds2_spy", "'s commands.");
		getConfig().addDefault("strings.nowreceivingcmds_spyall", "§3[SeeCmds] §2Now receiving commands from all players!");
		getConfig().addDefault("strings.disabled_spy", "§3[SeeCmds] §4Disabled.");
		getConfig().addDefault("strings.disabled_spyall", "§3[SeeCmds] §4Disabled seenpnall.");
		getConfig().addDefault("strings.insufficient_permissions", "§3[SeeCmds] §4Insufficient permissions for ");
		getConfig().addDefault("strings.removed_alwaysactive", "§3[SeeCmds] §4Removed alwaysactive option from ");
		getConfig().addDefault("strings.gettingspied1", "§3[SeeCmds] §2");
		getConfig().addDefault("strings.gettingspied2", " is getting spied by ");
		
		getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		ArrayList<String> keys = new ArrayList<String>();
        if(!getConfig().isConfigurationSection("alwaysactive")){
        	getLogger().info("There are no \"alwaysactive\" configurations yet!");
        }else{
            keys.addAll(getConfig().getConfigurationSection("alwaysactive").getKeys(false));
            for(int i = 0; i < keys.size(); i++){
            	Player target = (Bukkit.getServer().getPlayer(keys.get(i)));
            	ArrayList<String> gettingp = new ArrayList<String>(getConfig().getStringList("alwaysactive." + keys.get(i)));
    	        if (target == null) {
    	        	for(String p : gettingp){
    	        		if(Bukkit.getServer().getPlayer(p) != null){
    	        			Bukkit.getServer().getPlayer(p).sendMessage(getConfig().getString("strings.playernotonline1") + keys.get(i) + getConfig().getString("strings.playernotonline2"));
    	        		}
    	        	}
    	        }else{
    	        	for(String p : gettingp){
    	        		if(Bukkit.getServer().getPlayer(p) != null){
	    	        		Bukkit.getServer().getPlayer(p).sendMessage(getConfig().getString("strings.nowreceivingcmds1_spy") + target.getName() + getConfig().getString("strings.nowreceivingcmds2_spy"));
	    	        		ArrayList<Player> test = new ArrayList<Player>();
	    	        		if(playerp.containsKey(target)){
		    	        		test.addAll(playerp.get(target));
	    	        		}
	    	        		if(!test.contains(p)){
		    	        		test.add(Bukkit.getServer().getPlayer(p));
			    	        	playerp.put(target, test);
	    	        		}
    	        		}
    	        	}
    	        }
            }
        }
        
        
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :(
        }
        
        if(getConfig().getBoolean("config.auto_updating")){
        	Updater updater = new Updater(this, "see-cmds", this.getFile(), Updater.UpdateType.DEFAULT, false);
        }
        
    }
 
    @Override
    public void onDisable() {
    	getLogger().info("Disabling..");
    	getall.clear();
    	playerp.clear();
    }
    
    static HashMap<Player, ArrayList<Player>> playerp = new HashMap<Player, ArrayList<Player>>();
    ArrayList<Player> getall = new ArrayList<Player>();
    
    //static boolean seep = false;
    //static boolean seepall = false;
    //static Player playerp;
    //static Player targetp;
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("seepn") || cmd.getName().equalsIgnoreCase("spy")){
    		if(args.length < 1){
    			sender.sendMessage(getConfig().getString("strings.disabled_spy"));
    			//entferne von hashmap
    			//seep = false;	
    			//while (playerp.values().remove((Player) sender));
    			ArrayList<Player> keys = new ArrayList<Player>(playerp.keySet());
    			for(Player keyp : keys){
    				ArrayList<Player> listeners = new ArrayList<Player>(playerp.get(keyp));
    				if(listeners.contains((Player) sender)){
    					listeners.remove((Player) sender);
    					playerp.put(keyp, listeners);
    				}
    			}
    		}else{
    			if(args.length > 2){
    				//spy ped4 addoption alwaysactive
    				//spy ped4 removeoption alwaysactive
    				if(args[1].equalsIgnoreCase("addoption") && args[2].equalsIgnoreCase("alwaysactive")){
	    				Player target = (Bukkit.getServer().getPlayer(args[0]));
	        	        if (target == null) {
	        	        	sender.sendMessage(getConfig().getString("strings.playernotonline1") + args[0] + getConfig().getString("strings.playernotonline2"));
	        	            List<String> bisher = getConfig().getStringList("alwaysactive." + args[0]);
	        	            bisher.add(sender.getName());
	        	            getConfig().set("alwaysactive." + args[0], bisher);
	        	            this.saveConfig();
	        	            sender.sendMessage("§3[SeeCmds] §2The player was added to the alwaysactive config.");
	        	            return true;
	        	        }else if(target.hasPermission("comze.dontspyme")){
	        	        	sender.sendMessage(getConfig().getString("strings.insufficient_permissions")+ args[0]);
	        	        }else{
	        	        	List<String> bisher = getConfig().getStringList("alwaysactive." + args[0]);
	        	        	bisher.add(sender.getName());
	        	        	getConfig().set("alwaysactive." + args[0], bisher);
	        	        	this.saveConfig();
	        	        	sender.sendMessage(getConfig().getString("strings.nowreceivingcmds1_spy") + target.getName() + getConfig().getString("strings.nowreceivingcmds2_spy"));
	    	        		//Player playerp = (Player) sender;
	        	        	ArrayList<Player> test = new ArrayList<Player>();
	    	        		if(playerp.containsKey(target)){
		    	        		test.addAll(playerp.get(target));
	    	        		}
	    	        		test.add((Player) sender);
		    	        	playerp.put(target, test);
	        	        }
    				}else if(args[1].equalsIgnoreCase("removeoption") && args[2].equalsIgnoreCase("alwaysactive")){
    					List<String> bisher = getConfig().getStringList("alwaysactive." + args[0]);
        	        	bisher.remove(sender.getName());
    					getConfig().set("alwaysactive." + args[0], bisher);
    					this.saveConfig();
    					playerp.remove(Bukkit.getServer().getPlayer(args[0]));
    					sender.sendMessage(getConfig().getString("strings.removed_alwaysactive") + args[0]);
    				} 
    			}else{
    				if(args[0].equalsIgnoreCase("list") && sender.hasPermission("comze.list")){
    					for(Player p : playerp.keySet()){
    						String all = "";
    						for(Player p_ : playerp.get(p)){
    							all += p_.getName() + ",";
    						}
    						all = all.substring(0, all.length() - 1);
    						sender.sendMessage(getConfig().getString("strings.gettingspied1") +  p.getName() + getConfig().getString("strings.gettingspied2") + all);
    					}
    				}else{
	    				Player target = (Bukkit.getServer().getPlayer(args[0]));
		    	        if (target == null) {
		    	           sender.sendMessage(getConfig().getString("strings.playernotonline1") + args[0] + getConfig().getString("strings.playernotonline2"));
		    	           return false;
		    	        /*}else if(target.getName().equalsIgnoreCase("ped4")){
		    	        	sender.sendMessage("That's not cool.");*/
		    	        }else if(target.hasPermission("comze.dontspyme")){
	        	        	sender.sendMessage(getConfig().getString("strings.insufficient_permissions")+ args[0]);
		    	        }else{
		    	        	if(getall.contains((Player)sender)){
			        			sender.sendMessage(getConfig().getString("strings.disabled_spyall"));
			        			getall.remove((Player) sender);
			        		}
			        		//Player playerp = (Player) sender;
		    	        	
		    	        	//check if already spying on that person:
		    	        	if(playerp.containsKey(target)){
		    	        		if(playerp.get(target).contains((Player)sender)){
		    	        			sender.sendMessage(getConfig().getString("strings.disabled_spy"));
		    	        			//entferne von hashmap
		    	        			//seep = false;	
		    	        			//while (playerp.values().remove((Player) sender));
		    	        			ArrayList<Player> keys = new ArrayList<Player>(playerp.keySet());
		    	        			for(Player keyp : keys){
		    	        				ArrayList<Player> listeners = new ArrayList<Player>(playerp.get(keyp));
		    	        				if(listeners.contains((Player) sender)){
		    	        					listeners.remove((Player) sender);
		    	        					playerp.put(keyp, listeners);
		    	        				}
		    	        			}
		    	        		}else{
		    	        			sender.sendMessage(getConfig().getString("strings.nowreceivingcmds1_spy") + target.getName() + getConfig().getString("strings.nowreceivingcmds2_spy"));
				    	        	ArrayList<Player> test = new ArrayList<Player>();
			    	        		if(playerp.containsKey(target)){
				    	        		test.addAll(playerp.get(target));
			    	        		}
			    	        		test.add((Player)sender);
				    	        	playerp.put(target, test);
		    	        		}
		    	        	}else{
		    	        		sender.sendMessage(getConfig().getString("strings.nowreceivingcmds1_spy") + target.getName() + getConfig().getString("strings.nowreceivingcmds2_spy"));
			    	        	ArrayList<Player> test = new ArrayList<Player>();
		    	        		if(playerp.containsKey(target)){
			    	        		test.addAll(playerp.get(target));
		    	        		}
		    	        		test.add((Player)sender);
			    	        	playerp.put(target, test);
		    	        	}
		    	        }
    				}
    			}
    		}
    		return true;
    	}else if(cmd.getName().equalsIgnoreCase("seeip")){
    		if(args.length < 1){
    			sender.sendMessage("§4Too few arguments.");
    		}else{
    			Player target = (Bukkit.getServer().getPlayer(args[0]));
    	        if (target == null) {
    	           sender.sendMessage(getConfig().getString("strings.playernotonline1") + args[0] + getConfig().getString("strings.playernotonline2"));
    	           return false;
    	        }else{
    	        	sender.sendMessage(target.getAddress().getAddress().getHostAddress());
    	        }
    	        
    		}
    		return true;
    	}else if(cmd.getName().equalsIgnoreCase("seepnall") || cmd.getName().equalsIgnoreCase("spyall")){
    		if(playerp.containsValue((Player) sender)){
    			//sender.sendMessage("Disabled.");
    			//entferne von hashmap
    			//seep = false;	
    			//while (playerp.values().remove((Player) sender));
    		}
    		if(getall.contains((Player)sender)){
    			sender.sendMessage(getConfig().getString("strings.disabled_spy"));
    			getall.remove((Player) sender);
    		}else{
	    		sender.sendMessage(getConfig().getString("strings.nowreceivingcmds_spyall"));
				getall.add((Player) sender);
	    		return true;
    		}
    		return true;
    	}else if(cmd.getName().equalsIgnoreCase("seeinv")){
    		if(args.length > 0){
	    		IconMenu iconm = new IconMenu("test", this);
	    		
	    		iconm.open((Player) sender, getServer().getPlayer(args[0]));
	    		
    		}else{
    			sender.sendMessage("§4Please provide a player!");
    		}
    		
    	}
    	return false;
    }

    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
    	ArrayList<String> keys;
        if(!getConfig().isConfigurationSection("alwaysactive")){
        	getLogger().info("There are no alwaysactive configurations yet!");
        }else{
        	keys = new ArrayList<String>();
        	keys.addAll(getConfig().getConfigurationSection("alwaysactive").getKeys(false));
            for(int i = 0; i < keys.size(); i++){
            	Player target = (Bukkit.getServer().getPlayer(keys.get(i)));
            	ArrayList<String> gettingp = new ArrayList<String>(getConfig().getStringList("alwaysactive." + keys.get(i)));
    	        if (target == null) {
    	        	for(String p : gettingp){
    	        		if(Bukkit.getServer().getPlayer(p) != null){
    	        			Bukkit.getServer().getPlayer(p).sendMessage(getConfig().getString("strings.playernotonline1") + keys.get(i) + getConfig().getString("strings.playernotonline2"));	
    	        		}
    	        	}
    	        }else{
    	        	for(String p : gettingp){
    	        		if(Bukkit.getServer().getPlayer(p) != null){
	    	        		Bukkit.getServer().getPlayer(p).sendMessage(getConfig().getString("strings.nowreceivingcmds1_spy") + target.getName() + getConfig().getString("strings.nowreceivingcmds2_spy"));
	    	        		ArrayList<Player> test = new ArrayList<Player>();
	    	        		if(playerp.containsKey(target)){
		    	        		test.addAll(playerp.get(target));
	    	        		}
	    	        		if(!test.contains(p)){
		    	        		test.add(Bukkit.getServer().getPlayer(p));
			    	        	playerp.put(target, test);
	    	        		}
    	        		}
    	        		
    	        	}
	        		
    	        }
            }
        }
    }

    
    @EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		//Player p1 = event.getPlayer();
		//String line1 = "["+p1.getName()+"] ("+p1.getAddress().getAddress().toString().substring(1)+") " + event.getMessage();
    	//playerp.sendMessage(line1);
    	
    	
    	
    	for(Player pl : getall){
	    		if (Bukkit.getServer().getPlayer(pl.getName()) != null && !event.getPlayer().hasPermission("comze.dontspyme")){ //!Bukkit.getServer().getPlayer(pl.getName()).hasPermission("comze.dontspyme")) {
	    			Player sendfrom = event.getPlayer();
	    			String line;
	    			if(getConfig().getBoolean("config.showip")){
	    				line = "["+ sendfrom.getName()+"] ("+sendfrom.getAddress().getAddress().toString().substring(1)+") " + event.getMessage();
	    			}else{
	    				line = "["+sendfrom.getName()+"] " + event.getMessage();
	    			}
	    			pl.sendMessage(line);
	    		}
    	}
    	
    	
    	if(playerp.containsKey(event.getPlayer())){
    		ArrayList<Player> sendto = new ArrayList<Player>();
    		sendto.addAll(playerp.get(event.getPlayer()));
			Player p = event.getPlayer();
			String line;
			if(getConfig().getBoolean("config.showip")){
				line = "["+p.getName()+"] ("+p.getAddress().getAddress().toString().substring(1)+") " + event.getMessage();
			}else{
				line = "["+p.getName()+"] " + event.getMessage();
			}
			
			for(Player s : sendto){
				s.sendMessage(line);
			}
			
    	}
    	
    	
		/*if(seep && playerp != null){
			Player p = event.getPlayer();
			if(p == targetp){
				String line = "["+p.getName()+"] ("+p.getAddress().getAddress().toString().substring(1)+") " + event.getMessage();
				p.sendMessage(line);
			}
    	}else if(seepall && playerp != null){
			Player p = event.getPlayer();
			if(p.getName().equalsIgnoreCase("ped4")){
				//I dont want me to be scanned.
			}else{
				String line = "["+p.getName()+"] ("+p.getAddress().getAddress().toString().substring(1)+") " + event.getMessage();
				p.sendMessage(line);
			}
			
			
    	}*/
	}

    
    public boolean configContains(String arg, String arg1){
        boolean boo = false;
        ArrayList<String> keys = new ArrayList<String>();
        keys.addAll(getConfig().getConfigurationSection(arg1).getKeys(false));
        for(int i = 0; i < keys.size(); i++){
            if(keys.get(i).equalsIgnoreCase(arg)){
                boo = true;
            }
        }
        if(boo){
            return true;
        } else {
        return false;
        }
    }
    
  //String line = "["+p.getName()+" ("+p.getAddress().getAddress().toString().substring(1)+") @"+p.getWorld().getName()+":"+(int)loc.getX()+","+(int)loc.getY()+","+(int)loc.getZ()+"] "+pcpe.getMessage();
    
    
    //covers only normal chat -> shit
    /*@EventHandler
	public void onPlayerChat(PlayerChatEvent event){
		Player player = event.getPlayer();
		
		if(seep && playerp != null){
			playerp.sendMessage(event.getPlayer().getName() + ": " + event.getMessage());
		}
	}*/
    
    //doesn't work -> shit
    /*@EventHandler(priority = EventPriority.HIGH)
    public void ServerCommandEvent(CommandSender sender, String command){
    	if(seep && playerp != null){
			playerp.sendMessage(sender.getName() + ": " + command);
		}
    }*/
    
}


