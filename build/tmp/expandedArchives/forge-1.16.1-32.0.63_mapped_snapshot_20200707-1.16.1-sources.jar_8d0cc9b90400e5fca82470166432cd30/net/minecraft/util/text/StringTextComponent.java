package net.minecraft.util.text;

import javax.annotation.Nullable;

public class StringTextComponent extends TextComponent {
   public static final ITextComponent EMPTY = new StringTextComponent("");
   private final String text;
   @Nullable
   private LanguageMap lang;
   private String message;

   public StringTextComponent(String msg) {
      this.text = msg;
      this.message = msg;
   }

   /**
    * Gets the text value of this component. This is used to access the {@link #text} property
    */
   public String getText() {
      return this.text;
   }

   /**
    * Gets the raw content of this component (but not its sibling components)
    */
   public String getUnformattedComponentText() {
      if (this.text.isEmpty()) {
         return this.text;
      } else {
         LanguageMap languagemap = LanguageMap.getInstance();
         if (this.lang != languagemap) {
            this.message = languagemap.func_230504_a_(this.text, false);
            this.lang = languagemap;
         }

         return this.message;
      }
   }

   public StringTextComponent copyRaw() {
      return new StringTextComponent(this.text);
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof StringTextComponent)) {
         return false;
      } else {
         StringTextComponent stringtextcomponent = (StringTextComponent)p_equals_1_;
         return this.text.equals(stringtextcomponent.getText()) && super.equals(p_equals_1_);
      }
   }

   public String toString() {
      return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
   }
}