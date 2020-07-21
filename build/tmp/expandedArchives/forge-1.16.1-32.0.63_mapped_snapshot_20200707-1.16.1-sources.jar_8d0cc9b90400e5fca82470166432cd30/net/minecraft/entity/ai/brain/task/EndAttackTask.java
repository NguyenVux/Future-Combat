package net.minecraft.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import java.util.function.BiPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;

public class EndAttackTask extends Task<LivingEntity> {
   private final int field_233978_b_;
   private final BiPredicate<LivingEntity, LivingEntity> field_233979_c_;

   public EndAttackTask(int p_i231538_1_, BiPredicate<LivingEntity, LivingEntity> p_i231538_2_) {
      super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.ANGRY_AT, MemoryModuleStatus.REGISTERED, MemoryModuleType.CELEBRATE_LOCATION, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.DANCING, MemoryModuleStatus.REGISTERED));
      this.field_233978_b_ = p_i231538_1_;
      this.field_233979_c_ = p_i231538_2_;
   }

   protected boolean shouldExecute(ServerWorld worldIn, LivingEntity owner) {
      return this.func_233980_a_(owner).getShouldBeDead();
   }

   protected void startExecuting(ServerWorld worldIn, LivingEntity entityIn, long gameTimeIn) {
      LivingEntity livingentity = this.func_233980_a_(entityIn);
      if (this.field_233979_c_.test(entityIn, livingentity)) {
         entityIn.getBrain().func_233696_a_(MemoryModuleType.DANCING, true, (long)this.field_233978_b_);
      }

      entityIn.getBrain().func_233696_a_(MemoryModuleType.CELEBRATE_LOCATION, livingentity.func_233580_cy_(), (long)this.field_233978_b_);
      if (livingentity.getType() != EntityType.PLAYER || worldIn.getGameRules().getBoolean(GameRules.field_234895_F_)) {
         entityIn.getBrain().removeMemory(MemoryModuleType.ATTACK_TARGET);
         entityIn.getBrain().removeMemory(MemoryModuleType.ANGRY_AT);
      }

   }

   private LivingEntity func_233980_a_(LivingEntity p_233980_1_) {
      return p_233980_1_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
   }
}