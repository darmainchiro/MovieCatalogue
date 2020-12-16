package id.ajiguna.moviecatalogue5.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class DetailTV(
    @SerializedName("id") val id: Long,
    @SerializedName("original_name") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("first_air_date") val releaseDate: String
)