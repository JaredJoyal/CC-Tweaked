/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2020. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.computercraft.api.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * A model to render, combined with a transformation matrix to apply.
 */
public final class TransformedModel
{
    private final IBakedModel model;
    private final TransformationMatrix matrix;

    public TransformedModel( @Nonnull IBakedModel model, @Nonnull TransformationMatrix matrix )
    {
        this.model = Objects.requireNonNull( model );
        this.matrix = Objects.requireNonNull( matrix );
    }

    public TransformedModel( @Nonnull IBakedModel model )
    {
        this.model = Objects.requireNonNull( model );
        this.matrix = TransformationMatrix.func_227983_a_();
    }

    public static TransformedModel of( @Nonnull ModelResourceLocation location )
    {
        ModelManager modelManager = Minecraft.getInstance().getModelManager();
        return new TransformedModel( modelManager.getModel( location ) );
    }

    public static TransformedModel of( @Nonnull ItemStack item, @Nonnull TransformationMatrix transform )
    {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel( item );
        return new TransformedModel( model, transform );
    }

    @Nonnull
    public IBakedModel getModel()
    {
        return model;
    }

    @Nonnull
    public TransformationMatrix getMatrix()
    {
        return matrix;
    }
}
