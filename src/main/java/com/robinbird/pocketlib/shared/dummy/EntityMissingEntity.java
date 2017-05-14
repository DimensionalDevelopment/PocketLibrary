/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robinbird.pocketlib.shared.dummy;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

/**
 *
 * @author Robijnvogel
 */
public class EntityMissingEntity extends Entity implements DummyObject{
    
    String wrappedEntityModID;
    String wrappedEntityName;
    NBTTagCompound wrappedEntityNBT;

    public EntityMissingEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        crash();
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        wrappedEntityModID = nbt.getString("wrappedEntityModID");
        wrappedEntityName = nbt.getString("wrappedEntityName");
        wrappedEntityNBT = nbt.getCompoundTag("wrappedEntityNBT");
        tryUnwrap();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setString("wrappedEntityModID", wrappedEntityModID);
        nbt.setString("wrappedEntityName", wrappedEntityName);
        nbt.setTag("wrappedEntityNBT", wrappedEntityNBT);
    }

    @Override
    public boolean tryUnwrap() {
        if (Loader.isModLoaded(wrappedEntityModID)) {
            dostuff
        }
        return false;
        
    }
    
}
