package app.ij.mlwithtensorflowlite.model;

public class ParseItemModel {

    private String imgUrl;
    private String title;
    private String detailUrl;


    public ParseItemModel() {
    }

    public ParseItemModel(String imgUrl, String title, String detailUrl) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.detailUrl = detailUrl;

    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }



}

