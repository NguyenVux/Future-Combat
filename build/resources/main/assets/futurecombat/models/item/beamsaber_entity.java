// Made with Blockbench 3.6.3
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


public class beamsaber_activate extends EntityModel<Entity> {
	private final ModelRenderer body;

	public beamsaber_activate() {
		textureWidth = 32;
		textureHeight = 32;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		setRotationAngle(body, 0.0F, 0.0F, 1.5708F);
		body.setTextureOffset(4, 4).addBox(-2.0F, -8.0F, -0.5F, 2.0F, 8.0F, 2.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addBox(-1.5F, -16.0F, 0.0F, 1.0F, 16.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}