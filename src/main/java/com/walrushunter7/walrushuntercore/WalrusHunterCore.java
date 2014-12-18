package com.walrushunter7.walrushuntercore;

import com.walrushunter7.walrushuntercore.command.TeamCommand;
import com.walrushunter7.walrushuntercore.event.WHEventHandler;
import com.walrushunter7.walrushuntercore.reference.Ref;
import com.walrushunter7.walrushuntercore.team.TeamHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
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
    }

    @Mod.EventHandler
    public void PostInit(FMLPostInitializationEvent event){

    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        TeamHandler.instance = new TeamHandler();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        ICommandManager command = server.getCommandManager();
        ServerCommandManager manager = (ServerCommandManager) command;
        manager.registerCommand(new TeamCommand());
    }

}
