package com.github.aaarisle.keep_hotbar_items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@Mod(modid = KeepHotbarItems.MODID, name = KeepHotbarItems.NAME, version = KeepHotbarItems.VERSION, acceptedMinecraftVersions = "1.8.9")
public class KeepHotbarItems {
    public static final String MODID = "keephotbaritems";
    public static final String NAME = "Keep Hotbar Items";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(KeepHotbarItems.MODID)
    public static KeepHotbarItems instance;

    public KeepHotbarItems() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    Map<Integer, ItemStack[][]> map = new HashMap<Integer, ItemStack[][]>();

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        // https://skmedix.github.io/ForgeJavaDocs/javadoc/forge/1.8.9-11.15.1.2318/net/minecraftforge/event/entity/living/LivingDeathEvent.html

        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            ItemStack[] hotbars = new ItemStack[9];
            ItemStack[] armors = new ItemStack[4];

            // 保存快捷栏的道具列表
            for (int i = 0; i < 9; i++) {
                hotbars[i] = player.inventory.getStackInSlot(i);
                player.inventory.setInventorySlotContents(i, null);
            }
            // 保存护甲栏的道具列表
            for (int i = 0; i < 4; i++) {
                armors[i] = player.inventory.armorItemInSlot(i);
                player.inventory.armorInventory[i] = null;
            }

            map.put(player.getEntityId(), new ItemStack[][]{hotbars, armors});
        }
    }

    @SubscribeEvent
    public void onPlayerDrop(PlayerDropsEvent event) {
        // https://skmedix.github.io/ForgeJavaDocs/javadoc/forge/1.8.9-11.15.1.2318/net/minecraftforge/event/entity/player/PlayerDropsEvent.html

        EntityPlayer player = (EntityPlayer) event.entity;
        ItemStack[] hotbars = map.get(player.getEntityId())[0];
        ItemStack[] armors = map.get(player.getEntityId())[1];

        // 快捷栏的道具
        for (int i = 0; i < 9; i++) {
            player.inventory.setInventorySlotContents(i, hotbars[i]);
        }
        // 护甲栏的道具
        for (int i = 0; i < 4; i++) {
            player.inventory.armorInventory[i] = armors[i];
        }
    }

    @SubscribeEvent
    public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        if (event.wasDeath) {
            EntityPlayer old_player = event.original;
            ItemStack[] hotbars = new ItemStack[9];
            ItemStack[] armors = new ItemStack[4];
            // 保存快捷栏的道具列表
            for (int i = 0; i < 9; i++) {
                hotbars[i] = old_player.inventory.getStackInSlot(i);
            }
            // 保存护甲栏的道具列表
            for (int i = 0; i < 4; i++) {
                armors[i] = old_player.inventory.armorItemInSlot(i);
            }

            EntityPlayer new_player = event.entityPlayer;
            // 恢复快捷栏的道具列表
            for (int i = 0; i < 9; i++) {
                new_player.inventory.setInventorySlotContents(i, hotbars[i]);
            }
            // 恢复护甲栏的道具列表
            for (int i = 0; i < 4; i++) {
                new_player.inventory.armorInventory[i] = armors[i];
            }
        }
    }
}
