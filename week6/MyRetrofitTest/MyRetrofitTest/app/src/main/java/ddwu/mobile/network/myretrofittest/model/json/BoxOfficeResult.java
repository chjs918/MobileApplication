package ddwu.mobile.network.myretrofittest.model.json;

import java.util.List;

public class BoxOfficeResult {
    private List<DailyBoxOffice> dailyBoxOfiiceList;

    public List<DailyBoxOffice> getDailyBoxOfiiceList() {
        return dailyBoxOfiiceList;
    }

    public void setDailyBoxOfiiceList(List<DailyBoxOffice> dailyBoxOfiiceList) {
        this.dailyBoxOfiiceList = dailyBoxOfiiceList;
    }
}
