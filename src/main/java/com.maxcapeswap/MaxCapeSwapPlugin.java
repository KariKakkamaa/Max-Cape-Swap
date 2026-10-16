package com.maxcapeswap;

import com.google.inject.Provides;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.PostMenuSort;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Max Cape Swap",
	description = "Swap the Max cape left-click option while in chosen regions",
	tags = {"max", "cape", "menu", "swap", "teleport"}
)
public class MaxCapeSwapPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private MaxCapeSwapConfig config;

	private final Set<Integer> regions = new HashSet<>();
	private int lastRegion = -1;

	@Provides
	MaxCapeSwapConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MaxCapeSwapConfig.class);
	}

	@Override
	protected void startUp()
	{
		parseRegions();
	}

	@Override
	protected void shutDown()
	{
		regions.clear();
		lastRegion = -1;
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if ("maxcapeswap".equals(event.getGroup()))
		{
			parseRegions();
		}
	}

	private void parseRegions()
	{
		regions.clear();
		for (String s : config.regionIds().split(","))
		{
			try
			{
				regions.add(Integer.parseInt(s.trim()));
			}
			catch (NumberFormatException ignored)
			{
			}
		}
	}

	private int currentRegion()
	{
		Player p = client.getLocalPlayer();
		if (p == null)
		{
			return -1;
		}
		// fromLocalInstance gives the real-world region even inside instances
		return WorldPoint.fromLocalInstance(client, p.getLocalLocation()).getRegionID();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (!config.printRegion())
		{
			return;
		}
		int region = currentRegion();
		if (region != lastRegion)
		{
			lastRegion = region;
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Region ID: " + region, null);
		}
	}

	@Subscribe(priority = -10)
	public void onPostMenuSort(PostMenuSort event)
	{
		if (client.isMenuOpen() || !regions.contains(currentRegion()))
		{
			return;
		}

		MenuEntry[] entries = client.getMenuEntries();
		String wanted = config.option().trim();
		int idx = -1;

		for (int i = 0; i < entries.length; i++)
		{
			String target = Text.removeTags(entries[i].getTarget()).toLowerCase();
			String option = Text.removeTags(entries[i].getOption());
			if (target.contains("max cape") && option.equalsIgnoreCase(wanted))
			{
				idx = i;
				break;
			}
		}

		int top = entries.length - 1; // last entry in the array is the left-click one
		if (idx < 0 || idx == top)
		{
			return;
		}

		MenuEntry tmp = entries[idx];
		entries[idx] = entries[top];
		entries[top] = tmp;
		client.setMenuEntries(entries);
	}
}
