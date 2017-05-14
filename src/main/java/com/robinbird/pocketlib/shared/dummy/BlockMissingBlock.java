/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robinbird.pocketlib.shared.dummy;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 *
 * @author Robijnvogel
 */
public class BlockMissingBlock extends Block implements ITileEntityProvider {
    
    public BlockMissingBlock() {
        super(Material.WOOD);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMissingBlock();
    }
}
