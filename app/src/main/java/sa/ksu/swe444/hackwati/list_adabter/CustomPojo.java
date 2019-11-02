package sa.ksu.swe444.hackwati.list_adabter;

import java.util.ArrayList;

public class CustomPojo {

    private String name;
    private String content;
    private String img;
    private ArrayList<CustomPojo> customPojo =new ArrayList<>();

    public CustomPojo(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
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