package me.frodenkvist.horserace;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Race
{
	public static final int WIN_LAPS_AMOUNT = 2;
	private List<HRPlayer> participants = new ArrayList<HRPlayer>();
	private CuboidArea raceArea;
	private CuboidArea firstLine;
	private CuboidArea secondLine;
	private Location startLoc;
	private Block signalTorch;
	private boolean started;
	
	public Race()
	{
	}
	
	public void setRaceArea(CuboidArea ca)
	{
		raceArea = ca;
	}
	
	public void setFirstLine(CuboidArea ca)
	{
		firstLine = ca;
	}
	
	public void setSecondLine(CuboidArea ca)
	{
		secondLine = ca;
	}
	
	public void setSignalTorch(Block block)
	{
		signalTorch = block;
	}
	
	public boolean addParticipant(HRPlayer hrp)
	{
		return participants.add(hrp);
	}
	
	public void startRace()
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(p == null)
				continue;
			if(!raceArea.containsLoc(p.getLocation()))
				continue;
			//p.teleport(startLoc);
			participants.add(RaceHandler.getPlayer(p));
		}
		signalTorch.setType(Material.REDSTONE_TORCH_ON);
		started = true;
	}
	
	public void setWinner(HRPlayer winner)
	{
		for(HRPlayer hrp : participants)
		{
			if(hrp.equals(winner))
				continue;
			hrp.setLaps(0);
			participants.remove(hrp);
		}
		Race.globalAnnouncing(winner.getName() + " Has Won!");
		winner.setLaps(0);
		participants.remove(winner);
		signalTorch.setType(Material.REDSTONE_TORCH_OFF);
		started = false;
	}
	
	public boolean hasStarted()
	{
		return started;
	}
	
	public boolean contains(HRPlayer hrp)
	{
		return participants.contains(hrp);
	}
	
	public boolean isOnFirstLine(HRPlayer hrp)
	{
		return firstLine.containsLoc(hrp.getLocation());
	}
	
	public boolean isOnSecondLine(HRPlayer hrp)
	{
		return secondLine.containsLoc(hrp.getLocation());
	}
	
	public void setStartLoc(Location loc)
	{
		startLoc = loc;
	}
	
	public Block getSignalTorch()
	{
		return signalTorch;
	}
	
	public static void localAnnouncing(String msg, CuboidArea raceArea)
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(p == null)
				continue;
			if(!raceArea.containsLoc(p.getLocation()))
				continue;
			p.sendMessage(ChatColor.GREEN + "[Race]: " + ChatColor.RESET + msg);
		}
	}
	
	public static void globalAnnouncing(String msg)
	{
		Bukkit.broadcastMessage(ChatColor.GREEN + "[Race]: " + ChatColor.RESET + msg);
	}
}
