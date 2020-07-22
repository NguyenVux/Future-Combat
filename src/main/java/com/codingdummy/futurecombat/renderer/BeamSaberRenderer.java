package com.codingdummy.futurecombat.renderer;

import com.codingdummy.futurecombat.FutureCombat;
import com.codingdummy.futurecombat.model.BeamSaberEntityModel;
import com.codingdummy.futurecombat.projectile.BeamSaberEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TridentRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class BeamSaberRenderer extends EntityRenderer<BeamSaberEntity>
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FutureCombat.ModID,"textures/entities/beamsaber_entity.png");
    private final BeamSaberEntityModel BEAMSABER = new BeamSaberEntityModel();

    public BeamSaberRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(BeamSaberEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        BEAMSABER.render(matrixStackIn, bufferIn.getBuffer(BEAMSABER.getRenderType(this.getEntityTexture(entityIn))),packedLightIn,OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(BeamSaberEntity entity) {
        return TEXTURE;
    }
}