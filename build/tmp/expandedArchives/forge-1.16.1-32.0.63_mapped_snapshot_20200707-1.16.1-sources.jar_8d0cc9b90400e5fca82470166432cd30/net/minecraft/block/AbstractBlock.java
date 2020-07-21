package net.minecraft.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.Property;
import net.minecraft.state.StateHolder;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.EmptyBlockReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//TODO, Delegates are weird here now, because Block extends this.
public abstract class AbstractBlock extends net.minecraftforge.registries.ForgeRegistryEntry<Block> {
   protected static final Direction[] UPDATE_ORDER = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP};
   protected final Material material;
   protected final boolean canCollide;
   protected final float blastResistance;
   /**
    * Flags whether or not this block is of a type that needs random ticking. Ref-counted by ExtendedBlockStorage in
    * order to broadly cull a chunk from the random chunk update list for efficiency's sake.
    */
   protected final boolean ticksRandomly;
   protected final SoundType soundType;
   /** Determines how much velocity is maintained while moving on top of this block */
   protected final float slipperiness;
   protected final float speedFactor;
   protected final float jumpFactor;
   protected final boolean variableOpacity;
   protected final AbstractBlock.Properties properties;
   @Nullable
   protected ResourceLocation lootTable;

   public AbstractBlock(AbstractBlock.Properties propertiesIn) {
      this.material = propertiesIn.material;
      this.canCollide = propertiesIn.blocksMovement;
      this.lootTable = propertiesIn.lootTable;
      this.blastResistance = propertiesIn.resistance;
      this.ticksRandomly = propertiesIn.ticksRandomly;
      this.soundType = propertiesIn.soundType;
      this.slipperiness = propertiesIn.slipperiness;
      this.speedFactor = propertiesIn.speedFactor;
      this.jumpFactor = propertiesIn.jumpFactor;
      this.variableOpacity = propertiesIn.variableOpacity;
      this.properties = propertiesIn;
      final ResourceLocation lootTableCache = propertiesIn.lootTable;
      this.lootTableSupplier = lootTableCache != null ? () -> lootTableCache : propertiesIn.lootTableSupplier != null ? propertiesIn.lootTableSupplier : () -> new ResourceLocation(this.getRegistryName().getNamespace(), "blocks/" + this.getRegistryName().getPath());
   }

   /**
    * performs updates on diagonal neighbors of the target position and passes in the flags. The flags can be referenced
    * from the docs for {@link IWorldWriter#setBlockState(IBlockState
    */
   @Deprecated
   public void updateDiagonalNeighbors(BlockState state, IWorld worldIn, BlockPos pos, int flags, int p_196248_5_) {
   }

   @Deprecated
   public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
      switch(type) {
      case LAND:
         return !state.func_235785_r_(worldIn, pos);
      case WATER:
         return worldIn.getFluidState(pos).isTagged(FluidTags.WATER);
      case AIR:
         return !state.func_235785_r_(worldIn, pos);
      default:
         return false;
      }
   }

   /**
    * Update the provided state given the provided neighbor facing and neighbor state
    */
   @Deprecated
   public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
      return stateIn;
   }

   @Deprecated
   @OnlyIn(Dist.CLIENT)
   public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
      return false;
   }

   @Deprecated
   public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
      DebugPacketSender.func_218806_a(worldIn, pos);
   }

   @Deprecated
   public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
   }

   @Deprecated
   public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
      if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
         worldIn.removeTileEntity(pos);
      }

   }

   @Deprecated
   public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
      return ActionResultType.PASS;
   }

   /**
    * Called on server when World#addBlockEvent is called. If server returns true
    */
   @Deprecated
   public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
      return false;
   }

   /**
    * The type of render function called. MODEL for mixed tesr and static model
    */
   @Deprecated
   public BlockRenderType getRenderType(BlockState state) {
      return BlockRenderType.MODEL;
   }

   @Deprecated
   public boolean isTransparent(BlockState state) {
      return false;
   }

   /**
    * Can this block provide power. Only wire currently seems to have this change based on its state.
    * @deprecated call via {@link IBlockState#canProvidePower()} whenever possible. Implementing/overriding is fine.
    */
   @Deprecated
   public boolean canProvidePower(BlockState state) {
      return false;
   }

   /**
    * @deprecated call via {@link IBlockState#getMobilityFlag()} whenever possible. Implementing/overriding is fine.
    */
   @Deprecated
   public PushReaction getPushReaction(BlockState state) {
      return this.material.getPushReaction();
   }

   @Deprecated
   public FluidState getFluidState(BlockState state) {
      return Fluids.EMPTY.getDefaultState();
   }

   /**
    * @deprecated call via {@link IBlockState#hasComparatorInputOverride()} whenever possible. Implementing/overriding
    * is fine.
    */
   @Deprecated
   public boolean hasComparatorInputOverride(BlockState state) {
      return false;
   }

   /**
    * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
    */
   public AbstractBlock.OffsetType getOffsetType() {
      return AbstractBlock.OffsetType.NONE;
   }

   /**
    * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable
    */
   @Deprecated
   public BlockState rotate(BlockState state, Rotation rot) {
      return state;
   }

   /**
    * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable
    */
   @Deprecated
   public BlockState mirror(BlockState state, Mirror mirrorIn) {
      return state;
   }

   @Deprecated
   public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
      return state.getMaterial().isReplaceable() && (useContext.getItem().isEmpty() || useContext.getItem().getItem() != this.asItem());
   }

   @Deprecated
   public boolean isReplaceable(BlockState p_225541_1_, Fluid p_225541_2_) {
      return this.material.isReplaceable() || !this.material.isSolid();
   }

   @Deprecated
   public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
      ResourceLocation resourcelocation = this.getLootTable();
      if (resourcelocation == LootTables.EMPTY) {
         return Collections.emptyList();
      } else {
         LootContext lootcontext = builder.withParameter(LootParameters.BLOCK_STATE, state).build(LootParameterSets.BLOCK);
         ServerWorld serverworld = lootcontext.getWorld();
         LootTable loottable = serverworld.getServer().getLootTableManager().getLootTableFromLocation(resourcelocation);
         return loottable.generate(lootcontext);
      }
   }

   /**
    * Return a random long to be passed to {@link IBakedModel#getQuads}
    */
   @Deprecated
   @OnlyIn(Dist.CLIENT)
   public long getPositionRandom(BlockState state, BlockPos pos) {
      return MathHelper.getPositionRandom(pos);
   }

   @Deprecated
   public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
      return state.getShape(worldIn, pos);
   }

   @Deprecated
   public VoxelShape func_230335_e_(BlockState p_230335_1_, IBlockReader p_230335_2_, BlockPos p_230335_3_) {
      return this.getCollisionShape(p_230335_1_, p_230335_2_, p_230335_3_, ISelectionContext.dummy());
   }

   @Deprecated
   public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
      return VoxelShapes.empty();
   }

   @Deprecated
   public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
      if (state.isOpaqueCube(worldIn, pos)) {
         return worldIn.getMaxLightLevel();
      } else {
         return state.propagatesSkylightDown(worldIn, pos) ? 0 : 1;
      }
   }

   @Nullable
   @Deprecated
   public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
      return null;
   }

   @Deprecated
   public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
      return true;
   }

   @Deprecated
   @OnlyIn(Dist.CLIENT)
   public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
      return state.func_235785_r_(worldIn, pos) ? 0.2F : 1.0F;
   }

   /**
    * @deprecated call via {@link IBlockState#getComparatorInputOverride(World
    */
   @Deprecated
   public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
      return 0;
   }

   @Deprecated
   public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
      return VoxelShapes.fullCube();
   }

   @Deprecated
   public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
      return this.canCollide ? state.getShape(worldIn, pos) : VoxelShapes.empty();
   }

   @Deprecated
   public VoxelShape func_230322_a_(BlockState p_230322_1_, IBlockReader p_230322_2_, BlockPos p_230322_3_, ISelectionContext p_230322_4_) {
      return this.getCollisionShape(p_230322_1_, p_230322_2_, p_230322_3_, p_230322_4_);
   }

   /**
    * Performs a random tick on a block.
    */
   @Deprecated
   public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
      this.tick(state, worldIn, pos, random);
   }

   @Deprecated
   public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
   }

   /**
    * Get the hardness of this Block relative to the ability of the given player
    * @deprecated call via {@link IBlockState#getPlayerRelativeBlockHardness(EntityPlayer
    */
   @Deprecated
   public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
      float f = state.getBlockHardness(worldIn, pos);
      if (f == -1.0F) {
         return 0.0F;
      } else {
         int i = net.minecraftforge.common.ForgeHooks.canHarvestBlock(state, player, worldIn, pos) ? 30 : 100;
         return player.getDigSpeed(state, pos) / f / (float)i;
      }
   }

   /**
    * Perform side-effects from block dropping
    */
   @Deprecated
   public void spawnAdditionalDrops(BlockState state, World worldIn, BlockPos pos, ItemStack stack) {
   }

   @Deprecated
   public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
   }

   /**
    * @deprecated call via {@link IBlockState#getWeakPower(IBlockAccess
    */
   @Deprecated
   public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
      return 0;
   }

   @Deprecated
   public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
   }

   /**
    * @deprecated call via {@link IBlockState#getStrongPower(IBlockAccess
    */
   @Deprecated
   public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
      return 0;
   }

   @Deprecated //Forge: Use state.hasTileEntity()
   public final boolean func_235695_q_() {
      return this instanceof ITileEntityProvider;
   }

   public final ResourceLocation getLootTable() {
      if (this.lootTable == null) {
         this.lootTable = this.lootTableSupplier.get();
      }

      return this.lootTable;
   }

   @Deprecated
   public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
   }

   public abstract Item asItem();

   protected abstract Block getSelf();

   public MaterialColor getMaterialColor() {
      return this.properties.field_235800_b_.apply(this.getSelf().getDefaultState());
   }

   /* ======================================== FORGE START ===================================== */
   private final java.util.function.Supplier<ResourceLocation> lootTableSupplier;
   /* ========================================= FORGE END ====================================== */

   public abstract static class AbstractBlockState extends StateHolder<Block, BlockState> {
      private final int lightLevel;
      private final boolean transparent;
      private final boolean isAir;
      private final Material material;
      private final MaterialColor materialColor;
      private final float hardness;
      private final boolean requiresTool;
      private final boolean isSolid;
      private final AbstractBlock.IPositionPredicate isNormalCube;
      private final AbstractBlock.IPositionPredicate field_235709_m_;
      private final AbstractBlock.IPositionPredicate blocksVision;
      private final AbstractBlock.IPositionPredicate needsPostProcessing;
      private final AbstractBlock.IPositionPredicate emissiveRendering;
      @Nullable
      protected AbstractBlock.AbstractBlockState.Cache cache;

      protected AbstractBlockState(Block p_i231870_1_, ImmutableMap<Property<?>, Comparable<?>> p_i231870_2_, MapCodec<BlockState> p_i231870_3_) {
         super(p_i231870_1_, p_i231870_2_, p_i231870_3_);
         AbstractBlock.Properties abstractblock$properties = p_i231870_1_.properties;
         this.lightLevel = abstractblock$properties.lightLevel.applyAsInt(this.getSelf());
         this.transparent = p_i231870_1_.isTransparent(this.getSelf());
         this.isAir = abstractblock$properties.isAir;
         this.material = abstractblock$properties.material;
         this.materialColor = abstractblock$properties.field_235800_b_.apply(this.getSelf());
         this.hardness = abstractblock$properties.hardness;
         this.requiresTool = abstractblock$properties.requiresTool;
         this.isSolid = abstractblock$properties.isSolid;
         this.isNormalCube = abstractblock$properties.isOpaque;
         this.field_235709_m_ = abstractblock$properties.suffocates;
         this.blocksVision = abstractblock$properties.blocksVision;
         this.needsPostProcessing = abstractblock$properties.needsPostProcessing;
         this.emissiveRendering = abstractblock$properties.emmissiveRendering;
      }

      public void cacheState() {
         if (!this.getBlock().isVariableOpacity()) {
            this.cache = new AbstractBlock.AbstractBlockState.Cache(this.getSelf());
         }

      }

      public Block getBlock() {
         return this.field_235892_c_;
      }

      public Material getMaterial() {
         return this.material;
      }

      public boolean canEntitySpawn(IBlockReader worldIn, BlockPos pos, EntityType<?> type) {
         return this.getBlock().properties.propagatesLightDownwards.test(this.getSelf(), worldIn, pos, type);
      }

      public boolean propagatesSkylightDown(IBlockReader worldIn, BlockPos pos) {
         return this.cache != null ? this.cache.propagatesSkylightDown : this.getBlock().propagatesSkylightDown(this.getSelf(), worldIn, pos);
      }

      public int getOpacity(IBlockReader worldIn, BlockPos pos) {
         return this.cache != null ? this.cache.opacity : this.getBlock().getOpacity(this.getSelf(), worldIn, pos);
      }

      public VoxelShape getFaceOcclusionShape(IBlockReader worldIn, BlockPos pos, Direction directionIn) {
         return this.cache != null && this.cache.renderShapes != null ? this.cache.renderShapes[directionIn.ordinal()] : VoxelShapes.getFaceShape(this.func_235754_c_(worldIn, pos), directionIn);
      }

      public VoxelShape func_235754_c_(IBlockReader p_235754_1_, BlockPos p_235754_2_) {
         return this.getBlock().getRenderShape(this.getSelf(), p_235754_1_, p_235754_2_);
      }

      public boolean isCollisionShapeLargerThanFullBlock() {
         return this.cache == null || this.cache.isCollisionShapeLargerThanFullBlock;
      }

      public boolean isTransparent() {
         return this.transparent;
      }

      public int getLightValue() {
         return this.lightLevel;
      }

      /** @deprecated use {@link BlockState#isAir(IBlockReader, BlockPos) */
      @Deprecated
      public boolean isAir() {
         return this.isAir;
      }

      public MaterialColor getMaterialColor(IBlockReader worldIn, BlockPos pos) {
         return this.materialColor;
      }

      /** @deprecated use {@link BlockState#rotate(IWorld, BlockPos, Rotation) */
      /**
       * Returns the blockstate with the given rotation. If inapplicable
       */
      @Deprecated
      public BlockState rotate(Rotation rot) {
         return this.getBlock().rotate(this.getSelf(), rot);
      }

      /**
       * Returns the blockstate mirrored in the given way. If inapplicable
       */
      public BlockState mirror(Mirror mirrorIn) {
         return this.getBlock().mirror(this.getSelf(), mirrorIn);
      }

      public BlockRenderType getRenderType() {
         return this.getBlock().getRenderType(this.getSelf());
      }

      @OnlyIn(Dist.CLIENT)
      public boolean isEmissiveRendering(IBlockReader p_227035_1_, BlockPos p_227035_2_) {
         return this.emissiveRendering.test(this.getSelf(), p_227035_1_, p_227035_2_);
      }

      @OnlyIn(Dist.CLIENT)
      public float getAmbientOcclusionLightValue(IBlockReader reader, BlockPos pos) {
         return this.getBlock().getAmbientOcclusionLightValue(this.getSelf(), reader, pos);
      }

      public boolean isNormalCube(IBlockReader reader, BlockPos pos) {
         return this.isNormalCube.test(this.getSelf(), reader, pos);
      }

      public boolean canProvidePower() {
         return this.getBlock().canProvidePower(this.getSelf());
      }

      public int getWeakPower(IBlockReader blockAccess, BlockPos pos, Direction side) {
         return this.getBlock().getWeakPower(this.getSelf(), blockAccess, pos, side);
      }

      public boolean hasComparatorInputOverride() {
         return this.getBlock().hasComparatorInputOverride(this.getSelf());
      }

      public int getComparatorInputOverride(World worldIn, BlockPos pos) {
         return this.getBlock().getComparatorInputOverride(this.getSelf(), worldIn, pos);
      }

      public float getBlockHardness(IBlockReader worldIn, BlockPos pos) {
         return this.hardness;
      }

      public float getPlayerRelativeBlockHardness(PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
         return this.getBlock().getPlayerRelativeBlockHardness(this.getSelf(), player, worldIn, pos);
      }

      public int getStrongPower(IBlockReader blockAccess, BlockPos pos, Direction side) {
         return this.getBlock().getStrongPower(this.getSelf(), blockAccess, pos, side);
      }

      public PushReaction getPushReaction() {
         return this.getBlock().getPushReaction(this.getSelf());
      }

      public boolean isOpaqueCube(IBlockReader worldIn, BlockPos pos) {
         if (this.cache != null) {
            return this.cache.opaqueCube;
         } else {
            BlockState blockstate = this.getSelf();
            return blockstate.isSolid() ? Block.isOpaque(blockstate.func_235754_c_(worldIn, pos)) : false;
         }
      }

      public boolean isSolid() {
         return this.isSolid;
      }

      @OnlyIn(Dist.CLIENT)
      public boolean isSideInvisible(BlockState state, Direction face) {
         return this.getBlock().isSideInvisible(this.getSelf(), state, face);
      }

      public VoxelShape getShape(IBlockReader worldIn, BlockPos pos) {
         return this.getShape(worldIn, pos, ISelectionContext.dummy());
      }

      public VoxelShape getShape(IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
         return this.getBlock().getShape(this.getSelf(), worldIn, pos, context);
      }

      public VoxelShape getCollisionShape(IBlockReader worldIn, BlockPos pos) {
         return this.cache != null ? this.cache.collisionShape : this.getCollisionShape(worldIn, pos, ISelectionContext.dummy());
      }

      public VoxelShape getCollisionShape(IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
         return this.getBlock().getCollisionShape(this.getSelf(), worldIn, pos, context);
      }

      public VoxelShape getRenderShape(IBlockReader worldIn, BlockPos pos) {
         return this.getBlock().func_230335_e_(this.getSelf(), worldIn, pos);
      }

      public VoxelShape getRaytraceShape(IBlockReader worldIn, BlockPos pos, ISelectionContext p_199611_3_) {
         return this.getBlock().func_230322_a_(this.getSelf(), worldIn, pos, p_199611_3_);
      }

      public VoxelShape func_235777_m_(IBlockReader p_235777_1_, BlockPos p_235777_2_) {
         return this.getBlock().getRaytraceShape(this.getSelf(), p_235777_1_, p_235777_2_);
      }

      public final boolean func_235719_a_(IBlockReader p_235719_1_, BlockPos p_235719_2_, Entity p_235719_3_) {
         return this.isTopSolid(p_235719_1_, p_235719_2_, p_235719_3_, Direction.UP);
      }

      /**
       * True if the collision box of this state covers the entire upper face of the blockspace
       */
      public final boolean isTopSolid(IBlockReader reader, BlockPos pos, Entity entityIn, Direction p_215682_4_) {
         return Block.doesSideFillSquare(this.getCollisionShape(reader, pos, ISelectionContext.forEntity(entityIn)), p_215682_4_);
      }

      public Vector3d getOffset(IBlockReader access, BlockPos pos) {
         AbstractBlock.OffsetType abstractblock$offsettype = this.getBlock().getOffsetType();
         if (abstractblock$offsettype == AbstractBlock.OffsetType.NONE) {
            return Vector3d.ZERO;
         } else {
            long i = MathHelper.getCoordinateRandom(pos.getX(), 0, pos.getZ());
            return new Vector3d(((double)((float)(i & 15L) / 15.0F) - 0.5D) * 0.5D, abstractblock$offsettype == AbstractBlock.OffsetType.XYZ ? ((double)((float)(i >> 4 & 15L) / 15.0F) - 1.0D) * 0.2D : 0.0D, ((double)((float)(i >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D);
         }
      }

      public boolean func_235728_a_(World p_235728_1_, BlockPos p_235728_2_, int p_235728_3_, int p_235728_4_) {
         return this.getBlock().eventReceived(this.getSelf(), p_235728_1_, p_235728_2_, p_235728_3_, p_235728_4_);
      }

      public void neighborChanged(World worldIn, BlockPos posIn, Block blockIn, BlockPos fromPosIn, boolean isMoving) {
         this.getBlock().neighborChanged(this.getSelf(), worldIn, posIn, blockIn, fromPosIn, isMoving);
      }

      public final void func_235734_a_(IWorld p_235734_1_, BlockPos p_235734_2_, int p_235734_3_) {
         this.func_241482_a_(p_235734_1_, p_235734_2_, p_235734_3_, 512);
      }

      public final void func_241482_a_(IWorld p_241482_1_, BlockPos p_241482_2_, int p_241482_3_, int p_241482_4_) {
         this.getBlock();
         BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

         for(Direction direction : AbstractBlock.UPDATE_ORDER) {
            blockpos$mutable.func_239622_a_(p_241482_2_, direction);
            BlockState blockstate = p_241482_1_.getBlockState(blockpos$mutable);
            BlockState blockstate1 = blockstate.updatePostPlacement(direction.getOpposite(), this.getSelf(), p_241482_1_, blockpos$mutable, p_241482_2_);
            Block.func_241468_a_(blockstate, blockstate1, p_241482_1_, blockpos$mutable, p_241482_3_, p_241482_4_);
         }

      }

      /**
       * Performs validations on the block state and possibly neighboring blocks to validate whether the incoming state
       * is valid to stay in the world. Currently used only by redstone wire to update itself if neighboring blocks have
       * changed and to possibly break itself.
       */
      public final void updateDiagonalNeighbors(IWorld worldIn, BlockPos pos, int flags) {
         this.updateDiagonalNeighbors(worldIn, pos, flags, 512);
      }

      public void updateDiagonalNeighbors(IWorld worldIn, BlockPos posIn, int flagsIn, int p_241483_4_) {
         this.getBlock().updateDiagonalNeighbors(this.getSelf(), worldIn, posIn, flagsIn, p_241483_4_);
      }

      public void onBlockAdded(World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
         this.getBlock().onBlockAdded(this.getSelf(), worldIn, pos, oldState, isMoving);
      }

      public void onReplaced(World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
         this.getBlock().onReplaced(this.getSelf(), worldIn, pos, newState, isMoving);
      }

      public void tick(ServerWorld worldIn, BlockPos posIn, Random randomIn) {
         this.getBlock().tick(this.getSelf(), worldIn, posIn, randomIn);
      }

      public void randomTick(ServerWorld worldIn, BlockPos posIn, Random randomIn) {
         this.getBlock().randomTick(this.getSelf(), worldIn, posIn, randomIn);
      }

      public void onEntityCollision(World worldIn, BlockPos pos, Entity entityIn) {
         this.getBlock().onEntityCollision(this.getSelf(), worldIn, pos, entityIn);
      }

      public void spawnAdditionalDrops(World worldIn, BlockPos pos, ItemStack stack) {
         this.getBlock().spawnAdditionalDrops(this.getSelf(), worldIn, pos, stack);
      }

      public List<ItemStack> getDrops(LootContext.Builder builder) {
         return this.getBlock().getDrops(this.getSelf(), builder);
      }

      public ActionResultType onBlockActivated(World worldIn, PlayerEntity player, Hand handIn, BlockRayTraceResult resultIn) {
         return this.getBlock().onBlockActivated(this.getSelf(), worldIn, resultIn.getPos(), player, handIn, resultIn);
      }

      public void onBlockClicked(World worldIn, BlockPos pos, PlayerEntity player) {
         this.getBlock().onBlockClicked(this.getSelf(), worldIn, pos, player);
      }

      public boolean isSuffocating(IBlockReader blockReaderIn, BlockPos blockPosIn) {
         return this.field_235709_m_.test(this.getSelf(), blockReaderIn, blockPosIn);
      }

      @OnlyIn(Dist.CLIENT)
      public boolean causesSuffocation(IBlockReader worldIn, BlockPos pos) {
         return this.blocksVision.test(this.getSelf(), worldIn, pos);
      }

      public BlockState updatePostPlacement(Direction face, BlockState queried, IWorld worldIn, BlockPos currentPos, BlockPos offsetPos) {
         return this.getBlock().updatePostPlacement(this.getSelf(), face, queried, worldIn, currentPos, offsetPos);
      }

      public boolean allowsMovement(IBlockReader worldIn, BlockPos pos, PathType type) {
         return this.getBlock().allowsMovement(this.getSelf(), worldIn, pos, type);
      }

      public boolean isReplaceable(BlockItemUseContext useContext) {
         return this.getBlock().isReplaceable(this.getSelf(), useContext);
      }

      public boolean isReplaceable(Fluid fluidIn) {
         return this.getBlock().isReplaceable(this.getSelf(), fluidIn);
      }

      public boolean isValidPosition(IWorldReader worldIn, BlockPos pos) {
         return this.getBlock().isValidPosition(this.getSelf(), worldIn, pos);
      }

      public boolean blockNeedsPostProcessing(IBlockReader worldIn, BlockPos pos) {
         return this.needsPostProcessing.test(this.getSelf(), worldIn, pos);
      }

      @Nullable
      public INamedContainerProvider getContainer(World worldIn, BlockPos pos) {
         return this.getBlock().getContainer(this.getSelf(), worldIn, pos);
      }

      public boolean isIn(ITag<Block> p_235714_1_) {
         return this.getBlock().isIn(p_235714_1_);
      }

      public boolean isInAndMatches(ITag<Block> p_235715_1_, Predicate<AbstractBlock.AbstractBlockState> p_235715_2_) {
         return this.getBlock().isIn(p_235715_1_) && p_235715_2_.test(this);
      }

      public boolean isIn(Block tagIn) {
         return this.getBlock().matchesBlock(tagIn);
      }

      public FluidState getFluidState() {
         return this.getBlock().getFluidState(this.getSelf());
      }

      public boolean ticksRandomly() {
         return this.getBlock().ticksRandomly(this.getSelf());
      }

      @OnlyIn(Dist.CLIENT)
      public long getPositionRandom(BlockPos pos) {
         return this.getBlock().getPositionRandom(this.getSelf(), pos);
      }

      public SoundType getSoundType() {
         return this.getBlock().getSoundType(this.getSelf());
      }

      public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
         this.getBlock().onProjectileCollision(worldIn, state, hit, projectile);
      }

      public boolean isSolidSide(IBlockReader blockReaderIn, BlockPos blockPosIn, Direction directionIn) {
         return this.cache != null ? this.cache.solidSides[directionIn.ordinal()] : Block.hasSolidSide(this.getSelf(), blockReaderIn, blockPosIn, directionIn);
      }

      public boolean func_235785_r_(IBlockReader p_235785_1_, BlockPos p_235785_2_) {
         return this.cache != null ? this.cache.opaqueCollisionShape : Block.isOpaque(this.getCollisionShape(p_235785_1_, p_235785_2_));
      }

      protected abstract BlockState getSelf();

      public boolean getRequiresTool() {
         return this.requiresTool;
      }

      static final class Cache {
         private static final Direction[] DIRECTIONS = Direction.values();
         protected final boolean opaqueCube;
         private final boolean propagatesSkylightDown;
         private final int opacity;
         @Nullable
         private final VoxelShape[] renderShapes;
         protected final VoxelShape collisionShape;
         protected final boolean isCollisionShapeLargerThanFullBlock;
         protected final boolean[] solidSides;
         protected final boolean opaqueCollisionShape;

         private Cache(BlockState stateIn) {
            Block block = stateIn.getBlock();
            this.opaqueCube = stateIn.isOpaqueCube(EmptyBlockReader.INSTANCE, BlockPos.ZERO);
            this.propagatesSkylightDown = block.propagatesSkylightDown(stateIn, EmptyBlockReader.INSTANCE, BlockPos.ZERO);
            this.opacity = block.getOpacity(stateIn, EmptyBlockReader.INSTANCE, BlockPos.ZERO);
            if (!stateIn.isSolid()) {
               this.renderShapes = null;
            } else {
               this.renderShapes = new VoxelShape[DIRECTIONS.length];
               VoxelShape voxelshape = block.getRenderShape(stateIn, EmptyBlockReader.INSTANCE, BlockPos.ZERO);

               for(Direction direction : DIRECTIONS) {
                  this.renderShapes[direction.ordinal()] = VoxelShapes.getFaceShape(voxelshape, direction);
               }
            }

            this.collisionShape = block.getCollisionShape(stateIn, EmptyBlockReader.INSTANCE, BlockPos.ZERO, ISelectionContext.dummy());
            this.isCollisionShapeLargerThanFullBlock = Arrays.stream(Direction.Axis.values()).anyMatch((p_235796_1_) -> {
               return this.collisionShape.getStart(p_235796_1_) < 0.0D || this.collisionShape.getEnd(p_235796_1_) > 1.0D;
            });
            this.solidSides = new boolean[6];

            for(Direction direction1 : DIRECTIONS) {
               this.solidSides[direction1.ordinal()] = Block.hasSolidSide(stateIn, EmptyBlockReader.INSTANCE, BlockPos.ZERO, direction1);
            }

            this.opaqueCollisionShape = Block.isOpaque(stateIn.getCollisionShape(EmptyBlockReader.INSTANCE, BlockPos.ZERO));
         }
      }
   }

   public interface IExtendedPositionPredicate<A> {
      boolean test(BlockState p_test_1_, IBlockReader p_test_2_, BlockPos p_test_3_, A p_test_4_);
   }

   public interface IPositionPredicate {
      boolean test(BlockState p_test_1_, IBlockReader p_test_2_, BlockPos p_test_3_);
   }

   public static enum OffsetType {
      NONE,
      XZ,
      XYZ;
   }

   public static class Properties {
      private Material material;
      private Function<BlockState, MaterialColor> field_235800_b_;
      private boolean blocksMovement = true;
      private SoundType soundType = SoundType.STONE;
      private ToIntFunction<BlockState> lightLevel = (p_235830_0_) -> {
         return 0;
      };
      private float resistance;
      private float hardness;
      private boolean requiresTool;
      private boolean ticksRandomly;
      private float slipperiness = 0.6F;
      private float speedFactor = 1.0F;
      private float jumpFactor = 1.0F;
      /** Sets loot table information */
      private ResourceLocation lootTable;
      private boolean isSolid = true;
      private boolean isAir;
      private int harvestLevel = -1;
      private net.minecraftforge.common.ToolType harvestTool;
      private java.util.function.Supplier<ResourceLocation> lootTableSupplier;
      private AbstractBlock.IExtendedPositionPredicate<EntityType<?>> propagatesLightDownwards = (p_235832_0_, p_235832_1_, p_235832_2_, p_235832_3_) -> {
         return p_235832_0_.isSolidSide(p_235832_1_, p_235832_2_, Direction.UP) && p_235832_0_.getLightValue() < 14;
      };
      private AbstractBlock.IPositionPredicate isOpaque = (p_235853_0_, p_235853_1_, p_235853_2_) -> {
         return p_235853_0_.getMaterial().isOpaque() && p_235853_0_.func_235785_r_(p_235853_1_, p_235853_2_);
      };
      private AbstractBlock.IPositionPredicate suffocates = (p_235848_1_, p_235848_2_, p_235848_3_) -> {
         return this.material.blocksMovement() && p_235848_1_.func_235785_r_(p_235848_2_, p_235848_3_);
      };
      /** If it blocks vision on the client side */
      private AbstractBlock.IPositionPredicate blocksVision = this.suffocates;
      private AbstractBlock.IPositionPredicate needsPostProcessing = (p_235843_0_, p_235843_1_, p_235843_2_) -> {
         return false;
      };
      private AbstractBlock.IPositionPredicate emmissiveRendering = (p_235831_0_, p_235831_1_, p_235831_2_) -> {
         return false;
      };
      private boolean variableOpacity;

      private Properties(Material materialIn, MaterialColor mapColorIn) {
         this(materialIn, (p_235837_1_) -> {
            return mapColorIn;
         });
      }

      private Properties(Material p_i241199_1_, Function<BlockState, MaterialColor> p_i241199_2_) {
         this.material = p_i241199_1_;
         this.field_235800_b_ = p_i241199_2_;
      }

      public static AbstractBlock.Properties create(Material materialIn) {
         return create(materialIn, materialIn.getColor());
      }

      public static AbstractBlock.Properties create(Material materialIn, DyeColor color) {
         return create(materialIn, color.getMapColor());
      }

      public static AbstractBlock.Properties create(Material materialIn, MaterialColor mapColorIn) {
         return new AbstractBlock.Properties(materialIn, mapColorIn);
      }

      public static AbstractBlock.Properties create(Material p_235836_0_, Function<BlockState, MaterialColor> p_235836_1_) {
         return new AbstractBlock.Properties(p_235836_0_, p_235836_1_);
      }

      public static AbstractBlock.Properties from(AbstractBlock blockIn) {
         AbstractBlock.Properties abstractblock$properties = new AbstractBlock.Properties(blockIn.material, blockIn.properties.field_235800_b_);
         abstractblock$properties.material = blockIn.properties.material;
         abstractblock$properties.hardness = blockIn.properties.hardness;
         abstractblock$properties.resistance = blockIn.properties.resistance;
         abstractblock$properties.blocksMovement = blockIn.properties.blocksMovement;
         abstractblock$properties.ticksRandomly = blockIn.properties.ticksRandomly;
         abstractblock$properties.lightLevel = blockIn.properties.lightLevel;
         abstractblock$properties.field_235800_b_ = blockIn.properties.field_235800_b_;
         abstractblock$properties.soundType = blockIn.properties.soundType;
         abstractblock$properties.slipperiness = blockIn.properties.slipperiness;
         abstractblock$properties.speedFactor = blockIn.properties.speedFactor;
         abstractblock$properties.variableOpacity = blockIn.properties.variableOpacity;
         abstractblock$properties.isSolid = blockIn.properties.isSolid;
         abstractblock$properties.isAir = blockIn.properties.isAir;
         abstractblock$properties.requiresTool = blockIn.properties.requiresTool;
         abstractblock$properties.harvestLevel = blockIn.properties.harvestLevel;
         abstractblock$properties.harvestTool = blockIn.properties.harvestTool;
         return abstractblock$properties;
      }

      public AbstractBlock.Properties doesNotBlockMovement() {
         this.blocksMovement = false;
         this.isSolid = false;
         return this;
      }

      public AbstractBlock.Properties notSolid() {
         this.isSolid = false;
         return this;
      }

      public AbstractBlock.Properties harvestLevel(int harvestLevel) {
         this.harvestLevel = harvestLevel;
         return this;
      }

      public AbstractBlock.Properties harvestTool(net.minecraftforge.common.ToolType harvestTool) {
         this.harvestTool = harvestTool;
         return this;
      }

      public int getHarvestLevel() {
         return this.harvestLevel;
      }

      public net.minecraftforge.common.ToolType getHarvestTool() {
         return this.harvestTool;
      }

      public AbstractBlock.Properties slipperiness(float slipperinessIn) {
         this.slipperiness = slipperinessIn;
         return this;
      }

      public AbstractBlock.Properties speedFactor(float factor) {
         this.speedFactor = factor;
         return this;
      }

      public AbstractBlock.Properties jumpFactor(float factor) {
         this.jumpFactor = factor;
         return this;
      }

      public AbstractBlock.Properties sound(SoundType soundTypeIn) {
         this.soundType = soundTypeIn;
         return this;
      }

      public AbstractBlock.Properties setLightLevel(ToIntFunction<BlockState> p_235838_1_) {
         this.lightLevel = p_235838_1_;
         return this;
      }

      public AbstractBlock.Properties hardnessAndResistance(float hardnessIn, float resistanceIn) {
         this.hardness = hardnessIn;
         this.resistance = Math.max(0.0F, resistanceIn);
         return this;
      }

      public AbstractBlock.Properties zeroHardnessAndResistance() {
         return this.hardnessAndResistance(0.0F);
      }

      public AbstractBlock.Properties hardnessAndResistance(float hardnessAndResistance) {
         this.hardnessAndResistance(hardnessAndResistance, hardnessAndResistance);
         return this;
      }

      public AbstractBlock.Properties tickRandomly() {
         this.ticksRandomly = true;
         return this;
      }

      public AbstractBlock.Properties variableOpacity() {
         this.variableOpacity = true;
         return this;
      }

      public AbstractBlock.Properties noDrops() {
         this.lootTable = LootTables.EMPTY;
         return this;
      }

      public AbstractBlock.Properties lootFrom(Block blockIn) {
         this.lootTableSupplier = () -> blockIn.delegate.get().getLootTable();
         return this;
      }

      public AbstractBlock.Properties setAir() {
         this.isAir = true;
         return this;
      }

      /**
       * What can spawn under these blocks
       */
      public AbstractBlock.Properties setPropagatesDownwards(AbstractBlock.IExtendedPositionPredicate<EntityType<?>> p_235827_1_) {
         this.propagatesLightDownwards = p_235827_1_;
         return this;
      }

      public AbstractBlock.Properties setOpaque(AbstractBlock.IPositionPredicate p_235828_1_) {
         this.isOpaque = p_235828_1_;
         return this;
      }

      public AbstractBlock.Properties setSuffocates(AbstractBlock.IPositionPredicate p_235842_1_) {
         this.suffocates = p_235842_1_;
         return this;
      }

      /**
       * If it blocks vision on the client side
       */
      public AbstractBlock.Properties setBlocksVision(AbstractBlock.IPositionPredicate p_235847_1_) {
         this.blocksVision = p_235847_1_;
         return this;
      }

      public AbstractBlock.Properties setNeedsPostProcessing(AbstractBlock.IPositionPredicate p_235852_1_) {
         this.needsPostProcessing = p_235852_1_;
         return this;
      }

      public AbstractBlock.Properties setEmmisiveRendering(AbstractBlock.IPositionPredicate p_235856_1_) {
         this.emmissiveRendering = p_235856_1_;
         return this;
      }

      public AbstractBlock.Properties setRequiresTool() {
         this.requiresTool = true;
         return this;
      }
   }
}