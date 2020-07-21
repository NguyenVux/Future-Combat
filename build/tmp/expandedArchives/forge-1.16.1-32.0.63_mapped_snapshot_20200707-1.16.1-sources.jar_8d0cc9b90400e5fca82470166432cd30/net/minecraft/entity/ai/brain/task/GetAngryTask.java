package net.minecraft.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;

public class GetAngryTask<E extends MobEntity> extends Task<E> {
   public GetAngryTask() {
      super(ImmutableMap.of(MemoryModuleType.ANGRY_AT, MemoryModuleStatus.VALUE_PRESENT));
   }

   protected void startExecuting(ServerWorld worldIn, E entityIn, long gameTimeIn) {
      BrainUtil.func_233864_a_(entityIn, MemoryModuleType.ANGRY_AT).ifPresent((p_233988_2_) -> {
         if (p_233988_2_.getShouldBeDead() && (p_233988_2_.getType() != EntityType.PLAYER || worldIn.getGameRules().getBoolean(GameRules.field_234895_F_))) {
            entityIn.getBrain().removeMemory(MemoryModuleType.ANGRY_AT);
         }

      });
   }
}