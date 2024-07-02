package com.github.aaarisle.keep_hotbar_items.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CapabilityLoader
{
    @CapabilityInject(Iitems.class)
    public static Capability<Iitems> items;

    public CapabilityLoader(FMLPreInitializationEvent event)
    {
        CapabilityManager.INSTANCE.register(Iitems.class, new CapabilityItems.Storage(),
                CapabilityItems.Implementation.class);
    }
}
