package net.minecraft.loot;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class LootEntryManager {
   public static final LootPoolEntryType EMPTY = register("empty", new EmptyLootEntry.Serializer());
   public static final LootPoolEntryType ITEM = register("item", new ItemLootEntry.Serializer());
   public static final LootPoolEntryType LOOT_TABLE = register("loot_table", new TableLootEntry.Serializer());
   public static final LootPoolEntryType DYNAMIC = register("dynamic", new DynamicLootEntry.Serializer());
   public static final LootPoolEntryType TAG = register("tag", new TagLootEntry.Serializer());
   public static final LootPoolEntryType ALTERNATIVE = register("alternatives", ParentedLootEntry.func_237409_a_(AlternativesLootEntry::new));
   public static final LootPoolEntryType SEQUENCE = register("sequence", ParentedLootEntry.func_237409_a_(SequenceLootEntry::new));
   public static final LootPoolEntryType GROUP = register("group", ParentedLootEntry.func_237409_a_(GroupLootEntry::new));

   private static LootPoolEntryType register(String nameIn, ILootSerializer<? extends LootEntry> serializerIn) {
      return Registry.register(Registry.field_239693_aY_, new ResourceLocation(nameIn), new LootPoolEntryType(serializerIn));
   }

   public static Object func_237418_a_() {
      return LootTypesManager.func_237389_a_(Registry.field_239693_aY_, "entry", "type", LootEntry::func_230420_a_).func_237395_a_();
   }
}