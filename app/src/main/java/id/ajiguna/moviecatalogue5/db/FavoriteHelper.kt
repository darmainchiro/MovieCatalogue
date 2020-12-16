package id.ajiguna.moviecatalogue5.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.BACKDROP
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.DATE
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.DESCRIPTION
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.POSTER
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.TABLE_NAME
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.TITLE
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.VOTE
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion._ID
import id.ajiguna.moviecatalogue5.model.Movie
import java.sql.SQLException
import java.util.ArrayList

class FavoriteHelper (context: Context){

    private val dataBaseHelper: DatabaseHelper = DatabaseHelper(context)

    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = FavoriteHelper(context)
                    }
                }
            }
            return INSTANCE as FavoriteHelper
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$_ID ASC",
                null)
    }

    fun getListFavoriteMovie(): ArrayList<Movie> {
        val arrayList = ArrayList<Movie>()
        database = dataBaseHelper.getReadableDatabase()
        val cursor = database.query(
            DATABASE_TABLE,
            arrayOf(_ID, TITLE, DESCRIPTION, POSTER, BACKDROP, VOTE, DATE),
            null,
            null,
            null, null,
            "$_ID ASC", null
        )
        cursor.moveToFirst()
        if (cursor.getCount() > 0) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                val poster = cursor.getString(cursor.getColumnIndexOrThrow(POSTER))
                val backdrop = cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP))
                val vote = cursor.getFloat(cursor.getColumnIndexOrThrow(VOTE))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
                arrayList.add(Movie(id,title,description,poster,backdrop,vote,date))
                cursor.moveToNext()
            } while (!cursor.isAfterLast())
        }
        cursor.close()
        return arrayList
    }

    fun queryById(id: String): Cursor {
        return database.query(DATABASE_TABLE, null, "$_ID = ?", arrayOf(id), null, null, null, null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}