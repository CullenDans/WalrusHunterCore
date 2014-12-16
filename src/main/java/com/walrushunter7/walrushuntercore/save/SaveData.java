package com.walrushunter7.walrushuntercore.save;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.io.File;

public abstract class SaveData {

    public File dataDir;
    public String fileName;

    protected boolean dirty;

    public SaveData() {

    }

    public abstract void WriteToNBT(NBTTagCompound tagCompound);

    public abstract void ReadFromNBT(NBTTagCompound tagCompound);

    public void Save() {
        if (this.dirty) {
            this.dirty = false;
            NBTTagCompound tagCompound = new NBTTagCompound();
            this.WriteToNBT(tagCompound);
            NBTtoFile.writeTag(dataDir, fileName, tagCompound);
        }
    }

    public void Load() {
        if (dataDir == null) {
            dataDir = new File(MinecraftServer.getServer().worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "data");
        }
        NBTTagCompound tagCompound =NBTtoFile.readTag(dataDir, fileName);
        this.ReadFromNBT(tagCompound);
    }

    public void markDirty() {
        this.dirty = true;
    }

}
