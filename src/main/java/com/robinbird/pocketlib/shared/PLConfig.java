/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robinbird.pocketlib.shared;

import java.io.File;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 *
 * @author Robijnvogel
 */
public class PLConfig {

    public static File configurationFolder;
    private static int pocketGridSize = 32;

    public static int setConfigIntWithMaxAndMin(Configuration config, String category, String key, int defaultValue, String comment, int minValue, int maxValue) {
        Property prop = config.get(category, key, defaultValue,
                comment, minValue, maxValue);
        int value = prop.getInt(defaultValue);
        if (value < minValue) {
            value = minValue;
        } else if (value > maxValue) {
            value = maxValue;
        }
        prop.set(value);
        return value;
    }

    public static void loadConfig(FMLPreInitializationEvent event) {

        // Load config
        configurationFolder = new File(event.getModConfigurationDirectory(), "/PocketLib");
        if (!configurationFolder.exists()) {
            configurationFolder.mkdirs();
        }
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        // Setup general
        //@todo a comment in the config files about how these values only influence new worlds
        Property prop;
        pocketGridSize = setConfigIntWithMaxAndMin(config, Configuration.CATEGORY_GENERAL, "pocketGridSize", pocketGridSize,
                "Sets how many chunks apart all pockets in pocket dimensions should be placed. [min: 4, max: 32, default: 32]", 4, 32);

        // Save config
        config.save();
    }

    public static int getPocketGridSize() {
        return pocketGridSize;
    }
}
