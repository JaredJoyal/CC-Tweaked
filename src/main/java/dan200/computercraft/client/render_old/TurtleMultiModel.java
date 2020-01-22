/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2020. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.vecmath.Matrix4f;
import java.util.*;

public class TurtleMultiModel implements IBakedModel
{
    private final IBakedModel m_baseModel;
    private final IBakedModel m_overlayModel;
    private final TransformationMatrix m_generalTransform;
    private final IBakedModel m_leftUpgradeModel;
    private final TransformationMatrix m_leftUpgradeTransform;
    private final IBakedModel m_rightUpgradeModel;
    private final TransformationMatrix m_rightUpgradeTransform;
    private List<BakedQuad> m_generalQuads = null;
    private Map<Direction, List<BakedQuad>> m_faceQuads = new EnumMap<>( Direction.class );

    public TurtleMultiModel(
        IBakedModel baseModel, IBakedModel overlayModel, TransformationMatrix generalTransform,
        IBakedModel leftUpgradeModel, TransformationMatrix leftUpgradeTransform,
        IBakedModel rightUpgradeModel, TransformationMatrix rightUpgradeTransform
    )
    {
        // Get the models
        m_baseModel = baseModel;
        m_overlayModel = overlayModel;
        m_leftUpgradeModel = leftUpgradeModel;
        m_leftUpgradeTransform = leftUpgradeTransform;
        m_rightUpgradeModel = rightUpgradeModel;
        m_rightUpgradeTransform = rightUpgradeTransform;
        m_generalTransform = generalTransform;
    }

    @Nonnull
    @Override
    @Deprecated
    public List<BakedQuad> getQuads( BlockState state, Direction side, @Nonnull Random rand )
    {
        return getQuads( state, side, rand, EmptyModelData.INSTANCE );
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads( BlockState state, Direction side, @Nonnull Random rand, @Nonnull IModelData data )
    {
        if( side != null )
        {
            if( !m_faceQuads.containsKey( side ) ) m_faceQuads.put( side, buildQuads( state, side, rand ) );
            return m_faceQuads.get( side );
        }
        else
        {
            if( m_generalQuads == null ) m_generalQuads = buildQuads( state, side, rand );
            return m_generalQuads;
        }
    }

    private List<BakedQuad> buildQuads( BlockState state, Direction side, Random rand )
    {
        ArrayList<BakedQuad> quads = new ArrayList<>();
        ModelTransformer.transformQuadsTo( quads, m_baseModel.getQuads( state, side, rand, EmptyModelData.INSTANCE ), m_generalTransform );
        if( m_overlayModel != null )
        {
            ModelTransformer.transformQuadsTo( quads, m_overlayModel.getQuads( state, side, rand, EmptyModelData.INSTANCE ), m_generalTransform );
        }
        if( m_leftUpgradeModel != null )
        {
            TransformationMatrix upgradeTransform = m_generalTransform;
            if( m_leftUpgradeTransform != null )
            {
                upgradeTransform = new TransformationMatrix( m_generalTransform );
                upgradeTransform.mul( m_leftUpgradeTransform );
            }
            ModelTransformer.transformQuadsTo( quads, m_leftUpgradeModel.getQuads( state, side, rand, EmptyModelData.INSTANCE ), upgradeTransform );
        }
        if( m_rightUpgradeModel != null )
        {
            TransformationMatrix upgradeTransform = m_generalTransform;
            if( m_rightUpgradeTransform != null )
            {
                upgradeTransform = new Matrix4f( m_generalTransform );
                upgradeTransform.mul( m_rightUpgradeTransform );
            }
            ModelTransformer.transformQuadsTo( quads, m_rightUpgradeModel.getQuads( state, side, rand, EmptyModelData.INSTANCE ), upgradeTransform );
        }
        quads.trimToSize();
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return m_baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return m_baseModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return m_baseModel.isBuiltInRenderer();
    }

    @Nonnull
    @Override
    @Deprecated
    public TextureAtlasSprite getParticleTexture()
    {
        return m_baseModel.getParticleTexture();
    }

    @Nonnull
    @Override
    @Deprecated
    public net.minecraft.client.renderer.model.ItemCameraTransforms getItemCameraTransforms()
    {
        return m_baseModel.getItemCameraTransforms();
    }

    @Nonnull
    @Override
    public ItemOverrideList getOverrides()
    {
        return ItemOverrideList.EMPTY;
    }
}
