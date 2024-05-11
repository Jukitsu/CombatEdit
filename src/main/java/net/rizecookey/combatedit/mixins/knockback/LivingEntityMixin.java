package net.rizecookey.combatedit.mixins.knockback;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.rizecookey.combatedit.utils.KnockbackOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, World level) {
        super(entityType, level);
    }


    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);


    @Inject(method="takeKnockback", at=@At("HEAD"), cancellable = true)
    public void handleTakeKnockback(double d, double x, double z, CallbackInfo ci) {
        double resistanceFactor = 1.0D - this.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
        if (resistanceFactor > 0.0D) {
            this.velocityDirty = true;
            Vec3d velocity = this.getVelocity();
            Vec3d knockbackVector = (new Vec3d(x, 0.0D, z)).normalize().multiply(KnockbackOptions.knockbackHorizontal * resistanceFactor);
            this.setVelocity(
                    velocity.x / KnockbackOptions.knockbackFriction - knockbackVector.x,
                    Math.min(KnockbackOptions.knockbackVerticalLimit, velocity.y / 2.0D + KnockbackOptions.knockbackVertical * resistanceFactor),
                    velocity.z / KnockbackOptions.knockbackFriction - knockbackVector.z
            );
        }
        ci.cancel();
    }

}
