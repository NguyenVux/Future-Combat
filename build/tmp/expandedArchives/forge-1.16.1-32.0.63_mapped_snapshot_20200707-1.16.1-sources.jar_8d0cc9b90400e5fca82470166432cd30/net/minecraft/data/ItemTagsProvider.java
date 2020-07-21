package net.minecraft.data;

import java.nio.file.Path;
import java.util.function.Function;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ItemTagsProvider extends TagsProvider<Item> {
   private final Function<ITag.INamedTag<Block>, ITag.Builder> field_240520_d_;

   public ItemTagsProvider(DataGenerator p_i232552_1_, BlockTagsProvider p_i232552_2_) {
      super(p_i232552_1_, Registry.ITEM);
      this.field_240520_d_ = p_i232552_2_::func_240525_b_;
   }

   protected void registerTags() {
      this.func_240521_a_(BlockTags.WOOL, ItemTags.WOOL);
      this.func_240521_a_(BlockTags.PLANKS, ItemTags.PLANKS);
      this.func_240521_a_(BlockTags.STONE_BRICKS, ItemTags.STONE_BRICKS);
      this.func_240521_a_(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
      this.func_240521_a_(BlockTags.BUTTONS, ItemTags.BUTTONS);
      this.func_240521_a_(BlockTags.CARPETS, ItemTags.CARPETS);
      this.func_240521_a_(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
      this.func_240521_a_(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
      this.func_240521_a_(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
      this.func_240521_a_(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
      this.func_240521_a_(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
      this.func_240521_a_(BlockTags.DOORS, ItemTags.DOORS);
      this.func_240521_a_(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
      this.func_240521_a_(BlockTags.OAK_LOGS, ItemTags.OAK_LOGS);
      this.func_240521_a_(BlockTags.DARK_OAK_LOGS, ItemTags.DARK_OAK_LOGS);
      this.func_240521_a_(BlockTags.BIRCH_LOGS, ItemTags.BIRCH_LOGS);
      this.func_240521_a_(BlockTags.ACACIA_LOGS, ItemTags.ACACIA_LOGS);
      this.func_240521_a_(BlockTags.SPRUCE_LOGS, ItemTags.SPRUCE_LOGS);
      this.func_240521_a_(BlockTags.JUNGLE_LOGS, ItemTags.JUNGLE_LOGS);
      this.func_240521_a_(BlockTags.CRIMSON_STEMS, ItemTags.field_232913_w_);
      this.func_240521_a_(BlockTags.WARPED_STEMS, ItemTags.field_232914_x_);
      this.func_240521_a_(BlockTags.LOGS_THAT_BURN, ItemTags.field_232912_o_);
      this.func_240521_a_(BlockTags.LOGS, ItemTags.LOGS);
      this.func_240521_a_(BlockTags.SAND, ItemTags.SAND);
      this.func_240521_a_(BlockTags.SLABS, ItemTags.SLABS);
      this.func_240521_a_(BlockTags.WALLS, ItemTags.WALLS);
      this.func_240521_a_(BlockTags.STAIRS, ItemTags.STAIRS);
      this.func_240521_a_(BlockTags.ANVIL, ItemTags.ANVIL);
      this.func_240521_a_(BlockTags.RAILS, ItemTags.RAILS);
      this.func_240521_a_(BlockTags.LEAVES, ItemTags.LEAVES);
      this.func_240521_a_(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
      this.func_240521_a_(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);
      this.func_240521_a_(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);
      this.func_240521_a_(BlockTags.BEDS, ItemTags.BEDS);
      this.func_240521_a_(BlockTags.FENCES, ItemTags.FENCES);
      this.func_240521_a_(BlockTags.TALL_FLOWERS, ItemTags.TALL_FLOWERS);
      this.func_240521_a_(BlockTags.FLOWERS, ItemTags.FLOWERS);
      this.func_240521_a_(BlockTags.GOLD_ORES, ItemTags.field_232904_O_);
      this.func_240521_a_(BlockTags.SOUL_FIRE_BASE_BLOCKS, ItemTags.field_232906_Q_);
      this.func_240522_a_(ItemTags.BANNERS).func_240534_a_(Items.WHITE_BANNER, Items.ORANGE_BANNER, Items.MAGENTA_BANNER, Items.LIGHT_BLUE_BANNER, Items.YELLOW_BANNER, Items.LIME_BANNER, Items.PINK_BANNER, Items.GRAY_BANNER, Items.LIGHT_GRAY_BANNER, Items.CYAN_BANNER, Items.PURPLE_BANNER, Items.BLUE_BANNER, Items.BROWN_BANNER, Items.GREEN_BANNER, Items.RED_BANNER, Items.BLACK_BANNER);
      this.func_240522_a_(ItemTags.BOATS).func_240534_a_(Items.OAK_BOAT, Items.SPRUCE_BOAT, Items.BIRCH_BOAT, Items.JUNGLE_BOAT, Items.ACACIA_BOAT, Items.DARK_OAK_BOAT);
      this.func_240522_a_(ItemTags.FISHES).func_240534_a_(Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH);
      this.func_240521_a_(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);
      this.func_240522_a_(ItemTags.field_232907_V_).func_240534_a_(Items.MUSIC_DISC_13, Items.MUSIC_DISC_CAT, Items.MUSIC_DISC_BLOCKS, Items.MUSIC_DISC_CHIRP, Items.MUSIC_DISC_FAR, Items.MUSIC_DISC_MALL, Items.MUSIC_DISC_MELLOHI, Items.MUSIC_DISC_STAL, Items.MUSIC_DISC_STRAD, Items.MUSIC_DISC_WARD, Items.MUSIC_DISC_11, Items.MUSIC_DISC_WAIT);
      this.func_240522_a_(ItemTags.MUSIC_DISCS).func_240531_a_(ItemTags.field_232907_V_).func_240532_a_(Items.MUSIC_DISC_PIGSTEP);
      this.func_240522_a_(ItemTags.COALS).func_240534_a_(Items.COAL, Items.CHARCOAL);
      this.func_240522_a_(ItemTags.ARROWS).func_240534_a_(Items.ARROW, Items.TIPPED_ARROW, Items.SPECTRAL_ARROW);
      this.func_240522_a_(ItemTags.LECTERN_BOOKS).func_240534_a_(Items.WRITTEN_BOOK, Items.WRITABLE_BOOK);
      this.func_240522_a_(ItemTags.field_232908_Z_).func_240534_a_(Items.NETHERITE_INGOT, Items.EMERALD, Items.DIAMOND, Items.GOLD_INGOT, Items.IRON_INGOT);
      this.func_240522_a_(ItemTags.field_232902_M_).func_240532_a_(Items.SOUL_TORCH).func_240532_a_(Items.SOUL_LANTERN).func_240532_a_(Items.SOUL_CAMPFIRE);
      this.func_240522_a_(ItemTags.field_232903_N_).func_240531_a_(ItemTags.field_232904_O_).func_240534_a_(Items.GOLD_BLOCK, Items.GILDED_BLACKSTONE, Items.LIGHT_WEIGHTED_PRESSURE_PLATE, Items.GOLD_INGOT, Items.BELL, Items.CLOCK, Items.GOLDEN_CARROT, Items.GLISTERING_MELON_SLICE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLDEN_HORSE_ARMOR, Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE);
      this.func_240522_a_(ItemTags.field_232905_P_).func_240534_a_(Items.WARPED_STEM, Items.STRIPPED_WARPED_STEM, Items.WARPED_HYPHAE, Items.STRIPPED_WARPED_HYPHAE, Items.CRIMSON_STEM, Items.STRIPPED_CRIMSON_STEM, Items.CRIMSON_HYPHAE, Items.STRIPPED_CRIMSON_HYPHAE, Items.CRIMSON_PLANKS, Items.WARPED_PLANKS, Items.CRIMSON_SLAB, Items.WARPED_SLAB, Items.CRIMSON_PRESSURE_PLATE, Items.WARPED_PRESSURE_PLATE, Items.CRIMSON_FENCE, Items.WARPED_FENCE, Items.CRIMSON_TRAPDOOR, Items.WARPED_TRAPDOOR, Items.CRIMSON_FENCE_GATE, Items.WARPED_FENCE_GATE, Items.CRIMSON_STAIRS, Items.WARPED_STAIRS, Items.CRIMSON_BUTTON, Items.WARPED_BUTTON, Items.CRIMSON_DOOR, Items.WARPED_DOOR, Items.CRIMSON_SIGN, Items.WARPED_SIGN);
      this.func_240522_a_(ItemTags.field_232909_aa_).func_240534_a_(Items.COBBLESTONE, Items.BLACKSTONE);
      this.func_240522_a_(ItemTags.field_232910_ab_).func_240534_a_(Items.COBBLESTONE, Items.BLACKSTONE);
   }

   protected void func_240521_a_(ITag.INamedTag<Block> p_240521_1_, ITag.INamedTag<Item> p_240521_2_) {
      ITag.Builder itag$builder = this.func_240525_b_(p_240521_2_);
      ITag.Builder itag$builder1 = this.field_240520_d_.apply(p_240521_1_);
      itag$builder1.getProxyStream().forEach(itag$builder::addProxyTag);
   }

   /**
    * Resolves a Path for the location to save the given tag.
    */
   protected Path makePath(ResourceLocation id) {
      return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/items/" + id.getPath() + ".json");
   }

   /**
    * Gets a name for this provider
    */
   public String getName() {
      return "Item Tags";
   }
}