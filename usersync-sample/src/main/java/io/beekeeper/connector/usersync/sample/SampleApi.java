package io.beekeeper.connector.usersync.sample;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit HTTP interface for Sample API endpoints.
 */
public interface SampleApi {

    @GET("api/1/users")
    Call<ResponseBody> getUsers(
            @Query("limit") Integer limit,
            @Query("offset") Integer offset
    );

}
