package sa.ksu.swe444.hackwati;


public class Item {
    private String title;
    private String userId;
    private String image;
    private String description;
    private String rate;
    private String sound;
    private String channelName;
    private String channelImage;
    private int soundImage;
    private String storyId;
    boolean isMain ;
    private String status;
    private int color;




    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelImage() {
        return channelImage;
    }

    public void setChannelImage(String channelImage) {
        this.channelImage = channelImage;
    }

    public Item(String title, String image, String description, String rate , String sound, String userId) {
        this.title = title;
        this.image = image;
        this.userId=userId;
        this.description=description;
        this.rate=rate;
        this.sound=sound;
    }

    public Item(String storyId, String title, String image,String userId) {
        this.title = title;
        this.image = image;
        this.userId=userId;
        this.channelName=channelName;
        this.channelImage=channelImage;
        this.storyId=storyId;

    }





    public Item(String storyId, String title, String image,String userId,String sound, boolean x) {
        this.title = title;
        this.image = image;
        this.userId=userId;
        this.channelName=channelName;
        this.channelImage=channelImage;
        this.storyId=storyId;
        this.sound=sound;

    }
    public Item(String storyId, String title, String image,String userId,String sound) {
        this.title = title;
        this.image = image;
        this.userId=userId;
        this.channelName=channelName;
        this.channelImage=channelImage;
        this.storyId=storyId;
        this.sound=sound;

    }

    public Item(boolean x,String storyId, String title, String image,String sound,String userId, String userName,String userImg) {
        this.title = title;
        this.image = image;
        this.userId=userId;
        this.channelName=userName;
        this.channelImage=userImg;
        this.storyId=storyId;
        this.sound=sound;

    }


    public Item ( String title, String image, String userId) {
        this.title = title;
        this.image = image;
        this.userId=userId;


    }

public Item ( String title, String image){
    this.title = title;
    this.image = image;
}

/*    public Item(String title, int image, String channelImage, String views, String channelName, int soundImage) {
        this.title = title;
        this.image = channelName;
        this.channelImage = channelImage;
        this.soundImage = soundImage;
        this.rate = views;
        this.title=channelName;
    }*/

    public Item(String title, int image) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


   public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getStoryId() {
        return storyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}