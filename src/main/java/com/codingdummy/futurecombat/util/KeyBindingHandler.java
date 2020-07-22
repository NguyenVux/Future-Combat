package com.codingdummy.futurecombat.util;

import com.codingdummy.futurecombat.FutureCombat;
import com.codingdummy.futurecombat.items.weapon.BeamSaber;
import com.codingdummy.futurecombat.projectile.BeamSaberEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber
public class KeyBindingHandler {
    public static final KeyBinding throw_saber = new KeyBinding("", GLFW.GLFW_KEY_F,"Future Combat");

    public static void init()
    {
        ClientRegistry.registerKeyBinding(throw_saber);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void PlayerTickEvent(final TickEvent.PlayerTickEvent event)
    {

        if(KeyBindingHandler.throw_saber.isPressed())
        {
            PlayerEntity player = event.player;
            World world = player.getEntityWorld();
            ItemStack mainHand = player.getHeldItemMainhand();
            if(mainHand.getItem() instanceof BeamSaber && mainHand.getTag().getBoolean("futurecombat:is_active"))
            {
                BeamSaberEntity a = new BeamSaberEntity(world,mainHand, player);
                world.addEntity(a);
                FutureCombat.LOGGER.debug("throw");
            }
        }
    }

}
