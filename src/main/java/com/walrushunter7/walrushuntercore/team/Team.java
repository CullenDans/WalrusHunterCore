package com.walrushunter7.walrushuntercore.team;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTUtil;

import java.util.HashSet;
import java.util.Set;

public class Team {

    private String teamName;
    private String teamId;

    private Set<GameProfile> players = new HashSet<GameProfile>();
    public Set<String> allies = new HashSet<String>();
    public Set<String> enemies = new HashSet<String>();

    public Team(String teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public Team(NBTTagCompound tagCompound) {
        this.ReadFromNBT(tagCompound);
    }

    public void addPlayer(GameProfile gameProfile) {
        if (!players.contains(gameProfile)) {
            players.add(gameProfile);
        }
    }

    public void removePlayer(GameProfile gameProfile) {
        if (players.contains(gameProfile)) {
            players.remove(gameProfile);
        }
    }

    public Set<GameProfile> getPlayers() {
        return players;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void WriteToNBT(NBTTagCompound tagCompound) {
        tagCompound.setString("TeamId", this.teamId);
        tagCompound.setString("TeamName", this.teamName);

        NBTTagList playersList = new NBTTagList();
        for (GameProfile gameProfile: players) {
            NBTTagCompound gameProfileTag = new NBTTagCompound();
            NBTUtil.func_152460_a(gameProfileTag, gameProfile);
            playersList.appendTag(gameProfileTag);
        }
        tagCompound.setTag("PlayersList", playersList);

        NBTTagList alliesList = new NBTTagList();
        for (String allieId: allies) {
            NBTTagString allieTag = new NBTTagString(allieId);
            alliesList.appendTag(allieTag);
        }
        tagCompound.setTag("AlliesList", alliesList);

        NBTTagList enemiesList = new NBTTagList();
        for (String enemyId: enemies) {
            NBTTagString enemyTag = new NBTTagString(enemyId);
            alliesList.appendTag(enemyTag);
        }
        tagCompound.setTag("EnemiesList", enemiesList);
    }

    public void ReadFromNBT(NBTTagCompound tagCompound) {
        this.teamId = tagCompound.getString("TeamId");
        this.teamName = tagCompound.getString("TeamName");

        NBTTagList playersList = tagCompound.getTagList("PlayersList", 10);
        for (int i = 0; playersList.tagCount() > i; i++) {
            NBTTagCompound gameProfileTag = playersList.getCompoundTagAt(i);
            GameProfile gameProfile = NBTUtil.func_152459_a(gameProfileTag);
            this.players.add(gameProfile);
        }

        NBTTagList alliesList = tagCompound.getTagList("AlliesList", 8);
        for (int i = 0; alliesList.tagCount() > i; i++) {
            String allieId = alliesList.getStringTagAt(i);
            this.allies.add(allieId);
        }

        NBTTagList enemiesList = tagCompound.getTagList("EnemiesList", 8);
        for (int i = 0; enemiesList.tagCount() > i; i++) {
            String enemyId = enemiesList.getStringTagAt(i);
            this.enemies.add(enemyId);
        }
    }

}
