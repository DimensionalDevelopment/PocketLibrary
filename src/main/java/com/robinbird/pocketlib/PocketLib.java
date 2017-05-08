package com.robinbird.pocketlib;

import com.robinbird.pocketlib.shared.PLCommonProxy;
import com.robinbird.pocketlib.shared.PLConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import java.util.List;

@Mod(modid = PocketLib.MODID, name = "Pocket Library", version = PocketLib.VERSION, dependencies = "required-after:Forge@[12.18.3.2281)")
public class PocketLib {

    public static final String VERSION = "${version}";
    public static final String MODID = "pocketlib";

    @SidedProxy(clientSide = "com.robinbird.pocketlib.client.PLClientProxy",
            serverSide = "com.robinbird.pocketlib.shared.PLCommonProxy")
    private static PLCommonProxy proxy;

    @Mod.Instance(PocketLib.MODID)
    public static PocketLib instance;

    @Mod.EventHandler
    public void onPreInitialization(FMLPreInitializationEvent event) {
        proxy.onPreInitialization(event);
        PLConfig.loadConfig(event);
    }

    @Mod.EventHandler
    public void onInitialization(FMLInitializationEvent event) {
        proxy.onInitialization(event);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        registerCommands(event);
        PocketRegistry.INSTANCE.reset();
    }

    private void registerCommands(FMLServerStartingEvent event) {
        event.registerServerCommand(new TeleportCommand());
        event.registerServerCommand(new PocketCommand());
    }

    public static boolean isClient() {
        return proxy.isClient();
    }

    public static boolean isServer() {
        return !isClient();
    }

    public static World getDefWorld() {
        return proxy.getDefWorld(); //gets the server or client world dim 0 handler
    }

    /**
     *
     * @param dimID the dimension ID of the World to obtain
     * @return the World with dimension ID {@code dimID}
     */
    public static World getWorld(int dimID) {
        return proxy.getWorldServer(dimID);
    }

    public static void chat(EntityPlayer player, String text) {
        player.sendMessage(new TextComponentString("[DimDoors] " + text));
    }

    public static void warn(Class classFiredFrom, String text) {
        FMLLog.warning("[DimDoors] " + text + " (" + classFiredFrom.toString() + " )", 0);
    }

    public static void log(Class classFiredFrom, String text) {
        FMLLog.info("[DimDoors] " + text + " (" + classFiredFrom.toString() + " )", 0);
    }

    public static void translateAndAdd(String key, List<String> list) {
        for (int i = 0; i < 10; i++) {
            if (I18n.canTranslate(key + Integer.toString(i))) {
                String line = I18n.translateToLocal(key + Integer.toString(i));
                list.add(line);
            } else {
                break;
            }
        }
    }
}
