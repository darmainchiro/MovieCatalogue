package id.ajiguna.moviecatalogue5.api

import id.ajiguna.moviecatalogue5.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("discover/movie")
    fun getMovie(@Query("api_key") api_key: String?,
                 @Query("language") language: String?): Call<MovieResponse>

    @GET("discover/tv")
    fun getTV(@Query("api_key") api_key: String?,
                 @Query("language") language: String?): Call<TVShowResponse>

    @GET("discover/movie")
    fun getReleasedMovies(@Query("api_key") api_key: String?,
                          @Query("primary_release_date.gte") date: String,
                          @Query("primary_release_date.lte") today: String): Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getDetailMovie(@Path("movie_id") movie_id: String?,
                    @Query("api_key") api_key: String?,
                    @Query("language") language: String?): Call<DetailMovie>

    @GET("tv/{tv_id}")
    fun getDetailTV(@Path("tv_id") tv_id: String?,
                    @Query("api_key") api_key: String?,
                    @Query("language") language: String?): Call<DetailTV>

    @GET("search/multi")
    fun searchMovies(@Query("api_key") api_key: String?,
                     @Query("query") query: String?): Call<MovieResponse>

    @GET("search/movie")
    fun getSearchMovie(@Query("api_key") api_key: String?,
                    @Query("language") language: String?,
                    @Query("query") search: String?): Call<MovieResponse>

    @GET("search/tv")
    fun getSearchTV(@Query("api_key") api_key: String?,
                    @Query("language") language: String?,
                    @Query("query") search: String?): Call<TVShowResponse>
}