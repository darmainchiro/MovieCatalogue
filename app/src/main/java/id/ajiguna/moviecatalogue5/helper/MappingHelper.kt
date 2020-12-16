package id.ajiguna.moviecatalogue5.helper

import android.database.Cursor
import id.ajiguna.moviecatalogue5.db.DatabaseContract
import id.ajiguna.moviecatalogue5.model.Movie

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor): ArrayList<Movie>{
        val noteList = ArrayList<Movie>()
        notesCursor.moveToFirst()
        while (!notesCursor.isAfterLast()){
            val id = notesCursor.getLong(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColums._ID))
            val title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColums.TITLE))
            val description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColums.DESCRIPTION))
            val poster = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColums.POSTER))
            val backdrop = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColums.BACKDROP))
            val vote = notesCursor.getFloat(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColums.VOTE))
            val date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColums.DATE))
            noteList.add(Movie(id,title,description,poster,backdrop,vote,date))
            notesCursor.moveToNext()

        }

        return noteList
    }
}