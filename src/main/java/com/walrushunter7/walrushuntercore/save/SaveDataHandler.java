package com.walrushunter7.walrushuntercore.save;

import java.util.HashSet;
import java.util.Set;

public class SaveDataHandler {

    private static Set<SaveData> saveDataSet = new HashSet<SaveData>();

    public static void addSaveData(SaveData saveData) {
        saveDataSet.add(saveData);
    }

    public static void Save() {
        for (SaveData saveData: saveDataSet) {
            saveData.Save();
        }
    }

    public static void Load() {
        for (SaveData saveData: saveDataSet) {
            saveData.Load();
        }
    }

}
