/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robinbird.pocketlib.shared.schematic;

import com.robinbird.pocketlib.PocketLib;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 *
 * @author Robijnvogel
 */
public class Schematic {

    int version = Integer.parseInt("1"); //@todo set in build.gradle ${spongeSchematicVersion}
    String author = PocketLib.MODID;
    String schematicName = "Unknown";
    long creationDate;
    String[] requiredMods = new String[]{PocketLib.MODID};
    short width;
    short height;
    short length;
    int[] offset = new int[]{0, 0, 0};
    int paletteMax;
    List<IBlockState> pallette = new ArrayList();
    int[][][] blockData; //[x][y][z]
    List<NBTTagCompound> tileEntities = new ArrayList();
    List<NBTTagCompound> entities = new ArrayList();

    private Schematic() {
    }

    public static Schematic loadFromNBT(NBTTagCompound nbt, String parName) {
        //loading version
        if (!nbt.hasKey("Version")) {
            PocketLib.warn(Schematic.class, "Schematic nbt lacks the attribute 'Version' and therefore isn't getting loaded.");
            return null;
        }
        int version = nbt.getInteger("Version"); //Version is required
        Schematic schematic = new Schematic();
        if (version > schematic.version) {
            PocketLib.warn(Schematic.class, "Schematic 'Version' seems to be newer than this mod's load function.");
        }
        schematic.version = version;

        //loading metadata
        schematic.creationDate = System.currentTimeMillis();
        if (nbt.hasKey("Metadata")) { //Metadata is not required
            NBTTagCompound metadataCompound = nbt.getCompoundTag("Metadata").getCompoundTag(".");
            if (nbt.hasKey("Author")) { //Author is not required
                schematic.author = metadataCompound.getString("Author");
            }
            //Name is not required (may be null)
            schematic.schematicName = (parName == null || parName.equals("")) && metadataCompound.hasKey("Name") ? metadataCompound.getString("Name") : parName;

            if (nbt.hasKey("Date")) { //Date is not required
                schematic.creationDate = metadataCompound.getLong("Date");
            }
            if (nbt.hasKey("RequiredMods")) { //RequiredMods is not required (ironically)
                NBTTagList requiredModsTagList = ((NBTTagList) metadataCompound.getTag("RequiredMods"));
                schematic.requiredMods = new String[requiredModsTagList.tagCount()];
                for (int i = 0; i < requiredModsTagList.tagCount(); i++) {
                    String modString = requiredModsTagList.getStringTagAt(i);
                    schematic.requiredMods[i] = modString;
                    if (!Loader.isModLoaded(modString)) {
                        PocketLib.warn(Schematic.class, "The mod " + modString + " is required to load this Schematic, yet it does not seem to be installed. This may cause problems during placement.");
                    }
                }
            }
        }
        
        //loading spacial parameters
        schematic.width = nbt.getShort("Width"); //Width is required
        schematic.height = nbt.getShort("Height"); //Height is required
        schematic.length = nbt.getShort("Length"); //Length is required
        if (nbt.hasKey("Offset")) { //Offset is not required
            schematic.offset = nbt.getIntArray("Offset");
        }

        //loading palette
        NBTTagCompound paletteNBT = nbt.getCompoundTag("Palette"); //Palette is not required, however since we assume that the schematic contains at least some blocks, we can also assume that thee has to be a Palette
        Map<Integer, String> paletteMap = new HashMap();
        for (String key : paletteNBT.getKeySet()) {
            int paletteID = paletteNBT.getInteger(key);
            paletteMap.put(paletteID, key); //basically use the reversed order (key becomes value and value becomes key)
        }
        for (int i = 0; i < paletteMap.size(); i++) {
            String blockStateString = paletteMap.get(i);
            char lastBlockStateStringChar = blockStateString.charAt(blockStateString.length() - 1);
            String blockString;
            String stateString;
            if (lastBlockStateStringChar == ']') {
                String[] blockAndStateStrings = blockStateString.split("\\[");
                blockString = blockAndStateStrings[0];
                stateString = blockAndStateStrings[1];
                stateString = stateString.substring(0, stateString.length() - 1); //remove the "]" at the end
            } else {
                blockString = blockStateString;
                stateString = "";
            }
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockString));

