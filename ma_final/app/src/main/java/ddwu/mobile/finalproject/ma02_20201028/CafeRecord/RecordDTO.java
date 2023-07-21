package ddwu.mobile.finalproject.ma02_20201028.CafeRecord;

import java.io.Serializable;

public class RecordDTO implements Serializable {

    private long _id;
    private String cafeName;
    private String location;
    private String content;
    private String imgPath;
    private String desk;
    private int power; //자리마다 콘센트 있는지 있음:1, 없음:0
    private String star;


    public RecordDTO() {
    }

    public RecordDTO(long _id, String content, String cafeName, String location, String img, String desk, int power, String star) {
        this._id = _id;
        this.content = content;
        this.cafeName = cafeName;
        this.location = location;
        this.imgPath = img;
        this.desk = desk;
        this.power = power;
        this.star = star;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCafeName() {
        return cafeName;
    }

    public void setCafeName(String cafeName) {
        this.cafeName = cafeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }
}
