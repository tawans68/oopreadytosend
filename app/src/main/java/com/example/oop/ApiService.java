package com.example.oop;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("posts")  // Endpoint
    Call<List<TextNote>> getTextNote();
}
