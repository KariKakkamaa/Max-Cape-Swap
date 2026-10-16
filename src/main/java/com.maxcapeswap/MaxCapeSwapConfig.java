package com.maxcapeswap;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("maxcapeswap")
public interface MaxCapeSwapConfig extends Config
{
	@ConfigItem(
		keyName = "regionIds",
		name = "Region IDs",
		description = "Comma-separated region IDs where the swap is active (e.g. 11828,12342)",
		position = 1
	)
	default String regionIds()
	{
		return "";
	}

	@ConfigItem(
		keyName = "option",
		name = "Left-click option",
		description = "Exact text of the Max cape menu option to make left-click (e.g. Crafting Guild, Tele to POH)",
		position = 2
	)
	default String option()
	{
		return "Crafting Guild";
	}

	@ConfigItem(
		keyName = "printRegion",
		name = "Print region ID",
		description = "Prints your current region ID in chat whenever it changes, so you can find the IDs to use",
		position = 3
	)
	default boolean printRegion()
	{
		return false;
	}
}
