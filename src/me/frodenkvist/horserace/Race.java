package me.frodenkvist.horserace;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.frodenkvist.utils.FireworkEffectPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Race
{
	public static final int WIN_LAPS_AMOUNT = 2;
	private List<HRPlayer> participants = new ArrayList<HRPlayer>();
	private CuboidArea raceArea;
	private CuboidArea firstLine;
	private CuboidArea secondLine;
	private Location startLoc;
	private Block signalBlock;
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
			//p.teleport(startLoc);
			HRPlayer hrp = RaceHandler.getPlayer(p);
			participants.add(hrp);
			hrp.setLastTime(new Date().getTime());
			hrp.setTotalTime(0);
			String message = "SIDEBAR,Health," + "Laps:" + ChatColor.RESET + ",1";
			String message2 = "SIDEBAR,Health," + "Laps:" + ChatColor.RESET + ",0";
			Bukkit.getMessenger().dispatchIncomingMessage(p, "Scoreboard", message.getBytes());
			Bukkit.getMessenger().dispatchIncomingMessage(p, "Scoreboard", message2.getBytes());
		}
		signalBlock.setType(Material.REDSTONE_BLOCK);
		started = true;
		Race.globalAnnouncing(ChatColor.YELLOW + "The Race Has STARTED!!");
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
		Race.globalAnnouncing(winner.getName() + " Has Won!");
		winnerFireworks(winner.getPlayer());
		winner.setLaps(0);
		giveReward(winner);
		Bukkit.getMessenger().dispatchIncomingMessage(winner.getPlayer(), "Scoreboard", message.getBytes());
		participants.remove(winner);
		signalBlock.setType(Material.AIR);
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
	
	public Block getSignalBlock()
	{
		return signalBlock;
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
			inv.addItem(new ItemStack(99));
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
