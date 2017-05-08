/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robinbird.pocketlib.shared.dimension;

import net.minecraft.world.WorldProvider;

/**
 *
 * @author Robijnvogel
 */
public class PocketDimEntry {

    public final WorldProvider world;
    public final int dimID;
    public final String upperCaseName;
    public final String lowerCaseExtension;

    public PocketDimEntry(WorldProvider world, int dimID, String upperCaseName, String lowerCaseExtension) {
        this.world = world;
        this.dimID = dimID;
        this.upperCaseName = upperCaseName;
        this.lowerCaseExtension = lowerCaseExtension;
    }
}
