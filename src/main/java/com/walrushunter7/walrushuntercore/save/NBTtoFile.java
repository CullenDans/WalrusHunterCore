package com.walrushunter7.walrushuntercore.save;

import com.walrushunter7.walrushuntercore.util.Log;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class NBTtoFile {

    public static boolean doesTagExist() {
        return false;
    }

    public static NBTTagCompound readTag(File directory, String fileName) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        try
        {
            File file1 = new File(directory, fileName + ".dat");

            if (file1.exists())
            {
                tagCompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
            }
        }
        catch (Exception exception) {
            Log.error("Unable to read");
            Log.error(exception);}
        return tagCompound;
    }

    public static void writeTag(File directory, String fileName, NBTTagCompound tagCompound) {
        try {

            File file1 = new File(directory, fileName + ".dat.tmp");
            File file2 = new File(directory, fileName + ".dat");
            CompressedStreamTools.writeCompressed(tagCompound, new FileOutputStream(file1));

            if (file2.exists())
            {
                file2.delete();
            }

            file1.renameTo(file2);
        }
        catch (Exception exception) {
            Log.error("Unable to save");
            Log.error(exception);
        }
    }

}
