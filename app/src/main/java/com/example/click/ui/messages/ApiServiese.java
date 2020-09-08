package com.example.click.ui.messages;

import com.example.click.ui.Notifications.MyResponse;
import com.example.click.ui.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiServiese {
    @Headers({"Content-Type:application/json","Authorization:key=AAAAWHEh2uA:APA91bFhkDH1m9SWqMTNiZg5050mbgGZNhh_owbnkZxWlf9kfFEZuFK5I5DER5nPmjJXfJaokPgJqvkDFHWUGq58jwb1FxWw13WTlOkKE0vcGshiSgsIXHkUXF5BGORQFWx8tqrst0kJ"})
    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body Sender body);
}
