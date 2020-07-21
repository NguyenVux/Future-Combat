package com.codingdummy.futurecombat.items.weapon;

import com.codingdummy.futurecombat.FutureCombat;
import com.codingdummy.futurecombat.items.tools.FutureCombatItemTier;
import com.codingdummy.futurecombat.util.WeaponRegistryHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;


public class BeamSaber extends SwordItem {
    private static final Logger LOGGER = FutureCombat.LOGGER;
    public static KeyBinding key;
    public BeamSaber()
    {
        super(FutureCombatItemTier.HighEnergy,11,-3F,new Properties().group(WeaponRegistryHandler.FUTURE_COMBAT_WEAPON_TAB));
        this.addPropertyOverride(new ResourceLocation("futurecombat:is_active"),(stack,WorldIN,Entities)->{
            CompoundNBT Tag = stack.getTag();
            if(Tag != null) {
                if(Tag.getBoolean("futurecombat:is_active"))
                {
                    return 1.0f;
                }
                else
                {
                    return 0.0f;
                }
            }
            else
            {
                return 0.0f;
            }
        });
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        CompoundNBT Tag = stack.getTag();
        if(Tag != null) {
            if(Tag.getBoolean("futurecombat:is_active"))
            {
                Tag.putBoolean("futurecombat:is_active",false);
            }
            else
            {
                Tag.putBoolean("futurecombat:is_active",true);
            }
        }
        else
        {
            Tag = new CompoundNBT();
            Tag.putBoolean("futurecombat:is_active",false);
            stack.setTag(Tag);
        }
        super.onCreated(stack, worldIn, playerIn);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        items.forEach((consumer)->
        {
            CompoundNBT Tag = consumer.getTag();
            if(Tag != null) {
                if(Tag.getBoolean("futurecombat:is_active"))
                {
                    Tag.putBoolean("futurecombat:is_active",false);
                }
                else
                {
                    Tag.putBoolean("futurecombat:is_active",true);
                }
            }
            else
            {
                Tag = new CompoundNBT();
                Tag.putBoolean("futurecombat:is_active",false);
                consumer.setTag(Tag);
            }
        });
        super.fillItemGroup(group, items);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        CompoundNBT Tag = stack.getTag();
        if(Tag != null) {
            if(Tag.getBoolean("futurecombat:is_active"))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            Tag = new CompoundNBT();
            Tag.putBoolean("futurecombat:is_active",false);
            stack.setTag(Tag);
            return true;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        ItemStack main_hand = playerIn.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        TridentEntity trident = new TridentEntity(worldIn,playerIn,main_hand);
        worldIn.addEntity(trident);
        CompoundNBT Tag = main_hand.getTag();
        if(Tag != null) {
            if(Tag.getBoolean("futurecombat:is_active"))
            {
                Tag.putBoolean("futurecombat:is_active",false);
            }
            else
            {
                Tag.putBoolean("futurecombat:is_active",true);
            }
        }
        else
        {
            Tag = new CompoundNBT();
            Tag.putBoolean("futurecombat:is_active",false);
            main_hand.setTag(Tag);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
