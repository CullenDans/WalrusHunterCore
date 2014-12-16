package com.walrushunter7.walrushuntercore.team;

import com.walrushunter7.walrushuntercore.save.SaveData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Set;

public class TeamSaveData extends SaveData {

    public TeamSaveData(String fileName) {
        this.fileName = fileName;
    }

    public void WriteToNBT(NBTTagCompound tagCompound) {
        Set<Team> teams = TeamHandler.getTeams();
        NBTTagList teamsList = new NBTTagList();
        for (Team team: teams) {
            NBTTagCompound teamTag = new NBTTagCompound();
            team.WriteToNBT(teamTag);
            teamsList.appendTag(teamTag);
        }
        tagCompound.setTag("TeamsList", teamsList);


    }

    public void ReadFromNBT(NBTTagCompound tagCompound) {
        TeamHandler.clearExisting();
        NBTTagList teamsList = tagCompound.getTagList("TeamsList", 10);
        for (int i = 0; teamsList.tagCount() > i; i++) {
            NBTTagCompound teamTag = teamsList.getCompoundTagAt(i);
            Team team = new Team(teamTag);
            TeamHandler.addTeam(team);
        }
        TeamHandler.setupPlayerTeamIds();

    }

}
