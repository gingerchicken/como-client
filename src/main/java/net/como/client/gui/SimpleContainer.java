package net.como.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import net.como.client.ComoClient;
import net.como.client.utils.Render2DUtils;
import net.como.client.utils.RenderUtils;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat;

public class SimpleContainer extends Widget {

    public SimpleContainer(Vec2f position, Vec2f size) {
        super(position, size);
    }

    @Override
    public void render(MatrixStack matrixStack) {
      Render2DUtils.renderBox(matrixStack,
        (int)this.getPosition().x,
        (int)this.getPosition().y,
        (int)this.getRightPosition().x,
        (int)this.getRightPosition().y
      );
    }

}
