package com.codingdummy.futurecombat.renderer;

import com.codingdummy.futurecombat.FutureCombat;
import com.codingdummy.futurecombat.model.BeamSaberEntityModel;
import com.codingdummy.futurecombat.projectile.BeamSaberEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static com.codingdummy.futurecombat.util.MathHelperCodingDummy.toRad;
import static net.minecraft.util.math.MathHelper.cos;
import static net.minecraft.util.math.MathHelper.sin;

public class BeamSaberRenderer extends EntityRenderer<BeamSaberEntity>
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FutureCombat.ModID,"textures/entities/beamsaber_entity.png");
    private final BeamSaberEntityModel BEAMSABER = new BeamSaberEntityModel();

    public BeamSaberRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(BeamSaberEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        BEAMSABER.SaveState();
        float DegAngle = MathHelper.lerp(partialTicks,entityYaw,entityYaw+50.0f);
        float RadAngle = toRad(-DegAngle);
        BEAMSABER.setRotationAngle(new Vec3d(toRad(-90.0f),RadAngle,0.0f));
        Vec3d TranslateVector = new Vec3d(-sin(RadAngle),0.0f,-cos(RadAngle)).normalize().scale(12.0f);
        BEAMSABER.translate(TranslateVector);
        BEAMSABER.render(matrixStackIn, bufferIn.getBuffer(BEAMSABER.getRenderType(this.getEntityTexture(entityIn))),packedLightIn,OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        BEAMSABER.Restore();
    }

    @Override
    public ResourceLocation getEntityTexture(BeamSaberEntity entity) {
        return TEXTURE;
    }
}