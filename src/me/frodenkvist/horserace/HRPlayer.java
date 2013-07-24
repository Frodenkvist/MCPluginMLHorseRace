package me.frodenkvist.horserace;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HRPlayer
{
	private final Player player;
	private boolean checkpoint;
	private int laps;
	
	public HRPlayer(Player player)
	{
		this.player = player;
		checkpoint = false;
		laps = 0;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public Location getLocation()
	{
		return player.getLocation();
	}
	
	public String getName()
	{
		return player.getName();
	}
	
	public boolean hasGoneByCheckpoint()
	{
		return checkpoint;
	}
	
	public void setGoneByCheckpoint(boolean value)
	{
		checkpoint = value;
	}
	
	public void addLaps(int value)
	{
		laps += value;
	}
	
	public void setLaps(int value)
	{
		laps = value;
	}
	
	public int getLaps()
	{
		return laps;
	}
}
