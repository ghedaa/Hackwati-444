package sa.ksu.swe444.hackwati.explor;

public class Classifications {
    private String classificationTitle;
    private int classificationPic;

    public Classifications(String classificationTitle, int classificationPic) {
        this.classificationTitle = classificationTitle;
        this.classificationPic = classificationPic;
    }

    public String getClassificationTitle() {
        return classificationTitle;
    }

    public void setClassificationTitle(String classificationTitle) {
        this.classificationTitle = classificationTitle;
    }

    public int getClassificationPic() {
        return classificationPic;
    }

    public void setClassificationPic(int classificationPic) {
        this.classificationPic = classificationPic;
    }
}
