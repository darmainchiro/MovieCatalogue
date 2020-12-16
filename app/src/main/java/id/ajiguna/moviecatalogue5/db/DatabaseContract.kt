package id.ajiguna.moviecatalogue5.db
import android.net.Uri
import android.provider.BaseColumns
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.TABLE_NAME

internal class DatabaseContract {
    internal class MovieColums : BaseColumns {
        companion object {
            const val TABLE_NAME = "movie"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val POSTER = "poster"
            const val BACKDROP = "backdrop"
            const val VOTE = "vote"
            const val DATE = "date"
        }
    }

    companion object {
        val CONTENT_URI = Uri.Builder().scheme("content")
            .authority("id.ajiguna.moviecatalogue5")
            .appendPath(TABLE_NAME)
            .build()
    }

}