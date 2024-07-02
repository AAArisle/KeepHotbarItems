package com.github.aaarisle.keep_hotbar_items.capability;

import net.minecraft.item.ItemStack;

public interface Iitems {
    public void setHotbarItems(int index, ItemStack item);
    public ItemStack getHotbarItem(int index);

    public void setArmorItems(int index, ItemStack item);
    public ItemStack getArmorItem(int index);
}
