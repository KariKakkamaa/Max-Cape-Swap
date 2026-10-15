package com.maxcapeswap;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MaxCapeSwapPluginTest
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(MaxCapeSwapPlugin.class);
        RuneLite.main(args);
    }
}