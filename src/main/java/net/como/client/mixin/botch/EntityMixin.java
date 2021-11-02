package net.como.client.mixin.botch;

import java.util.UUID;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.como.client.interfaces.mixin.IEntity;
import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public class EntityMixin implements IEntity {
    @Shadow
    protected boolean inNetherPortal;

    @Override
    public boolean getInNetherPortal() {
        return this.inNetherPortal;
    }
}
