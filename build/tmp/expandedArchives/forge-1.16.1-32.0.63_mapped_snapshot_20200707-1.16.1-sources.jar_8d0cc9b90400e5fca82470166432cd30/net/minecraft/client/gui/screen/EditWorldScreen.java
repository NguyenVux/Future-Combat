package net.minecraft.client.gui.screen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonWriter;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.DataResult.PartialResult;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.IDynamicRegistries;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.WorldGenSettingsExport;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.storage.FolderName;
import net.minecraft.world.storage.SaveFormat;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class EditWorldScreen extends Screen {
   private static final Logger field_239014_a_ = LogManager.getLogger();
   private static final Gson field_239015_b_ = (new GsonBuilder()).setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
   private Button saveButton;
   private final BooleanConsumer field_214311_b;
   private TextFieldWidget nameEdit;
   private final SaveFormat.LevelSave field_239016_r_;

   public EditWorldScreen(BooleanConsumer p_i232318_1_, SaveFormat.LevelSave p_i232318_2_) {
      super(new TranslationTextComponent("selectWorld.edit.title"));
      this.field_214311_b = p_i232318_1_;
      this.field_239016_r_ = p_i232318_2_;
   }

   public void tick() {
      this.nameEdit.tick();
   }

   protected void init() {
      this.minecraft.keyboardListener.enableRepeatEvents(true);
      Button button = this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 0 + 5, 200, 20, new TranslationTextComponent("selectWorld.edit.resetIcon"), (p_214309_1_) -> {
         FileUtils.deleteQuietly(this.field_239016_r_.func_237298_f_());
         p_214309_1_.active = false;
      }));
      this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 24 + 5, 200, 20, new TranslationTextComponent("selectWorld.edit.openFolder"), (p_214303_1_) -> {
         Util.getOSType().openFile(this.field_239016_r_.func_237285_a_(FolderName.field_237253_i_).toFile());
      }));
      this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 48 + 5, 200, 20, new TranslationTextComponent("selectWorld.edit.backup"), (p_214304_1_) -> {
         boolean flag = func_239019_a_(this.field_239016_r_);
         this.field_214311_b.accept(!flag);
      }));
      this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 72 + 5, 200, 20, new TranslationTextComponent("selectWorld.edit.backupFolder"), (p_214302_1_) -> {
         SaveFormat saveformat = this.minecraft.getSaveLoader();
         Path path = saveformat.getBackupsFolder();

         try {
            Files.createDirectories(Files.exists(path) ? path.toRealPath() : path);
         } catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
         }

         Util.getOSType().openFile(path.toFile());
      }));
      this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 96 + 5, 200, 20, new TranslationTextComponent("selectWorld.edit.optimize"), (p_214310_1_) -> {
         this.minecraft.displayGuiScreen(new ConfirmBackupScreen(this, (p_214305_1_, p_214305_2_) -> {
            if (p_214305_1_) {
               func_239019_a_(this.field_239016_r_);
            }

            this.minecraft.displayGuiScreen(OptimizeWorldScreen.func_239025_a_(this.minecraft, this.field_214311_b, this.minecraft.getDataFixer(), this.field_239016_r_, p_214305_2_));
         }, new TranslationTextComponent("optimizeWorld.confirm.title"), new TranslationTextComponent("optimizeWorld.confirm.description"), true));
      }));
      this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 120 + 5, 200, 20, new TranslationTextComponent("selectWorld.edit.export_worldgen_settings"), (p_239023_1_) -> {
         IDynamicRegistries.Impl idynamicregistries$impl = IDynamicRegistries.func_239770_b_();

         DataResult<String> dataresult;
         try (Minecraft.PackManager minecraft$packmanager = this.minecraft.func_238189_a_(idynamicregistries$impl, Minecraft::func_238180_a_, Minecraft::func_238181_a_, false, this.field_239016_r_)) {
            DynamicOps<JsonElement> dynamicops = WorldGenSettingsExport.func_240896_a_(JsonOps.INSTANCE, idynamicregistries$impl);
            DataResult<JsonElement> dataresult1 = DimensionGeneratorSettings.field_236201_a_.encodeStart(dynamicops, minecraft$packmanager.func_238226_c_().func_230418_z_());
            dataresult = dataresult1.flatMap((p_239017_1_) -> {
               Path path = this.field_239016_r_.func_237285_a_(FolderName.field_237253_i_).resolve("worldgen_settings_export.json");

               try (JsonWriter jsonwriter = field_239015_b_.newJsonWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8))) {
                  field_239015_b_.toJson(p_239017_1_, jsonwriter);
               } catch (JsonIOException | IOException ioexception) {
                  return DataResult.error("Error writing file: " + ioexception.getMessage());
               }

               return DataResult.success(path.toString());
            });
         } catch (ExecutionException | InterruptedException interruptedexception) {
            dataresult = DataResult.error("Could not parse level data!");
         }

         ITextComponent itextcomponent = new StringTextComponent(dataresult.get().map(Function.identity(), PartialResult::message));
         ITextComponent itextcomponent1 = new TranslationTextComponent(dataresult.result().isPresent() ? "selectWorld.edit.export_worldgen_settings.success" : "selectWorld.edit.export_worldgen_settings.failure");
         dataresult.error().ifPresent((p_239018_0_) -> {
            field_239014_a_.error("Error exporting world settings: {}", (Object)p_239018_0_);
         });
         this.minecraft.getToastGui().add(SystemToast.func_238534_a_(this.minecraft, SystemToast.Type.WORLD_GEN_SETTINGS_TRANSFER, itextcomponent1, itextcomponent));
      }));
      this.saveButton = this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 144 + 5, 98, 20, new TranslationTextComponent("selectWorld.edit.save"), (p_214308_1_) -> {
         this.saveChanges();
      }));
      this.addButton(new Button(this.width / 2 + 2, this.height / 4 + 144 + 5, 98, 20, DialogTexts.field_240633_d_, (p_214306_1_) -> {
         this.field_214311_b.accept(false);
      }));
      button.active = this.field_239016_r_.func_237298_f_().isFile();
      WorldSummary worldsummary = this.field_239016_r_.func_237296_d_();
      String s = worldsummary == null ? "" : worldsummary.getDisplayName();
      this.nameEdit = new TextFieldWidget(this.font, this.width / 2 - 100, 38, 200, 20, new TranslationTextComponent("selectWorld.enterName"));
      this.nameEdit.setText(s);
      this.nameEdit.setResponder((p_214301_1_) -> {
         this.saveButton.active = !p_214301_1_.trim().isEmpty();
      });
      this.children.add(this.nameEdit);
      this.setFocusedDefault(this.nameEdit);
   }

   public void resize(Minecraft p_231152_1_, int p_231152_2_, int p_231152_3_) {
      String s = this.nameEdit.getText();
      this.init(p_231152_1_, p_231152_2_, p_231152_3_);
      this.nameEdit.setText(s);
   }

   public void onClose() {
      this.field_214311_b.accept(false);
   }

   public void removed() {
      this.minecraft.keyboardListener.enableRepeatEvents(false);
   }

   /**
    * Saves changes to the world name and closes this GUI.
    */
   private void saveChanges() {
      try {
         this.field_239016_r_.func_237290_a_(this.nameEdit.getText().trim());
         this.field_214311_b.accept(true);
      } catch (IOException ioexception) {
         field_239014_a_.error("Failed to access world '{}'", this.field_239016_r_.func_237282_a_(), ioexception);
         SystemToast.func_238535_a_(this.minecraft, this.field_239016_r_.func_237282_a_());
         this.field_214311_b.accept(true);
      }

   }

   public static void func_241651_a_(SaveFormat p_241651_0_, String p_241651_1_) {
      boolean flag = false;

      try (SaveFormat.LevelSave saveformat$levelsave = p_241651_0_.func_237274_c_(p_241651_1_)) {
         flag = true;
         func_239019_a_(saveformat$levelsave);
      } catch (IOException ioexception) {
         if (!flag) {
            SystemToast.func_238535_a_(Minecraft.getInstance(), p_241651_1_);
         }

         field_239014_a_.warn("Failed to create backup of level {}", p_241651_1_, ioexception);
      }

   }

   public static boolean func_239019_a_(SaveFormat.LevelSave p_239019_0_) {
      long i = 0L;
      IOException ioexception = null;

      try {
         i = p_239019_0_.func_237300_h_();
      } catch (IOException ioexception1) {
         ioexception = ioexception1;
      }

      if (ioexception != null) {
         ITextComponent itextcomponent2 = new TranslationTextComponent("selectWorld.edit.backupFailed");
         ITextComponent itextcomponent3 = new StringTextComponent(ioexception.getMessage());
         Minecraft.getInstance().getToastGui().add(new SystemToast(SystemToast.Type.WORLD_BACKUP, itextcomponent2, itextcomponent3));
         return false;
      } else {
         ITextComponent itextcomponent = new TranslationTextComponent("selectWorld.edit.backupCreated", p_239019_0_.func_237282_a_());
         ITextComponent itextcomponent1 = new TranslationTextComponent("selectWorld.edit.backupSize", MathHelper.ceil((double)i / 1048576.0D));
         Minecraft.getInstance().getToastGui().add(new SystemToast(SystemToast.Type.WORLD_BACKUP, itextcomponent, itextcomponent1));
         return true;
      }
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 15, 16777215);
      this.drawString(p_230430_1_, this.font, I18n.format("selectWorld.enterName"), this.width / 2 - 100, 24, 10526880);
      this.nameEdit.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }
}