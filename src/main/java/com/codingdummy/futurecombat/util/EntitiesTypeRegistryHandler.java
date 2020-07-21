package com.codingdummy.futurecombat.util;

import com.codingdummy.futurecombat.FutureCombat;
import com.codingdummy.futurecombat.projectile.BeamSaberEntity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntitiesTypeRegistryHandler {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, FutureCombat.ModID);
    public static final RegistryObject<EntityType<BeamSaberEntity>> BEAMSABER_ENTITY = ENTITY_TYPES.register("beamsaber_entity",() -> EntitiesType.BEAM_SABER_ENTITY_TYPE);
    public static void init()
    {
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}


