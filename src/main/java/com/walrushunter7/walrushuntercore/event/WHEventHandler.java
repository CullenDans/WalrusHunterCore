package com.walrushunter7.walrushuntercore.event;

import com.walrushunter7.walrushuntercore.save.SaveDataHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class WHEventHandler {

    @SubscribeEvent
    public void load(WorldEvent.Load event) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            if (event.world.provider.dimensionId == 0) {
                SaveDataHandler.Load();
            }
        }
    }

    @SubscribeEvent
    public void save(WorldEvent.Save event) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            if (event.world.provider.dimensionId == 0) {
                SaveDataHandler.Save();
            }
        }
    }

}
