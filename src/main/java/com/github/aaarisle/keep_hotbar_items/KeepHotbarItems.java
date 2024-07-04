package com.github.aaarisle.keep_hotbar_items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KeepHotbarItems.MODID)
public class KeepHotbarItems {
    public static final String MODID = "keephotbaritems";

    public KeepHotbarItems() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    Map<Integer, ItemStack[][]> map = new HashMap<>();
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack[] hotbars = new ItemStack[10];
            ItemStack[] armors = new ItemStack[4];

            // 保存快捷栏的道具列表
            for (int i = 0; i < 9; i++) {
                hotbars[i] = player.getInventory().items.get(i);
                player.getInventory().items.set(i, ItemStack.EMPTY);
            }
            // 保存副手的道具
            hotbars[9] = player.getInventory().offhand.get(0);
            player.getInventory().offhand.set(0, ItemStack.EMPTY);
            // 保存护甲栏的道具列表
            for (int i = 0; i < 4; i++) {
                armors[i] = player.getInventory().armor.get(i);
                player.getInventory().armor.set(i, ItemStack.EMPTY);
            }
            System.out.println(player.getId());
            map.put(player.getId(), new ItemStack[][]{hotbars, armors});
        }
    }

    @SubscribeEvent
    public void onPlayerDrop(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack[] hotbars = map.get(player.getId())[0];
            ItemStack[] armors = map.get(player.getId())[1];
            // 快捷栏的道具
            for (int i = 0; i < 9; i++) {
                player.getInventory().items.set(i, hotbars[i]);
            }
            // 副手的道具
            if (hotbars[9] != null) {
                player.getInventory().offhand.set(0, hotbars[9]);
            }
            // 护甲栏的道具
            for (int i = 0; i < 4; i++) {
                player.getInventory().armor.set(i, armors[i]);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player old_player = event.getOriginal();
            ItemStack[] hotbars = new ItemStack[10];
            ItemStack[] armors = new ItemStack[4];
            // 保存快捷栏的道具列表
            for (int i = 0; i < 9; i++) {
                hotbars[i] = old_player.getInventory().items.get(i);
            }
            // 保存副手的道具
            hotbars[9] = old_player.getInventory().offhand.get(0);
            // 保存护甲栏的道具列表
            for (int i = 0; i < 4; i++) {
                armors[i] = old_player.getInventory().armor.get(i);
            }

            Player new_player = event.getEntity();
            // 恢复快捷栏的道具列表
            for (int i = 0; i < 9; i++) {
                new_player.getInventory().items.set(i, hotbars[i]);
            }
            // 恢复副手的道具
            new_player.getInventory().offhand.set(0, hotbars[9]);
            // 恢复护甲栏的道具列表
            for (int i = 0; i < 4; i++) {
                new_player.getInventory().armor.set(i, armors[i]);
            }
        }
    }
}
