package com.codingdummy.futurecombat.util;

import com.codingdummy.futurecombat.FutureCombat;
import com.codingdummy.futurecombat.projectile.BeamSaberEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class BeamSaberRenderer extends ArrowRenderer<BeamSaberEntity> {
    public BeamSaberRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(BeamSaberEntity entity) {
        entity.getArrowStack().getItem().getRegistryName().getPath();
        return new ResourceLocation(FutureCombat.ModID,"textures/entity/projectile");
    }
}
