package com.codingdummy.futurecombat.projectile;

import com.codingdummy.futurecombat.FutureCombat;
import com.codingdummy.futurecombat.items.weapon.BeamSaber;
import com.codingdummy.futurecombat.util.EntitiesType;
import net.minecraft.client.renderer.entity.TridentRenderer;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class BeamSaberEntity extends Entity {

    private Vec3d origin = Vec3d.ZERO;
    private ItemStack throwStack = new ItemStack(BeamSaber::new);
    private UUID thrower;
    private boolean onReturn = false;
    private final double FlyingDistance = 30.0f;

    public BeamSaberEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public void setPosition(Vec3d dir) {
        this.setPosition(dir.getX(),dir.getY(),dir.getZ());
    }

    @Override
    public void tick() {
       super.tick();
       this.rotationYaw = (this.rotationYaw + 50.0f) % 360;
       if(!this.world.isRemote) {
           if (this.world.checkBlockCollision(this.getBoundingBox())) {
               //this.onReturn = true;
               this.world.createExplosion(this,this.getPosX(),this.getPosY(),this.getPosZ(),15.0f, Explosion.Mode.BREAK);
           }
           if (this.getPositionVec().subtract(this.origin).length() > this.FlyingDistance) {
               this.onReturn = true;

           }
           LivingEntity livingEntity = (LivingEntity) this.getThrower();
           float d1 = (float) livingEntity.getPosX();
           float d2 = (float) livingEntity.getPosY();
           float d3 = (float) livingEntity.getPosZ();
           d2 += livingEntity.getEyeHeight() / 2.0f;
           float r1 = d1 - (float) this.getPosX();
           float r2 = d2 - (float) this.getPosY();
           float r3 = d3 - (float) this.getPosZ();
           Vec3d motion = new Vec3d(r1, r2, r3);
           if (this.onReturn && livingEntity instanceof LivingEntity) {
               this.setMotion(motion.normalize().scale(0.6f));
           }
           List<Entity> Entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox());
           for (Entity entity : Entities) {
               if(entity instanceof PlayerEntity && this.onReturn)
               {
                   ((PlayerEntity) entity).addItemStackToInventory(this.throwStack);
                   this.remove();
               }
               else
               {
                   if(!(entity instanceof  BeamSaberEntity))
                   {
                       BeamSaber BeamSaberItem = (BeamSaber) this.throwStack.getItem();
                       float Damage = BeamSaberItem.getAttackDamage();
                       entity.attackEntityFrom(DamageSource.GENERIC, BeamSaberItem.getAttackDamage());
                   }
               }
           }
       }
        this.setPosition(this.getMotion().add(this.getPositionVec()));
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        if(compound.hasUniqueId("futurecombat:Owner"))
        {
            this.thrower = compound.getUniqueId("futurecombat:Owner");
        }
        this.onReturn = compound.getBoolean("futurecombat:onReturn");
        this.origin = new Vec3d(compound.getList("futurecombat:origin",99).getDouble(0),
                compound.getList("futurecombat:origin",99).getDouble(1),
                compound.getList("futurecombat:origin",99).getDouble(2));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        if (this.thrower != null) {
            compound.putUniqueId("futurecombat:Owner",this.thrower);
        }
        compound.putBoolean("futurecombat:onReturn",this.onReturn);
        compound.put("futurecombat:origin", this.newDoubleNBTList(this.origin.x,this.origin.y,this.origin.z));

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public BeamSaberEntity(World worldIn, ItemStack stackIn, LivingEntity thrower) {
        super(EntitiesType.BEAM_SABER_ENTITY_TYPE, worldIn);
        this.setMotion(thrower.getLookVec().normalize().scale(0.3f));

        this.setPosition(thrower.getPositionVec().add(0.0f,thrower.getEyeHeight()/2.0f,0.0f).add(this.getMotion().normalize()));
        this.setThrower(thrower);
        this.origin = thrower.getPositionVec();
        this.throwStack = stackIn.copy();
    }

    public void setThrower(Entity entityIn)
    {
        this.thrower = entityIn.getUniqueID();
    }

    @Override
    public void move(MoverType typeIn, Vec3d pos) {
        this.setBoundingBox(this.getBoundingBox().offset(pos));
        this.resetPositionToBB();
    }

    @Nullable
    public Entity getThrower()
    {
        return this.thrower == null ? null: this.world.getPlayerByUuid(thrower);
    }
}
