package sa.ksu.swe444.hackwati.uploaded_stories;

public class story {

    private String name;
    private String img;
    private String id;
    private String isSubscribed;

    public String isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(String subscribed) {
        isSubscribed = subscribed;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public story(String id, String name, String img,String isSubscribed) {
        this.id =id;
        this.name = name;
        this.img = img;
        this.isSubscribed = isSubscribed;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}