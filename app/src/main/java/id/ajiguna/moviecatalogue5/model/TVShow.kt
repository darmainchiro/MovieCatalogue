package id.ajiguna.moviecatalogue5.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TVShow(
    @SerializedName("id") var id: Long= 0,
    @SerializedName("original_name") val title: String?= null,
    @SerializedName("overview") val overview: String?= null,
    @SerializedName("poster_path") val posterPath: String?= null,
    @SerializedName("backdrop_path") val backdropPath: String?= null,
    @SerializedName("vote_average") val rating: Float?= null,
    @SerializedName("first_air_date") val releaseDate: String?= null
):Parcelable