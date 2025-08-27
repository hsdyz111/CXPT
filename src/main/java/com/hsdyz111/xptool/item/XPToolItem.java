package com.hsdyz111.xptool.item;

import java.util.Map;

import com.hsdyz111.xptool.integration.create.CreateIntegration;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class XPToolItem extends Item {
    private static final int EXPERIENCE_PER_NUGGET = 3; // 每个经验颗粒代表3点经验
    
    public XPToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (!level.isClientSide && player.totalExperience >= EXPERIENCE_PER_NUGGET) {
            // 抽取一次经验（不消耗耐久度）
            extractExperience(player, stack, hand, false);
            return InteractionResultHolder.success(stack);
        }
        
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide && attacker instanceof Player player) {
            int xpToDrain = calculateXPDrain(stack, player);
            
            if (target instanceof Player targetPlayer) {
                // 攻击玩家
                if (targetPlayer.totalExperience >= xpToDrain) {
                    targetPlayer.giveExperiencePoints(-xpToDrain);
                    spawnExperienceNuggets(target, xpToDrain);
                }
            } else {
                // 攻击生物 - 从生物获取经验
                spawnExperienceNuggets(target, xpToDrain);
            }
            
            // 攻击时消耗耐久度
            stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
        }
        
        return super.hurtEnemy(stack, target, attacker);
    }

    public static void extractExperience(Player player, ItemStack stack, InteractionHand hand, boolean consumeDurability) {
        if (player.totalExperience >= EXPERIENCE_PER_NUGGET) {
            // 减少玩家经验
            player.giveExperiencePoints(-EXPERIENCE_PER_NUGGET);
            
            // 生成经验颗粒或经验球
            Level level = player.level();
            if (CreateIntegration.isCreateLoaded() && CreateIntegration.getExperienceNugget() != null) {
                // 生成经验颗粒物品
                ItemStack nuggetStack = new ItemStack(CreateIntegration.getExperienceNugget(), 1);
                level.addFreshEntity(new ItemEntity(
                    level, 
                    player.getX(), 
                    player.getY() + player.getEyeHeight(), 
                    player.getZ(), 
                    nuggetStack
                ));
            } else {
                // 生成原版经验球
                level.addFreshEntity(new ExperienceOrb(
                    level, 
                    player.getX(), 
                    player.getY() + player.getEyeHeight(), 
                    player.getZ(), 
                    EXPERIENCE_PER_NUGGET
                ));
            }
            
            // 只有在需要时才消耗耐久度
            if (consumeDurability) {
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            }
        }
    }

    private int calculateXPDrain(ItemStack stack, Player player) {
        int baseDrain = 10;
        
        // 检查效率附魔
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if (enchantments.containsKey(Enchantments.BLOCK_EFFICIENCY)) {
            int efficiencyLevel = enchantments.get(Enchantments.BLOCK_EFFICIENCY);
            baseDrain += efficiencyLevel * 5;
        }
        
        return baseDrain;
    }

    private void spawnExperienceNuggets(Entity target, int xpAmount) {
        Level level = target.level();
        
        if (CreateIntegration.isCreateLoaded() && CreateIntegration.getExperienceNugget() != null) {
            // 生成经验颗粒物品
            int nuggets = xpAmount / EXPERIENCE_PER_NUGGET;
            int remainder = xpAmount % EXPERIENCE_PER_NUGGET;
            
            for (int i = 0; i < nuggets; i++) {
                ItemStack nuggetStack = new ItemStack(CreateIntegration.getExperienceNugget(), 1);
                level.addFreshEntity(new ItemEntity(
                    level, 
                    target.getX(), 
                    target.getY() + target.getBbHeight() / 2, 
                    target.getZ(), 
                    nuggetStack
                ));
            }
            
            // 处理剩余的经验值
            if (remainder > 0) {
                level.addFreshEntity(new ExperienceOrb(
                    level, 
                    target.getX(), 
                    target.getY() + target.getBbHeight() / 2, 
                    target.getZ(), 
                    remainder
                ));
            }
        } else {
            // 生成原版经验球
            level.addFreshEntity(new ExperienceOrb(
                level, 
                target.getX(), 
                target.getY() + target.getBbHeight() / 2, 
                target.getZ(), 
                xpAmount
            ));
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }
}
