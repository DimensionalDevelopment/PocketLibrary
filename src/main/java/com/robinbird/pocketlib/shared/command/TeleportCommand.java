package com.robinbird.pocketlib.shared.command;

import com.robinbird.pocketlib.shared.location.TeleporterPocketLib;
import com.robinbird.pocketlib.shared.util.PLStringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;

public class TeleportCommand extends CommandBase {

    private final List<String> aliases;

    public TeleportCommand() {
        aliases = new ArrayList<>();

        aliases.add("plDimTp");
    }

    @Override
    public String getName() {
        return "plDimTp";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "plDimTp <dimension>";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        int id = Integer.parseInt(args[0]);

        if (sender instanceof EntityPlayerMP) {
            server.getPlayerList().transferPlayerToDimension((EntityPlayerMP) sender, id, TeleporterPocketLib.instance());
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        List<String> list = new ArrayList();
        if (args == null || args.length < 2) { //counts an empty ("") argument as an argument as well...
            list = PLStringUtils.getAsStringList(DimensionManager.getIDs());
            list = PLStringUtils.getMatchingStrings(args[0], list, false);
        }
        return list;
    }
}
