package org.codeisland.sonosservice.network;


import org.codeisland.sonosservice.network.model.Status;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by lukas on 30.03.16.
 */
public interface Api {

    @POST("/call/received")
    public Call<Status> callReceived();

    @POST("/call/ended")
    public Call<Status> callEnded();

}