            IBlockState blockstate = block.getDefaultState();
            if (!stateString.equals("")) {
                String[] properties = stateString.split(",");
                blockstate = getBlockStateWithProperties(block, properties);
            } else {
            }
            schematic.pallette.add(blockstate); //@todo, can we assume that a schematic file always has all palette integers used from 0 to pallettemax-1?
        }
        if (nbt.hasKey("PaletteMax")) { //PaletteMax is not required
            schematic.paletteMax = nbt.getInteger("PaletteMax");
        } else {
            schematic.paletteMax = schematic.pallette.size() - 1;
        }

        byte[] blockDataIntArray = nbt.getByteArray("BlockData"); //BlockData is required
        schematic.blockData = new int[schematic.width][schematic.height][schematic.length];
        for (int x = 0; x < schematic.width; x++) {
            for (int y = 0; y < schematic.height; y++) {
                for (int z = 0; z < schematic.length; z++) {
                    schematic.blockData[x][y][z] = blockDataIntArray[x + z * schematic.width + y * schematic.width * schematic.length]; //according to the documentation on https://github.com/SpongePowered/Schematic-Specification/blob/master/versions/schematic-1.md
                }
            }
        }

        if (nbt.hasKey("TileEntities")) { //TileEntities is not required
            NBTTagList tileEntitiesTagList = (NBTTagList) nbt.getTag("TileEntities");
            for (int i = 0; i < tileEntitiesTagList.tagCount(); i++) {
                NBTTagCompound tileEntityTagCompound = tileEntitiesTagList.getCompoundTagAt(i);
                schematic.tileEntities.add(tileEntityTagCompound);
            }
        }

        return schematic;
    }

    public static NBTTagCompound saveToNBT(Schematic schematic) {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setInteger("Version", schematic.version);
        NBTTagCompound metadataCompound = new NBTTagCompound();
        metadataCompound.setString("Author", schematic.author);
        metadataCompound.setString("Name", schematic.schematicName);
        metadataCompound.setLong("Date", schematic.creationDate);
        NBTTagList requiredModsTagList = new NBTTagList();
        for (String requiredMod : schematic.requiredMods) {
            requiredModsTagList.appendTag(new NBTTagString(requiredMod));
        }
        metadataCompound.setTag("RequiredMods", requiredModsTagList);
        nbt.setTag("Metadata", metadataCompound);

        nbt.setShort("Width", schematic.width);
        nbt.setShort("Height", schematic.height);
        nbt.setShort("Length", schematic.length);
        nbt.setIntArray("Offset", schematic.offset);
        nbt.setInteger("PaletteMax", schematic.paletteMax);

        NBTTagCompound paletteNBT = new NBTTagCompound();
        Map<Integer, String> paletteMap = new HashMap();
        for (int i = 0; i < schematic.pallette.size(); i++) {
            IBlockState state = schematic.pallette.get(i);
            String blockStateString = getBlockStateStringFromState(state);
            paletteNBT.setInteger(blockStateString, i);
        }
        nbt.setTag("Palette", paletteNBT);

        byte[] blockDataIntArray = new byte[schematic.width * schematic.height * schematic.length];
        for (int x = 0; x < schematic.width; x++) {
            for (int y = 0; y < schematic.height; y++) {
                for (int z = 0; z < schematic.length; z++) {
                    blockDataIntArray[x + z * schematic.width + y * schematic.width * schematic.length] = (byte) schematic.blockData[x][y][z]; //according to the documentation on https://github.com/SpongePowered/Schematic-Specification/blob/master/versions/schematic-1.md
                }
            }
        }
        nbt.setByteArray("BlockData", blockDataIntArray);

        NBTTagList tileEntitiesTagList = new NBTTagList();
        for (int i = 0; i < schematic.tileEntities.size(); i++) {
            NBTTagCompound tileEntityTagCompound = schematic.tileEntities.get(i);
            tileEntitiesTagList.appendTag(tileEntityTagCompound);
        }
        nbt.setTag("TileEntities", tileEntitiesTagList);

        return nbt;
    }

    private static IBlockState getBlockStateWithProperties(Block block, String[] properties) {
        Map<String, String> propertyAndBlockStringsMap = new HashMap();
        for (int i = 0; i < properties.length; i++) {
            String propertyString = properties[i];
            String[] propertyAndBlockStrings = propertyString.split("=");
            propertyAndBlockStringsMap.put(propertyAndBlockStrings[0], propertyAndBlockStrings[1]);
        }
        BlockStateContainer container = block.getBlockState();
        IBlockState chosenState = block.getDefaultState();
        for (Entry<String, String> entry : propertyAndBlockStringsMap.entrySet()) {
            IProperty<?> property = container.getProperty(entry.getKey());
            if (property != null) {
                Comparable<?> value = null;
                for (Comparable<?> object : property.getAllowedValues()) {
                    if (object.toString().equals(entry.getValue())) {
                        value = object;
                        break;
                    }
                }
                if (value != null) {
                    chosenState = chosenState.withProperty((IProperty) property, (Comparable) value);
                }
            }
        }
        return chosenState;
    }

    private static String getBlockStateStringFromState(IBlockState state) {
        Block block = state.getBlock();
        String blockNameString = "" + Block.REGISTRY.getNameForObject(block);
        String blockStateString = "";
        String totalString;
        IBlockState defaultState = block.getDefaultState();
        if (state == defaultState) {
            totalString = blockNameString;
        } else { //there is at least one property not equal to the default state's property
            BlockStateContainer container = block.getBlockState();
            for (IProperty property : container.getProperties()) { //for every property that is valid for this type of Block
                String defaultPropertyValue = defaultState.getProperties().get(property).toString();
                String thisPropertyValue = state.getProperties().get(property).toString();
                if (defaultPropertyValue.equals(thisPropertyValue)) {
                    //do nothing
                } else {
                    String firstHalf = property.getName();
                    String secondHalf = state.getProperties().get(property).toString();
                    String propertyString = firstHalf + "=" + secondHalf;
                    blockStateString += propertyString + ",";
                }
            }
            blockStateString = blockStateString.substring(0, blockStateString.length() - 1); //removes the last comma
            totalString = blockNameString + "[" + blockStateString + "]";
        }
        return totalString;
    }

    public int getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public String getSchematicName() {
        return schematicName;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public String[] getRequiredMods() {
        return requiredMods;
    }

    public short getWidth() {
        return width;
    }

    public short getHeight() {
        return height;
    }

    public short getLength() {
        return length;
    }

    public int[] getOffset() {
        return offset;
    }

    public int getPaletteMax() {
        return paletteMax;
    }

    public List<IBlockState> getPallette() {
        return pallette;
    }

    public int[][][] getBlockData() {
        return blockData;
    }

    public List<NBTTagCompound> getTileEntities() {
        return tileEntities;
    }
}
