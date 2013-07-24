package me.frodenkvist.horserace;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
			hrp.setGoneByCheckpoint(false);
			if(hrp.getLaps() < Race.WIN_LAPS_AMOUNT)
				return;
			race.setWinner(hrp);
		}
		else
		{
			if(!race.isOnSecondLine(hrp))
				return;
			hrp.setGoneByCheckpoint(true);
			return;
		}
	}
	@EventHandler
	public void onBlockRedstoneEvent(BlockRedstoneEvent event)
	{
		Race race = RaceHandler.getRace();
		if(race.getSignalTorch().equals(event.getBlock()))
			event.setNewCurrent(event.getOldCurrent());
			
	}
}
