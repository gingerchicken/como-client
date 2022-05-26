package net.como.client.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.como.client.ComoClient;
import net.como.client.components.plugins.ServerClientRotation;
import net.como.client.config.settings.Setting;
import net.como.client.events.Event;
import net.como.client.events.client.ClientTickEvent;
import net.como.client.events.render.RenderWorldEvent;
import net.como.client.modules.Module;
import net.como.client.utils.BlockUtils;
import net.como.client.utils.ClientUtils;
import net.como.client.utils.InteractionUtils;
import net.como.client.utils.InventoryUtils;
import net.como.client.utils.RenderUtils;
import net.como.client.utils.RotationUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class CrystalAura extends Module {
    public CrystalAura() {
        super("CrystalAura");

        this.addSetting(new Setting("MaxDistance", 6d));
        this.addSetting(new Setting("LineOfSight", true));

        this.addSetting(new Setting("MaxHeightDiff", 1d));
        this.addSetting(new Setting("MaxCrystals", 4));

        this.addSetting(new Setting("RenderTargetBlock", true));

        // TODO just check if the local player is safe rather than just doing this.
        this.addSetting(new Setting("AllowLow", false));

        // Auto-select crystal as main hand
        this.addSetting(new Setting("SelectCrystal", true));

        // Auto-place the crystal close to the enemy 
        this.addSetting(new Setting("PlaceCrystal", true));

        this.addSetting(new Setting("Player", true));
        this.addSetting(new Setting("Mob", false));

        this.setDescription("Automatically places/destroys nearby crystals.");
        this.setCategory("Combat");
    }

    private ServerClientRotation scRot = new ServerClientRotation();

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        this.addListen(RenderWorldEvent.class);

        scRot.addListeners(this);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        this.removeListen(RenderWorldEvent.class);

        scRot.removeListeners(this);
    }

    private double getMaxDistance() {
        return (Double)this.getSetting("MaxDistance").value;
    }
    private double getSquaredDistance() {
        return this.getSquaredDistance(0.0d);
    }
    private double getSquaredDistance(double offset) {
        Double d = this.getMaxDistance() + offset;

        return d*d;
    }

    // Get the entities furthest away from the player.
    private Comparator<Entity> entitiesByDistance() {
        Vec3d pos = ComoClient.me().getPos();
        
        return (
            Comparator.<Entity> comparingDouble(e -> pos.squaredDistanceTo(e.getPos()))
            .reversed()
        );
    }

    // Get a stream of nearby entities
    private Stream<Entity> getEntityStream() {
        double d2 = this.getSquaredDistance();
        Vec3d pos = ComoClient.me().getPos();

        return StreamSupport.stream(ComoClient.getClient().world.getEntities().spliterator(), true)
            .filter(e -> !e.isRemoved())
            .filter(e -> pos.squaredDistanceTo(e.getPos()) <= d2);
    }

    // Get nearby end crystals
    private List<Entity> getCloseCrystals() {
        Comparator<Entity> fromPlayer = this.entitiesByDistance();

        return this.getEntityStream()
            .filter(e -> e instanceof EndCrystalEntity)
            .sorted(fromPlayer)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    // Get nearby targets
    private List<Entity> getCloseTargets() {
        Comparator<Entity> fromPlayer = this.entitiesByDistance();

        Stream<Entity> entities = this.getEntityStream();

        // Make sure that the entity is not us.
        entities = entities
            .filter(e -> e != ComoClient.me())

            // Must be alive and a living entity
            .filter(e -> (e instanceof LivingEntity))
            .filter(e -> e.isAlive())

            // Target Players
            .filter(e -> ((e instanceof PlayerEntity) && this.getBoolSetting("Player")) || ((e instanceof MobEntity) && this.getBoolSetting("Mob")));
        
        // TODO add more selectors

        return entities
            .sorted(fromPlayer)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean airAbove(BlockPos pos) {
        return BlockUtils.getState(pos.add(0, 1, 0)).isAir();
    }

    private boolean isCrystalSurface(BlockPos pos) {
        Block b = BlockUtils.getBlock(pos);

        // TODO make this a hashmap
        return b == Blocks.BEDROCK || b == Blocks.OBSIDIAN;
    }

    private boolean contactsEntity(BlockPos pos) {
        // i.e. on top of the block.
        pos = pos.add(0, 1, 0);
        Box b = new Box(pos);

        Vec3d vPos = Vec3d.of(pos);

        List<Entity> ents = this.getEntityStream()
            .filter(e -> e.isAlive())
            .filter(e -> e.getPos().squaredDistanceTo(vPos) < 2)
            .filter(e -> e.getBoundingBox().intersects(b))
            .collect(Collectors.toCollection(ArrayList::new));

        return ents.isEmpty();

    }

    private boolean checkHeight(Vec3d pos) {
        return this.getBoolSetting("AllowLow") || pos.getY() > ComoClient.me().getPos().getY();
    }

    // Get the blocks closest to a target.
    private List<BlockPos> getCloseOpenBlocks(Entity target, int totalBlocks) {
        Vec3d lookPos = RotationUtils.getEyePos().subtract(0.5, 0.5, 0.5);
        double d2 = this.getSquaredDistance(0.5);

        // Make sure that they are higher than us
        if (!this.checkHeight(target.getPos())) return new ArrayList<>();

        // Get the range of blocks that we should check around the player.
        BlockPos blockRange = new BlockPos(2, 2, 2);

        BlockPos centre = target.getBlockPos();

        BlockPos max    = centre.add(blockRange);
        BlockPos min    = centre.add(blockRange.multiply(-1));

        // Get the position which would do the most damage to the player.
        // TODO Maybe use this in killaura too?
        Vec3d targetEyeVec = target.getPos()
            .add(0, target.getEyeHeight(target.getPose()), 0);

        // Get the block positions closest to the target's head.
        Comparator<BlockPos> toTarget = Comparator.<BlockPos> comparingDouble(
            pos -> targetEyeVec.squaredDistanceTo(Vec3d.ofCenter(pos.add(0, 1, 0)))  
        );

        List<BlockPos> allBlocks = BlockUtils.getAllInBox(min, max);
        allBlocks.sort(toTarget);

        List<BlockPos> targetBlocks = new ArrayList<>();

        for (BlockPos pos : allBlocks) {
            if (totalBlocks <= 0) break;

            Vec3d vPos = Vec3d.of(pos);

            // Make sure that the block is a crystal surface.
            if (!this.isCrystalSurface(pos)) continue;

            // Make sure that there is room for the crystal
            if (!this.airAbove(pos)) continue;

            // Make sure that it isn't lower than us
            if (!this.checkHeight(vPos)) continue;

            // Make sure that the crystal would be the same height as the target
            if (pos.getY() - targetEyeVec.getY() > this.getDoubleSetting("MaxHeightDiff")) continue;

            // Make sure that the target is not too far away
            if (lookPos.squaredDistanceTo(vPos) > d2) continue;

            // Make sure that the space is not occupied
            if (!this.contactsEntity(pos)) continue;

            targetBlocks.add(pos);
            totalBlocks--;
        }
        
        return targetBlocks;
    }

    // Checks if a surface can be clicked and is not replaceable
    private boolean isClickable(BlockPos pos) {
        return BlockUtils.canBeClicked(pos);
    }

    private boolean holdThisTick = false;
    private boolean holdCrystal() {
        // If we already have a crystal then we don't need to do anything
        if (ComoClient.me().getMainHandStack().isOf(Items.END_CRYSTAL)) return true;
    
        // Check if we want to select a crystal
        if (!this.getBoolSetting("SelectCrystal") || holdThisTick) return false;

        int slot = InventoryUtils.getSlotWithItem(Items.END_CRYSTAL);
        if (slot == -1) return false;

        // Get the current hand's item slot
        int to = InventoryUtils.getMainHandSlot();

        // Move the item to the current hand
        InventoryUtils.moveItem(slot, to);

        holdThisTick = true;
        return true;
    }

    // Place a crystal at a block position
    private boolean placeCrystal(BlockPos pos) {
        Vec3d eyePos = RotationUtils.getEyePos();
        Vec3d posVec = Vec3d.ofCenter(pos);

        double d2 = this.getSquaredDistance();
        double dist = eyePos.squaredDistanceTo(posVec);

        // For each side.
        for (Direction side : Direction.values()) {
            BlockPos proximate = pos.offset(side);

            // Make sure it has a proximate block that is clickable.
            if (!this.hasProxClickable(proximate)) continue;

            // Get the hit vector
            Vec3d directionVec = Vec3d.of(side.getVector());
            Vec3d hitVec = posVec.add(directionVec.multiply(0.5d));

            // Make sure we're in range.
            if (eyePos.squaredDistanceTo(hitVec) > d2) continue;

            if (dist > eyePos.squaredDistanceTo(posVec.add(directionVec))) continue;

            // Check that we can see the side with a ray (i.e. line of sight).
            if (this.getBoolSetting("LineOfSight")
                && ComoClient.getClient().world.raycast(
                    new RaycastContext(eyePos, hitVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, ComoClient.me())
                ).getType() == HitResult.Type.MISS
            ) continue;

            // Attempt to select a crystal
            if (!this.holdCrystal()) return false;

            // Look at the position
            this.scRot.lookAtPosServer(hitVec);

            // Place the crystal
            int dy = ComoClient.me().getBlockY() - proximate.getY() >= 0 ? 1 : -1;
            InteractionUtils.rightClickBlock(proximate.add(0, dy, 0), side.getOpposite(), hitVec);

            return true;
        }

        return false;
    }

    // Checks if any surrounding blocks are clickable.
    private boolean hasProxClickable(BlockPos pos) {
        return this.isClickable(pos.up())
            || this.isClickable(pos.down())
            || this.isClickable(pos.north())
            || this.isClickable(pos.east())
            || this.isClickable(pos.south())
            || this.isClickable(pos.west()
        );
    }

    private void detonate(List<Entity> crystals) {
        for (Entity crystal : crystals) {
            if (!this.checkHeight(crystal.getPos())) continue;

            // Look at crystal
            this.scRot.lookAtPosServer(crystal.getBoundingBox().getCenter());

            // Hit it
            ClientUtils.hitEntity(crystal);
        }
    }

    private void layCrystals(List<Entity> targets) {
        for (Entity target : targets) {
            List<BlockPos> blocks = this.getCloseOpenBlocks(target, (Integer)this.getSetting("MaxCrystals").value);
            this.crystalBlocks.clear();

            for (BlockPos pos : blocks) {
                if (!this.placeCrystal(pos)) continue;

                this.crystalBlocks.add(pos);
                break;
            }
        }
    }

    private List<BlockPos> crystalBlocks = new ArrayList<>();
    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "RenderWorldEvent": {
                if (!this.getBoolSetting("RenderTargetBlock")) break;

                RenderWorldEvent e = (RenderWorldEvent)event;
                
                for (BlockPos bPos : crystalBlocks) {
                    RenderUtils.renderBlockBox(e.mStack, bPos);
                }

                break;
            }
            case "ClientTickEvent": {
                List<Entity> crystals = this.getCloseCrystals();
                List<Entity> targets = this.getCloseTargets();
                this.holdThisTick = false;
                
                // Hit all crystals
                if (!crystals.isEmpty() && !targets.isEmpty()) {
                    this.detonate(crystals);
                    crystalBlocks.clear();
                    break;
                }

                // Place crystals around the targets
                if (this.getBoolSetting("PlaceCrystal")) {
                    // Lay and maybe hit the crystals
                    this.layCrystals(targets);
                }

                break;
            }
        }
    }
}
