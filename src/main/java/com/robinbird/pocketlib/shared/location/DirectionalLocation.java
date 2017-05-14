package com.robinbird.pocketlib.shared.location;

import java.util.Objects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 *
 * @author Robijnvogel
 */
public class DirectionalLocation extends Location {

    protected EnumFacing facing;

    public DirectionalLocation(World world, int x, int y, int z, EnumFacing facing) {
        super(world, new BlockPos(x, y, z));
        this.facing = facing;
    }

    public DirectionalLocation(World world, BlockPos pos, EnumFacing facing) {
        super(world.provider.getDimension(), pos);
        this.facing = facing;
    }

    public DirectionalLocation(int dimID, int x, int y, int z, EnumFacing facing) {
        super(dimID, new BlockPos(x, y, z));
        this.facing = facing;
    }

    public DirectionalLocation(int dimID, BlockPos pos, EnumFacing facing) {
        super(dimID, pos);
        this.facing = facing;
    }

    public static NBTTagCompound writeToNBT(DirectionalLocation location) {
        NBTTagCompound locationNBT = new NBTTagCompound();
        locationNBT.setInteger("worldID", location.dimensionID);
        locationNBT.setInteger("x", location.pos.getX());
        locationNBT.setInteger("y", location.pos.getY());
        locationNBT.setInteger("z", location.pos.getZ());
        locationNBT.setShort("facing", (short) location.facing.ordinal());
        return locationNBT;
    }

    public static DirectionalLocation readFromNBT(NBTTagCompound locationNBT) {
        int worldID = locationNBT.getInteger("worldID");
        int x = locationNBT.getInteger("x");
        int y = locationNBT.getInteger("y");
        int z = locationNBT.getInteger("z");
        BlockPos blockPos = new BlockPos(x, y, z);
        EnumFacing facing = EnumFacing.getFront(locationNBT.getInteger("facing"));
        return new DirectionalLocation(worldID, blockPos, facing);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof DirectionalLocation)) {
            return false;
        }
        DirectionalLocation other = (DirectionalLocation) o;
        return other.dimensionID == this.dimensionID && other.pos.equals(this.pos) && this.facing.equals(other.facing);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.dimensionID;
        hash = 37 * hash + Objects.hashCode(this.pos);
        hash = 37 * hash + Objects.hashCode(this.facing);
        return hash;
    }

    @Override
    public String toString() {
        return "DirectionalLocation: dimID: " + this.dimensionID + ", position: " + this.pos.toString() + ", facing: " + this.facing.toString();
    }

    public void loadInfoFrom(DirectionalLocation location) {
        super.loadInfoFrom(location);
        this.facing = location.facing;
    }

    public void loadInfoFrom(Location location) {
        if (location instanceof DirectionalLocation) {
            loadInfoFrom((DirectionalLocation) location);
        } else {
            super.loadInfoFrom(location);
            this.facing = EnumFacing.SOUTH;
        }
    }
}
