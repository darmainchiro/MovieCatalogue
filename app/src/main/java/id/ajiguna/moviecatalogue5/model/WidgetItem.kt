package id.ajiguna.moviecatalogue5.model

import android.graphics.Bitmap
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WidgetItem(
    @SerializedName("poster_path") val posterPath: Bitmap?= null
    ): Parcelable