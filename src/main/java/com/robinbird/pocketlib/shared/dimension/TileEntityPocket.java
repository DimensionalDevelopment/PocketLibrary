/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robinbird.pocketlib.shared.dimension;

import com.robinbird.pocketlib.shared.location.Location;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;

/**
 *
 * @author Robijnvogel
 */
public class TileEntityPocket extends TileEntity {

    private int ID;
    private String modName;
    private String dimName;
    private int xCoord;
    private int zCoord;
    private int xSize;
    private int zSize;
    private final List<String> allowedPlayers; //list of UUIDs of players allowed to teleport into- and be in- this Pocket

    public TileEntityPocket(String modName, String dimName, int x, int z, int xSize, int zSize) {
        this.modName = modName;
        this.dimName = dimName;
        this.xCoord = x;
        this.zCoord = z;
        this.xSize = xSize;
        this.zSize = zSize;
        allowedPlayers = new ArrayList();
    }

    public int getID() {
        return ID;
    }

    public int getX() {
        return xCoord;
    }

    public int getZ() {
        return zCoord;
    }

    public void setID(int newID) {
        ID = newID;
    }

    @Override
    public void readFromNBT(NBTTagCompound pocketNBT) {
        ID = pocketNBT.getInteger("ID");
        modName = pocketNBT.getString("modName");
        dimName = pocketNBT.getString("dimName");
        xCoord = pocketNBT.getInteger("xCoord");
        zCoord = pocketNBT.getInteger("zCoord");
        xSize = pocketNBT.getInteger("xSize");
        zSize = pocketNBT.getInteger("zSize");

        NBTTagList playersTagList = (NBTTagList) pocketNBT.getTag("playerUUIDs");
        for (int i = 0; i < playersTagList.tagCount(); i++) {
            String playerUUID = playersTagList.getStringTagAt(i);
            allowedPlayers.add(playerUUID);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound pocketNBT) {
        pocketNBT.setInteger("ID", ID);
        pocketNBT.setString("modName", modName);
        pocketNBT.setString("dimName", dimName);
        pocketNBT.setInteger("xCoord", xCoord);
        pocketNBT.setInteger("zCoord", zCoord);
        pocketNBT.setInteger("xSize", xSize);
        pocketNBT.setInteger("zSize", zSize);

        NBTTagList playersTagList = new NBTTagList();
        for (String UUID : allowedPlayers) {
            NBTTagString playerTag = new NBTTagString(UUID);
            playersTagList.appendTag(playerTag);
        }
        pocketNBT.setTag("playerUUIDs", playersTagList);
        return pocketNBT;
    }

    public void validatePlayerEntry(EntityPlayer player) {
        String playerUUID = player.getCachedUniqueIdString();
        if (!allowedPlayers.contains(playerUUID)) { //the 'contains' method uses the 'equals' method to check, so for Strings, this should work.
            allowedPlayers.add(playerUUID);
        }
    }

    public boolean isPlayerAllowedInPocket(EntityPlayer player) {
        String playerUUID = player.getCachedUniqueIdString();
        return allowedPlayers.contains(playerUUID);
    }

    boolean isLocationWithinPocketBounds(final Location location, final int gridSize) {
        int locX = location.getPos().getX();
        int locZ = location.getPos().getY();
        //minimum bounds of the pocket
        int pocX = xCoord;
        int pocZ = zCoord;
        if (pocX <= locX && pocZ <= locZ) {
            //convert to maximum bounds of the pocket
            pocX += xSize;
            pocZ += zSize;
            if (locX < pocX && locZ < pocZ) {
                return true;
            }
        }
        return false;
    }
}
