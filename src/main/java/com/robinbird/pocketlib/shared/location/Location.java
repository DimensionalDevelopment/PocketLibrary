package com.robinbird.pocketlib.shared.location;

import com.robinbird.pocketlib.PocketLib;
import java.io.Serializable;
import java.util.Objects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 *
 * @author Robijnvogel
 */
public class Location implements Serializable {

    protected int dimensionID;
    protected BlockPos pos;

    public Location(World world, int x, int y, int z) {
        this(world, new BlockPos(x, y, z));
    }
    
    public Location(World world, BlockPos pos) {
        this(world.provider.getDimension(), pos);
    }

    public Location(int dimID, int x, int y, int z) {
        this(dimID, new BlockPos(x, y, z));
    }

    public Location(int dimID, BlockPos pos) {
        this.dimensionID = dimID;
        this.pos = pos; //copyOf
    }

    public BlockPos getPos() {
        return pos;
    }

    public int getDimensionID() {
        return dimensionID;
    }


    public World getWorld() {
        return PocketLib.getWorld(dimensionID);
    }

    public TileEntity getTileEntity() {
        return getWorld().getTileEntity(pos);
    }

    public IBlockState getBlockState() {
        return getWorld().getBlockState(getPos());
    }
    
    public static Location getLocation(TileEntity tileEntity) {
        World world = tileEntity.getWorld();
        BlockPos blockPos = tileEntity.getPos();
        return new Location(world, blockPos);
    }

    public static Location getLocation(Entity entity) {
        World world = entity.world;
        BlockPos blockPos = entity.getPosition();
        return new Location(world, blockPos);
    }

    public static NBTTagCompound writeToNBT(Location location) {
        NBTTagCompound locationNBT = new NBTTagCompound();
        locationNBT.setInteger("worldID", location.dimensionID);
        locationNBT.setInteger("x", location.pos.getX());
        locationNBT.setInteger("y", location.pos.getY());
        locationNBT.setInteger("z", location.pos.getZ());
        return locationNBT;
    }

    public static Location readFromNBT(NBTTagCompound locationNBT) {
        int worldID = locationNBT.getInteger("worldID");
        int x = locationNBT.getInteger("x");
        int y = locationNBT.getInteger("y");
        int z = locationNBT.getInteger("z");
        BlockPos blockPos = new BlockPos(x, y, z);
        return new Location(worldID, blockPos);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Location)) {
            return false;
        }
        Location other = (Location) o;
        return other.dimensionID == this.dimensionID && other.pos.equals(this.pos);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.dimensionID;
        hash = 89 * hash + Objects.hashCode(this.pos);
        return hash;
    }

    @Override
    public String toString() {
        return "Location: dimID: " + this.dimensionID + " position: " + this.pos.toString();
    }
    
    public void loadInfoFrom(Location location) {
        this.dimensionID = location.dimensionID;
        this.pos = location.pos;
    }
}
