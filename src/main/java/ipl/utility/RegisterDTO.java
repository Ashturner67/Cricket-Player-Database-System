package ipl.utility;

import java.io.Serializable;

public class RegisterDTO implements Serializable {

    private String clubName;
    private String password;

    public RegisterDTO(String clubName, String password) {
        this.clubName = clubName;
        this.password = HashUtil.getSHA256Hash(password);
    }

    public String getClubName() {
        return clubName;
    }
    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = HashUtil.getSHA256Hash(password);
    }
}
