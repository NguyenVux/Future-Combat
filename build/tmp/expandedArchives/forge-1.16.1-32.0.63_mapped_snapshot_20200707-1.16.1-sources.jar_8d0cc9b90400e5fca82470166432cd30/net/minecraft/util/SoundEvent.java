package net.minecraft.util;

import com.mojang.serialization.Codec;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SoundEvent extends net.minecraftforge.registries.ForgeRegistryEntry<SoundEvent> {
   public static final Codec<SoundEvent> SOUND_EVENT_CODEC = ResourceLocation.RESOURCE_LOCATION_CODEC.xmap(SoundEvent::new, (p_232679_0_) -> {
      return p_232679_0_.name;
   });
   private final ResourceLocation name;

   public SoundEvent(ResourceLocation name) {
      this.name = name;
   }

   @OnlyIn(Dist.CLIENT)
   public ResourceLocation getName() {
      return this.name;
   }
}