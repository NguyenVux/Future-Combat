package com.codingdummy.futurecombat.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import static com.codingdummy.futurecombat.util.MathHelperCodingDummy.toRad;

public class BeamSaberEntityModel extends EntityModel<Entity> {
    private final ModelRenderer body;
    private Vec3d PrevPos = new Vec3d(0.0f,0.0f,0.0f);
    private Vec3d PrevRot = new Vec3d(0.0f,0.0f,0.0f);
    public BeamSaberEntityModel() {
        textureWidth = 32;
        textureHeight = 32;

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 4.0F, 0.0F);

        body.setTextureOffset(4, 4).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);
        body.setTextureOffset(0, 0).addBox(-0.5F, -24.0F, -0.5F, 1.0F, 16.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void setLivingAnimations(Entity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.body.rotateAngleY = 6.0f+entityIn.getPitch(partialTick);
        super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);

    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(Vec3d RotationMatrix)
    {
        this.body.rotateAngleX = (float) RotationMatrix.x;
        this.body.rotateAngleY = (float) RotationMatrix.y;
        this.body.rotateAngleZ = (float) RotationMatrix.z;
    }

    public void setRotationAngle(float x,float y,float z)
    {
        this.body.rotateAngleX =x;
        this.body.rotateAngleY =y;
        this.body.rotateAngleZ =z;
    }

    public Vec3d getRotationAngle()
    {
        Vec3d Angle = new Vec3d(this.body.rotateAngleX,this.body.rotateAngleY,this.body.rotateAngleZ);
        return Angle;
    }
    public void rotate(float x,float y,float z)
    {
        this.body.rotateAngleX +=x;
        this.body.rotateAngleY +=y;
        this.body.rotateAngleZ +=z;
    }

    public Vec3d getRotationPoint()
    {
        Vec3d rotationPoint = new Vec3d(this.body.rotationPointX,this.body.rotationPointY,this.body.rotationPointZ);
        return rotationPoint;
    }

    public void setRotationPoint(Vec3d vec)
    {
        this.body.rotationPointX = (float) vec.x;
        this.body.rotationPointY = (float) vec.y;
        this.body.rotationPointZ = (float) vec.z;
    }
    public void translate(Vec3d vec)
    {
        this.body.rotationPointX += vec.x;
        this.body.rotationPointY += vec.y;
        this.body.rotationPointZ += vec.z;
    }

    public void SaveState()
    {
        PrevPos = this.getRotationPoint();
        PrevRot = this.getRotationAngle();
    }

    public void Restore()
    {
        this.setRotationPoint(PrevPos);
        this.setRotationAngle(PrevRot);
    }


    public ModelRenderer getBody() {
        return body;
    }

}
