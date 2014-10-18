package com.ddtpt.android.yffa;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by e228596 on 10/14/2014.
 */
public class JSONParser {

    public ArrayList<Player> parsePlayerList(String json) {
        ArrayList<Player> players = new ArrayList<Player>();

        try {

            final ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(json);

            //Parse player information out of the JsonTree
            JsonNode playersNode = rootNode.findPath("players");
            Iterator<JsonNode> iterator = playersNode.elements();
            while (iterator.hasNext()) {
                Player rosterPlayer = new Player();
                JsonNode playerData = iterator.next();
                if (!playerData.findPath("player").isMissingNode()) {
                    playerData = playerData.findPath("player");
                    rosterPlayer.setPlayerKey(playerData.findValue("player_key").asText(""));
                    rosterPlayer.setFullName(playerData.findPath("name").findValue("full").asText(""));
                    rosterPlayer.setEditorialPlayerKey(playerData.findValue("editorial_player_key").asText(""));
                    rosterPlayer.setEditorialTeamKey(playerData.findValue("editorial_team_key").asText(""));
                    rosterPlayer.setEditorialTeamFullName(playerData.findValue("editorial_team_full_name").asText(""));
                    rosterPlayer.setEditorialTeamAbbr(playerData.findValue("editorial_team_abbr").asText(""));
                    rosterPlayer.setByeWeek(playerData.findPath("bye_weeks").findValue("week").asText(""));
                    rosterPlayer.setUniformNumber(playerData.findValue("uniform_number").asText(""));
                    rosterPlayer.setImageUrl(playerData.findValue("image_url").asText(""));
                    rosterPlayer.setDisplayPosition(playerData.findValue("display_position").asText(""));
                    rosterPlayer.setPlayerTotal(playerData.findValue("player_points").get("total").asDouble());
                    JsonNode temp = playerData.findValue("status");
                    if (temp != null) {
                        rosterPlayer.setInjuryStatus(playerData.findValue("status").asText());
                    } else {
                        rosterPlayer.setInjuryStatus("");
                    }
                    temp = playerData.findValue("injury_note");
                    if (temp != null) {
                        rosterPlayer.setInjuryStatus(playerData.findValue("injury_note").asText());
                    } else {
                        rosterPlayer.setInjuryStatus("");
                    }
                    temp = playerData.findValue("has_player_notes");
                    if (temp != null) {
                        rosterPlayer.setHasPlayerNotes(playerData.findValue("has_player_notes").asInt());
                    } else {
                        rosterPlayer.setHasPlayerNotes(0);
                    }
                    rosterPlayer.setSelectedPosition(playerData.findPath("selected_position").findValue("position").asText(""));

                    //Parse player stats out of the JsonTree
                    JsonNode playerStats = playerData.findPath("player_stats").get("stats");
                    Iterator<JsonNode> innerIterator = playerStats.elements();
                    HashMap<String, Double> scoreMap = new HashMap<String, Double>();
                    while (innerIterator.hasNext()) {
                        playerStats = innerIterator.next().get("stat");
                        scoreMap.put(playerStats.get("stat_id").asText(), playerStats.get("value").asDouble());
                    }
                    rosterPlayer.setPlayerScores(scoreMap);

                    players.add(rosterPlayer);
                }

            }

        }catch(Exception e) {
                Log.e("JSONPARSER", e.toString());
            }

        return players;
    }

    public ArrayList<Stats> parseStatMap(String json) {
        HashMap<Integer, String> modMap = new HashMap<Integer, String>();
        ArrayList<Stats> leagueStats = new ArrayList<Stats>();

        final ObjectMapper mapper = new ObjectMapper();
        try {
            String name, shortname;
            boolean scored;
            int id;
            double mod;
            JsonNode rootNode = mapper.readTree(json);
            JsonNode statMod = rootNode.findPath("stat_modifiers").get("stats");
            Iterator<JsonNode> iterator = statMod.elements();
            while (iterator.hasNext()) {
                JsonNode singleStat = iterator.next().findPath("stat");
                if (!singleStat.isMissingNode()) {
                    modMap.put(singleStat.get("stat_id").asInt(), singleStat.get("value").asText());
                }
            }

            JsonNode statGroup = rootNode.findPath("stat_categories").get("stats");
            iterator = statGroup.elements();
            while (iterator.hasNext()) {
                JsonNode singleStat = iterator.next().findPath("stat");
                if (!singleStat.isMissingNode()) {
                    id = singleStat.get("stat_id").asInt();
                    name = singleStat.get("name").asText();
                    shortname = singleStat.get("display_name").asText();
                    if (singleStat.findPath("is_only_display_stat").isMissingNode()) {
                        scored = true;
                    } else {
                        scored = false;
                    }
                    if (modMap.get(id) == null) {
                        mod = 0;
                    } else {
                        mod = Double.valueOf(modMap.get(id));
                    }
                    leagueStats.add(new Stats(id, mod, name, shortname, scored));
                }
            }

        } catch(Exception e) {
            Log.e("STATMAPERROR", e.toString());
        }

        return leagueStats;
    }

    public String parseGameKey(String json) {
        String gameKey = "";

        try {
            JSONObject games = new JSONObject(json);
            JSONObject stub = games.getJSONObject("fantasy_content").getJSONObject("users").getJSONObject("0")
                    .getJSONArray("user").getJSONObject(1).getJSONObject("games");
            int value = stub.getInt("count")-1;
            gameKey = stub.getJSONObject(String.valueOf(value)).getJSONArray("game").getJSONObject(0).getString("game_key");
        } catch(JSONException e) {
            Log.e("JSONPARSE", e.toString());
        }
        return gameKey;
    }

    public String parseLeagueKey(String json) {
        String leagueKey = "";

        try {
            JSONObject leagues = new JSONObject(json);
            leagueKey = leagues.getJSONObject("fantasy_content").getJSONObject("users").getJSONObject("0")
                    .getJSONArray("user").getJSONObject(1).getJSONObject("games").getJSONObject("0").getJSONArray("game")
                    .getJSONObject(1).getJSONObject("leagues").getJSONObject("0").getJSONArray("league").getJSONObject(0).getString("league_key");

        } catch (JSONException e) {
            Log.e("JSONPARSE", e.toString());
        }
        return leagueKey;
    }

    public String parseTeamKey(String json) {
        String teamKey = "";

        try {
            JSONObject teams = new JSONObject(json);
            teamKey = teams.getJSONObject("fantasy_content").getJSONObject("users").getJSONObject("0")
                    .getJSONArray("user").getJSONObject(1).getJSONObject("games").getJSONObject("0")
                    .getJSONArray("game").getJSONObject(1).getJSONObject("teams").getJSONObject("0")
                    .getJSONArray("team").getJSONArray(0).getJSONObject(0).getString("team_key");
            //teamKey = teams.

        } catch (JSONException e) {
            Log.e("JSONPARSE", e.toString());
        }
        return teamKey;
    }

    public ArrayList<MatchupObject> parseMatchupData(String json) {
        ArrayList<MatchupObject> matchups = new ArrayList<MatchupObject>();

        return matchups;
    }
}
