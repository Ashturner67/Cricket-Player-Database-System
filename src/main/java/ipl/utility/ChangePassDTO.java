package ipl.utility;

import java.io.Serializable;

public class ChangePassDTO implements Serializable {

    private String clubName;
    private String currentPass;
    private String newPass;

    public ChangePassDTO(String clubName, String currentPass, String newPass) {
        this.clubName = clubName;
        this.currentPass = HashUtil.getSHA256Hash(currentPass);
        this.newPass = HashUtil.getSHA256Hash(newPass);
    }

    public String getClubName(){
        return clubName;
    }

    public String getCurrentPass() {
        return currentPass;
    }

    public String getNewPass() {
        return newPass;
    }
}

