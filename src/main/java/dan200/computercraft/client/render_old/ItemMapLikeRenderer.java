/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2020. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public abstract class ItemMapLikeRenderer
{
    /**
     * The main rendering method for the item.
     *
     * @param stack The stack to render
     * @see FirstPersonRenderer#renderMapFirstPerson(ItemStack)
     */
    protected abstract void renderItem( ItemStack stack );

    protected void renderItemFirstPerson( Hand hand, float pitch, float equipProgress, float swingProgress, ItemStack stack )
    {
        PlayerEntity player = Minecraft.getInstance().player;

        RenderSystem.pushMatrix();
        if( hand == Hand.MAIN_HAND && player.getHeldItemOffhand().isEmpty() )
        {
            renderItemFirstPersonCenter( pitch, equipProgress, swingProgress, stack );
        }
        else
        {
            renderItemFirstPersonSide(
                hand == Hand.MAIN_HAND ? player.getPrimaryHand() : player.getPrimaryHand().opposite(),
                equipProgress, swingProgress, stack
            );
        }
        RenderSystem.popMatrix();
    }

    /**
     * Renders the item to one side of the player.
     *
     * @param side          The side to render on
     * @param equipProgress The equip progress of this item
     * @param swingProgress The swing progress of this item
     * @param stack         The stack to render
     * @see FirstPersonRenderer#renderMapFirstPersonSide(float, HandSide, float, ItemStack)
     */
    private void renderItemFirstPersonSide( HandSide side, float equipProgress, float swingProgress, ItemStack stack )
    {
        Minecraft minecraft = Minecraft.getInstance();
        float offset = side == HandSide.RIGHT ? 1f : -1f;
        RenderSystem.translatef( offset * 0.125f, -0.125f, 0f );

        // If the player is not invisible then render a single arm
        if( !minecraft.player.isInvisible() )
        {
            RenderSystem.pushMatrix();
            RenderSystem.rotatef( offset * 10f, 0f, 0f, 1f );
            minecraft.getFirstPersonRenderer().renderArmFirstPerson( equipProgress, swingProgress, side );
            RenderSystem.popMatrix();
        }

        // Setup the appropriate transformations. This is just copied from the
        // corresponding method in ItemRenderer.
        RenderSystem.pushMatrix();
        RenderSystem.translatef( offset * 0.51f, -0.08f + equipProgress * -1.2f, -0.75f );
        float f1 = MathHelper.sqrt( swingProgress );
        float f2 = MathHelper.sin( f1 * (float) Math.PI );
        float f3 = -0.5f * f2;
        float f4 = 0.4f * MathHelper.sin( f1 * ((float) Math.PI * 2f) );
        float f5 = -0.3f * MathHelper.sin( swingProgress * (float) Math.PI );
        RenderSystem.translatef( offset * f3, f4 - 0.3f * f2, f5 );
        RenderSystem.rotatef( f2 * -45f, 1f, 0f, 0f );
        RenderSystem.rotatef( offset * f2 * -30f, 0f, 1f, 0f );

        renderItem( stack );

        RenderSystem.popMatrix();
    }

    /**
     * Render an item in the middle of the screen.
     *
     * @param pitch         The pitch of the player
     * @param equipProgress The equip progress of this item
     * @param swingProgress The swing progress of this item
     * @param stack         The stack to render
     * @see FirstPersonRenderer#renderMapFirstPerson(float, float, float)
     */
    private void renderItemFirstPersonCenter( float pitch, float equipProgress, float swingProgress, ItemStack stack )
    {
        FirstPersonRenderer renderer = Minecraft.getInstance().getFirstPersonRenderer();

        // Setup the appropriate transformations. This is just copied from the
        // corresponding method in ItemRenderer.
        float swingRt = MathHelper.sqrt( swingProgress );
        float tX = -0.2f * MathHelper.sin( swingProgress * (float) Math.PI );
        float tZ = -0.4f * MathHelper.sin( swingRt * (float) Math.PI );
        RenderSystem.translatef( 0f, -tX / 2f, tZ );
        float pitchAngle = renderer.getMapAngleFromPitch( pitch );
        RenderSystem.translatef( 0f, 0.04f + equipProgress * -1.2f + pitchAngle * -0.5f, -0.72f );
        RenderSystem.rotatef( pitchAngle * -85f, 1f, 0f, 0f );
        renderer.renderArms();
        float rX = MathHelper.sin( swingRt * (float) Math.PI );
        RenderSystem.rotatef( rX * 20f, 1f, 0f, 0f );
        RenderSystem.scalef( 2f, 2f, 2f );

        renderItem( stack );
    }
}
