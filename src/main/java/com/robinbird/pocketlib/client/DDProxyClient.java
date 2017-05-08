package com.robinbird.pocketlib.client;

import com.robinbird.pocketlib.shared.PLCommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings({"MethodCallSideOnly", "NewExpressionSideOnly"})
public class DDProxyClient extends PLCommonProxy {

    @Override
    public void onPreInitialization(FMLPreInitializationEvent event) {
        super.onPreInitialization(event);

        ModelManager.registerModelVariants();
        ModelManager.addCustomStateMappers();
    }

    @Override
    public void onInitialization(FMLInitializationEvent event) {
        super.onInitialization(event);
        ModelManager.registerModels();
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public EntityPlayer getLocalPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public World getDefWorld() {
        return getWorldServer(0); //gets the client world dim 0 handler
    }

    @Override
    public WorldServer getWorldServer(int dimId) {
        return Minecraft.getMinecraft().getIntegratedServer().worldServerForDimension(dimId);
    }
}
