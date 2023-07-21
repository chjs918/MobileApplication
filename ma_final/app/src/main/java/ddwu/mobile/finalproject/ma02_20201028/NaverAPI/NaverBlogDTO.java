package ddwu.mobile.finalproject.ma02_20201028.NaverAPI;

import android.text.Html;
import android.text.Spanned;

import java.io.Serializable;

public class NaverBlogDTO implements Serializable {
    private  int _id;
    private String title;
    private String description;
    private String link;

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

    public String getDescription() {
        Spanned spanned = Html.fromHtml(description);
        return spanned.toString();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return  getTitle() + " (" + description +")";
    }
}