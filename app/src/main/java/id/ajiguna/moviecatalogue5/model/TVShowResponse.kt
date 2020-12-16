package id.ajiguna.moviecatalogue5.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TVShowResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val tvshow: List<TVShow>,
    @SerializedName("total_pages") val pages: Int,
    @SerializedName("total_results") val total_result: Int
):Parcelable