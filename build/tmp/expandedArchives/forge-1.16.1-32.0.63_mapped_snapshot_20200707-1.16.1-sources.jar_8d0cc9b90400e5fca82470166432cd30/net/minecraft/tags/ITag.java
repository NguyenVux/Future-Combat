package net.minecraft.tags;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public interface ITag<T> {
   static <T> Codec<ITag<T>> getTagCodec(Supplier<TagCollection<T>> p_232947_0_) {
      return ResourceLocation.RESOURCE_LOCATION_CODEC.flatXmap((p_232949_1_) -> {
         return Optional.ofNullable(p_232947_0_.get().get(p_232949_1_)).map(DataResult::success).orElseGet(() -> {
            return DataResult.error("Unknown tag: " + p_232949_1_);
         });
      }, (p_232948_1_) -> {
         return Optional.ofNullable(p_232947_0_.get().func_232973_a_(p_232948_1_)).map(DataResult::success).orElseGet(() -> {
            return DataResult.error("Unknown tag: " + p_232948_1_);
         });
      });
   }

   boolean contains(T elementIn);

   List<T> getAllElements();

   default T getRandomElement(Random random) {
      List<T> list = this.getAllElements();
      return list.get(random.nextInt(list.size()));
   }

   static <T> ITag<T> getTagOf(Set<T> elementsIn) {
      return Tag.func_241286_a_(elementsIn);
   }

   public static class Builder {
      private final List<ITag.Proxy> proxyTags = Lists.newArrayList();
      private boolean replace = false;

      public static ITag.Builder create() {
         return new ITag.Builder();
      }

      public ITag.Builder addProxyTag(ITag.Proxy proxyTagIn) {
         this.proxyTags.add(proxyTagIn);
         return this;
      }

      public ITag.Builder addTag(ITag.ITagEntry tagEntryIn, String identifierIn) {
         return this.addProxyTag(new ITag.Proxy(tagEntryIn, identifierIn));
      }

      public ITag.Builder addItemEntry(ResourceLocation registryNameIn, String identifierIn) {
         return this.addTag(new ITag.ItemEntry(registryNameIn), identifierIn);
      }

      public ITag.Builder addTagEntry(ResourceLocation tagIn, String identifierIn) {
         return this.addTag(new ITag.TagEntry(tagIn), identifierIn);
      }

      public ITag.Builder replace(boolean value) {
         this.replace = value;
         return this;
      }

      public ITag.Builder replace() {
         return replace(true);
      }

      public <T> Optional<ITag<T>> func_232959_a_(Function<ResourceLocation, ITag<T>> p_232959_1_, Function<ResourceLocation, T> p_232959_2_) {
         ImmutableSet.Builder<T> builder = ImmutableSet.builder();

         for(ITag.Proxy itag$proxy : this.proxyTags) {
            if (!itag$proxy.getEntry().func_230238_a_(p_232959_1_, p_232959_2_, builder::add)) {
               return Optional.empty();
            }
         }

         return Optional.of(ITag.getTagOf(builder.build()));
      }

      public Stream<ITag.Proxy> getProxyStream() {
         return this.proxyTags.stream();
      }

      public <T> Stream<ITag.Proxy> func_232963_b_(Function<ResourceLocation, ITag<T>> p_232963_1_, Function<ResourceLocation, T> p_232963_2_) {
         return this.getProxyStream().filter((p_232960_2_) -> {
            return !p_232960_2_.getEntry().func_230238_a_(p_232963_1_, p_232963_2_, (p_232957_0_) -> {
            });
         });
      }

      public ITag.Builder deserialize(JsonObject jsonIn, String identifierIn) {
         JsonArray jsonarray = JSONUtils.getJsonArray(jsonIn, "values");
         List<ITag.ITagEntry> list = Lists.newArrayList();

         for(JsonElement jsonelement : jsonarray) {
            String s = JSONUtils.getString(jsonelement, "value");
            if (s.startsWith("#")) {
               list.add(new ITag.TagEntry(new ResourceLocation(s.substring(1))));
            } else {
               list.add(new ITag.ItemEntry(new ResourceLocation(s)));
            }
         }

         if (JSONUtils.getBoolean(jsonIn, "replace", false)) {
            this.proxyTags.clear();
         }

         net.minecraftforge.common.ForgeHooks.deserializeTagAdditions(list, jsonIn, proxyTags);
         list.forEach((p_232958_2_) -> {
            this.proxyTags.add(new ITag.Proxy(p_232958_2_, identifierIn));
         });
         return this;
      }

      public JsonObject serialize() {
         JsonObject jsonobject = new JsonObject();
         JsonArray jsonarray = new JsonArray();

         for(ITag.Proxy itag$proxy : this.proxyTags) {
            if(!(itag$proxy.entry instanceof net.minecraftforge.common.data.IOptionalTagEntry))
            itag$proxy.getEntry().func_230237_a_(jsonarray);
         }

         JsonArray optopnals = new JsonArray();
          getProxyStream()
                 .map(e -> e.entry)
                 .filter(e -> e instanceof net.minecraftforge.common.data.IOptionalTagEntry)
                 .forEach(e -> e.func_230237_a_(optopnals));

         jsonobject.addProperty("replace", replace);
         jsonobject.add("values", jsonarray);
         if (optopnals.size() > 0)
             jsonobject.add("optional", optopnals);
         return jsonobject;
      }
   }

   public interface INamedTag<T> extends ITag<T> {
      ResourceLocation getName();
   }

   public interface ITagEntry {
      <T> boolean func_230238_a_(Function<ResourceLocation, ITag<T>> p_230238_1_, Function<ResourceLocation, T> p_230238_2_, Consumer<T> p_230238_3_);

      void func_230237_a_(JsonArray p_230237_1_);
   }

   public static class ItemEntry implements ITag.ITagEntry {
      private final ResourceLocation field_232969_a_;

      public ItemEntry(ResourceLocation p_i231435_1_) {
         this.field_232969_a_ = p_i231435_1_;
      }

      public <T> boolean func_230238_a_(Function<ResourceLocation, ITag<T>> p_230238_1_, Function<ResourceLocation, T> p_230238_2_, Consumer<T> p_230238_3_) {
         T t = p_230238_2_.apply(this.field_232969_a_);
         if (t == null) {
            return false;
         } else {
            p_230238_3_.accept(t);
            return true;
         }
      }

      public void func_230237_a_(JsonArray p_230237_1_) {
         p_230237_1_.add(this.field_232969_a_.toString());
      }

      public String toString() {
         return this.field_232969_a_.toString();
      }
      @Override public boolean equals(Object o) { return o == this || (o instanceof ITag.ItemEntry && java.util.Objects.equals(this.field_232969_a_, ((ITag.ItemEntry) o).field_232969_a_)); }
   }

   public static class Proxy {
      private final ITag.ITagEntry entry;
      private final String identifier;

      private Proxy(ITag.ITagEntry entryIn, String identifierIn) {
         this.entry = entryIn;
         this.identifier = identifierIn;
      }

      public ITag.ITagEntry getEntry() {
         return this.entry;
      }

      public String toString() {
         return this.entry.toString() + " (from " + this.identifier + ")";
      }
   }

   public static class TagEntry implements ITag.ITagEntry {
      private final ResourceLocation id;

      public TagEntry(ResourceLocation resourceLocationIn) {
         this.id = resourceLocationIn;
      }

      public <T> boolean func_230238_a_(Function<ResourceLocation, ITag<T>> p_230238_1_, Function<ResourceLocation, T> p_230238_2_, Consumer<T> p_230238_3_) {
         ITag<T> itag = p_230238_1_.apply(this.id);
         if (itag == null) {
            return false;
         } else {
            itag.getAllElements().forEach(p_230238_3_);
            return true;
         }
      }

      public void func_230237_a_(JsonArray p_230237_1_) {
         p_230237_1_.add("#" + this.id);
      }

      public String toString() {
         return "#" + this.id;
      }
      @Override public boolean equals(Object o) { return o == this || (o instanceof ITag.TagEntry && java.util.Objects.equals(this.id, ((ITag.TagEntry) o).id)); }
   }
}