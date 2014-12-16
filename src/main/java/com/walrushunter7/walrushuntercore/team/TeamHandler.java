package com.walrushunter7.walrushuntercore.team;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;

public class TeamHandler {

    private static Map<String, Team> teams = new HashMap<String, Team>();
    private static Map<GameProfile, String> playerTeamIds = new HashMap<GameProfile, String>();

    public static Set<Team> getTeams() {
        Set<Team> teamsSet = new HashSet<Team>();
        teamsSet.addAll(teams.values());
        return teamsSet;
    }

    public static TeamSaveData teamSaveData;

    public static Team getTeamFromID(String teamID) {
        if (teamID != null) {
            if (teams.containsKey(teamID)) {
                return teams.get(teamID);
            }
        }
        return null;
    }

    public static Team getPlayerTeam(EntityPlayer player) {
        return getPlayerTeam(player.getGameProfile());
    }

    public static Team getPlayerTeam(GameProfile gameProfile) {
        if (playerTeamIds.containsKey(gameProfile)) {
            String teamId = playerTeamIds.get(gameProfile);
            if (teams.containsKey(teamId)) {
                return teams.get(teamId);
            }
        }
        return null;
    }

    public static void addPlayerToTeam(EntityPlayer player, String team) {
        addPlayerToTeam(player.getGameProfile(), team);
    }

    public static void addPlayerToTeam(GameProfile gameProfile, String team) {
        if (playerTeamIds.containsKey(gameProfile)) {
            String oldTeam = playerTeamIds.get(gameProfile);
            playerTeamIds.remove(gameProfile);
            teams.get(oldTeam).removePlayer(gameProfile);
        }
        playerTeamIds.put(gameProfile, team);
        teams.get(team).addPlayer(gameProfile);
        teamSaveData.markDirty();
    }

    public static void removePlayerFromTeam(EntityPlayer player, String team) {
        removePlayerFromTeam(player.getGameProfile(), team);
    }

    public static void removePlayerFromTeam(GameProfile gameProfile, String team) {
        if (playerTeamIds.containsKey(gameProfile)) {
            if (playerTeamIds.get(gameProfile) == team) {
                String oldTeam = playerTeamIds.get(gameProfile);
                playerTeamIds.remove(gameProfile);
                teams.get(oldTeam).removePlayer(gameProfile);
            }
        }
        teamSaveData.markDirty();
    }

    public static void clearExisting() {
        teams.clear();
        playerTeamIds.clear();
    }

    public static void setupPlayerTeamIds() {
        for (Team team: getTeams()) {
            for (GameProfile gameProfile: team.getPlayers()) {
                playerTeamIds.put(gameProfile, team.getTeamId());
            }
        }
    }

    public static void addTeam(Team team) {
        teams.put(team.getTeamId(), team);
        teamSaveData.markDirty();
    }

    public static void removeTeam(Team team) {
        teams.remove(team.getTeamId());
        for (GameProfile gameProfile: team.getPlayers()) {
            playerTeamIds.remove(gameProfile);
        }
        teamSaveData.markDirty();
    }

}