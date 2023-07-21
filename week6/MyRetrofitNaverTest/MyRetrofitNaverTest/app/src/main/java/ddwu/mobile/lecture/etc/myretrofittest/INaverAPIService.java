package ddwu.mobile.lecture.etc.myretrofittest;

import ddwu.mobile.lecture.etc.myretrofittest.model.json.BookRoot;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface INaverAPIService {
    @GET("/v1/search/book.json")
    Call<BookRoot> getBookList (@Header("X-Naver-Client-Id") String clientId,
                                @Header("X-Naver-Client-secret") String clientSecret,
                                @Query("Query") String query);
}
