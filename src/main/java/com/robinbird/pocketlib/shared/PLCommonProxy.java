package com.robinbird.pocketlib.shared;

import com.robinbird.pocketlib.shared.dimension.TileEntityPocket;
import com.robinbird.pocketlib.shared.dimension.PocketDimensions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class PLCommonProxy implements IPLProxy {

    @Override
    public void onPreInitialization(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PLEventHandler());
        PocketDimensions.init();
        ModBlocks.registerBlocks();
        ModItems.registerItems();

        GameRegistry.registerTileEntity(TileEntityPocket.class, "TileEntityPocket");
        GameRegistry.registerTileEntity(TileEntityRift.class, "TileEntityRift");

    }

    @Override
    public void onInitialization(FMLInitializationEvent event) {
        EntityRegistry.registerModEntity(MobMonolith.class, "Monolith", 0, DimDoors.instance, 70, 1, true);
        EntityRegistry.registerEgg(MobMonolith.class, 0, 0xffffff);
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public EntityPlayer getLocalPlayer() {
        return null;
    }

    @Override
    public World getDefWorld() {
        return getWorldServer(0); //gets the server world dim 0 handler
    }

    @Override
    public WorldServer getWorldServer(int dimId) {
        return DimensionManager.getWorld(dimId);
    }

}
