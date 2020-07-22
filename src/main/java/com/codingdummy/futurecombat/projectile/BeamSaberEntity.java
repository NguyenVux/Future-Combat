package com.codingdummy.futurecombat.projectile;

import com.codingdummy.futurecombat.util.EntitiesType;
import net.minecraft.entity.*;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class BeamSaberEntity extends Entity {

    private LivingEntity thrower;
    private ItemStack throwStack;
    public BeamSaberEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public BeamSaberEntity(World worldIn, ItemStack stackIn, LivingEntity thrower) {
        super(EntitiesType.BEAM_SABER_ENTITY_TYPE, worldIn);
        this.thrower = thrower;
        this.throwStack = stackIn;
        Vec3d pos = thrower.getPositionVec();
        this.setPosition(pos.getX(),pos.getY(),pos.getZ());
    }
}
