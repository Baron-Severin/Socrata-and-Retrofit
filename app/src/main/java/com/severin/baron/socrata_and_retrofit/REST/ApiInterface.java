package com.severin.baron.socrata_and_retrofit.REST;

import com.severin.baron.socrata_and_retrofit.Model.BuildingPermit;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by erikrudie on 8/9/16.
 */
public interface ApiInterface {

    String CATEGORY_COMMERCIAL = "COMMERCIAL";
    String WHERE_VALUE_IS_UNDER_X = "value<";

    // Example query: https://data.seattle.gov/resource/i5jq-ms7b.json?$$app_token=GAuG06jfO7zdOLS1s0OktESQU&$where=value<500&$limit=10
    @GET("resource/i5jq-ms7b.json")
    Call<List<BuildingPermit>> getPermitsByValue(@Query("$$app_token") String apiToken,
                                                 @Query("$where") String value, @Query("$limit") int limit);

    // Example query: https://data.seattle.gov/resource/i5jq-ms7b.json?$$app_token=GAuG06jfO7zdOLS1s0OktESQU&category=COMMERCIAL&$limit=10
    @GET("resource/i5jq-ms7b.json")
    Call<List<BuildingPermit>> getPermitsByCategory(@Query("$$app_token") String apiToken,
                                                 @Query("category") String value, @Query("$limit") int limit);
}
