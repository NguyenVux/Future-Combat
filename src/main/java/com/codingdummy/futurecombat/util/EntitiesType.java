package com.codingdummy.futurecombat.util;

import com.codingdummy.futurecombat.FutureCombat;
import com.codingdummy.futurecombat.projectile.BeamSaberEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;

public class EntitiesType {
    public static final EntityType<BeamSaberEntity> BEAM_SABER_ENTITY_TYPE = EntityType.Builder.<BeamSaberEntity>create(BeamSaberEntity::new, EntityClassification.MISC).size(0.5f,0.5f).build(new ResourceLocation(FutureCombat.ModID,"beamsaber_entity").toString());
}
