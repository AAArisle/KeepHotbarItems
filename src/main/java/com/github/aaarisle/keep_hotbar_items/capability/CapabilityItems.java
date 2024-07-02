package com.github.aaarisle.keep_hotbar_items.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityItems {
    public static class Storage implements Capability.IStorage<Iitems>
    {
        //把对象序列化（Serialize）成一个NBT标签
        @Override
        public NBTBase writeNBT(Capability<Iitems> capability, Iitems instance, EnumFacing side)
        {
            NBTTagCompound compound = new NBTTagCompound();
            for (int i = 0; i < 9; i++)
            {
                if (instance.getHotbarItem(i) != null) {
                    compound.setTag("hotbar"+i, instance.getHotbarItem(i).serializeNBT());
                }
            }
            for (int i = 0; i < 4; i++)
            {
                if (instance.getArmorItem(i) != null) {
                    compound.setTag("armor"+i, instance.getArmorItem(i).serializeNBT());
                }
            }
            return compound;
        }

        //NBT标签逆序列化
        @Override
        public void readNBT(Capability<Iitems> capability, Iitems instance, EnumFacing side, NBTBase nbt)
        {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            for (int i = 0; i < 9; i++)
            {
                instance.setHotbarItems(i, ItemStack.loadItemStackFromNBT(compound.getCompoundTag("hotbar"+i)));
            }
            for (int i = 0; i < 4; i++)
            {
                instance.setArmorItems(i, ItemStack.loadItemStackFromNBT(compound.getCompoundTag("armor"+i)));
            }
        }
    }
    public static class Implementation implements Iitems
    {
        private ItemStack[] hotbars = new ItemStack[9];
        private ItemStack[] armors = new ItemStack[4];

        @Override
        public void setHotbarItems(int index, ItemStack item) {
            hotbars[index] = item;
        }

        @Override
        public ItemStack getHotbarItem(int index) {
            return hotbars[index];
        }

        @Override
        public void setArmorItems(int index, ItemStack item) {
            armors[index] = item;
        }

        @Override
        public ItemStack getArmorItem(int index) {
            return armors[index];
        }
    }
    public static class ProviderPlayer implements ICapabilitySerializable<NBTTagCompound>
    {
        private Iitems items = new Implementation();
        private Capability.IStorage<Iitems> storage = CapabilityLoader.items.getStorage();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing)
        {
            return CapabilityLoader.items.equals(capability);
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing)
        {
            if (CapabilityLoader.items.equals(capability))
            {
                @SuppressWarnings("unchecked")
                T result = (T) items;
                return result;
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("items", storage.writeNBT(CapabilityLoader.items, items, null));
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound)
        {
            storage.readNBT(CapabilityLoader.items, items, null, compound.getTag("items"));
        }
    }
}
