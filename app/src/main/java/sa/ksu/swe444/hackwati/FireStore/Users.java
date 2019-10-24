package sa.ksu.swe444.hackwati.FireStore;

import androidx.annotation.Nullable;

public class Users {
    private String uid;
    private String username;
    @Nullable
    private String urlPicture;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
