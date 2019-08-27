package BotApplicationCore.BotApplicationFiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class BotApplicationUser implements Serializable {

    public static final long serialVersionUID = 42L;

    private int xp;
    private int level;
    private String userName;
    private String preferedNickName;
    private String ytPlaylist;
    private String userId;
    private boolean admin = false;

    private int emoteLevel = 0;
    private int emoteLove = 0;

    public BotApplicationUser(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public void setEmoteLevel(int emoteLevel) {
        this.emoteLevel = emoteLevel;
    }

    public void setEmoteLove(int emoteLove) {
        this.emoteLove = emoteLove;
    }

    public int getEmoteLove() {
        return emoteLove;
    }

    public int getXp() {
        return xp;
    }

    public int getEmoteLevel() {
        return this.emoteLevel;
    }

    public void riseEmoteLevel(int riser) {
        boolean rise = false;
        if (getEmoteLevel() + riser >= 10) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 10);
            int randomNum2 = ThreadLocalRandom.current().nextInt(0, 10);
            if (randomNum > 6) {
                if (emoteLove > 5) {
                    if (randomNum2 > 6) {
                        rise = true;
                    }
                } else {
                    rise = true;
                }
            }
            if (!rise) {
                emoteLevel = 4;
            } else {
                riseLove(1);
            }
        } else {
            emoteLevel = emoteLevel + riser;
        }
    }

    public void riseLove(int riser) {
        if (getEmoteLove() + riser >= 10) {
            emoteLove = 10;
        } else {
            emoteLove = emoteLove + riser;
        }
    }

    public void increaseLove(int increaser) {
        if (getEmoteLove() - increaser <= -10) {
            emoteLove = -10;
        } else {
            emoteLove = emoteLove - increaser;
        }
    }

    public void inceaseEmoteLevel(int increaser) {
        if (getEmoteLevel() + increaser <= -10) {
            emoteLevel = 0;
            increaseLove(1);
        } else {
            emoteLevel = emoteLevel - increaser;
        }
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPreferedNickName() {
        return preferedNickName;
    }

    public void setPreferedNickName(String preferedNickName) {
        this.preferedNickName = preferedNickName;
    }

    public String getYtPlaylist() {
        return ytPlaylist;
    }

    public void setYtPlaylist(String ytPlaylist) {
        this.ytPlaylist = ytPlaylist;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}