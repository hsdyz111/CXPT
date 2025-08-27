package com.hsdyz111.xptool.integration.create;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateIntegration {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation EXPERIENCE_NUGGET_ID = new ResourceLocation("create:experience_nugget");
    
    private static Item experienceNugget = null;
    private static boolean createLoaded = false;
    
    public static void init() {
        // 检查 Create 是否加载
        createLoaded = ForgeRegistries.ITEMS.containsKey(EXPERIENCE_NUGGET_ID);
        
        if (createLoaded) {
            experienceNugget = ForgeRegistries.ITEMS.getValue(EXPERIENCE_NUGGET_ID);
            LOGGER.info("Create integration initialized successfully");
        } else {
            LOGGER.warn("Create mod not found. XP Tool will not be able to create experience nuggets.");
        }
    }
    
    public static boolean isCreateLoaded() {
        return createLoaded;
    }
    
    public static Item getExperienceNugget() {
        return experienceNugget;
    }
    
    public static ResourceLocation getExperienceNuggetId() {
        return EXPERIENCE_NUGGET_ID;
    }
}
