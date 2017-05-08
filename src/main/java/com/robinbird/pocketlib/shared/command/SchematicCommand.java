package com.robinbird.pocketlib.shared.command;

import com.robinbird.pocketlib.PocketLib;
import com.robinbird.pocketlib.shared.dimension.PocketDimensions;
import com.robinbird.pocketlib.shared.location.Location;
import com.robinbird.pocketlib.shared.util.PLStringUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SchematicCommand extends CommandBase {

    private final List<String> aliases;
    private final List<String> subCommands;

    public SchematicCommand() {
        aliases = new ArrayList<>();
        aliases.add("plSchematic");
        
        subCommands = new ArrayList<>();        
        subCommands.add("place");
        subCommands.add("save");
    }

    @Override
    public String getName() {
        return "plSchematic";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "plSchematic <save|place> <name>";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            if (areArgumentsValid(args, player)) {
                PocketLib.chat(player, "Executing subcommand " + args[0]);

                BlockPos pos = player.getPosition();
                World world = player.world;
                Location origLoc = new Location(world, pos);

                int dimID = origLoc.getDimensionID();
                if (PocketDimensions.isPocketDimensionID(dimID)) {
                    int pocketID = PocketRegistry.INSTANCE.getPocketIDFromCoords(origLoc);
                    EnumPocketType type = DimDoorDimensions.getPocketType(dimID);
                    Pocket oldPocket = PocketRegistry.INSTANCE.getPocket(pocketID, type);
                    origLoc = oldPocket.getDepthZeroLocation();
                }

                PocketTemplate template = SchematicHandler.INSTANCE.getDungeonTemplate(args[0], args[1]);
                Pocket pocket = PocketRegistry.INSTANCE.generatePocketAt(EnumPocketType.DUNGEON, 1, origLoc, template);
                int entranceDoorID = pocket.getEntranceDoorID();
                RiftRegistry.INSTANCE.setLastGeneratedEntranceDoorID(entranceDoorID);
            }
        } else {
            PocketLib.log(this.getClass(), "Not executing command, because it wasn't sent by a player.");
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        List<String> list = new ArrayList();
        if (args == null || args.length < 2) { //counts an empty ("") argument as an argument as well...
            list = PLStringUtils.getMatchingStrings(args[0], subCommands, false);
        } else if (args.length == 2) {
            list = SchematicHandler.INSTANCE.getDungeonTemplateNames();
            list = PLStringUtils.getMatchingStrings(args[1], list, false);
        } else if (args.length == 3) {
            list.add("Remove_this");
        } else {
            list.add("No_seriously");
        }
        return list;
    }

    private boolean areArgumentsValid(String[] args, EntityPlayerMP player) {
        if (args.length < 2) {
            PocketLib.chat(player, "Too few arguments.");
            return false;
        } else if (args.length > 2) {
            PocketLib.chat(player, "Too many arguments.");
            return false;
        } else { //exactly 2 arguments
            if (!subCommands.contains(args[0])) {
                PocketLib.chat(player, "Subcommand '" + args[0] + "' not found.");
                return false;
            } else if (!SchematicHandler.INSTANCE.getDungeonTemplateNames().contains(args[1])) {
                PocketLib.chat(player, "Schematic not found.");
                return false;
            } else {
                PocketLib.log(this.getClass(), "Command is valid.");
                return true;
            }
        }
    }
}
