package com.codingdummy.futurecombat.items.tools;

import com.codingdummy.futurecombat.util.WeaponRegistryHandler;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum FutureCombatItemTier  implements IItemTier {
    HighEnergy(3, 600, 10.0F, 3.0F, 0, () ->
    {
        return Ingredient.fromItems(WeaponRegistryHandler.BeamSaber.get());
    });

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchatability;
    private final Supplier<Ingredient> RepairMaterial;

    FutureCombatItemTier(int harvestLevel, int maxUses, float efficiency,float attackDamage,int enchantability,Supplier<Ingredient> RepairMaterial)
    {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchatability =enchantability;
        this.RepairMaterial = RepairMaterial;
    }

    @Override
    public int getMaxUses() {
        return maxUses;
    }

    @Override
    public float getEfficiency() {
        return efficiency;
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public int getHarvestLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantability() {
        return enchatability;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return RepairMaterial.get();
    }
}
