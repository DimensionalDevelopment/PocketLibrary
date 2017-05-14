/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robinbird.pocketlib.shared.dummy;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;

/**
 *
 * @author Robijnvogel
 */
public class ItemMissingItem extends Item implements DummyObject{
    
    String wrappedItemModID;
    String wrappedItemName;
    NBTTagCompound wrappedItemNBT;
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        wrappedItemModID = nbt.getString("wrappedItemModID");
        wrappedItemName = nbt.getString("wrappedItemName");
        wrappedItemNBT = nbt.getCompoundTag("wrappedItemNBT");
        tryUnwrap();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setString("wrappedItemModID", wrappedItemModID);
        nbt.setString("wrappedItemName", wrappedItemName);
        nbt.setTag("wrappedItemNBT", wrappedItemNBT);
        return nbt;
    }

    @Override
    public boolean tryUnwrap() {
        if (Loader.isModLoaded(wrappedItemModID)) {
            dostuff
        }
        return false;
        
    }
    
}
