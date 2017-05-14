/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robinbird.pocketlib.shared.dummy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Loader;

/**
 *
 * @author Robijnvogel
 */
public class TileEntityMissingBlock extends TileEntity implements DummyObject {

    String wrappedBlockModID;
    String wrappedBlockName;

    public TileEntityMissingBlock() {
    }

    public void setData(String wrappedBlockModID, String wrappedBlockName) {
        this.wrappedBlockModID = wrappedBlockModID;
        this.wrappedBlockName = wrappedBlockName;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        wrappedBlockModID = nbt.getString("wrappedBlockModID");
        wrappedBlockName = nbt.getString("wrappedBlockName");
        tryUnwrap();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setString("wrappedBlockModID", wrappedBlockModID);
        nbt.setString("wrappedBlockName", wrappedBlockName);
        return nbt;
    }

    @Override
    public boolean tryUnwrap() {
        if (Loader.isModLoaded(wrappedBlockModID)) {
            dostuff
        }
        return false;
    }

}
