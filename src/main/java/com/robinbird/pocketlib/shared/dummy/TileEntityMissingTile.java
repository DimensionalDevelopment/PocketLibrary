/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robinbird.pocketlib.shared.dummy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;

/**
 *
 * @author Robijnvogel
 */
public class TileEntityMissingTile extends TileEntityMissingBlock implements DummyObject{
    
    String wrappedTileModID;
    String wrappedTileName;
    NBTTagCompound wrappedTileNBT;
    
    public TileEntityMissingTile(TileEntityMissingBlock base, String wrappedTileModID, String wrappedTileName, NBTTagCompound wrappedTileNBT) {
        setData(base.wrappedBlockModID, base.wrappedBlockName);
        this.wrappedTileModID = wrappedTileModID;
        this.wrappedTileName = wrappedTileName;
        this.wrappedTileNBT = wrappedTileNBT;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        wrappedTileModID = nbt.getString("wrappedTileModID");
        wrappedTileName = nbt.getString("wrappedTileName");
        wrappedTileNBT = nbt.getCompoundTag("wrappedTileNBT");
        tryUnwrap();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setString("wrappedTileModID", wrappedTileModID);
        nbt.setString("wrappedTileName", wrappedTileName);
        nbt.setTag("wrappedTileNBT", wrappedTileNBT);
        return nbt;
    }
    
    @Override
    public boolean tryUnwrap() {
        if (Loader.isModLoaded(wrappedBlockModID) && Loader.isModLoaded(wrappedTileModID)) {
            dostuff
        }
        return false;        
    }
    
}
