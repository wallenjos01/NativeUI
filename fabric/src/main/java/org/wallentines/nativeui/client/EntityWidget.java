package org.wallentines.nativeui.client;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.wallentines.midnightcore.api.module.skin.Skin;
import org.wallentines.midnightcore.fabric.util.ConversionUtil;
import org.wallentines.nativeui.control.EntityModel;

import java.util.UUID;

public class EntityWidget extends PositionedWidget {

    private LivingEntity livingEntity;
    private final int mobScale;

    public EntityWidget(Container parent, EntityModel model) {
        super(parent, model.getX(), model.getY(), 0, 0, model.getId(), model.getClickId());

        this.mobScale = model.getScale();

        ClientLevel lvl = Minecraft.getInstance().level;
        if(lvl == null) return;

        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(ConversionUtil.toResourceLocation(model.getEntityType()));
        CompoundTag tag = model.getNbt() == null ? null : ConversionUtil.toCompoundTag(model.getNbt());

        Entity ent;
        if(type == EntityType.PLAYER) {

            GameProfile prof;
            if(model.getPlayerSkin() == null) {
                prof = new GameProfile(UUID.randomUUID(), "");
            } else {
                Skin s = model.getPlayerSkin();
                prof = new GameProfile(s.getUUID(), "");
                prof.getProperties().put("textures", new Property("textures", s.getValue(), s.getSignature()));
            }

            ent = new RemotePlayer(lvl, prof) {
                private final PlayerInfo inf = new PlayerInfo(prof, false);

                @Override
                protected PlayerInfo getPlayerInfo() {
                    return inf;
                }

                @Override
                public boolean isModelPartShown(@NotNull PlayerModelPart playerModelPart) {
                    return true;
                }
            };
        } else {

            ent = type.create(lvl);
        }

        if (!(ent instanceof LivingEntity)) return;

        this.width = (int) (mobScale * type.getDimensions().width);
        this.height = (int) (mobScale * type.getDimensions().height);

        if (tag != null) ent.load(tag);
        this.livingEntity = (LivingEntity) ent;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mx, int my, float delta) {

        /*
        * Clone of Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;renderEntityInInventory(IIIFFLnet/minecraft/world/entity/LivingEntity;)V
        */

        int ax = x + (width / 2);
        int ay = y + height;
        float mouseX = (float) x - mx + width;
        float mouseY = (float) y - my + height - (int) (livingEntity.getEyeHeight() * mobScale);

        float yRot = (float) Math.atan(mouseX / 40.0F);
        float xRot = (float) Math.atan(mouseY / 40.0F);

        PoseStack modelPoseStack = RenderSystem.getModelViewStack();
        modelPoseStack.pushPose();
        modelPoseStack.translate(ax, ay, 1050.0);
        modelPoseStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();

        poseStack.pushPose();
        poseStack.translate(0,0,100);
        poseStack.scale(mobScale, mobScale, mobScale);


        Quaternionf zQuat = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf xQuat = (new Quaternionf()).rotateX(xRot * 20.0F * 0.017453292F);

        zQuat.mul(xQuat);

        poseStack.mulPose(zQuat);

        livingEntity.yBodyRot = 180.0F + yRot * 20.0F;
        livingEntity.setYRot(180.0F + yRot * 40.0F);
        livingEntity.setXRot(-xRot * 20.0F);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();

        Lighting.setupForEntityInInventory();

        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        xQuat.conjugate();
        dispatcher.overrideCameraOrientation(xQuat);
        dispatcher.setRenderShadow(false);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        dispatcher.render(livingEntity, 0.0, 0.0, 0.0, 0.0F, 1.0F, poseStack, bufferSource, 15728880);

        bufferSource.endBatch();
        dispatcher.setRenderShadow(true);

        poseStack.popPose();
        modelPoseStack.popPose();

        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();

    }
}
