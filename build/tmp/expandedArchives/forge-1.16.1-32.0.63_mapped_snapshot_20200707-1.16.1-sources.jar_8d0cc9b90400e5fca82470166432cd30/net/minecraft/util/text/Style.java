package net.minecraft.util.text;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Style {
   public static final ResourceLocation DEFAULT_FONT = new ResourceLocation("minecraft", "default");
   public static final Style EMPTY = new Style((Color)null, (Boolean)null, (Boolean)null, (Boolean)null, (Boolean)null, (Boolean)null, (ClickEvent)null, (HoverEvent)null, (String)null, (ResourceLocation)null);
   @Nullable
   private final Color color;
   @Nullable
   private final Boolean bold;
   @Nullable
   private final Boolean italic;
   @Nullable
   private final Boolean underlined;
   @Nullable
   private final Boolean strikethrough;
   @Nullable
   private final Boolean obfuscated;
   @Nullable
   private final ClickEvent clickEvent;
   @Nullable
   private final HoverEvent hoverEvent;
   @Nullable
   private final String insertion;
   @Nullable
   private final ResourceLocation fontId;

   private Style(@Nullable Color colorIn, @Nullable Boolean boldIn, @Nullable Boolean italicIn, @Nullable Boolean underlinedIn, @Nullable Boolean strikethroughIn, @Nullable Boolean obfuscatedIn, @Nullable ClickEvent clickEventIn, @Nullable HoverEvent hoverEventIn, @Nullable String insertionIn, @Nullable ResourceLocation fontIdIn) {
      this.color = colorIn;
      this.bold = boldIn;
      this.italic = italicIn;
      this.underlined = underlinedIn;
      this.strikethrough = strikethroughIn;
      this.obfuscated = obfuscatedIn;
      this.clickEvent = clickEventIn;
      this.hoverEvent = hoverEventIn;
      this.insertion = insertionIn;
      this.fontId = fontIdIn;
   }

   @Nullable
   public Color getColor() {
      return this.color;
   }

   /**
    * Whether or not text of this ChatStyle should be in bold.
    */
   public boolean getBold() {
      return this.bold == Boolean.TRUE;
   }

   /**
    * Whether or not text of this ChatStyle should be italicized.
    */
   public boolean getItalic() {
      return this.italic == Boolean.TRUE;
   }

   /**
    * Whether or not to format text of this ChatStyle using strikethrough.
    */
   public boolean getStrikethrough() {
      return this.strikethrough == Boolean.TRUE;
   }

   /**
    * Whether or not text of this ChatStyle should be underlined.
    */
   public boolean getUnderlined() {
      return this.underlined == Boolean.TRUE;
   }

   /**
    * Whether or not text of this ChatStyle should be obfuscated.
    */
   public boolean getObfuscated() {
      return this.obfuscated == Boolean.TRUE;
   }

   /**
    * Whether or not this style is empty (inherits everything from the parent).
    */
   public boolean isEmpty() {
      return this == EMPTY;
   }

   /**
    * The effective chat click event.
    */
   @Nullable
   public ClickEvent getClickEvent() {
      return this.clickEvent;
   }

   /**
    * The effective chat hover event.
    */
   @Nullable
   public HoverEvent getHoverEvent() {
      return this.hoverEvent;
   }

   /**
    * Get the text to be inserted into Chat when the component is shift-clicked
    */
   @Nullable
   public String getInsertion() {
      return this.insertion;
   }

   public ResourceLocation getFontId() {
      return this.fontId != null ? this.fontId : DEFAULT_FONT;
   }

   public Style setColor(@Nullable Color colorIn) {
      return new Style(colorIn, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.fontId);
   }

   public Style setFormatting(@Nullable TextFormatting formattingIn) {
      return this.setColor(formattingIn != null ? Color.func_240744_a_(formattingIn) : null);
   }

   public Style setBold(@Nullable Boolean boldIn) {
      return new Style(this.color, boldIn, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.fontId);
   }

   public Style setItalic(@Nullable Boolean italicIn) {
      return new Style(this.color, this.bold, italicIn, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.fontId);
   }

   public Style setUnderlined(@Nullable Boolean underlined) {
      return new Style(this.color, this.bold, this.italic, underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.fontId);
   }

   public Style setStrikethrough(@Nullable Boolean strikethrough) {
      return new Style(this.color, this.bold, this.italic, this.underlined, strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.fontId);
   }

   public Style setObfuscated(@Nullable Boolean obfuscated) {
      return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.fontId);
   }

   public Style setClickEvent(@Nullable ClickEvent clickEventIn) {
      return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, clickEventIn, this.hoverEvent, this.insertion, this.fontId);
   }

   public Style setHoverEvent(@Nullable HoverEvent hoverEventIn) {
      return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, hoverEventIn, this.insertion, this.fontId);
   }

   public Style setInsertion(@Nullable String insertionIn) {
      return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, insertionIn, this.fontId);
   }

   @OnlyIn(Dist.CLIENT)
   public Style setFontId(@Nullable ResourceLocation fontIdIn) {
      return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, fontIdIn);
   }

   public Style applyFormatting(TextFormatting formattingIn) {
      Color color = this.color;
      Boolean obool = this.bold;
      Boolean obool1 = this.italic;
      Boolean obool2 = this.strikethrough;
      Boolean obool3 = this.underlined;
      Boolean obool4 = this.obfuscated;
      switch(formattingIn) {
      case OBFUSCATED:
         obool4 = true;
         break;
      case BOLD:
         obool = true;
         break;
      case STRIKETHROUGH:
         obool2 = true;
         break;
      case UNDERLINE:
         obool3 = true;
         break;
      case ITALIC:
         obool1 = true;
         break;
      case RESET:
         return EMPTY;
      default:
         color = Color.func_240744_a_(formattingIn);
      }

      return new Style(color, obool, obool1, obool3, obool2, obool4, this.clickEvent, this.hoverEvent, this.insertion, this.fontId);
   }

   @OnlyIn(Dist.CLIENT)
   public Style forceFormatting(TextFormatting formattingIn) {
      Color color = this.color;
      Boolean obool = this.bold;
      Boolean obool1 = this.italic;
      Boolean obool2 = this.strikethrough;
      Boolean obool3 = this.underlined;
      Boolean obool4 = this.obfuscated;
      switch(formattingIn) {
      case OBFUSCATED:
         obool4 = true;
         break;
      case BOLD:
         obool = true;
         break;
      case STRIKETHROUGH:
         obool2 = true;
         break;
      case UNDERLINE:
         obool3 = true;
         break;
      case ITALIC:
         obool1 = true;
         break;
      case RESET:
         return EMPTY;
      default:
         obool4 = false;
         obool = false;
         obool2 = false;
         obool3 = false;
         obool1 = false;
         color = Color.func_240744_a_(formattingIn);
      }

      return new Style(color, obool, obool1, obool3, obool2, obool4, this.clickEvent, this.hoverEvent, this.insertion, this.fontId);
   }

   public Style createStyleFromFormattings(TextFormatting... formatingsIn) {
      Color color = this.color;
      Boolean obool = this.bold;
      Boolean obool1 = this.italic;
      Boolean obool2 = this.strikethrough;
      Boolean obool3 = this.underlined;
      Boolean obool4 = this.obfuscated;

      for(TextFormatting textformatting : formatingsIn) {
         switch(textformatting) {
         case OBFUSCATED:
            obool4 = true;
            break;
         case BOLD:
            obool = true;
            break;
         case STRIKETHROUGH:
            obool2 = true;
            break;
         case UNDERLINE:
            obool3 = true;
            break;
         case ITALIC:
            obool1 = true;
            break;
         case RESET:
            return EMPTY;
         default:
            color = Color.func_240744_a_(textformatting);
         }
      }

      return new Style(color, obool, obool1, obool3, obool2, obool4, this.clickEvent, this.hoverEvent, this.insertion, this.fontId);
   }

   /**
    * Merges the style with another one. If either styles are empty the other will be returned. If a value already
    * exists on the current style it will not be overriden.
    */
   public Style mergeStyle(Style styleIn) {
      if (this == EMPTY) {
         return styleIn;
      } else {
         return styleIn == EMPTY ? this : new Style(this.color != null ? this.color : styleIn.color, this.bold != null ? this.bold : styleIn.bold, this.italic != null ? this.italic : styleIn.italic, this.underlined != null ? this.underlined : styleIn.underlined, this.strikethrough != null ? this.strikethrough : styleIn.strikethrough, this.obfuscated != null ? this.obfuscated : styleIn.obfuscated, this.clickEvent != null ? this.clickEvent : styleIn.clickEvent, this.hoverEvent != null ? this.hoverEvent : styleIn.hoverEvent, this.insertion != null ? this.insertion : styleIn.insertion, this.fontId != null ? this.fontId : styleIn.fontId);
      }
   }

   public String toString() {
      return "Style{ color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", strikethrough=" + this.strikethrough + ", obfuscated=" + this.obfuscated + ", clickEvent=" + this.getClickEvent() + ", hoverEvent=" + this.getHoverEvent() + ", insertion=" + this.getInsertion() + ", font=" + this.getFontId() + '}';
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof Style)) {
         return false;
      } else {
         Style style = (Style)p_equals_1_;
         return this.getBold() == style.getBold() && Objects.equals(this.getColor(), style.getColor()) && this.getItalic() == style.getItalic() && this.getObfuscated() == style.getObfuscated() && this.getStrikethrough() == style.getStrikethrough() && this.getUnderlined() == style.getUnderlined() && Objects.equals(this.getClickEvent(), style.getClickEvent()) && Objects.equals(this.getHoverEvent(), style.getHoverEvent()) && Objects.equals(this.getInsertion(), style.getInsertion()) && Objects.equals(this.getFontId(), style.getFontId());
      }
   }

   public int hashCode() {
      return Objects.hash(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion);
   }

   public static class Serializer implements JsonDeserializer<Style>, JsonSerializer<Style> {
      @Nullable
      public Style deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
         if (p_deserialize_1_.isJsonObject()) {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            if (jsonobject == null) {
               return null;
            } else {
               Boolean obool = deserializeBooleanValue(jsonobject, "bold");
               Boolean obool1 = deserializeBooleanValue(jsonobject, "italic");
               Boolean obool2 = deserializeBooleanValue(jsonobject, "underlined");
               Boolean obool3 = deserializeBooleanValue(jsonobject, "strikethrough");
               Boolean obool4 = deserializeBooleanValue(jsonobject, "obfuscated");
               Color color = deserializeColor(jsonobject);
               String s = deserializeInsertion(jsonobject);
               ClickEvent clickevent = deserializeClickEvent(jsonobject);
               HoverEvent hoverevent = deserializeHoverEvent(jsonobject);
               ResourceLocation resourcelocation = deserializeFont(jsonobject);
               return new Style(color, obool, obool1, obool2, obool3, obool4, clickevent, hoverevent, s, resourcelocation);
            }
         } else {
            return null;
         }
      }

      @Nullable
      private static ResourceLocation deserializeFont(JsonObject jsonIn) {
         if (jsonIn.has("font")) {
            String s = JSONUtils.getString(jsonIn, "font");

            try {
               return new ResourceLocation(s);
            } catch (ResourceLocationException resourcelocationexception) {
               throw new JsonSyntaxException("Invalid font name: " + s);
            }
         } else {
            return null;
         }
      }

      @Nullable
      private static HoverEvent deserializeHoverEvent(JsonObject jsonIn) {
         if (jsonIn.has("hoverEvent")) {
            JsonObject jsonobject = JSONUtils.getJsonObject(jsonIn, "hoverEvent");
            HoverEvent hoverevent = HoverEvent.deserialize(jsonobject);
            if (hoverevent != null && hoverevent.getAction().shouldAllowInChat()) {
               return hoverevent;
            }
         }

         return null;
      }

      @Nullable
      private static ClickEvent deserializeClickEvent(JsonObject jsonIn) {
         if (jsonIn.has("clickEvent")) {
            JsonObject jsonobject = JSONUtils.getJsonObject(jsonIn, "clickEvent");
            String s = JSONUtils.getString(jsonobject, "action", (String)null);
            ClickEvent.Action clickevent$action = s == null ? null : ClickEvent.Action.getValueByCanonicalName(s);
            String s1 = JSONUtils.getString(jsonobject, "value", (String)null);
            if (clickevent$action != null && s1 != null && clickevent$action.shouldAllowInChat()) {
               return new ClickEvent(clickevent$action, s1);
            }
         }

         return null;
      }

      @Nullable
      private static String deserializeInsertion(JsonObject jsonIn) {
         return JSONUtils.getString(jsonIn, "insertion", (String)null);
      }

      @Nullable
      private static Color deserializeColor(JsonObject jsonIn) {
         if (jsonIn.has("color")) {
            String s = JSONUtils.getString(jsonIn, "color");
            return Color.func_240745_a_(s);
         } else {
            return null;
         }
      }

      @Nullable
      private static Boolean deserializeBooleanValue(JsonObject jsonIn, String memberNameIn) {
         return jsonIn.has(memberNameIn) ? jsonIn.get(memberNameIn).getAsBoolean() : null;
      }

      @Nullable
      public JsonElement serialize(Style p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
         if (p_serialize_1_.isEmpty()) {
            return null;
         } else {
            JsonObject jsonobject = new JsonObject();
            if (p_serialize_1_.bold != null) {
               jsonobject.addProperty("bold", p_serialize_1_.bold);
            }

            if (p_serialize_1_.italic != null) {
               jsonobject.addProperty("italic", p_serialize_1_.italic);
            }

            if (p_serialize_1_.underlined != null) {
               jsonobject.addProperty("underlined", p_serialize_1_.underlined);
            }

            if (p_serialize_1_.strikethrough != null) {
               jsonobject.addProperty("strikethrough", p_serialize_1_.strikethrough);
            }

            if (p_serialize_1_.obfuscated != null) {
               jsonobject.addProperty("obfuscated", p_serialize_1_.obfuscated);
            }

            if (p_serialize_1_.color != null) {
               jsonobject.addProperty("color", p_serialize_1_.color.func_240747_b_());
            }

            if (p_serialize_1_.insertion != null) {
               jsonobject.add("insertion", p_serialize_3_.serialize(p_serialize_1_.insertion));
            }

            if (p_serialize_1_.clickEvent != null) {
               JsonObject jsonobject1 = new JsonObject();
               jsonobject1.addProperty("action", p_serialize_1_.clickEvent.getAction().getCanonicalName());
               jsonobject1.addProperty("value", p_serialize_1_.clickEvent.getValue());
               jsonobject.add("clickEvent", jsonobject1);
            }

            if (p_serialize_1_.hoverEvent != null) {
               jsonobject.add("hoverEvent", p_serialize_1_.hoverEvent.serialize());
            }

            if (p_serialize_1_.fontId != null) {
               jsonobject.addProperty("font", p_serialize_1_.fontId.toString());
            }

            return jsonobject;
         }
      }
   }
}