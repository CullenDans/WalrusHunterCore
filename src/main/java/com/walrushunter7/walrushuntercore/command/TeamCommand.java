package com.walrushunter7.walrushuntercore.command;

import com.mojang.authlib.GameProfile;
import com.walrushunter7.walrushuntercore.team.Team;
import com.walrushunter7.walrushuntercore.team.TeamHandler;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class TeamCommand extends CommandBase{

    public String getCommandName()
    {
        return "teams";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender iCommandSender)
    {
        return "commands.teams.usage";
    }

    public void processCommand(ICommandSender iCommandSender, String[] args) {

        if (args.length > 1) {

            //teams create test [TeatTeam]
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length < 2) {
                    throw new WrongUsageException("commands.scoreboard.teams.add.usage", 0);
                }
                this.add(iCommandSender, args, 1);
            }

            //teams remove test
            else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length < 2) {
                    throw new WrongUsageException("commands.scoreboard.teams.remove.usage", 0);
                }
                this.remove(iCommandSender, args, 1);
            }

            //teams join test [player]
            else if (args[0].equalsIgnoreCase("join")) {
                if (args.length < 3 && (args.length != 2 || !(iCommandSender instanceof EntityPlayer))) {
                    throw new WrongUsageException("commands.scoreboard.teams.join.usage", 0);
                }
                this.join(iCommandSender, args, 1);
            }

            //teams leave [player]
            else if (args[0].equalsIgnoreCase("leave")) {
                if (args.length < 1 && !(iCommandSender instanceof EntityPlayer)) {
                    throw new WrongUsageException("commands.scoreboard.teams.leave.usage", 0);
                }
                this.leave(iCommandSender, args, 1);
            }

            //team list
            else if (args[0].equalsIgnoreCase("list")) {
                if (args.length < 1) {
                    throw new WrongUsageException("commands.scoreboard.teams.list.usage", 0);
                }
                this.list(iCommandSender, args, 1);
            }
            return;
        }

        throw new WrongUsageException("commands.scoreboard.teams.usage");

    }

    public void add(ICommandSender iCommandSender, String[] args, int num) {
        String teamId = args[num];
        String teamName;
        if (args.length == 3) {
            teamName = args[2];
        }
        else {
            teamName = teamId;
        }
        TeamHandler teamHandler = TeamHandler.instance;

        if (teamHandler.teamExists(teamId, teamName))
        {
            throw new CommandException("commands.scoreboard.teams.add.alreadyExists", teamName);
        }
        else if (teamId.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", teamId, 16);
        }
        else if (teamId.length() == 0)
        {
            throw new WrongUsageException("commands.scoreboard.teams.add.usage", 0);
        }
        else
        {
            if (!teamId.equals(teamName))
            {

                if (teamName.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", teamName, 32);
                }
                else if (!(teamName.length() > 0))
                {
                    throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooShort");
                }
                else {
                    Team team = new Team(teamId, teamName);
                    teamHandler.addTeam(team);
                }
            }
            else
            {
                Team team = new Team(teamId, teamName);
                teamHandler.addTeam(team);
            }

            func_152373_a(iCommandSender, this, "commands.scoreboard.teams.add.success", teamName);
        }
    }

    public void remove(ICommandSender iCommandSender, String[] args, int num) {
        String teamId = args[num];
        TeamHandler teamHandler = TeamHandler.instance;
        Team team = teamHandler.getTeam(teamId);

        if (team != null) {
            teamHandler.removeTeam(team);
            func_152373_a(iCommandSender, this, "commands.scoreboard.teams.remove.success", team.getTeamName());
        }
    }

    public void join(ICommandSender iCommandSender, String[] args, int num) {
        TeamHandler teamHandler = TeamHandler.instance;
        MinecraftServer minecraftServer = MinecraftServer.getServer();
        String teamId = args[num++];

        HashSet<String> addedPlayers = new HashSet<String>();
        HashSet<String> errorPlayers = new HashSet<String>();
        String playerName;

        if (iCommandSender instanceof EntityPlayer && args.length == num)
        {
            playerName = getCommandSenderAsPlayer(iCommandSender).getCommandSenderName();

            GameProfile gameProfile = minecraftServer.func_152358_ax().func_152655_a(playerName);

            if (teamHandler.addPlayerToTeam(gameProfile, teamId))
            {
                addedPlayers.add(gameProfile.getName());
            }
            else
            {
                errorPlayers.add(gameProfile.getName());
            }
        }
        else
        {
            while (num < args.length)
            {
                playerName = func_96332_d(iCommandSender, args[num++]);
                GameProfile gameProfile = minecraftServer.func_152358_ax().func_152655_a(playerName);

                if (teamHandler.addPlayerToTeam(gameProfile, teamId))
                {
                    addedPlayers.add(gameProfile.getName());
                }
                else
                {
                    errorPlayers.add(gameProfile.getName());
                }
            }
        }

        if (!addedPlayers.isEmpty())
        {

            func_152373_a(iCommandSender, this, "commands.scoreboard.teams.join.success", addedPlayers.size(), teamId, joinNiceString(addedPlayers.toArray()));
        }

        if (!errorPlayers.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.join.failure", errorPlayers.size(), teamId, joinNiceString(errorPlayers.toArray()));
        }
    }

    public void leave(ICommandSender iCommandSender, String[] args, int num) {
        TeamHandler teamHandler = TeamHandler.instance;
        MinecraftServer minecraftServer = MinecraftServer.getServer();

        HashSet<String> removedPlayers = new HashSet<String>();
        HashSet<String> errorPlayers = new HashSet<String>();
        String playerName;

        if (iCommandSender instanceof EntityPlayer && args.length == num)
        {
            playerName = getCommandSenderAsPlayer(iCommandSender).getCommandSenderName();

            GameProfile gameProfile = minecraftServer.func_152358_ax().func_152655_a(playerName);

            if (teamHandler.removePlayerFromTeam(gameProfile))
            {
                removedPlayers.add(gameProfile.getName());
            }
            else
            {
                errorPlayers.add(gameProfile.getName());
            }
        }
        else
        {
            while (num < args.length)
            {
                playerName = func_96332_d(iCommandSender, args[num++]);
                GameProfile gameProfile = minecraftServer.func_152358_ax().func_152655_a(playerName);

                if (teamHandler.removePlayerFromTeam(gameProfile))
                {
                    removedPlayers.add(gameProfile.getName());
                }
                else
                {
                    errorPlayers.add(gameProfile.getName());
                }
            }
        }

        if (!removedPlayers.isEmpty())
        {

            func_152373_a(iCommandSender, this, "commands.scoreboard.teams.leave.success", removedPlayers.size(), joinNiceString(removedPlayers.toArray()));
        }

        if (!errorPlayers.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.leave.failure", errorPlayers.size(), joinNiceString(errorPlayers.toArray()));
        }
    }

    public void list(ICommandSender iCommandSender, String[] args, int num) {
        TeamHandler teamHandler = TeamHandler.instance;

        if (args.length > num)
        {
            Team team = teamHandler.getTeam(args[num]);

            if (team == null)
            {
                return;
            }

            Collection<GameProfile> gameProfiles = team.getPlayers();
            Collection<String> players = new ArrayList<String>();
            for (GameProfile gameProfile: gameProfiles) {
                players.add(gameProfile.getName());
            }

            if (players.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.teams.list.player.empty", team.getTeamName());
            }

            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.scoreboard.teams.list.player.count", players.size(), team.getTeamName());
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            iCommandSender.addChatMessage(chatcomponenttranslation);
            iCommandSender.addChatMessage(new ChatComponentText(joinNiceString(players.toArray())));
        }
        else
        {
            Collection<Team> teams = teamHandler.getTeams();

            if (teams.size() <= 0)
            {
                iCommandSender.addChatMessage(new ChatComponentTranslation("No Teams"));
                throw new CommandException("commands.scoreboard.teams.list.empty", 0);
            }

            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.scoreboard.teams.list.count", teams.size());
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            iCommandSender.addChatMessage(chatComponentTranslation);

            for (Team team : teams) {
                iCommandSender.addChatMessage(new ChatComponentTranslation("commands.scoreboard.teams.list.entry", team.getTeamName(), team.getPlayers().size()));
            }
        }
    }


}
