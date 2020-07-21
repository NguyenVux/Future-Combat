package com.codingdummy.futurecombat.util;

import com.codingdummy.futurecombat.FutureCombat;
import com.codingdummy.futurecombat.items.weapon.BeamSaber;
import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.Future;

public class WeaponRegistryHandler
{
    private static final IEventBus EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, FutureCombat.ModID);
    public static final ItemGroup FUTURE_COMBAT_WEAPON_TAB = new ItemGroup("futurecombatweapon") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.PUMPKIN);
        }
    };
    public static void init()
    {
        ITEMS.register(EVENT_BUS);
    }


    public static final RegistryObject<SwordItem> BeamSaber = ITEMS.register("beamsaber",BeamSaber::new);
}
