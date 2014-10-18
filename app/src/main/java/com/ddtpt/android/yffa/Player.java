package com.ddtpt.android.yffa;

import java.util.HashMap;

/**
 * Created by e228596 on 10/12/2014.
 */
public class Player {
    private String mFullName,mPlayerKey, mEditorialPlayerKey, mEditorialTeamKey, mEditorialTeamFullName,
    mEditorialTeamAbbr, mByeWeek, mUniformNumber, mImageUrl, mDisplayPosition, mSelectedPosition,
    mInjuryStatus, mInjuryNote;

    Double mPlayerTotal;

    private int mHasPlayerNotes;


    public Double getPlayerTotal() {
        return mPlayerTotal;
    }

    public void setPlayerTotal(Double playerTotal) {
        mPlayerTotal = playerTotal;
    }

    private HashMap<String, Double> mPlayerScores;

    public HashMap<String, Double> getPlayerScores() {
        return mPlayerScores;
    }

    public void setPlayerScores(HashMap<String, Double> playerScores) {
        mPlayerScores = playerScores;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null) {
            fullName = "";
        }
        mFullName = fullName;
    }

    public String getPlayerKey() {
        return mPlayerKey;
    }

    public void setPlayerKey(String playerKey) {
        if (playerKey == null) {
            playerKey = "";
        }
        mPlayerKey = playerKey;
    }

    public String getEditorialPlayerKey() {
        return mEditorialPlayerKey;
    }

    public void setEditorialPlayerKey(String editorialPlayerKey) {
        if (editorialPlayerKey == null) {
            editorialPlayerKey = "";
        }
        mEditorialPlayerKey = editorialPlayerKey;
    }

    public String getEditorialTeamKey() {
        return mEditorialTeamKey;
    }

    public void setEditorialTeamKey(String editorialTeamKey) {
        if (editorialTeamKey == null) {
            editorialTeamKey = "";
        }
        mEditorialTeamKey = editorialTeamKey;
    }

    public String getEditorialTeamFullName() {
        return mEditorialTeamFullName;
    }

    public void setEditorialTeamFullName(String editorialTeamFullName) {
        if (editorialTeamFullName == null) {
            editorialTeamFullName = "";
        }
        mEditorialTeamFullName = editorialTeamFullName;
    }

    public String getEditorialTeamAbbr() {
        return mEditorialTeamAbbr;
    }

    public void setEditorialTeamAbbr(String editorialTeamAbbr) {
        if (editorialTeamAbbr == null) {
            editorialTeamAbbr = "";
        }
        mEditorialTeamAbbr = editorialTeamAbbr;
    }

    public String getByeWeek() {
        return mByeWeek;
    }

    public void setByeWeek(String byeWeek) {
        if (byeWeek == null) {
            byeWeek = "";
        }
        mByeWeek = byeWeek;
    }

    public String getUniformNumber() {
        return mUniformNumber;
    }

    public void setUniformNumber(String uniformNumber) {
        if (uniformNumber == null) {
            uniformNumber = "";
        }
        mUniformNumber = uniformNumber;
    }

    public void setUniformNumber(boolean uniformNumber) {
        mUniformNumber = "";
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        if (imageUrl == null) {
            imageUrl = "";
        }
        mImageUrl = imageUrl;
    }

    public String getDisplayPosition() {
        return mDisplayPosition;
    }

    public void setDisplayPosition(String displayPosition) {
        if (displayPosition == null) {
            displayPosition = "";
        }
        mDisplayPosition = displayPosition;
    }

    public String getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(String selectedPosition) {
        if (selectedPosition == null) {
            selectedPosition = "";
        }
        mSelectedPosition = selectedPosition;
    }

    public String getInjuryStatus() {
        return mInjuryStatus;
    }

    public void setInjuryStatus(String injuryStatus) {
        if (injuryStatus == null) {
            injuryStatus = "";
        }
        mInjuryStatus = injuryStatus;
    }

    public String getInjuryNote() {
        return mInjuryNote;
    }

    public void setInjuryNote(String injuryNote) {
        if (injuryNote == null) {
            injuryNote = "";
        }
        mInjuryNote = injuryNote;
    }

    public int getHasPlayerNotes() {
        return mHasPlayerNotes;
    }

    public void setHasPlayerNotes(int hasPlayerNotes) {
        mHasPlayerNotes = hasPlayerNotes;
    }
}