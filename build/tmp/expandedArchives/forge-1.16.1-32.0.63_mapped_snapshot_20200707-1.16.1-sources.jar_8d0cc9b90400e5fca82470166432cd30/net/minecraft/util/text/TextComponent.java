package net.minecraft.util.text;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;

public abstract class TextComponent implements IFormattableTextComponent {
   /** The later siblings of this component. If this component turns the text bold */
   protected final List<ITextComponent> siblings = Lists.newArrayList();
   private Style style = Style.EMPTY;

   public IFormattableTextComponent func_230529_a_(ITextComponent p_230529_1_) {
      this.siblings.add(p_230529_1_);
      return this;
   }

   /**
    * Gets the raw content of this component (but not its sibling components)
    */
   public String getUnformattedComponentText() {
      return "";
   }

   /**
    * Gets the sibling components of this one.
    */
   public List<ITextComponent> getSiblings() {
      return this.siblings;
   }

   public IFormattableTextComponent func_230530_a_(Style p_230530_1_) {
      this.style = p_230530_1_;
      return this;
   }

   /**
    * Gets the style of this component. Returns a direct reference; changes to this style will modify the style of this
    * component (IE
    */
   public Style getStyle() {
      return this.style;
   }

   public abstract TextComponent copyRaw();

   public final IFormattableTextComponent deepCopy() {
      TextComponent textcomponent = this.copyRaw();
      textcomponent.siblings.addAll(this.siblings);
      textcomponent.func_230530_a_(this.style);
      return textcomponent;
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof TextComponent)) {
         return false;
      } else {
         TextComponent textcomponent = (TextComponent)p_equals_1_;
         return this.siblings.equals(textcomponent.siblings) && Objects.equals(this.getStyle(), textcomponent.getStyle());
      }
   }

   public int hashCode() {
      return Objects.hash(this.getStyle(), this.siblings);
   }

   public String toString() {
      return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
   }
}