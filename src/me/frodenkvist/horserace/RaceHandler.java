package me.frodenkvist.horserace;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(p == null)
				continue;
			players.add(new HRPlayer(p));
		}
	}
	
	public static Race getRace()
	{
		return race;
	}
}
