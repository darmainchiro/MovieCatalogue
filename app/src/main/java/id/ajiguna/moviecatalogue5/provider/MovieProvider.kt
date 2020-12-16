package id.ajiguna.moviecatalogue5.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.media.tv.TvContract.AUTHORITY
import android.net.Uri
import id.ajiguna.moviecatalogue5.db.DatabaseContract.Companion.CONTENT_URI
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.TABLE_NAME
import id.ajiguna.moviecatalogue5.db.FavoriteHelper

class MovieProvider : ContentProvider() {

    companion object {
        private const val MOVIE = 1
        private const val MOVIE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper
        init {
            // content://com.dicoding.picodiploma.mynotesapp/note
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE)
            // content://com.dicoding.picodiploma.mynotesapp/note/id
            sUriMatcher.addURI(AUTHORITY,
                "$TABLE_NAME/#",
                MOVIE_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            MOVIE -> cursor = favoriteHelper.queryAll()
            MOVIE_ID -> cursor = favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        return cursor    }


    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (MOVIE) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (MOVIE_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (MOVIE_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

}
