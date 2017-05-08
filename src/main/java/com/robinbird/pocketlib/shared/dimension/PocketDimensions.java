package com.robinbird.pocketlib.shared.dimension;

import akka.japi.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public class PocketDimensions {

    private static final Map<String, Map<String, Pair<PocketDimEntry, DimensionType>>> POCKETDIMENSIONS = new HashMap<>();

    //should be called on dependent mods on pre-init to register their pocket dimensions on init
    public static void addPocketDimension(String modID, PocketDimEntry pDEntry) {
        Pair<PocketDimEntry, DimensionType> pair = new Pair(pDEntry, null);
        Map<String, Pair<PocketDimEntry, DimensionType>> modmap;
        if (POCKETDIMENSIONS.containsKey(modID)) {
            modmap = POCKETDIMENSIONS.get(modID);
        } else {
            modmap = new HashMap();
            POCKETDIMENSIONS.put(modID, modmap);
        }
        modmap.put(pDEntry.upperCaseName, pair);
    }

    public static void init() {
        for (String modID : POCKETDIMENSIONS.keySet()) {
            for (String dimName : POCKETDIMENSIONS.get(modID).keySet()) {
                Pair<PocketDimEntry, DimensionType> pair = POCKETDIMENSIONS.get(modID).get(dimName);
                PocketDimEntry pDEntry = pair.first();
                //register
                DimensionType dimType = DimensionType.register(pDEntry.upperCaseName, pDEntry.lowerCaseExtension, pDEntry.dimID, pDEntry.world.getClass(), false);
                registerDimension(dimType);

                pair = new Pair(pair.first(), dimType);
                POCKETDIMENSIONS.get(modID).put(dimName, pair);
            }
        }
    }

    public static void registerDimension(DimensionType dimension) {
        DimensionManager.registerDimension(dimension.getId(), dimension);
    }

    public static List<Integer> getPocketDimIDs() {
        List<Integer> ids = new ArrayList();

        for (String modID : POCKETDIMENSIONS.keySet()) {
            for (String dimName : POCKETDIMENSIONS.get(modID).keySet()) {
                Pair<PocketDimEntry, DimensionType> pair = POCKETDIMENSIONS.get(modID).get(dimName);
                PocketDimEntry pDEntry = pair.first();
                ids.add(pDEntry.dimID);
            }
        }
        return ids;
    }

    public static boolean isPocketDimensionID(int id) {
        return getPocketDimIDs().contains((Integer) id);
    }
    
    private static Pair<PocketDimEntry, DimensionType> getInfo(String modID, String dimName) {
        return POCKETDIMENSIONS.get(modID).get(dimName);
    }
    
    public static int getDimID(String modID, String dimName) {
        return getInfo(modID, dimName).first().dimID;
    }
    
    public static String getUpperName(String modID, String dimName) {
        return getInfo(modID, dimName).first().upperCaseName;
    }
    
    public static WorldProvider getProvider(String modID, String dimName) {
        return getInfo(modID, dimName).first().world;
    }
    
    public static DimensionType getDimType(String modID, String dimName) {
        return getInfo(modID, dimName).second();
    }
}
