package id.ajiguna.moviecatalogue5.ui.movies

import id.ajiguna.moviecatalogue5.R
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.img_photo
import kotlinx.android.synthetic.main.activity_detail.txt_name
import kotlinx.android.synthetic.main.activity_detail.txt_year
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import id.ajiguna.moviecatalogue5.db.DatabaseContract
import android.util.Log
import id.ajiguna.moviecatalogue5.db.FavoriteHelper
import id.ajiguna.moviecatalogue5.model.DetailMovie
import id.ajiguna.moviecatalogue5.model.Movie
import id.ajiguna.moviecatalogue5.api.ApiClient

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var favoriteHelper: FavoriteHelper
    private var movie: Movie? = null
    private var position: Int = 0

    companion object {
        private const val STATE_TITLE = "state_title"
        private const val STATE_YEAR = "state_year"
        private const val STATE_OVERVIEW = "state_overview"
        private const val STATE_POSTER = "state_poster"
        private const val url_poster = "https://image.tmdb.org/t/p/w500"
        var id: String= null.toString()
        var id_movie: String =null.toString()
        var titleku: String = ""
        var poster:String = ""
        var text_poster: String = ""
        var coba: Boolean = false

        const val EXTRA_MOVIE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        movie = intent.getParcelableExtra(EXTRA_MOVIE)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        position = intent.getIntExtra(EXTRA_POSITION, 0)


        if (savedInstanceState == null) {
            if (isRecordExists(movie?.id.toString())) {
                img_fav.setImageResource(R.drawable.ic_favorite_24dp)
                Log.v("MovieDetail", "" + movie?.id)
//                Toast.makeText(this@DetailMovieActivity, "oke "+movie?.id, Toast.LENGTH_SHORT).show()
                getOffline()

            }else {
                getDetail()
            }
        } else {
            val title = savedInstanceState.getString(STATE_TITLE) as String
            val year = savedInstanceState.getString(STATE_YEAR) as String
            val overview = savedInstanceState.getString(STATE_OVERVIEW) as String
            val poster = savedInstanceState.getString(STATE_POSTER) as String
            txt_name.text = title
            txt_year.text = year
            text_description.text = overview
            Glide.with(this@DetailMovieActivity)
                .load(url_poster+ poster)
                .into(img_photo)
            progressBar.setVisibility(View.GONE);
            if (isRecordExists(movie?.id.toString())) {
                img_fav.setImageResource(R.drawable.ic_favorite_24dp)
             }else {
                img_fav.setImageResource(R.drawable.ic_favorite_border_24dp)
            }

        }

        img_fav.setOnClickListener({ favorite() })
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

    private fun getDetail() {
        val apiKey = getString(R.string.api_key)
        val language = getString(R.string.language)
        ApiClient().services.getDetailMovie(movie?.id.toString(),apiKey,language).enqueue(object :
            Callback<DetailMovie> {
            override fun onResponse(call: Call<DetailMovie>, response: Response<DetailMovie>) {
                if(response.code() == 200) {
                    txt_name.text = response.body()?.title
                    txt_year.text = response.body()?.releaseDate
                    text_description.text = response.body()?.overview
                    text_poster = response.body()?.posterPath.toString()
                    id_movie = response.body()?.id.toString()
                    Glide.with(this@DetailMovieActivity)
                        .load(url_poster+ response.body()?.posterPath)
                        .into(img_photo)
                    progressBar.setVisibility(View.GONE);
                }
            }
            override fun onFailure(call: Call<DetailMovie>, t: Throwable){
            }
        })
    }

    private fun getOffline(){
        txt_name.text = movie?.title
        txt_year.text = movie?.releaseDate
        text_description.text = movie?.overview
        text_poster = movie?.posterPath.toString()
        Glide.with(this@DetailMovieActivity)
            .load(url_poster+ movie?.posterPath)
            .into(img_photo)
        progressBar.setVisibility(View.GONE);
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_TITLE, txt_name.text.toString())
        outState.putString(STATE_YEAR, txt_year.text.toString())
        outState.putString(STATE_OVERVIEW, text_description.text.toString())
        outState.putString(STATE_POSTER, text_poster)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)  {
            android.R.id.home -> finish()
//            R.id.action_favorite -> favorite(item)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun favorite() {

        val values = ContentValues()
        values.put(DatabaseContract.MovieColums._ID, id_movie)
        values.put(DatabaseContract.MovieColums.TITLE, txt_name.text.toString())
        values.put(DatabaseContract.MovieColums.DESCRIPTION, text_description.text.toString())
        values.put(DatabaseContract.MovieColums.DATE,txt_year.text.toString())
        values.put(DatabaseContract.MovieColums.POSTER,text_poster)
        values.put(DatabaseContract.MovieColums.BACKDROP,text_poster)
        values.put(DatabaseContract.MovieColums.VOTE,txt_name.text.toString())

        if (!isRecordExists(movie?.id.toString())){
            img_fav.setImageResource(R.drawable.ic_favorite_24dp)
            val result = favoriteHelper.insert(values)
            if (result > 0) {
                movie?.id = result.toLong()
                setResult(RESULT_ADD, intent)
            } else {
                Toast.makeText(this@DetailMovieActivity, R.string.error_add, Toast.LENGTH_SHORT).show()
            }
        }else {
            img_fav.setImageResource(R.drawable.ic_favorite_border_24dp)
            val result = favoriteHelper.deleteById(movie?.id.toString()).toLong()
            if (result > 0) {
                val intent = Intent()
                intent.putExtra(EXTRA_POSITION, position)
                setResult(RESULT_DELETE, intent)
            }else {
                Toast.makeText(this@DetailMovieActivity, R.string.error_clear, Toast.LENGTH_SHORT).show()
            }
        }
//        coba = !coba
    }

    private fun isRecordExists(id: String): Boolean {
        val hasil = favoriteHelper.queryById(id)
        val exists = hasil.count > 0

        Log.v("isi", java.lang.Boolean.toString(exists))
        return exists
    }
}
