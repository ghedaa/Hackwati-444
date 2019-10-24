package sa.ksu.swe444.hackwati.ui.profileActivity;

import java.util.ArrayList;

public class CustomPojo {

    private String name;
    private String content;
    private int img;
    private ArrayList<CustomPojo> customPojo =new ArrayList<>();

    public CustomPojo() {

    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    //getting content value
    public String getContent(){return content;}
    //setting content value
    public void setContent(String content){this.content=content;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}