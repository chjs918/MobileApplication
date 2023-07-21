package ddwu.mobile.finalproject.ma02_20201028.NaverAPI.LocalSearch;

import android.text.Html;
import android.text.Spanned;

import java.io.Serializable;

public class NaverLocalDTO implements Serializable {
    private  int _id;
    private String title;
    private String roadAddress; // 도로명주소

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        Spanned spanned = Html.fromHtml(title);
        return spanned.toString();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String toString() {return "제목)" + getTitle() +"\n도로명주소)" + getRoadAddress();}
}