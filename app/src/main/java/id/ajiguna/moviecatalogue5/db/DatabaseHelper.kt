package id.ajiguna.moviecatalogue5.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.ajiguna.moviecatalogue5.db.DatabaseContract
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.TABLE_NAME

internal class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbmovieapp"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.MovieColums._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.MovieColums.TITLE} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColums.DESCRIPTION} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColums.POSTER} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColums.BACKDROP} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColums.VOTE} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColums.DATE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}