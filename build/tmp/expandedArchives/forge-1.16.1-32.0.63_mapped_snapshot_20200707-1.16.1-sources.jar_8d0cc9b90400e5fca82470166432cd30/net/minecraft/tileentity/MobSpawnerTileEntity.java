package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;

public class MobSpawnerTileEntity extends TileEntity implements ITickableTileEntity {
   private final AbstractSpawner spawnerLogic = new AbstractSpawner() {
      public void broadcastEvent(int id) {
         MobSpawnerTileEntity.this.world.addBlockEvent(MobSpawnerTileEntity.this.pos, Blocks.SPAWNER, id, 0);
      }

      public World getWorld() {
         return MobSpawnerTileEntity.this.world;
      }

      public BlockPos getSpawnerPosition() {
         return MobSpawnerTileEntity.this.pos;
      }

      public void setNextSpawnData(WeightedSpawnerEntity nextSpawnData) {
         super.setNextSpawnData(nextSpawnData);
         if (this.getWorld() != null) {
            BlockState blockstate = this.getWorld().getBlockState(this.getSpawnerPosition());
            this.getWorld().notifyBlockUpdate(MobSpawnerTileEntity.this.pos, blockstate, blockstate, 4);
         }

      }
   };

   public MobSpawnerTileEntity() {
      super(TileEntityType.MOB_SPAWNER);
   }

   public void read(BlockState stateIn, CompoundNBT nbtIn) {
      super.read(stateIn, nbtIn);
      this.spawnerLogic.read(nbtIn);
   }

   public CompoundNBT write(CompoundNBT compound) {
      super.write(compound);
      this.spawnerLogic.write(compound);
      return compound;
   }

   public void tick() {
      this.spawnerLogic.tick();
   }

   /**
    * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
    * modded TE's
    */
   @Nullable
   public SUpdateTileEntityPacket getUpdatePacket() {
      return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
   }

   /**
    * Get an NBT compound to sync to the client with SPacketChunkData
    */
   public CompoundNBT getUpdateTag() {
      CompoundNBT compoundnbt = this.write(new CompoundNBT());
      compoundnbt.remove("SpawnPotentials");
      return compoundnbt;
   }

   /**
    * See {@link Block#eventReceived} for more information. This must return true serverside before it is called
    * clientside.
    */
   public boolean receiveClientEvent(int id, int type) {
      return this.spawnerLogic.setDelayToMin(id) ? true : super.receiveClientEvent(id, type);
   }

   /**
    * Checks if players can use this tile entity to access operator (permission level 2) commands either directly or
    * indirectly
    */
   public boolean onlyOpsCanSetNbt() {
      return true;
   }

   public AbstractSpawner getSpawnerBaseLogic() {
      return this.spawnerLogic;
   }
}