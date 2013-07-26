package me.frodenkvist.horserace;

import java.text.DecimalFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener
{
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event)
	{
		Race race = RaceHandler.getRace();
		if(!race.hasStarted())
			return;
		Player player = event.getPlayer();
		HRPlayer hrp = RaceHandler.getPlayer(player);
		if(!race.contains(hrp))
			return;
		if(hrp.hasGoneByCheckpoint())
		{
			if(!race.isOnFirstLine(hrp))
				return;
			hrp.addLaps(1);
			DecimalFormat dcf = new DecimalFormat("###.##");
			Date date = new Date();
			double time = (date.getTime() - hrp.getLastTime()) / 1000.0D;
			hrp.setLastTime(date.getTime());
			hrp.setTotalTime(hrp.getTotalTime() + time);
			hrp.getPlayer().sendMessage(ChatColor.GREEN + "Total Time: " + ChatColor.YELLOW + dcf.format(hrp.getTotalTime()) + ChatColor.GREEN + " Last Time: " + ChatColor.YELLOW + dcf.format(time));
			String message = "SIDEBAR,Health," + "Laps:" + ChatColor.RESET + "," + hrp.getLaps();
			Bukkit.getMessenger().dispatchIncomingMessage(player, "Scoreboard", message.getBytes());
			hrp.setGoneByCheckpoint(false);
			if(hrp.getLaps() < Race.WIN_LAPS_AMOUNT)
				return;
			race.setWinner(hrp);
		}
		else
		{
			if(!race.isOnSecondLine(hrp))
				return;
			DecimalFormat dcf = new DecimalFormat("###.##");
			Date date = new Date();
			double time = (date.getTime() - hrp.getLastTime()) / 1000.0D;
			hrp.setLastTime(date.getTime());
			hrp.setTotalTime(hrp.getTotalTime() + time);
			hrp.getPlayer().sendMessage(ChatColor.GREEN + "Total Time: " + ChatColor.YELLOW + dcf.format(hrp.getTotalTime()) + ChatColor.GREEN + " Last Time: " + ChatColor.YELLOW + dcf.format(time));
			hrp.setGoneByCheckpoint(true);
			return;
		}
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		RaceHandler.addPlayer(new HRPlayer(event.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		RaceHandler.removePlayer(event.getPlayer());
	}
}
