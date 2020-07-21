package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreateFlatWorldScreen extends Screen {
   private final Screen createWorldGui;
   private final Consumer<FlatGenerationSettings> field_238601_b_;
   private FlatGenerationSettings generatorInfo;
   /** The text used to identify the material for a layer */
   private ITextComponent materialText;
   /** The text used to identify the height of a layer */
   private ITextComponent heightText;
   private CreateFlatWorldScreen.DetailsList createFlatWorldListSlotGui;
   /** The remove layer button */
   private Button removeLayerButton;

   public CreateFlatWorldScreen(Screen p_i232273_1_, Consumer<FlatGenerationSettings> p_i232273_2_, FlatGenerationSettings p_i232273_3_) {
      super(new TranslationTextComponent("createWorld.customize.flat.title"));
      this.createWorldGui = p_i232273_1_;
      this.field_238601_b_ = p_i232273_2_;
      this.generatorInfo = p_i232273_3_;
   }

   public FlatGenerationSettings func_238603_g_() {
      return this.generatorInfo;
   }

   public void func_238602_a_(FlatGenerationSettings p_238602_1_) {
      this.generatorInfo = p_238602_1_;
   }

   protected void init() {
      this.materialText = new TranslationTextComponent("createWorld.customize.flat.tile");
      this.heightText = new TranslationTextComponent("createWorld.customize.flat.height");
      this.createFlatWorldListSlotGui = new CreateFlatWorldScreen.DetailsList();
      this.children.add(this.createFlatWorldListSlotGui);
      this.removeLayerButton = this.addButton(new Button(this.width / 2 - 155, this.height - 52, 150, 20, new TranslationTextComponent("createWorld.customize.flat.removeLayer"), (p_213007_1_) -> {
         if (this.hasSelectedLayer()) {
            List<FlatLayerInfo> list = this.generatorInfo.getFlatLayers();
            int i = this.createFlatWorldListSlotGui.children().indexOf(this.createFlatWorldListSlotGui.getSelected());
            int j = list.size() - i - 1;
            list.remove(j);
            this.createFlatWorldListSlotGui.setSelected(list.isEmpty() ? null : this.createFlatWorldListSlotGui.children().get(Math.min(i, list.size() - 1)));
            this.generatorInfo.updateLayers();
            this.createFlatWorldListSlotGui.func_214345_a();
            this.onLayersChanged();
         }
      }));
      this.addButton(new Button(this.width / 2 + 5, this.height - 52, 150, 20, new TranslationTextComponent("createWorld.customize.presets"), (p_213011_1_) -> {
         this.minecraft.displayGuiScreen(new FlatPresetsScreen(this));
         this.generatorInfo.updateLayers();
         this.onLayersChanged();
      }));
      this.addButton(new Button(this.width / 2 - 155, this.height - 28, 150, 20, DialogTexts.field_240632_c_, (p_213010_1_) -> {
         this.field_238601_b_.accept(this.generatorInfo);
         this.minecraft.displayGuiScreen(this.createWorldGui);
         this.generatorInfo.updateLayers();
      }));
      this.addButton(new Button(this.width / 2 + 5, this.height - 28, 150, 20, DialogTexts.field_240633_d_, (p_213009_1_) -> {
         this.minecraft.displayGuiScreen(this.createWorldGui);
         this.generatorInfo.updateLayers();
      }));
      this.generatorInfo.updateLayers();
      this.onLayersChanged();
   }

   /**
    * Would update whether or not the edit and remove buttons are enabled
    */
   private void onLayersChanged() {
      this.removeLayerButton.active = this.hasSelectedLayer();
   }

   /**
    * Returns whether there is a valid layer selection
    */
   private boolean hasSelectedLayer() {
      return this.createFlatWorldListSlotGui.getSelected() != null;
   }

   public void onClose() {
      this.minecraft.displayGuiScreen(this.createWorldGui);
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      this.createFlatWorldListSlotGui.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 8, 16777215);
      int i = this.width / 2 - 92 - 16;
      this.drawString(p_230430_1_, this.font, this.materialText, i, 32, 16777215);
      this.drawString(p_230430_1_, this.font, this.heightText, i + 2 + 213 - this.font.func_238414_a_(this.heightText), 32, 16777215);
      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   @OnlyIn(Dist.CLIENT)
   class DetailsList extends ExtendedList<CreateFlatWorldScreen.DetailsList.LayerEntry> {
      public DetailsList() {
         super(CreateFlatWorldScreen.this.minecraft, CreateFlatWorldScreen.this.width, CreateFlatWorldScreen.this.height, 43, CreateFlatWorldScreen.this.height - 60, 24);

         for(int i = 0; i < CreateFlatWorldScreen.this.generatorInfo.getFlatLayers().size(); ++i) {
            this.addEntry(new CreateFlatWorldScreen.DetailsList.LayerEntry());
         }

      }

      public void setSelected(@Nullable CreateFlatWorldScreen.DetailsList.LayerEntry p_241215_1_) {
         super.setSelected(p_241215_1_);
         if (p_241215_1_ != null) {
            FlatLayerInfo flatlayerinfo = CreateFlatWorldScreen.this.generatorInfo.getFlatLayers().get(CreateFlatWorldScreen.this.generatorInfo.getFlatLayers().size() - this.children().indexOf(p_241215_1_) - 1);
            Item item = flatlayerinfo.getLayerMaterial().getBlock().asItem();
            if (item != Items.AIR) {
               NarratorChatListener.INSTANCE.say((new TranslationTextComponent("narrator.select", item.getDisplayName(new ItemStack(item)))).getString());
            }
         }

         CreateFlatWorldScreen.this.onLayersChanged();
      }

      protected boolean isFocused() {
         return CreateFlatWorldScreen.this.getFocused() == this;
      }

      protected int getScrollbarPosition() {
         return this.width - 70;
      }

      public void func_214345_a() {
         int i = this.children().indexOf(this.getSelected());
         this.clearEntries();

         for(int j = 0; j < CreateFlatWorldScreen.this.generatorInfo.getFlatLayers().size(); ++j) {
            this.addEntry(new CreateFlatWorldScreen.DetailsList.LayerEntry());
         }

         List<CreateFlatWorldScreen.DetailsList.LayerEntry> list = this.children();
         if (i >= 0 && i < list.size()) {
            this.setSelected(list.get(i));
         }

      }

      @OnlyIn(Dist.CLIENT)
      class LayerEntry extends ExtendedList.AbstractListEntry<CreateFlatWorldScreen.DetailsList.LayerEntry> {
         private LayerEntry() {
         }

         public void render(MatrixStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
            FlatLayerInfo flatlayerinfo = CreateFlatWorldScreen.this.generatorInfo.getFlatLayers().get(CreateFlatWorldScreen.this.generatorInfo.getFlatLayers().size() - p_230432_2_ - 1);
            BlockState blockstate = flatlayerinfo.getLayerMaterial();
            Item item = blockstate.getBlock().asItem();
            if (item == Items.AIR) {
               if (blockstate.isIn(Blocks.WATER)) {
                  item = Items.WATER_BUCKET;
               } else if (blockstate.isIn(Blocks.LAVA)) {
                  item = Items.LAVA_BUCKET;
               }
            }

            ItemStack itemstack = new ItemStack(item);
            this.func_238605_a_(p_230432_1_, p_230432_4_, p_230432_3_, itemstack);
            CreateFlatWorldScreen.this.font.func_238422_b_(p_230432_1_, item.getDisplayName(itemstack), (float)(p_230432_4_ + 18 + 5), (float)(p_230432_3_ + 3), 16777215);
            String s;
            if (p_230432_2_ == 0) {
               s = I18n.format("createWorld.customize.flat.layer.top", flatlayerinfo.getLayerCount());
            } else if (p_230432_2_ == CreateFlatWorldScreen.this.generatorInfo.getFlatLayers().size() - 1) {
               s = I18n.format("createWorld.customize.flat.layer.bottom", flatlayerinfo.getLayerCount());
            } else {
               s = I18n.format("createWorld.customize.flat.layer", flatlayerinfo.getLayerCount());
            }

            CreateFlatWorldScreen.this.font.drawString(p_230432_1_, s, (float)(p_230432_4_ + 2 + 213 - CreateFlatWorldScreen.this.font.getStringWidth(s)), (float)(p_230432_3_ + 3), 16777215);
         }

         public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
            if (p_231044_5_ == 0) {
               DetailsList.this.setSelected(this);
               return true;
            } else {
               return false;
            }
         }

         private void func_238605_a_(MatrixStack p_238605_1_, int p_238605_2_, int p_238605_3_, ItemStack p_238605_4_) {
            this.func_238604_a_(p_238605_1_, p_238605_2_ + 1, p_238605_3_ + 1);
            RenderSystem.enableRescaleNormal();
            if (!p_238605_4_.isEmpty()) {
               CreateFlatWorldScreen.this.itemRenderer.renderItemIntoGUI(p_238605_4_, p_238605_2_ + 2, p_238605_3_ + 2);
            }

            RenderSystem.disableRescaleNormal();
         }

         private void func_238604_a_(MatrixStack p_238604_1_, int p_238604_2_, int p_238604_3_) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            DetailsList.this.minecraft.getTextureManager().bindTexture(AbstractGui.STATS_ICON_LOCATION);
            AbstractGui.blit(p_238604_1_, p_238604_2_, p_238604_3_, CreateFlatWorldScreen.this.getBlitOffset(), 0.0F, 0.0F, 18, 18, 128, 128);
         }
      }
   }
}