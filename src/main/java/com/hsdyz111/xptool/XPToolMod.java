package com.hsdyz111.xptool;

import com.hsdyz111.xptool.integration.create.CreateIntegration;
import com.hsdyz111.xptool.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(XPToolMod.MODID)
public class XPToolMod {
    public static final String MODID = "xptool";
    private static final Logger LOGGER = LogUtils.getLogger();

    public XPToolMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        LOGGER.info("Registering items...");
        ModItems.ITEMS.register(modEventBus);
        
        LOGGER.info("Registering enchantments...");
        ModItems.ENCHANTMENTS.register(modEventBus);
        
        // 注册设置事件
        modEventBus.addListener(this::setup);
        
        // 注册创造模式标签页事件
        modEventBus.addListener(this::addCreative);
        
        LOGGER.info("XP Tool mod constructor complete");
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        // 初始化 Create 集成
        CreateIntegration.init();
        LOGGER.info("XP Tool mod setup complete");
        
        // 输出注册的物品信息
        ModItems.ITEMS.getEntries().forEach(item -> {
            LOGGER.info("Registered item: {}", item.getId());
        });
    }
    
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // 将 XP 工具添加到工具标签页
        if (event.getTabKey() == net.minecraft.world.item.CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.XP_TOOL.get());
        }
    }
}
