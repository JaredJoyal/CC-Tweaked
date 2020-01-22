/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2020. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.peripheral.modem.wired.BlockCable;
import dan200.computercraft.shared.peripheral.modem.wired.CableShapes;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber( modid = ComputerCraft.MOD_ID, value = Dist.CLIENT )
public final class CableHighlightRenderer
{
    private CableHighlightRenderer()
    {
    }

    /**
     * Draw an outline for a specific part of a cable "Multipart".
     *
     * @param event The event to observe
     * @see WorldRenderer#drawSelectionBox(ActiveRenderInfo, RayTraceResult, int)
     */
    @SubscribeEvent
    public static void drawHighlight( DrawHighlightEvent.HighlightBlock event )
    {
        BlockRayTraceResult hit = event.getTarget();
        BlockPos pos = hit.getPos();
        World world = event.getInfo().getRenderViewEntity().getEntityWorld();
        ActiveRenderInfo info = event.getInfo();

        BlockState state = world.getBlockState( pos );

        // We only care about instances with both cable and modem.
        if( state.getBlock() != ComputerCraft.Blocks.cable || state.get( BlockCable.MODEM ).getFacing() == null || !state.get( BlockCable.CABLE ) )
        {
            return;
        }

        event.setCanceled( true );

        Minecraft mc = Minecraft.getInstance();

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );
        RenderSystem.lineWidth( Math.max( 2.5F, mc.getMainWindow().getFramebufferWidth() / 1920.0F * 2.5F ) );
        RenderSystem.disableTexture();
        RenderSystem.depthMask( false );
        RenderSystem.matrixMode( GL11.GL_PROJECTION );
        RenderSystem.pushMatrix();
        RenderSystem.scalef( 1.0F, 1.0F, 0.999F );

        VoxelShape shape = WorldUtil.isVecInside( CableShapes.getModemShape( state ), hit.getHitVec().subtract( pos.getX(), pos.getY(), pos.getZ() ) )
            ? CableShapes.getModemShape( state )
            : CableShapes.getCableShape( state );

        Vec3d cameraPos = info.getProjectedView();
        WorldRenderer.drawShape(
            shape, pos.getX() - cameraPos.getX(), pos.getY() - cameraPos.getY(), pos.getZ() - cameraPos.getZ(),
            0.0F, 0.0F, 0.0F, 0.4F
        );

        RenderSystem.popMatrix();
        RenderSystem.matrixMode( GL11.GL_MODELVIEW );
        RenderSystem.depthMask( true );
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
