package net.minecraft.entity.ai.brain.sensor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class PiglinMobsSensor extends Sensor<LivingEntity> {
   public Set<MemoryModuleType<?>> getUsedMemories() {
      return ImmutableSet.of(MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.MOBS, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEAREST_ADULT_PIGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_REPELLENT);
   }

   protected void update(ServerWorld worldIn, LivingEntity entityIn) {
      Brain<?> brain = entityIn.getBrain();
      brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, func_234126_c_(worldIn, entityIn));
      Optional<MobEntity> optional = Optional.empty();
      Optional<HoglinEntity> optional1 = Optional.empty();
      Optional<HoglinEntity> optional2 = Optional.empty();
      Optional<PiglinEntity> optional3 = Optional.empty();
      Optional<LivingEntity> optional4 = Optional.empty();
      Optional<PlayerEntity> optional5 = Optional.empty();
      Optional<PlayerEntity> optional6 = Optional.empty();
      int i = 0;
      List<PiglinEntity> list = Lists.newArrayList();
      List<PiglinEntity> list1 = Lists.newArrayList();

      for(LivingEntity livingentity : brain.getMemory(MemoryModuleType.VISIBLE_MOBS).orElse(ImmutableList.of())) {
         if (livingentity instanceof HoglinEntity) {
            HoglinEntity hoglinentity = (HoglinEntity)livingentity;
            if (hoglinentity.isChild() && !optional2.isPresent()) {
               optional2 = Optional.of(hoglinentity);
            } else if (hoglinentity.func_234363_eJ_()) {
               ++i;
               if (!optional1.isPresent() && hoglinentity.func_234365_eM_()) {
                  optional1 = Optional.of(hoglinentity);
               }
            }
         } else if (livingentity instanceof PiglinEntity) {
            PiglinEntity piglinentity = (PiglinEntity)livingentity;
            if (piglinentity.isChild() && !optional3.isPresent()) {
               optional3 = Optional.of(piglinentity);
            } else if (piglinentity.func_234421_eJ_()) {
               list.add(piglinentity);
            }
         } else if (livingentity instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)livingentity;
            if (!optional5.isPresent() && EntityPredicates.field_233583_f_.test(livingentity) && !PiglinTasks.func_234460_a_(playerentity)) {
               optional5 = Optional.of(playerentity);
            }

            if (!optional6.isPresent() && !playerentity.isSpectator() && PiglinTasks.func_234482_b_(playerentity)) {
               optional6 = Optional.of(playerentity);
            }
         } else if (optional.isPresent() || !(livingentity instanceof WitherSkeletonEntity) && !(livingentity instanceof WitherEntity)) {
            if (!optional4.isPresent() && PiglinTasks.func_234459_a_(livingentity.getType())) {
               optional4 = Optional.of(livingentity);
            }
         } else {
            optional = Optional.of((MobEntity)livingentity);
         }
      }

      for(LivingEntity livingentity1 : brain.getMemory(MemoryModuleType.MOBS).orElse(ImmutableList.of())) {
         if (livingentity1 instanceof PiglinEntity && ((PiglinEntity)livingentity1).func_234421_eJ_()) {
            list1.add((PiglinEntity)livingentity1);
         }
      }

      brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional);
      brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, optional1);
      brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, optional2);
      brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_BABY_PIGLIN, optional3);
      brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, optional4);
      brain.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, optional5);
      brain.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, optional6);
      brain.setMemory(MemoryModuleType.NEAREST_ADULT_PIGLINS, list1);
      brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, list);
      brain.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, list.size());
      brain.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, i);
   }

   private static Optional<BlockPos> func_234126_c_(ServerWorld p_234126_0_, LivingEntity p_234126_1_) {
      return BlockPos.func_239584_a_(p_234126_1_.func_233580_cy_(), 8, 4, (p_234125_1_) -> {
         return func_241391_a_(p_234126_0_, p_234125_1_);
      });
   }

   private static boolean func_241391_a_(ServerWorld p_241391_0_, BlockPos p_241391_1_) {
      BlockState blockstate = p_241391_0_.getBlockState(p_241391_1_);
      boolean flag = blockstate.isIn(BlockTags.PIGLIN_REPELLENTS);
      return flag && blockstate.isIn(Blocks.SOUL_CAMPFIRE) ? CampfireBlock.func_226915_i_(blockstate) : flag;
   }
}