package id.ajiguna.moviecatalogue5.ui.tvshows

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import id.ajiguna.moviecatalogue5.R
import id.ajiguna.moviecatalogue5.api.ApiClient
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.BACKDROP
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.DATE
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.DESCRIPTION
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.POSTER
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.TITLE
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion.VOTE
import id.ajiguna.moviecatalogue5.db.DatabaseContract.MovieColums.Companion._ID
import id.ajiguna.moviecatalogue5.db.FavoriteHelper
import id.ajiguna.moviecatalogue5.model.DetailTV
import id.ajiguna.moviecatalogue5.model.TVShow
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.img_photo
import kotlinx.android.synthetic.main.activity_detail.txt_name
import kotlinx.android.synthetic.main.activity_detail.txt_year
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTVActivity : AppCompatActivity() {

    private lateinit var favoriteHelper: FavoriteHelper
    var tvShow: TVShow? = null
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

        tvShow = intent.getParcelableExtra(EXTRA_MOVIE)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            if (isRecordExists(tvShow?.id.toString())) {
                img_fav.setImageResource(R.drawable.ic_favorite_24dp);
                Log.v("MovieDetail", "" + tvShow?.id)
//                Toast.makeText(this@DetailTVActivity, "oke "+tvShow?.id, Toast.LENGTH_SHORT).show()
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
            Glide.with(this@DetailTVActivity)
                .load(url_poster + poster)
                .into(img_photo)
            progressBar.setVisibility(View.GONE);
            if (isRecordExists(tvShow?.id.toString())) {
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
        ApiClient().services.getDetailTV(tvShow?.id.toString(),apiKey,language).enqueue(object :
            Callback<DetailTV> {
            override fun onResponse(call: Call<DetailTV>, response: Response<DetailTV>) {
                if(response.code() == 200) {
                    txt_name.text = response.body()?.title
                    txt_year.text = response.body()?.releaseDate
                    text_description.text = response.body()?.overview
                    text_poster = response.body()?.posterPath.toString()
                    id_movie = response.body()?.id.toString()
                    Glide.with(this@DetailTVActivity)
                        .load("https://image.tmdb.org/t/p/w500"+ response.body()?.posterPath)
                        .into(img_photo)
                    progressBar.setVisibility(View.GONE);
                }
            }
            override fun onFailure(call: Call<DetailTV>, t: Throwable){
            }
        })
    }

    private fun getOffline(){
        txt_name.text = tvShow?.title
        txt_year.text = tvShow?.releaseDate
        text_description.text = tvShow?.overview
        text_poster = tvShow?.posterPath.toString()
        Glide.with(this@DetailTVActivity)
            .load(url_poster + tvShow?.posterPath)
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
        values.put(_ID,id_movie)
        values.put(TITLE, txt_name.text.toString())
        values.put(DESCRIPTION, text_description.text.toString())
        values.put(DATE,txt_year.text.toString())
        values.put(POSTER,text_poster)
        values.put(BACKDROP,text_poster)
        values.put(VOTE,txt_name.text.toString())

        if (!isRecordExists(tvShow?.id.toString())){
            img_fav.setImageResource(R.drawable.ic_favorite_24dp)
            val result = favoriteHelper.insert(values)
            if (result > 0) {
                tvShow?.id = result.toLong()
                setResult(RESULT_ADD, intent)
            } else {
                Toast.makeText(this@DetailTVActivity, "Gagal menambah data", Toast.LENGTH_SHORT).show()
            }
        }else {
            img_fav.setImageResource(R.drawable.ic_favorite_border_24dp)
            val result = favoriteHelper.deleteById(tvShow?.id.toString()).toLong()
            if (result > 0) {
                val intent = Intent()
                intent.putExtra(EXTRA_POSITION, position)
                setResult(RESULT_DELETE, intent)
            }else {
                Toast.makeText(this@DetailTVActivity, "Gagal menghapus", Toast.LENGTH_SHORT).show()
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
