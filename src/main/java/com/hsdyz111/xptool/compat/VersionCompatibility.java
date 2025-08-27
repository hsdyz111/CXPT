package com.hsdyz111.xptool.compat;

import net.minecraftforge.fml.ModList;

public class VersionCompatibility {
    private static final String CREATE_MOD_ID = "create";
    private static String createVersion;
    
    static {
        // 获取Create模组的版本
        createVersion = ModList.get().getModContainerById(CREATE_MOD_ID)
            .map(container -> container.getModInfo().getVersion().toString())
            .orElse("unknown");
    }
    
    public static String getCreateVersion() {
        return createVersion;
    }
    
    public static boolean isCreateVersionAtLeast(String minVersion) {
        try {
            String[] currentParts = createVersion.split("\\.");
            String[] minParts = minVersion.split("\\.");
            
            int length = Math.max(currentParts.length, minParts.length);
            for (int i = 0; i < length; i++) {
                int currentPart = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
                int minPart = i < minParts.length ? Integer.parseInt(minParts[i]) : 0;
                
                if (currentPart < minPart) return false;
                if (currentPart > minPart) return true;
            }
            return true; // 版本完全相同
        } catch (NumberFormatException e) {
            return false; // 版本格式错误，假设不兼容
        }
    }
}
