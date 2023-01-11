package com.calicdan.florsgardenapp.Fragments;

import com.calicdan.florsgardenapp.Notifications.MyResponse;
import com.calicdan.florsgardenapp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAsP4q1Zk:APA91bFo1HZSTYg7uKV9wU8rdupfh1bS3z3706EtfVDCyOE_Tqys3I42yyAiV8lN1qyLfgTPQgzY_E_KGEk9QBSvdBS1HmdzuBdhbSgNtkF8TfMJM8JRors93j653sqH1xUXauoeXpVG"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}