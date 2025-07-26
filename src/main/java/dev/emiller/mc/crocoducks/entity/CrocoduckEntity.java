package dev.emiller.mc.crocoducks.entity;

import dev.emiller.mc.crocoducks.registries.CrocoducksEntities;
import dev.emiller.mc.crocoducks.registries.CrocoducksItems;
import dev.emiller.mc.crocoducks.registries.CrocoducksSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CrocoduckEntity extends TamableAnimal implements NeutralMob {
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(
            CrocoduckEntity.class,
            EntityDataSerializers.INT
    );
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(BuiltInRegistries.ITEM.stream()
                                                                                     .filter(item -> new ItemStack(item).getFoodProperties(
                                                                                             null) != null)
                                                                                     .toArray(Item[]::new));
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    public int eggTime = this.random.nextInt(6000) + 6000;
    private float nextFlap = 1.0F;

    @Nullable
    private UUID persistentAngerTarget;

    public CrocoduckEntity(EntityType<? extends CrocoduckEntity> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public static Boolean PREY_SELECTOR(LivingEntity entity, CrocoduckEntity crocoduck) {
        if (crocoduck.isTame()) {
            return false;
        }

        if (entity instanceof Player player) {
            if (crocoduck.isOwnedBy(player)) {
                return false;
            }

            ItemStack itemInMainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack itemInSecondHand = player.getItemInHand(InteractionHand.OFF_HAND);
            List<ItemStack> items = List.of(itemInMainHand, itemInSecondHand);
            if (items.stream().anyMatch(crocoduck::isFood)) {
                return false;
            }
        }

        if (entity instanceof TamableAnimal tamableAnimal) {
            if (tamableAnimal.isTame()) {
                return false;
            }
        }

        return !(entity instanceof CrocoduckEntity) && !entity.isUnderWater();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(
                Attributes.ATTACK_DAMAGE,
                2.0D
        );
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(
                4,
                new NearestAttackableTargetGoal<>(
                        this,
                        Player.class,
                        10,
                        true,
                        false,
                        this::isAngryAt
                )
        );
        this.targetSelector.addGoal(
                5,
                new NonTameRandomTargetGoal<>(
                        this,
                        LivingEntity.class,
                        false,
                        (entity) -> PREY_SELECTOR(entity, this)
                )
        );
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public void aiStep() {
        super.aiStep();
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (this.onGround() ? -1.0F : 4.0F) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
        }

        this.flap += this.flapping * 2.0F;
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && --this.eggTime <= 0) {
            this.playSound(
                    SoundEvents.CHICKEN_EGG,
                    1.0F,
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F
            );
            this.spawnAtLocation(CrocoducksItems.CROCODUCK_EGG.get());
            this.gameEvent(GameEvent.ENTITY_PLACE);
            this.eggTime = this.random.nextInt(6000) + 6000;
        }
    }

    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    protected void onFlap() {
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    protected SoundEvent getAmbientSound() {
        if (isBaby()) {
            return CrocoducksSoundEvents.ENTITY_CROCODUCK_BABY.get();
        }

        if (isInWater()) {
            return CrocoducksSoundEvents.ENTITY_CROCODUCK_WATER.get();
        }

        return CrocoducksSoundEvents.ENTITY_CROCODUCK_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return CrocoducksSoundEvents.ENTITY_CROCODUCK_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CrocoducksSoundEvents.ENTITY_CROCODUCK_DEATH.get();
    }

    protected SoundEvent getBiteSound() {
        return CrocoducksSoundEvents.ENTITY_CROCODUCK_BITE.get();
    }

    public boolean doHurtTarget(@NotNull Entity entity) {
        boolean value = super.doHurtTarget(entity);

        if (value) {
            this.playSound(
                    getBiteSound(),
                    this.getSoundVolume(),
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F
            );
        }

        return value;
    }

    protected void playStepSound(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    public boolean isFood(@NotNull ItemStack item) {
        return FOOD_ITEMS.test(item);
    }


    @Override
    protected void applyTamingSideEffects() {
        super.applyTamingSideEffects();

        AttributeInstance maxHealthAttribute = this.getAttribute(Attributes.MAX_HEALTH);
        if (isTame()) {
            if (maxHealthAttribute != null) {
                maxHealthAttribute.setBaseValue(10.0F);
            }
            this.setHealth(10.0F);
        } else {
            if (maxHealthAttribute != null) {
                maxHealthAttribute.setBaseValue(6.0F);
            }
        }
    }

    @Nullable
    public CrocoduckEntity getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return CrocoducksEntities.CROCODUCK.get().create(level);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("EggLayTime")) {
            this.eggTime = tag.getInt("EggLayTime");
        }

    }

    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("EggLayTime", this.eggTime);
    }

    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.level().isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || isFood(itemstack) && !this.isTame() && !this.isAngry();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        }

        boolean isFood = this.isFood(itemstack);

        if (!isFood) {
            if (this.isTame()) {
                this.setOrderedToSit(!isInSittingPose());
            }

            return super.mobInteract(player, hand);
        }

        @Nullable FoodProperties foodProperties = itemstack.getFoodProperties(this);
        if (foodProperties == null || foodProperties.nutrition() <= 0) {
            return InteractionResult.PASS;
        }

        int nutrition = foodProperties.nutrition();

        boolean wasUsed = false;

        if (this.isBaby() && !this.isAngry()) {
            this.ageUp(nutrition * 100, true);
            wasUsed = true;
        }

        if (this.isTame()) {
            if (this.getHealth() < this.getMaxHealth()) {
                this.heal((float) nutrition);
                wasUsed = true;
            }
        } else if (!this.isAngry()) {
            if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }

            wasUsed = true;
        }


        if (wasUsed) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            this.gameEvent(GameEvent.EAT, this);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);

    }

    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    public void setRemainingPersistentAngerTime(int time) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, time);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    public void setPersistentAngerTarget(@Nullable UUID id) {
        this.persistentAngerTarget = id;
    }
}
