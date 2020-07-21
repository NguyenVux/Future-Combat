package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ContainerBlock extends Block implements ITileEntityProvider {
   protected ContainerBlock(AbstractBlock.Properties builder) {
      super(builder);
   }

   /**
    * The type of render function called. MODEL for mixed tesr and static model
    */
   public BlockRenderType getRenderType(BlockState state) {
      return BlockRenderType.INVISIBLE;
   }

   /**
    * Called on server when World#addBlockEvent is called. If server returns true
    */
   public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
      super.eventReceived(state, worldIn, pos, id, param);
      TileEntity tileentity = worldIn.getTileEntity(pos);
      return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
   }

   @Nullable
   public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
      return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider)tileentity : null;
   }
}