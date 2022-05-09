package io.beekeeper.connector.usersync.sample;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SampleApi {

    @GET("api/1/users")
    Call<ResponseBody> getUsers();

}
