/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2020. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import dan200.computercraft.api.AbstractTurtleUpgrade;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

public class TurtleCraftingTable extends AbstractTurtleUpgrade
{
    @OnlyIn( Dist.CLIENT )
    private ModelResourceLocation m_leftModel;

    @OnlyIn( Dist.CLIENT )
    private ModelResourceLocation m_rightModel;

    public TurtleCraftingTable( ResourceLocation id )
    {
        super( id, TurtleUpgradeType.Peripheral, Blocks.CRAFTING_TABLE );
    }

    @Override
    public IPeripheral createPeripheral( @Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side )
    {
        return new CraftingTablePeripheral( turtle );
    }

    @OnlyIn( Dist.CLIENT )
    private void loadModelLocations()
    {
        if( m_leftModel == null )
        {
            m_leftModel = new ModelResourceLocation( "computercraft:turtle_crafting_table_left", "inventory" );
            m_rightModel = new ModelResourceLocation( "computercraft:turtle_crafting_table_right", "inventory" );
        }
    }

    @Nonnull
    @Override
    @OnlyIn( Dist.CLIENT )
    public Pair<IBakedModel, TransformationMatrix> getModel( ITurtleAccess turtle, @Nonnull TurtleSide side )
    {
        loadModelLocations();

        ModelManager modelManager = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getModelManager();
        return Pair.of( modelManager.getModel( side == TurtleSide.Left ? m_leftModel : m_rightModel ), null );
    }
}
