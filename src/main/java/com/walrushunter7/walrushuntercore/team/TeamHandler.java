package com.walrushunter7.walrushuntercore.team;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;

public class TeamHandler {

    private Map<String, Team> teams = new HashMap<String, Team>();
    private Map<String, String> teamNameMap = new HashMap<String, String>();
    private Map<GameProfile, String> playerTeamIds = new HashMap<GameProfile, String>();
    public TeamSaveData teamSaveData;

    public static TeamHandler instance;

    public Set<Team> getTeams() {
        Set<Team> teamsSet = new HashSet<Team>();
        teamsSet.addAll(teams.values());
        return teamsSet;
    }

    public Team getTeam(String team) {
        if (team != null) {
            if (teams.containsKey(team)) {
                return teams.get(team);
            } else if (teamNameMap.containsKey(team)) {
                return teams.get(teamNameMap.get(team));
            }
        }
        return null;
    }

    public Collection<String> getTeamNames() {
        return teamNameMap.keySet();
    }

    public boolean teamExists(String teamId, String teamName) {
        return teams.containsKey(teamId) || teamNameMap.containsKey(teamName);
    }

    public String teamIdFromName(String teamName) {
        if (teamNameMap.containsKey(teamName)) {
            return teamNameMap.get(teamName);
        } else if (teams.containsKey(teamName)) {
            return teamName;
        } else {
            return null;
        }
    }

    public Team getPlayerTeam(EntityPlayer player) {
        return getPlayerTeam(player.getGameProfile());
    }

    public Team getPlayerTeam(GameProfile gameProfile) {
        if (playerTeamIds.containsKey(gameProfile)) {
            String teamId = playerTeamIds.get(gameProfile);
            if (teams.containsKey(teamId)) {
                return teams.get(teamId);
            }
        }
        return null;
    }

    public boolean addPlayerToTeam(EntityPlayer player, String team) {
        return this.addPlayerToTeam(player.getGameProfile(), team);
    }

    public boolean addPlayerToTeam(GameProfile gameProfile, String team) {
        String teamId = this.teamIdFromName(team);
        if (teamId != null) {
            if (playerTeamIds.containsKey(gameProfile)) {
                String oldTeam = playerTeamIds.get(gameProfile);
                playerTeamIds.remove(gameProfile);
                teams.get(oldTeam).removePlayer(gameProfile);
            }
            playerTeamIds.put(gameProfile, teamId);
            teams.get(teamId).addPlayer(gameProfile);
            teamSaveData.markDirty();
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayerFromTeam(EntityPlayer player) {
        return this.removePlayerFromTeam(player.getGameProfile());
    }

    public boolean removePlayerFromTeam(GameProfile gameProfile) {
        if (playerTeamIds.containsKey(gameProfile)) {
            String oldTeam = playerTeamIds.get(gameProfile);
            playerTeamIds.remove(gameProfile);
            teams.get(oldTeam).removePlayer(gameProfile);
            teamSaveData.markDirty();
            return true;
        }
        return false;
    }

    public void clearExisting() {
        teams.clear();
        playerTeamIds.clear();
    }

    public void setupPlayerTeamIds() {
        for (Team team: getTeams()) {
            for (GameProfile gameProfile: team.getPlayers()) {
                playerTeamIds.put(gameProfile, team.getTeamId());
            }
        }
    }

    public void addTeam(Team team) {
        teams.put(team.getTeamId(), team);
        teamNameMap.put(team.getTeamName(), team.getTeamId());
        teamSaveData.markDirty();
    }

    public void removeTeam(Team team) {
        if (team != null && teams.containsValue(team)) {
            teams.remove(team.getTeamId());
            teams.remove(team.getTeamName());
            for (GameProfile gameProfile : team.getPlayers()) {
                playerTeamIds.remove(gameProfile);
            }
            teamSaveData.markDirty();
        }
    }

}