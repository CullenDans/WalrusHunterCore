package com.walrushunter7.walrushuntercore.event;

import com.walrushunter7.walrushuntercore.team.TeamHandler;
import com.walrushunter7.walrushuntercore.team.TeamSaveData;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;

public class WHEventHandler {

    @SubscribeEvent
    public void load(WorldEvent.Load event) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            if (event.world.provider.dimensionId == 0) {
                MapStorage mapStorage = event.world.mapStorage;
                TeamSaveData saveData = (TeamSaveData) mapStorage.loadData(TeamSaveData.class, "WalrusHunterTeams");
                if (saveData == null) {
                    saveData = new TeamSaveData("WalrusHunterTeams");
                    mapStorage.setData("WalrusHunterTeams", saveData);
                }
                TeamHandler.instance.teamSaveData = saveData;
            }
        }
    }
}
