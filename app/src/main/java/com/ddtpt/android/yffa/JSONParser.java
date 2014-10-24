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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by e228596 on 10/14/2014.
 */
public class JSONParser {

 /*   public ArrayList<Player> parsePlayerList(String json) {
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
    } */

    public ArrayList<HashMap<String, Object>> parsePlayerScoring(String json) {
        ArrayList<HashMap<String, Object>> fullStats = new ArrayList<HashMap<String, Object>>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode base = root.findPath("league").findPath("players");
            Iterator<JsonNode> iterator = base.elements();
            while (iterator.hasNext()) {
                HashMap<String, Object> tempStats = new HashMap<String, Object>();
                base = iterator.next().get("player");
                if (!(base == null)) {
                    tempStats.put("player_id", base.get(0).findPath("player_id").asInt());
                    tempStats.put("player_points", base.get(1).get("player_points").get("total").asDouble());
                    base = base.findPath("player_stats").get("stats");
                    Iterator<JsonNode> innerIter = base.elements();
                    while (innerIter.hasNext()) {
                        base = innerIter.next().get("stat");
                        if (!(base == null)) {
                            tempStats.put(base.get("stat_id").asText(), base.get("value").asInt());
                        }
                    }
                fullStats.add(tempStats);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("parsePlayerScoring EXCEPTION", e.toString());
        }


        return fullStats;
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

    public ArrayList<HashMap<String, Object>> parseMatchupData(String json) {
        final ObjectMapper mapper = new ObjectMapper();
        ArrayList<HashMap<String, Object>> allTeamData = new ArrayList<HashMap<String, Object>>();
        ArrayList<Integer> ids = new ArrayList<Integer>();

        String temp;
        int team_id;
        int index;

        try {
            JsonNode root = mapper.readTree(json);
            JsonNode matches = root.findPath("league").get(1).get("scoreboard").findPath("matchups");
            JsonNode standings = root.findPath("league").get(2).findPath("teams");
            JsonNode roster = root.findPath("league").get(3).findPath("teams");

            Iterator<JsonNode> iterator = matches.elements();

            while (iterator.hasNext()) {
                matches = iterator.next().findPath("teams");
                if (!matches.isMissingNode()) {
                    HashMap<String, Object> dataPointsHome = new HashMap<String, Object>();
                    HashMap<String, Object> dataPointsAway = new HashMap<String, Object>();
                    JsonNode homeNode = matches.get("0");
                    JsonNode awayNode = matches.get("1");

                    dataPointsHome.put("team_name", homeNode.findPath("name").asText());
                    dataPointsHome.put("team_key", homeNode.findPath("team_key").asText());
                    dataPointsHome.put("team_id", homeNode.findPath("team_id").asText());
                    dataPointsHome.put("team_nickname", homeNode.findPath("nickname").asText());
                    dataPointsHome.put("team_logos", homeNode.findPath("team_logos").findPath("url").asText());
                    dataPointsHome.put("team_waiver_priority", homeNode.findPath("waiver_priority").asText());
                    dataPointsHome.put("team_number_of_moves", homeNode.findPath("number_of_moves").asText());
                    dataPointsHome.put("team_points", homeNode.findPath("team_points").get("total").asText());
                    dataPointsHome.put("team_projected_points", homeNode.findPath("team_projected_points").get("total").asText());
                    ids.add(Integer.valueOf(homeNode.findPath("team_id").asText()));

                    dataPointsAway.put("team_name", awayNode.findPath("name").asText());
                    dataPointsAway.put("team_key", awayNode.findPath("team_key").asText());
                    dataPointsAway.put("team_id", awayNode.findPath("team_id").asText());
                    dataPointsAway.put("team_nickname", awayNode.findPath("nickname").asText());
                    dataPointsAway.put("team_logos", awayNode.findPath("team_logos").findPath("url").asText());
                    dataPointsAway.put("team_waiver_priority", awayNode.findPath("waiver_priority").asText());
                    dataPointsAway.put("team_number_of_moves", awayNode.findPath("number_of_moves").asText());
                    dataPointsAway.put("team_points", awayNode.findPath("team_points").get("total").asText());
                    dataPointsAway.put("team_projected_points", awayNode.findPath("team_projected_points").get("total").asText());
                    ids.add(Integer.valueOf(awayNode.findPath("team_id").asText()));

                    allTeamData.add(dataPointsHome);
                    allTeamData.add(dataPointsAway);
                }

            }
            iterator = standings.elements();
            while (iterator.hasNext()) {
                standings = iterator.next().findPath("team");
                if (!standings.isMissingNode()) {
                    temp = standings.findPath("team_id").asText();
                    team_id = Integer.valueOf(temp);
                    index = ids.indexOf(team_id);
                    allTeamData.get(index).put("team_rank", standings.findPath("team_standings").get("rank").asText());
                    allTeamData.get(index).put("team_wins", standings.findPath("team_standings").get("outcome_totals").get("wins").asText());
                    allTeamData.get(index).put("team_losses", standings.findPath("team_standings").get("outcome_totals").get("losses").asText());
                    allTeamData.get(index).put("team_ties", standings.findPath("team_standings").get("outcome_totals").get("ties").asText());
                    allTeamData.get(index).put("team_streak_type", standings.findPath("team_standings").get("streak").get("type").asText());
                    allTeamData.get(index).put("team_streak_value", standings.findPath("team_standings").get("streak").get("value").asText());
                    allTeamData.get(index).put("team_points_for_year", standings.findPath("team_standings").get("points_for").asText());
                    allTeamData.get(index).put("team_points_against_year", standings.findPath("team_standings").get("points_against").asText());
                }
            }

            iterator = roster.elements();
            while (iterator.hasNext()) {
                JsonNode team = iterator.next();
                temp = team.findPath("team_id").asText();
                if (!temp.equals("")) {
                    team = team.get("team").findPath("roster").findPath("players");
                    team_id = Integer.valueOf(temp);
                    index = ids.indexOf(team_id);

                    Iterator<JsonNode> innerIter = team.elements();
                    ArrayList<Player> players = new ArrayList<Player>();
                    while (innerIter.hasNext()) {
                        team = innerIter.next().get("player");
                        if (team != null) {
                            HashMap<String, Object> data = new HashMap<String, Object>();
                            data.put("player_full_name", team.findPath("name").get("full").asText());
                            data.put("player_key", team.findPath("player_key").asText());
                            data.put("player_editorial_key", team.findPath("editorial_player_key").asText());
                            data.put("player_editorial_team_key", team.findPath("editorial_team_key").asText());
                            data.put("player_editorial_team_full_name", team.findPath("editorial_team_full_name").asText());
                            data.put("player_editorial_team_abbr", team.findPath("editorial_team_abbr").asText());
                            data.put("player_bye_week", team.findPath("bye_weeks").get("week").asText());
                            data.put("player_uniform_number", team.findPath("uniform_number").asText());
                            data.put("player_image_url", team.findPath("headshot").get("url").asText());
                            data.put("player_display_position", team.findPath("display_position").asText());
                            data.put("player_selected_position", team.findPath("selected_position").findPath("position").asText());
                            data.put("player_injury_status", team.findPath("status").asText());
                            data.put("player_injury_note", team.findPath("injury_note").asText());
                            data.put("player_id", team.findPath("player_id").asText());
                            data.put("player_notes", team.findPath("has_player_notes").asInt());
                            data.put("player_is_editable", team.findPath("is_editable").asInt());

                            Player p = new Player(data);

                            players.add(p);
                        }
                    }
                    allTeamData.get(index).put("players", players);
                }
            }


        } catch (Exception e)  {
            Log.e("JSONPARSE", e.toString());
        }
        return allTeamData;
    }
}
