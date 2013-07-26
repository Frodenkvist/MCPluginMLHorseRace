package me.frodenkvist.horserace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;

public class HorseRace extends JavaPlugin
{
	public File configFile;
	public final Logger logger = Logger.getLogger("Minecraft");
	public static HorseRace plugin;
	public final PlayerListener pl = new PlayerListener();
	public static WorldEdit we;
	public static WorldEditPlugin wep;
	
	@Override
	public void onDisable()
	{
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disabled!");
	}
	
	@Override
	public void onEnable()
	{
		plugin = this;
		configFile = new File(getDataFolder(), "config.yml");
		
		try
		{
			firstRun();
	    }
		catch (Exception e)
		{
	        e.printStackTrace();
	    }
		
		RaceHandler.load(getConfig());
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(pl, this);
		
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(!(sender instanceof Player))
			return false;
		Player player = (Player)sender;
		if(commandLabel.equalsIgnoreCase("hr"))
		{
			if(args.length == 1)
			{
				if(args[0].equalsIgnoreCase("start"))
					RaceHandler.getRace().startRace();
			}
			else if(args.length == 2)
			{
				if(args[0].equalsIgnoreCase("set"))
				{
					if(args[1].equalsIgnoreCase("area"))
					{
						we = WorldEdit.getInstance();
						wep = (WorldEditPlugin)getServer().getPluginManager().getPlugin("WorldEdit");
						if(!player.hasPermission("horserace.admin"))
							return false;
						Region region = null;
						try
						{
							region = wep.getSession(player).getSelection(wep.wrapPlayer(player).getWorld());
						}
						catch (IncompleteRegionException e)
						{
							e.printStackTrace();
						}
						if(region == null)
							return false;
						CuboidArea ca = new CuboidArea(new Location(player.getWorld(),region.getMinimumPoint().getBlockX(),region.getMinimumPoint().getBlockY(),region.getMinimumPoint().getBlockZ())
						,new Location(player.getWorld(),region.getMaximumPoint().getBlockX(),region.getMaximumPoint().getBlockY(),region.getMaximumPoint().getBlockZ()));
						
						getConfig().set("Race.Area.Pos1.x", ca.getHighLoc().getBlockX());
						getConfig().set("Race.Area.Pos1.y", ca.getHighLoc().getBlockY());
						getConfig().set("Race.Area.Pos1.z", ca.getHighLoc().getBlockZ());
						getConfig().set("Race.Area.Pos1.world", ca.getHighLoc().getWorld().getName());
						
						getConfig().set("Race.Area.Pos2.x", ca.getLowLoc().getBlockX());
						getConfig().set("Race.Area.Pos2.y", ca.getLowLoc().getBlockY());
						getConfig().set("Race.Area.Pos2.z", ca.getLowLoc().getBlockZ());
						getConfig().set("Race.Area.Pos2.world", ca.getLowLoc().getWorld().getName());
						
						saveConfig();
						
						RaceHandler.getRace().setRaceArea(ca);
						
						player.sendMessage(ChatColor.GREEN + "Area Set");
						
						return true;
					}
					else if(args[1].equalsIgnoreCase("firstline"))
					{
						we = WorldEdit.getInstance();
						wep = (WorldEditPlugin)getServer().getPluginManager().getPlugin("WorldEdit");
						if(!player.hasPermission("horserace.admin"))
							return false;
						Region region = null;
						try
						{
							region = wep.getSession(player).getSelection(wep.wrapPlayer(player).getWorld());
						}
						catch (IncompleteRegionException e)
						{
							e.printStackTrace();
						}
						if(region == null)
							return false;
						CuboidArea ca = new CuboidArea(new Location(player.getWorld(),region.getMinimumPoint().getBlockX(),region.getMinimumPoint().getBlockY(),region.getMinimumPoint().getBlockZ())
						,new Location(player.getWorld(),region.getMaximumPoint().getBlockX(),region.getMaximumPoint().getBlockY(),region.getMaximumPoint().getBlockZ()));
						
						getConfig().set("Race.FirstLine.Pos1.x", ca.getHighLoc().getBlockX());
						getConfig().set("Race.FirstLine.Pos1.y", ca.getHighLoc().getBlockY());
						getConfig().set("Race.FirstLine.Pos1.z", ca.getHighLoc().getBlockZ());
						getConfig().set("Race.FirstLine.Pos1.world", ca.getHighLoc().getWorld().getName());
						
						getConfig().set("Race.FirstLine.Pos2.x", ca.getLowLoc().getBlockX());
						getConfig().set("Race.FirstLine.Pos2.y", ca.getLowLoc().getBlockY());
						getConfig().set("Race.FirstLine.Pos2.z", ca.getLowLoc().getBlockZ());
						getConfig().set("Race.FirstLine.Pos2.world", ca.getLowLoc().getWorld().getName());
						
						saveConfig();
						RaceHandler.getRace().setFirstLine(ca);
						
						player.sendMessage(ChatColor.GREEN + "First Line Set");
						
						return true;
					}
					else if(args[1].equalsIgnoreCase("secondline"))
					{
						we = WorldEdit.getInstance();
						wep = (WorldEditPlugin)getServer().getPluginManager().getPlugin("WorldEdit");
						if(!player.hasPermission("horserace.admin"))
							return false;
						Region region = null;
						try
						{
							region = wep.getSession(player).getSelection(wep.wrapPlayer(player).getWorld());
						}
						catch (IncompleteRegionException e)
						{
							e.printStackTrace();
						}
						if(region == null)
							return false;
						CuboidArea ca = new CuboidArea(new Location(player.getWorld(),region.getMinimumPoint().getBlockX(),region.getMinimumPoint().getBlockY(),region.getMinimumPoint().getBlockZ())
						,new Location(player.getWorld(),region.getMaximumPoint().getBlockX(),region.getMaximumPoint().getBlockY(),region.getMaximumPoint().getBlockZ()));
						
						getConfig().set("Race.SecondLine.Pos1.x", ca.getHighLoc().getBlockX());
						getConfig().set("Race.SecondLine.Pos1.y", ca.getHighLoc().getBlockY());
						getConfig().set("Race.SecondLine.Pos1.z", ca.getHighLoc().getBlockZ());
						getConfig().set("Race.SecondLine.Pos1.world", ca.getHighLoc().getWorld().getName());
						
						getConfig().set("Race.SecondLine.Pos2.x", ca.getLowLoc().getBlockX());
						getConfig().set("Race.SecondLine.Pos2.y", ca.getLowLoc().getBlockY());
						getConfig().set("Race.SecondLine.Pos2.z", ca.getLowLoc().getBlockZ());
						getConfig().set("Race.SecondLine.Pos2.world", ca.getLowLoc().getWorld().getName());
						
						saveConfig();
						
						RaceHandler.getRace().setSecondLine(ca);
						
						player.sendMessage(ChatColor.GREEN + "Second Line Set");
						
						return true;
					}
					else if(args[1].equalsIgnoreCase("startloc"))
					{
						Location loc = player.getLocation();
						
						getConfig().set("Race.StartLoc.x", loc.getBlockX());
						getConfig().set("Race.StartLoc.y", loc.getBlockY());
						getConfig().set("Race.StartLoc.z", loc.getBlockZ());
						getConfig().set("Race.StartLoc.world", loc.getWorld().getName());
						
						saveConfig();
						
						RaceHandler.getRace().setStartLoc(loc);
						
						player.sendMessage(ChatColor.GREEN + "Start Location Set");
						
						return true;
					}
					else if(args[1].equalsIgnoreCase("signalblock"))
					{
						Block block = player.getTargetBlock(null, 10);
						//block = block.getLocation().add(0,1,0).getBlock();
						/*if(block.getTypeId() != 69)
						{
							player.sendMessage(ChatColor.RED + "You Must Target A Lever");
							player.sendMessage("" + block.getTypeId());
							return false;
						}*/
						
						getConfig().set("Race.SignalBlock.x", block.getLocation().getBlockX());
						getConfig().set("Race.SignalBlock.y", block.getLocation().getBlockY());
						getConfig().set("Race.SignalBlock.z", block.getLocation().getBlockZ());
						getConfig().set("Race.SignalBlock.world", block.getLocation().getWorld().getName());
						
						saveConfig();
						
						RaceHandler.getRace().setSignalBlock(block);
						
						block.setType(Material.AIR);
						
						player.sendMessage(ChatColor.GREEN + "Signal Block Set");
						
						return true;
					}
				}
			}
		}
		return true;
	}
	
	public static Server getServ()
	{
		return plugin.getServer();
	}
	
	private void firstRun() throws Exception
	{
	    if(!configFile.exists())
	    {
	        configFile.getParentFile().mkdirs();
	        copy(getResource("config.yml"), configFile);
	    }
	}
	private void copy(InputStream in, File file)
	{
	    try
	    {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0)
	        {
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	}
}
