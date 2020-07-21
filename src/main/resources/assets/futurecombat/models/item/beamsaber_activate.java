// Made with Blockbench 3.5.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


public class beamsaber_activate extends EntityModel<Entity> {
	private final ModelRenderer VoxelShape;

	public beamsaber_activate() {
		textureWidth = 16;
		textureHeight = 16;

		VoxelShape = new ModelRenderer(this);
		VoxelShape.setRotationPoint(-8.0F, 16.0F, 8.0F);
		VoxelShape.setTextureOffset(0, 0).addBox(-0.5F, 5.0F, 0.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);
		VoxelShape.setTextureOffset(0, 0).addBox(-0.5F, 4.25F, -0.5F, 1.0F, 3.0F, 0.0F, 0.0F, false);
		VoxelShape.setTextureOffset(0, 0).addBox(-0.375F, 2.742F, -0.375F, 0.0F, 3.0F, 0.0F, 0.0F, false);
		VoxelShape.setTextureOffset(1, 0).addBox(-0.5F, 1.25F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
		VoxelShape.setTextureOffset(1, 13).addBox(-0.5F, 1.25F, -0.5F, 1.0F, 2.0F, 0.0F, 0.0F, false);
		VoxelShape.setTextureOffset(0, 7).addBox(-0.375F, 0.942F, -0.375F, 0.0F, 0.0F, 0.0F, 0.0F, false);
		VoxelShape.setTextureOffset(0, 6).addBox(-0.25F, -14.0F, -0.25F, 0.0F, 15.0F, 0.0F, 0.0F, false);
		VoxelShape.setTextureOffset(0, 6).addBox(-0.125F, -13.75F, -0.125F, 0.0F, 15.0F, 0.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		VoxelShape.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}