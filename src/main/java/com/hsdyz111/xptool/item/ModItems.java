package com.hsdyz111.xptool.item;

import com.hsdyz111.xptool.XPToolMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, XPToolMod.MODID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, XPToolMod.MODID);
    
    public static final RegistryObject<Item> XP_TOOL = ITEMS.register("xp_tool", 
        () -> new XPToolItem(new Item.Properties().stacksTo(1).durability(32)));
    
    // 这里可以添加附魔注册
    // public static final RegistryObject<Enchantment> XP_SIPHON = ENCHANTMENTS.register("xp_siphon", 
    //     () -> new XPSiphonEnchantment(Enchantment.Rarity.UNCOMMON));
}
