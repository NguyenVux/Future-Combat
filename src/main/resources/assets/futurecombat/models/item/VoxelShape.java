Stream.of(
Block.makeCuboidShape(7.5, 0, 8, 8.5, 3, 8.5),
Block.makeCuboidShape(7.5, 0, 7.5, 8.5, 3.75, 8),
Block.makeCuboidShape(7.625, 1.558, 7.625, 8.375, 5.258, 8.375),
Block.makeCuboidShape(7.5, 4, 8, 8.5, 6.75, 8.5),
Block.makeCuboidShape(7.5, 4.75, 7.5, 8.5, 6.75, 8),
Block.makeCuboidShape(7.625, 6.558, 7.625, 8.375, 7.058, 8.375),
Block.makeCuboidShape(7.75, 6.5, 7.75, 8.25, 22, 8.25),
Block.makeCuboidShape(7.875, 6.5, 7.875, 8.125, 21.75, 8.125)
).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);});