package com.codingdummy.futurecombat;

import com.codingdummy.futurecombat.items.weapon.BeamSaber;
import com.codingdummy.futurecombat.renderer.BeamSaberRenderer;
import com.codingdummy.futurecombat.util.EntitiesType;
import com.codingdummy.futurecombat.util.EntitiesTypeRegistryHandler;
import com.codingdummy.futurecombat.util.KeyBindingHandler;
import com.codingdummy.futurecombat.util.WeaponRegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("futurecombat")
public class FutureCombat
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String ModID = "futurecombat";
    public static FutureCombat instance;

    public FutureCombat()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(KeyBindingHandler.class);
        KeyBindingHandler.init();
        EntitiesTypeRegistryHandler.init();
        WeaponRegistryHandler.init();
        MinecraftForge.EVENT_BUS.register(this);
    }
    private void setup(final FMLCommonSetupEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntitiesType.BEAM_SABER_ENTITY_TYPE, BeamSaberRenderer::new);
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {

    }
    @SubscribeEvent
    public  void onServerStarting(FMLServerStartingEvent event)
    {

    }
//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public void PlayerTickEvent(final TickEvent.PlayerTickEvent event)
//    {
//        PlayerEntity player = event.player;
//        World world = player.getEntityWorld();
//        ItemStack mainHand = player.getHeldItemMainhand();
//        if(KeyBindingHandler.throw_saber.isPressed() && mainHand.getItem() instanceof BeamSaber)
//        {
//            TridentEntity trident = new TridentEntity(world,player,mainHand);
//            world.addEntity(trident);
//        }
//    }
}
