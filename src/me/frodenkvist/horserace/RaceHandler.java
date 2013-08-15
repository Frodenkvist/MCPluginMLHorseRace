package me.frodenkvist.horserace;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class RaceHandler
{
	private static List<HRPlayer> players = new ArrayList<HRPlayer>();
	private static Race race;
	
	public static boolean addPlayer(HRPlayer hrp)
	{
		return players.add(hrp);
	}
	
	public static HRPlayer getPlayer(Player player)
	{
		for(HRPlayer hrp : players)
		{
			if(hrp.getPlayer().equals(player))
				return hrp;
		}
		return null;
	}
	
	public static boolean removePlayer(Player player)
	{
		for(HRPlayer hrp : players)
		{
			if(hrp.getPlayer().equals(player))
				return players.remove(hrp);
		}
		return false;
	}
	
	public static void load(FileConfiguration config)
	{
		players.clear();
		race = new Race();
		if(config.contains("Race.Area.Pos1"))
		{
			race.setRaceArea(new CuboidArea(new Location(Bukkit.getWorld(config.getString("Race.Area.Pos1.world")), config.getInt("Race.Area.Pos1.x"), config.getInt("Race.Area.Pos1.y"),
					config.getInt("Race.Area.Pos1.z")), new Location(Bukkit.getWorld(config.getString("Race.Area.Pos2.world")), config.getInt("Race.Area.Pos2.x"), config.getInt("Race.Area.Pos2.y"),
							config.getInt("Race.Area.Pos2.z"))));
		}
		if(config.contains("Race.FirstLine.Pos1"))
		{
			race.setFirstLine(new CuboidArea(new Location(Bukkit.getWorld(config.getString("Race.FirstLine.Pos1.world")), config.getInt("Race.FirstLine.Pos1.x"), config.getInt("Race.FirstLine.Pos1.y"),
					config.getInt("Race.FirstLine.Pos1.z")), new Location(Bukkit.getWorld(config.getString("Race.FirstLine.Pos2.world")), config.getInt("Race.FirstLine.Pos2.x"),
							config.getInt("Race.FirstLine.Pos2.y"), config.getInt("Race.FirstLine.Pos2.z"))));
		}
		if(config.contains("Race.SecondLine.Pos1"))
		{
			race.setSecondLine(new CuboidArea(new Location(Bukkit.getWorld(config.getString("Race.SecondLine.Pos1.world")), config.getInt("Race.SecondLine.Pos1.x"), config.getInt("Race.SecondLine.Pos1.y"),
					config.getInt("Race.SecondLine.Pos1.z")), new Location(Bukkit.getWorld(config.getString("Race.SecondLine.Pos2.world")), config.getInt("Race.SecondLine.Pos2.x"),
							config.getInt("Race.SecondLine.Pos2.y"), config.getInt("Race.SecondLine.Pos2.z"))));
		}
		if(config.contains("Race.StartLoc"))
		{
			race.setStartLoc(new Location(Bukkit.getWorld(config.getString("Race.StartLoc.world")), config.getInt("Race.StartLoc.x"), config.getInt("Race.StartLoc.y"),
					config.getInt("Race.StartLoc.z")));
		}
		if(config.contains("Race.SpectatorLoc"))
		{
			race.setStartLoc(new Location(Bukkit.getWorld(config.getString("Race.SpectatorLoc.world")), config.getInt("Race.SpectatorLoc.x"), config.getInt("Race.SpectatorLoc.y"),
					config.getInt("Race.SpectatorLoc.z")));
		}
		if(config.contains("Race.SignalBlock"))
		{
			Location loc = new Location(Bukkit.getWorld(config.getString("Race.SignalBlock.world")), config.getInt("Race.SignalBlock.x"), config.getInt("Race.SignalBlock.y"),
					config.getInt("Race.SignalBlock.z"));
			Block block = loc.getBlock();
			//if(block.getTypeId() == 69)
			//{
			race.setSignalBlock(block);
			//}
		}
		if(config.contains("Race.LocalAnnouncerArea.Pos1"))
		{
			race.setRaceArea(new CuboidArea(new Location(Bukkit.getWorld(config.getString("Race.LocalAnnouncerArea.Pos1.world")), config.getInt("Race.LocalAnnouncerArea.Pos1.x"), config.getInt("Race.LocalAnnouncerArea.Pos1.y"),
					config.getInt("Race.LocalAnnouncerArea.Pos1.z")), new Location(Bukkit.getWorld(config.getString("Race.LocalAnnouncerArea.Pos2.world")), config.getInt("Race.LocalAnnouncerArea.Pos2.x"), config.getInt("Race.LocalAnnouncerArea.Pos2.y"),
							config.getInt("Race.LocalAnnouncerArea.Pos2.z"))));
		}
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(p == null)
				continue;
			players.add(new HRPlayer(p));
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(HorseRace.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				Race.globalAnnouncing("HorseRace will begin in 10 minutes prepare your horses " + ChatColor.GREEN + "/rules race");
			}
		}, 20*60*20, 20*60*30);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(HorseRace.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				Race.globalAnnouncing("HorseRace will begin in 5 minutes prepare your horses " + ChatColor.GREEN + "/rules race");
			}
		}, 20*60*25, 20*60*30);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(HorseRace.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				Race.globalAnnouncing("HorseRace will begin in 1 minutes prepare your horses " + ChatColor.GREEN + "/rules race");
				Bukkit.getScheduler().scheduleSyncDelayedTask(HorseRace.plugin, new RaceRunnable(race),0L);
			}
		}, 20*60*29, 20*60*30);
	}
	
	public static Race getRace()
	{
		return race;
	}
}
