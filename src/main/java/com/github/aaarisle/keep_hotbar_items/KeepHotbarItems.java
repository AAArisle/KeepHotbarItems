package com.github.aaarisle.keep_hotbar_items;

import com.github.aaarisle.keep_hotbar_items.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = KeepHotbarItems.MODID, name = KeepHotbarItems.NAME, version = KeepHotbarItems.VERSION, acceptedMinecraftVersions = "1.8.9")
public class KeepHotbarItems {
    public static final String MODID = "keephotbaritems";
    public static final String NAME = "Keep Hotbar Items";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(KeepHotbarItems.MODID)
    public static KeepHotbarItems instance;

    @SidedProxy(clientSide = "com.github.aaarisle.keep_hotbar_items.client.ClientProxy",
            serverSide = "com.github.aaarisle.keep_hotbar_items.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}
