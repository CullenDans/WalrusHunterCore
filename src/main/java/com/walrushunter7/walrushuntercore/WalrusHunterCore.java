package com.walrushunter7.walrushuntercore;

import com.walrushunter7.walrushuntercore.event.WHEventHandler;
import com.walrushunter7.walrushuntercore.reference.Ref;
import com.walrushunter7.walrushuntercore.save.SaveDataHandler;
import com.walrushunter7.walrushuntercore.team.TeamHandler;
import com.walrushunter7.walrushuntercore.team.TeamSaveData;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Ref.MODID, name = Ref.NAME, version = Ref.VERSION)
public class WalrusHunterCore {

    @Mod.Instance(Ref.MODID)
    public static WalrusHunterCore instance;

    public static SimpleNetworkWrapper network;

    @Mod.EventHandler
    public void PreInit(FMLPreInitializationEvent event){
        network = NetworkRegistry.INSTANCE.newSimpleChannel("WHCoreChannel");
    }

    @Mod.EventHandler
    public void Init(FMLInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new WHEventHandler());

        TeamSaveData teamSaveData = new TeamSaveData("Teams");
        SaveDataHandler.addSaveData(teamSaveData);
        TeamHandler.teamSaveData = teamSaveData;
    }

    @Mod.EventHandler
    public void PostInit(FMLPostInitializationEvent event){

    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {

    }

}
