package me.frodenkvist.horserace;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.frodenkvist.powerball.PBHandler;
import me.frodenkvist.utils.FireworkEffectPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Race
{
	public static final int WIN_LAPS_AMOUNT = 4;
	private int firstLapPeep;
	private List<HRPlayer> participants = new ArrayList<HRPlayer>();
	private CuboidArea raceArea;
	private CuboidArea localAnnouncerArea;
	private CuboidArea firstLine;
	private CuboidArea secondLine;
	private Location startLoc;
	private Block signalBlock;
	private Location spectatorSpawn;
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
	
	public void setSignalBlock(Block block)
	{
		signalBlock = block;
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
			if(!p.isInsideVehicle())
			{
				p.teleport(spectatorSpawn);
				p.sendMessage(ChatColor.AQUA + "[" + ChatColor.GREEN + "HorseRace" + ChatColor.AQUA + "] " + ChatColor.YELLOW + "You Were Not On A Horse!");
				continue;
			}
			p.teleport(startLoc);
			HRPlayer hrp = RaceHandler.getPlayer(p);
			participants.add(hrp);
			hrp.setLastTime(new Date().getTime());
			hrp.setTotalTime(0);
			String message = "SIDEBAR,Health," + "Laps:" + ChatColor.RESET + ",1";
			String message2 = "SIDEBAR,Health," + "Laps:" + ChatColor.RESET + ",0";
			Bukkit.getMessenger().dispatchIncomingMessage(p, "Scoreboard", message.getBytes());
			Bukkit.getMessenger().dispatchIncomingMessage(p, "Scoreboard", message2.getBytes());
		}
		firstLapPeep = 0;
		signalBlock.setType(Material.REDSTONE_BLOCK);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(HorseRace.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				Race.localAnnouncing("ON YOUR MARKS", localAnnouncerArea);
				Bukkit.getScheduler().scheduleSyncDelayedTask(HorseRace.plugin, new Runnable()
				{
					@Override
					public void run()
					{
						Race.localAnnouncing("GET SET!", localAnnouncerArea);
						Bukkit.getScheduler().scheduleSyncDelayedTask(HorseRace.plugin, new Runnable()
						{
							@Override
							public void run()
							{
								Race.localAnnouncing("GOOOOOO!!!", localAnnouncerArea);
								signalBlock.setType(Material.AIR);
								started = true;
							}
						}, 20*2);
					}
				}, 20*2);
			}
		}, 20*2);
	}
	
	public void setWinner(HRPlayer winner)
	{
		String message = "REMOVE,Laps:";
		for(HRPlayer hrp : participants)
		{
			if(hrp.equals(winner))
				continue;
			hrp.setLaps(0);
			hrp.setLastTime(0);
			hrp.setTotalTime(0);
			Bukkit.getMessenger().dispatchIncomingMessage(hrp.getPlayer(), "Scoreboard", message.getBytes());
			participants.remove(hrp);
		}
		Race.globalAnnouncing(ChatColor.LIGHT_PURPLE + winner.getName() + ChatColor.YELLOW + " has won the horse race, riding " + ChatColor.LIGHT_PURPLE + ((LivingEntity)winner.getPlayer().getVehicle()).getCustomName()
				+ ChatColor.YELLOW + " and won a " + ChatColor.AQUA + "P" + ChatColor.YELLOW + "R" + ChatColor.RED + "I" + ChatColor.GREEN + "Z" + ChatColor.BLUE + "E" + ChatColor.YELLOW + " EGG, say: " + 
				ChatColor.GREEN + "/rules race " + ChatColor.YELLOW + " for more information.");
		winnerFireworks(winner.getPlayer());
		winner.setLaps(0);
		giveReward(winner);
		Bukkit.getMessenger().dispatchIncomingMessage(winner.getPlayer(), "Scoreboard", message.getBytes());
		participants.remove(winner);
		signalBlock.setType(Material.AIR);
		started = false;
		firstLapPeep = 0;
	}
	
	public void checkFirst(HRPlayer hrp)
	{
		if(hrp.getLaps() > firstLapPeep)
		{
			Race.localAnnouncing(ChatColor.LIGHT_PURPLE + hrp.getName() + ChatColor.YELLOW + " has started lap lapnumber in the lead riding " + ChatColor.LIGHT_PURPLE + 
					((LivingEntity)hrp.getPlayer().getVehicle()).getCustomName() + ChatColor.YELLOW + " " + hrp.getLaps() + "/" + Race.WIN_LAPS_AMOUNT, localAnnouncerArea);
			firstLapPeep = hrp.getLaps();
		}
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
	
	public Block getSignalBlock()
	{
		return signalBlock;
	}
	
	public void setLocalAnnouncerArea(CuboidArea area)
	{
		localAnnouncerArea = area;
	}
	
	public CuboidArea getLocalAnnouncerArea()
	{
		return localAnnouncerArea;
	}
	
	public void setSpectatorLoc(Location loc)
	{
		spectatorSpawn = loc;
	}
	
	private void giveReward(HRPlayer hrp)
	{
		Inventory inv = hrp.getPlayer().getInventory();
		boolean check = false;
		for(ItemStack is : inv.getContents())
		{
			if(is == null)
			{
				check = true;
				break;
			}
			if(is.getType().equals(Material.AIR))
			{
				check = true;
				break;
			}
		}
		if(check)
			inv.addItem(PBHandler.getPowerBall());
	}
	
	public void winnerFireworks(Player player)
	{
		FireworkEffectPlayer feplayer = new FireworkEffectPlayer();
		FireworkEffect fe = FireworkEffect.builder().withColor(Color.YELLOW).withColor(Color.RED).with(Type.STAR).flicker(true).build();
		try
		{
			feplayer.playFirework(player.getWorld(), player.getLocation(), fe);
		}
		catch(Exception e)
		{
		}
	}
	
	public static void localAnnouncing(String msg, CuboidArea localAnnouncerArea)
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(p == null)
				continue;
			if(!localAnnouncerArea.containsLoc(p.getLocation()))
				continue;
			p.sendMessage(ChatColor.AQUA + "[" + ChatColor.GREEN + "HorseRace" + ChatColor.AQUA + "] " + ChatColor.YELLOW + msg);
		}
	}
	
	public static void globalAnnouncing(String msg)
	{
		Bukkit.broadcastMessage(ChatColor.AQUA + "[" + ChatColor.GREEN + "HorseRace" + ChatColor.AQUA + "] " + ChatColor.YELLOW + msg);
	}
}
