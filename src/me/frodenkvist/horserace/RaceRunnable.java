package me.frodenkvist.horserace;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class RaceRunnable implements Runnable
{
	private Race race;
	
	public RaceRunnable(Race race)
	{
		this.race = race;
	}
	
	@Override
	public void run()
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(HorseRace.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				Race.localAnnouncing("HorseRace will begin in 30 seconds prepare your horses " + ChatColor.GREEN + "/rules race", race.getLocalAnnouncerArea());
				Bukkit.getScheduler().scheduleSyncDelayedTask(HorseRace.plugin, new Runnable()
				{
					@Override
					public void run()
					{
						race.startRace();
					}
				},20*30);
			}
		},20*30);
	}
}
