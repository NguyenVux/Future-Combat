package net.minecraft.client.gui.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.Commands;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.FolderPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.resources.ServerPackFinder;
import net.minecraft.server.IDynamicRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FileUtil;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.codec.DatapackCodec;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.storage.FolderName;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class CreateWorldScreen extends Screen {
   private static final Logger field_238935_p_ = LogManager.getLogger();
   private final Screen parentScreen;
   private TextFieldWidget worldNameField;
   private String saveDirName;
   private CreateWorldScreen.GameMode field_228197_f_ = CreateWorldScreen.GameMode.SURVIVAL;
   @Nullable
   private CreateWorldScreen.GameMode field_228198_g_;
   private Difficulty field_238936_v_ = Difficulty.NORMAL;
   private Difficulty field_238937_w_ = Difficulty.NORMAL;
   /** If cheats are allowed */
   private boolean allowCheats;
   /**
    * User explicitly clicked Allow Cheats at some point
    * Prevents value changes due to changing game mode
    */
   private boolean allowCheatsWasSetByUser;
   /** Set to true when hardcore is the currently-selected gamemode */
   public boolean hardCoreMode;
   protected DatapackCodec field_238933_b_ = DatapackCodec.field_234880_a_;
   @Nullable
   private Path field_238928_A_;
   private boolean inMoreWorldOptionsDisplay;
   private Button btnCreateWorld;
   private Button btnGameMode;
   private Button field_238929_E_;
   private Button btnMoreOptions;
   private Button field_238930_G_;
   private Button field_238931_H_;
   private Button btnAllowCommands;
   private ITextComponent gameModeDesc1;
   private ITextComponent gameModeDesc2;
   private String worldName;
   private GameRules field_238932_M_ = new GameRules();
   public final WorldOptionsScreen field_238934_c_;

   public CreateWorldScreen(@Nullable Screen p_i232303_1_, WorldSettings p_i232303_2_, DimensionGeneratorSettings p_i232303_3_, @Nullable Path p_i232303_4_, IDynamicRegistries.Impl p_i232303_5_) {
      this(p_i232303_1_, new WorldOptionsScreen(p_i232303_5_, p_i232303_3_));
      this.worldName = p_i232303_2_.func_234947_a_();
      this.allowCheats = p_i232303_2_.func_234956_e_();
      this.allowCheatsWasSetByUser = true;
      this.field_238936_v_ = p_i232303_2_.func_234955_d_();
      this.field_238937_w_ = this.field_238936_v_;
      this.field_238932_M_.func_234899_a_(p_i232303_2_.func_234957_f_(), (MinecraftServer)null);
      this.field_238933_b_ = p_i232303_2_.func_234958_g_();
      if (p_i232303_2_.func_234954_c_()) {
         this.field_228197_f_ = CreateWorldScreen.GameMode.HARDCORE;
      } else if (p_i232303_2_.func_234953_b_().isSurvivalOrAdventure()) {
         this.field_228197_f_ = CreateWorldScreen.GameMode.SURVIVAL;
      } else if (p_i232303_2_.func_234953_b_().isCreative()) {
         this.field_228197_f_ = CreateWorldScreen.GameMode.CREATIVE;
      }

      this.field_238928_A_ = p_i232303_4_;
   }

   public CreateWorldScreen(@Nullable Screen p_i46320_1_) {
      this(p_i46320_1_, new WorldOptionsScreen());
   }

   private CreateWorldScreen(@Nullable Screen p_i232304_1_, WorldOptionsScreen p_i232304_2_) {
      super(new TranslationTextComponent("selectWorld.create"));
      this.parentScreen = p_i232304_1_;
      this.worldName = I18n.format("selectWorld.newWorld");
      this.field_238934_c_ = p_i232304_2_;
   }

   public void tick() {
      this.worldNameField.tick();
      this.field_238934_c_.tick();
   }

   protected void init() {
      this.minecraft.keyboardListener.enableRepeatEvents(true);
      this.worldNameField = new TextFieldWidget(this.font, this.width / 2 - 100, 60, 200, 20, new TranslationTextComponent("selectWorld.enterName")) {
         protected IFormattableTextComponent getNarrationMessage() {
            return super.getNarrationMessage().func_240702_b_(". ").func_230529_a_(new TranslationTextComponent("selectWorld.resultFolder")).func_240702_b_(" ").func_240702_b_(CreateWorldScreen.this.saveDirName);
         }
      };
      this.worldNameField.setText(this.worldName);
      this.worldNameField.setResponder((p_214319_1_) -> {
         this.worldName = p_214319_1_;
         this.btnCreateWorld.active = !this.worldNameField.getText().isEmpty();
         this.calcSaveDirName();
      });
      this.children.add(this.worldNameField);
      int i = this.width / 2 - 155;
      int j = this.width / 2 + 5;
      this.btnGameMode = this.addButton(new Button(i, 100, 150, 20, new TranslationTextComponent("selectWorld.gameMode"), (p_214316_1_) -> {
         switch(this.field_228197_f_) {
         case SURVIVAL:
            this.func_228200_a_(CreateWorldScreen.GameMode.HARDCORE);
            break;
         case HARDCORE:
            this.func_228200_a_(CreateWorldScreen.GameMode.CREATIVE);
            break;
         case CREATIVE:
            this.func_228200_a_(CreateWorldScreen.GameMode.SURVIVAL);
         }

         p_214316_1_.queueNarration(250);
      }) {
         public ITextComponent getMessage() {
            return super.getMessage().deepCopy().func_240702_b_(": ").func_230529_a_(new TranslationTextComponent("selectWorld.gameMode." + CreateWorldScreen.this.field_228197_f_.field_228217_e_));
         }

         protected IFormattableTextComponent getNarrationMessage() {
            return super.getNarrationMessage().func_240702_b_(". ").func_230529_a_(CreateWorldScreen.this.gameModeDesc1).func_240702_b_(" ").func_230529_a_(CreateWorldScreen.this.gameModeDesc2);
         }
      });
      this.field_238929_E_ = this.addButton(new Button(j, 100, 150, 20, new TranslationTextComponent("options.difficulty"), (p_238956_1_) -> {
         this.field_238936_v_ = this.field_238936_v_.func_233536_d_();
         this.field_238937_w_ = this.field_238936_v_;
         p_238956_1_.queueNarration(250);
      }) {
         public ITextComponent getMessage() {
            return (new TranslationTextComponent("options.difficulty")).func_240702_b_(": ").func_230529_a_(CreateWorldScreen.this.field_238937_w_.getDisplayName());
         }
      });
      this.btnAllowCommands = this.addButton(new Button(i, 151, 150, 20, new TranslationTextComponent("selectWorld.allowCommands"), (p_214322_1_) -> {
         this.allowCheatsWasSetByUser = true;
         this.allowCheats = !this.allowCheats;
         p_214322_1_.queueNarration(250);
      }) {
         public ITextComponent getMessage() {
            return super.getMessage().deepCopy().func_240702_b_(" ").func_230529_a_(DialogTexts.func_240638_a_(CreateWorldScreen.this.allowCheats && !CreateWorldScreen.this.hardCoreMode));
         }

         protected IFormattableTextComponent getNarrationMessage() {
            return super.getNarrationMessage().func_240702_b_(". ").func_230529_a_(new TranslationTextComponent("selectWorld.allowCommands.info"));
         }
      });
      this.field_238931_H_ = this.addButton(new Button(j, 151, 150, 20, new TranslationTextComponent("selectWorld.dataPacks"), (p_214320_1_) -> {
         this.func_238958_v_();
      }));
      this.field_238930_G_ = this.addButton(new Button(i, 185, 150, 20, new TranslationTextComponent("selectWorld.gameRules"), (p_214312_1_) -> {
         this.minecraft.displayGuiScreen(new EditGamerulesScreen(this.field_238932_M_.func_234905_b_(), (p_238946_1_) -> {
            this.minecraft.displayGuiScreen(this);
            p_238946_1_.ifPresent((p_238941_1_) -> {
               this.field_238932_M_ = p_238941_1_;
            });
         }));
      }));
      this.field_238934_c_.func_239048_a_(this, this.minecraft, this.font);
      this.btnMoreOptions = this.addButton(new Button(j, 185, 150, 20, new TranslationTextComponent("selectWorld.moreWorldOptions"), (p_214321_1_) -> {
         this.toggleMoreWorldOptions();
      }));
      this.btnCreateWorld = this.addButton(new Button(i, this.height - 28, 150, 20, new TranslationTextComponent("selectWorld.create"), (p_214318_1_) -> {
         this.createWorld();
      }));
      this.btnCreateWorld.active = !this.worldName.isEmpty();
      this.addButton(new Button(j, this.height - 28, 150, 20, DialogTexts.field_240633_d_, (p_214317_1_) -> {
         this.func_238959_w_();
         this.minecraft.displayGuiScreen(this.parentScreen);
      }));
      this.func_238955_g_();
      this.setFocusedDefault(this.worldNameField);
      this.func_228200_a_(this.field_228197_f_);
      this.calcSaveDirName();
   }

   private void func_228199_a_() {
      this.gameModeDesc1 = new TranslationTextComponent("selectWorld.gameMode." + this.field_228197_f_.field_228217_e_ + ".line1");
      this.gameModeDesc2 = new TranslationTextComponent("selectWorld.gameMode." + this.field_228197_f_.field_228217_e_ + ".line2");
   }

   /**
    * Determine a save-directory name from the world name
    */
   private void calcSaveDirName() {
      this.saveDirName = this.worldNameField.getText().trim();
      if (this.saveDirName.isEmpty()) {
         this.saveDirName = "World";
      }

      try {
         this.saveDirName = FileUtil.func_214992_a(this.minecraft.getSaveLoader().getSavesDir(), this.saveDirName, "");
      } catch (Exception exception1) {
         this.saveDirName = "World";

         try {
            this.saveDirName = FileUtil.func_214992_a(this.minecraft.getSaveLoader().getSavesDir(), this.saveDirName, "");
         } catch (Exception exception) {
            throw new RuntimeException("Could not create save folder", exception);
         }
      }

   }

   public void removed() {
      this.minecraft.keyboardListener.enableRepeatEvents(false);
   }

   private void createWorld() {
      this.minecraft.func_241562_c_(new DirtMessageScreen(new TranslationTextComponent("createWorld.preparing")));
      if (this.func_238960_x_()) {
         DimensionGeneratorSettings dimensiongeneratorsettings = this.field_238934_c_.func_239054_a_(this.hardCoreMode);
         WorldSettings worldsettings;
         if (dimensiongeneratorsettings.func_236227_h_()) {
            GameRules gamerules = new GameRules();
            gamerules.get(GameRules.DO_DAYLIGHT_CYCLE).set(false, (MinecraftServer)null);
            worldsettings = new WorldSettings(this.worldNameField.getText().trim(), GameType.SPECTATOR, false, Difficulty.PEACEFUL, true, gamerules, DatapackCodec.field_234880_a_);
         } else {
            worldsettings = new WorldSettings(this.worldNameField.getText().trim(), this.field_228197_f_.field_228218_f_, this.hardCoreMode, this.field_238937_w_, this.allowCheats && !this.hardCoreMode, this.field_238932_M_, this.field_238933_b_);
         }

         this.minecraft.func_238192_a_(this.saveDirName, worldsettings, this.field_238934_c_.func_239055_b_(), dimensiongeneratorsettings);
      }
   }

   /**
    * Toggles between initial world-creation display
    */
   private void toggleMoreWorldOptions() {
      this.showMoreWorldOptions(!this.inMoreWorldOptionsDisplay);
   }

   private void func_228200_a_(CreateWorldScreen.GameMode p_228200_1_) {
      if (!this.allowCheatsWasSetByUser) {
         this.allowCheats = p_228200_1_ == CreateWorldScreen.GameMode.CREATIVE;
      }

      if (p_228200_1_ == CreateWorldScreen.GameMode.HARDCORE) {
         this.hardCoreMode = true;
         this.btnAllowCommands.active = false;
         this.field_238934_c_.field_239027_a_.active = false;
         this.field_238937_w_ = Difficulty.HARD;
         this.field_238929_E_.active = false;
      } else {
         this.hardCoreMode = false;
         this.btnAllowCommands.active = true;
         this.field_238934_c_.field_239027_a_.active = true;
         this.field_238937_w_ = this.field_238936_v_;
         this.field_238929_E_.active = true;
      }

      this.field_228197_f_ = p_228200_1_;
      this.func_228199_a_();
   }

   public void func_238955_g_() {
      this.showMoreWorldOptions(this.inMoreWorldOptionsDisplay);
   }

   /**
    * Shows additional world-creation options if toggle is true
    */
   private void showMoreWorldOptions(boolean toggle) {
      this.inMoreWorldOptionsDisplay = toggle;
      this.btnGameMode.visible = !this.inMoreWorldOptionsDisplay;
      this.field_238929_E_.visible = !this.inMoreWorldOptionsDisplay;
      if (this.field_238934_c_.func_239042_a_()) {
         this.field_238931_H_.visible = false;
         this.btnGameMode.active = false;
         if (this.field_228198_g_ == null) {
            this.field_228198_g_ = this.field_228197_f_;
         }

         this.func_228200_a_(CreateWorldScreen.GameMode.DEBUG);
         this.btnAllowCommands.visible = false;
      } else {
         this.btnGameMode.active = true;
         if (this.field_228198_g_ != null) {
            this.func_228200_a_(this.field_228198_g_);
         }

         this.btnAllowCommands.visible = !this.inMoreWorldOptionsDisplay;
         this.field_238931_H_.visible = !this.inMoreWorldOptionsDisplay;
      }

      this.field_238934_c_.func_239059_b_(this.inMoreWorldOptionsDisplay);
      this.worldNameField.setVisible(!this.inMoreWorldOptionsDisplay);
      if (this.inMoreWorldOptionsDisplay) {
         this.btnMoreOptions.setMessage(DialogTexts.field_240632_c_);
      } else {
         this.btnMoreOptions.setMessage(new TranslationTextComponent("selectWorld.moreWorldOptions"));
      }

      this.field_238930_G_.visible = !this.inMoreWorldOptionsDisplay;
   }

   public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
      if (super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_)) {
         return true;
      } else if (p_231046_1_ != 257 && p_231046_1_ != 335) {
         return false;
      } else {
         this.createWorld();
         return true;
      }
   }

   public void onClose() {
      if (this.inMoreWorldOptionsDisplay) {
         this.showMoreWorldOptions(false);
      } else {
         this.minecraft.displayGuiScreen(this.parentScreen);
      }

      this.func_238959_w_();
   }

   public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      this.renderBackground(p_230430_1_);
      this.drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 20, -1);
      if (this.inMoreWorldOptionsDisplay) {
         this.drawString(p_230430_1_, this.font, I18n.format("selectWorld.enterSeed"), this.width / 2 - 100, 47, -6250336);
         this.drawString(p_230430_1_, this.font, I18n.format("selectWorld.seedInfo"), this.width / 2 - 100, 85, -6250336);
         this.field_238934_c_.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
      } else {
         this.drawString(p_230430_1_, this.font, I18n.format("selectWorld.enterName"), this.width / 2 - 100, 47, -6250336);
         this.drawString(p_230430_1_, this.font, I18n.format("selectWorld.resultFolder") + " " + this.saveDirName, this.width / 2 - 100, 85, -6250336);
         this.worldNameField.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
         this.drawString(p_230430_1_, this.font, this.gameModeDesc1, this.width / 2 - 150, 122, -6250336);
         this.drawString(p_230430_1_, this.font, this.gameModeDesc2, this.width / 2 - 150, 134, -6250336);
         if (this.btnAllowCommands.visible) {
            this.drawString(p_230430_1_, this.font, I18n.format("selectWorld.allowCommands.info"), this.width / 2 - 150, 172, -6250336);
         }
      }

      super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
   }

   protected <T extends IGuiEventListener> T addListener(T p_230481_1_) {
      return super.addListener(p_230481_1_);
   }

   protected <T extends Widget> T addButton(T p_230480_1_) {
      return super.addButton(p_230480_1_);
   }

   @Nullable
   protected Path func_238957_j_() {
      if (this.field_238928_A_ == null) {
         try {
            this.field_238928_A_ = Files.createTempDirectory("mcworld-");
         } catch (IOException ioexception) {
            field_238935_p_.warn("Failed to create temporary dir", (Throwable)ioexception);
            SystemToast.func_238539_c_(this.minecraft, this.saveDirName);
            this.minecraft.displayGuiScreen(this.parentScreen);
         }
      }

      return this.field_238928_A_;
   }

   private void func_238958_v_() {
      Path path = this.func_238957_j_();
      if (path != null) {
         File file1 = path.toFile();
         ResourcePackList<ResourcePackInfo> resourcepacklist = new ResourcePackList<>(ResourcePackInfo::new, new ServerPackFinder(), new FolderPackFinder(file1, IPackNameDecorator.field_232625_a_));
         resourcepacklist.reloadPacksFromFinders();
         resourcepacklist.setEnabledPacks(this.field_238933_b_.func_234884_a_());
         this.minecraft.displayGuiScreen(new DataPackScreen(this, resourcepacklist, this::func_241621_a_, file1));
      }

   }

   private void func_241621_a_(ResourcePackList<ResourcePackInfo> p_241621_1_) {
      List<String> list = ImmutableList.copyOf(p_241621_1_.func_232621_d_());
      List<String> list1 = p_241621_1_.func_232616_b_().stream().filter((p_241626_1_) -> {
         return !list.contains(p_241626_1_);
      }).collect(ImmutableList.toImmutableList());
      DatapackCodec datapackcodec = new DatapackCodec(list, list1);
      if (list.equals(this.field_238933_b_.func_234884_a_())) {
         this.field_238933_b_ = datapackcodec;
      } else {
         this.minecraft.enqueue(() -> {
            this.minecraft.displayGuiScreen(new DirtMessageScreen(new TranslationTextComponent("dataPack.validation.working")));
         });
         DataPackRegistries.func_240961_a_(p_241621_1_.func_232623_f_(), Commands.EnvironmentType.INTEGRATED, 2, Util.getServerExecutor(), this.minecraft).handle((p_241623_2_, p_241623_3_) -> {
            if (p_241623_3_ != null) {
               field_238935_p_.warn("Failed to validate datapack", p_241623_3_);
               this.minecraft.enqueue(() -> {
                  this.minecraft.displayGuiScreen(new ConfirmScreen((p_241630_1_) -> {
                     if (p_241630_1_) {
                        this.func_238958_v_();
                     } else {
                        this.field_238933_b_ = DatapackCodec.field_234880_a_;
                        this.minecraft.displayGuiScreen(this);
                     }

                  }, new TranslationTextComponent("dataPack.validation.failed"), StringTextComponent.EMPTY, new TranslationTextComponent("dataPack.validation.back"), new TranslationTextComponent("dataPack.validation.reset")));
               });
            } else {
               this.minecraft.enqueue(() -> {
                  this.field_238933_b_ = datapackcodec;
                  this.minecraft.displayGuiScreen(this);
               });
            }

            return null;
         });
      }
   }

   private void func_238959_w_() {
      if (this.field_238928_A_ != null) {
         try (Stream<Path> stream = Files.walk(this.field_238928_A_)) {
            stream.sorted(Comparator.reverseOrder()).forEach((p_238948_0_) -> {
               try {
                  Files.delete(p_238948_0_);
               } catch (IOException ioexception1) {
                  field_238935_p_.warn("Failed to remove temporary file {}", p_238948_0_, ioexception1);
               }

            });
         } catch (IOException ioexception) {
            field_238935_p_.warn("Failed to list temporary dir {}", (Object)this.field_238928_A_);
         }

         this.field_238928_A_ = null;
      }

   }

   private static void func_238945_a_(Path p_238945_0_, Path p_238945_1_, Path p_238945_2_) {
      try {
         Util.func_240984_a_(p_238945_0_, p_238945_1_, p_238945_2_);
      } catch (IOException ioexception) {
         field_238935_p_.warn("Failed to copy datapack file from {} to {}", p_238945_2_, p_238945_1_);
         throw new CreateWorldScreen.DatapackException(ioexception);
      }
   }

   private boolean func_238960_x_() {
      if (this.field_238928_A_ != null) {
         try (
            SaveFormat.LevelSave saveformat$levelsave = this.minecraft.getSaveLoader().func_237274_c_(this.saveDirName);
            Stream<Path> stream = Files.walk(this.field_238928_A_);
         ) {
            Path path = saveformat$levelsave.func_237285_a_(FolderName.field_237251_g_);
            Files.createDirectories(path);
            stream.filter((p_238942_1_) -> {
               return !p_238942_1_.equals(this.field_238928_A_);
            }).forEach((p_238949_2_) -> {
               func_238945_a_(this.field_238928_A_, path, p_238949_2_);
            });
         } catch (CreateWorldScreen.DatapackException | IOException ioexception) {
            field_238935_p_.warn("Failed to copy datapacks to world {}", this.saveDirName, ioexception);
            SystemToast.func_238539_c_(this.minecraft, this.saveDirName);
            this.minecraft.displayGuiScreen(this.parentScreen);
            this.func_238959_w_();
            return false;
         }

         this.func_238959_w_();
      }

      return true;
   }

   @Nullable
   public static Path func_238943_a_(Path p_238943_0_, Minecraft p_238943_1_) {
      MutableObject<Path> mutableobject = new MutableObject<>();

      try (Stream<Path> stream = Files.walk(p_238943_0_)) {
         stream.filter((p_238944_1_) -> {
            return !p_238944_1_.equals(p_238943_0_);
         }).forEach((p_238947_2_) -> {
            Path path = mutableobject.getValue();
            if (path == null) {
               try {
                  path = Files.createTempDirectory("mcworld-");
               } catch (IOException ioexception1) {
                  field_238935_p_.warn("Failed to create temporary dir");
                  throw new CreateWorldScreen.DatapackException(ioexception1);
               }

               mutableobject.setValue(path);
            }

            func_238945_a_(p_238943_0_, path, p_238947_2_);
         });
      } catch (CreateWorldScreen.DatapackException | IOException ioexception) {
         field_238935_p_.warn("Failed to copy datapacks from world {}", p_238943_0_, ioexception);
         SystemToast.func_238539_c_(p_238943_1_, p_238943_0_.toString());
         return null;
      }

      return mutableobject.getValue();
   }

   @OnlyIn(Dist.CLIENT)
   static class DatapackException extends RuntimeException {
      public DatapackException(Throwable p_i232309_1_) {
         super(p_i232309_1_);
      }
   }

   @OnlyIn(Dist.CLIENT)
   static enum GameMode {
      SURVIVAL("survival", GameType.SURVIVAL),
      HARDCORE("hardcore", GameType.SURVIVAL),
      CREATIVE("creative", GameType.CREATIVE),
      DEBUG("spectator", GameType.SPECTATOR);

      private final String field_228217_e_;
      private final GameType field_228218_f_;

      private GameMode(String p_i225940_3_, GameType p_i225940_4_) {
         this.field_228217_e_ = p_i225940_3_;
         this.field_228218_f_ = p_i225940_4_;
      }
   }
}