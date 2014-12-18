package com.walrushunter7.walrushuntercore.team;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

import java.util.Set;

public class TeamSaveData extends WorldSavedData {

    public TeamSaveData(String fileName) {
        super(fileName);
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        Set<Team> teams = TeamHandler.instance.getTeams();
        NBTTagList teamsList = new NBTTagList();
        for (Team team: teams) {
            NBTTagCompound teamTag = new NBTTagCompound();
            team.WriteToNBT(teamTag);
            teamsList.appendTag(teamTag);
        }
        tagCompound.setTag("TeamsList", teamsList);


    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        TeamHandler.instance.clearExisting();
        TeamHandler.instance.teamSaveData = this;
        NBTTagList teamsList = tagCompound.getTagList("TeamsList", 10);
        for (int i = 0; teamsList.tagCount() > i; i++) {
            NBTTagCompound teamTag = teamsList.getCompoundTagAt(i);
            Team team = new Team(teamTag);
            TeamHandler.instance.addTeam(team);
        }
        TeamHandler.instance.setupPlayerTeamIds();

    }

}
