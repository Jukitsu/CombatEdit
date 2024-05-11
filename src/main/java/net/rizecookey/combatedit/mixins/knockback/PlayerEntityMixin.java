package net.rizecookey.combatedit.mixins.knockback;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.rizecookey.combatedit.utils.KnockbackOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"))
    public void handleTakeKnockback(LivingEntity livingEntity, double speed, double xMovement, double zMovement) {
        double amount = 2.0D * speed;
        amount *= KnockbackOptions.knockbackExtraHorizontal;
        amount *= 1.0D - livingEntity.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).getValue();
        livingEntity.addVelocity(-(xMovement * amount), KnockbackOptions.knockbackExtraVertical, -(zMovement * amount));
    }
}
