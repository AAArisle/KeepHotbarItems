package com.github.aaarisle.keep_hotbar_items.common;

import com.github.aaarisle.keep_hotbar_items.KeepHotbarItems;
import com.github.aaarisle.keep_hotbar_items.capability.CapabilityItems;
import com.github.aaarisle.keep_hotbar_items.capability.CapabilityLoader;
import com.github.aaarisle.keep_hotbar_items.capability.Iitems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.List;


public class EventLoader {
    public EventLoader()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event)
    {
        // https://skmedix.github.io/ForgeJavaDocs/javadoc/forge/1.8.9-11.15.1.2318/net/minecraftforge/event/entity/living/LivingDeathEvent.html

        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            Iitems items = player.getCapability(CapabilityLoader.items, null);

            // 保存快捷栏的道具列表
            for (int i = 0; i < 9; i++) {
                items.setHotbarItems(i, player.inventory.getStackInSlot(i));
            }
            // 保存护甲栏的道具列表
            for (int i = 0; i < 4; i++) {
                items.setArmorItems(i, player.inventory.armorItemInSlot(i));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDrop(PlayerDropsEvent event)
    {
        // https://skmedix.github.io/ForgeJavaDocs/javadoc/forge/1.8.9-11.15.1.2318/net/minecraftforge/event/entity/player/PlayerDropsEvent.html

        EntityPlayer player = (EntityPlayer) event.entity;
        List<EntityItem> drops = event.drops;

        Iitems items = player.getCapability(CapabilityLoader.items, null);
        // 快捷栏的道具
        for (int i = 0; i < 9; i++) {
            if (items.getHotbarItem(i) == null) {
                continue;
            }
            for (EntityItem item : drops) {
                if (item.getEntityItem() == items.getHotbarItem(i)) {
                    drops.remove(item);
                    player.inventory.setInventorySlotContents(i, items.getHotbarItem(i));
                    break;
                }
            }
        }
        // 护甲栏的道具
        for (int i = 0; i < 4; i++) {
            if (items.getArmorItem(i) == null) {
                continue;
            }
            for (EntityItem item : drops) {
                if (item.getEntityItem() == items.getArmorItem(i)) {
                    drops.remove(item);
                    player.inventory.armorInventory[i] = items.getArmorItem(i);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        EntityPlayer player = event.player;
        Iitems items = player.getCapability(CapabilityLoader.items, null);

        for (int i = 0; i < 9; i++) {
            player.inventory.setInventorySlotContents(i, items.getHotbarItem(i));
        }
        for (int i = 0; i < 4; i++) {
            player.inventory.armorInventory[i] = items.getArmorItem(i);
        }
    }

    // capability
    @SubscribeEvent
    public void onAttachCapabilitiesEntity(AttachCapabilitiesEvent.Entity event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            ICapabilitySerializable<NBTTagCompound> provider = new CapabilityItems.ProviderPlayer();
            event.addCapability(new ResourceLocation(KeepHotbarItems.MODID + ":" + "items"), provider);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event)
    {
        Capability<Iitems> capability = CapabilityLoader.items;
        Capability.IStorage<Iitems> storage = capability.getStorage();

        if (event.original.hasCapability(capability, null) && event.entityPlayer.hasCapability(capability, null))
        {
            NBTBase nbt = storage.writeNBT(capability, event.original.getCapability(capability, null), null);
            storage.readNBT(capability, event.entityPlayer.getCapability(capability, null), null, nbt);
        }
    }
}