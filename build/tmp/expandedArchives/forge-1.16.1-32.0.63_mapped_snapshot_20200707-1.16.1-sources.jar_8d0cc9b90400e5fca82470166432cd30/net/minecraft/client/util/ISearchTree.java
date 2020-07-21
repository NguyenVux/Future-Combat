package net.minecraft.client.util;

import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ISearchTree<T> {
   /**
    * Searches this search tree for the given text.
    * <p>
    * If the query does not contain a <code>:</code>
    */
   List<T> search(String searchText);
}