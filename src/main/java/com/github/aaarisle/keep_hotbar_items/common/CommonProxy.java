package com.github.aaarisle.keep_hotbar_items.common;

import com.github.aaarisle.keep_hotbar_items.capability.CapabilityLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        new CapabilityLoader(event);
    }

    public void init(FMLInitializationEvent event)
    {
        new EventLoader();
    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }
}