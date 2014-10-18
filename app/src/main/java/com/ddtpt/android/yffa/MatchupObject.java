package com.ddtpt.android.yffa;

/**
 * Created by e228596 on 10/17/2014.
 */
public class MatchupObject {
    private String mHomeTeam, mHomeOwner, mHomeIcon, mAwayTeam, mAwayOwner, mAwayIcon;
    private Double mHomeScore, mAwayScore;

    public MatchupObject(String hTeam, String hOwner, String hIcon, String aTeam, String aOwner, String aIcon, Double hScore, Double aScore) {
        mHomeTeam = hTeam;
        mAwayTeam = aTeam;
        mHomeOwner = hOwner;
        mAwayOwner = aOwner;
        mHomeIcon = hIcon;
        mAwayIcon = aIcon;
        mHomeScore = hScore;
        mAwayScore = aScore;
    }

    public String getHomeTeam() {
        return mHomeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        mHomeTeam = homeTeam;
    }

    public String getHomeOwner() {
        return mHomeOwner;
    }

    public void setHomeOwner(String homeOwner) {
        mHomeOwner = homeOwner;
    }

    public String getHomeIcon() {
        return mHomeIcon;
    }

    public void setHomeIcon(String homeIcon) {
        mHomeIcon = homeIcon;
    }

    public String getAwayTeam() {
        return mAwayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        mAwayTeam = awayTeam;
    }

    public String getAwayOwner() {
        return mAwayOwner;
    }

    public void setAwayOwner(String awayOwner) {
        mAwayOwner = awayOwner;
    }

    public String getAwayIcon() {
        return mAwayIcon;
    }

    public void setAwayIcon(String awayIcon) {
        mAwayIcon = awayIcon;
    }

    public Double getHomeScore() {
        return mHomeScore;
    }

    public void setHomeScore(Double homeScore) {
        mHomeScore = homeScore;
    }

    public Double getAwayScore() {
        return mAwayScore;
    }

    public void setAwayScore(Double awayScore) {
        mAwayScore = awayScore;
    }
}
