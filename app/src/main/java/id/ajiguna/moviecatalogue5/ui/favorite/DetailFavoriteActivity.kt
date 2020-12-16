package id.ajiguna.moviecatalogue5.ui.favorite

import id.ajiguna.moviecatalogue5.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import id.ajiguna.moviecatalogue5.model.DetailMovie
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.img_photo
import kotlinx.android.synthetic.main.activity_detail.txt_name
import kotlinx.android.synthetic.main.activity_detail.txt_year
import kotlinx.android.synthetic.main.item_content.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import id.ajiguna.moviecatalogue5.api.ApiClient

class DetailFavoriteActivity : AppCompatActivity() {

    companion object {
        private const val STATE_TITLE = "state_title"
        private const val STATE_YEAR = "state_year"
        private const val STATE_OVERVIEW = "state_overview"
        private const val STATE_POSTER = "state_poster"
        private const val url_poster = "https://image.tmdb.org/t/p/w500"
        var id: String= null.toString()
        var text_poster: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        id = intent.getStringExtra("id")

        if (savedInstanceState == null) {
            getDetail()
        } else {
            val title = savedInstanceState.getString(STATE_TITLE) as String
            val year = savedInstanceState.getString(STATE_YEAR) as String
            val overview = savedInstanceState.getString(STATE_OVERVIEW) as String
            val poster = savedInstanceState.getString(STATE_POSTER) as String
            txt_name.text = title
            txt_year.text = year
            text_description.text = overview
            Glide.with(this@DetailFavoriteActivity)
                .load(url_poster+ poster)
                .into(img_photo)
            progressBar.setVisibility(View.GONE);

        }
    }

    private fun getDetail() {
        val api_key = getString(R.string.api_key)
        val language = getString(R.string.language)
        ApiClient().services.getDetailMovie(id,api_key,language).enqueue(object :
            Callback<DetailMovie> {
            override fun onResponse(call: Call<DetailMovie>, response: Response<DetailMovie>) {
                if(response.code() == 200) {
                    txt_name.text = response.body()?.title
                    txt_year.text = response.body()?.releaseDate
                    text_description.text = response.body()?.overview
                    text_poster = response.body()?.posterPath.toString()
                    Glide.with(this@DetailFavoriteActivity)
                        .load(url_poster+ response.body()?.posterPath)
                        .into(img_photo)
                    progressBar.setVisibility(View.GONE);
                }
            }
            override fun onFailure(call: Call<DetailMovie>, t: Throwable){
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_TITLE, txt_name.text.toString())
        outState.putString(STATE_YEAR, txt_year.text.toString())
        outState.putString(STATE_OVERVIEW, text_description.text.toString())
        outState.putString(STATE_POSTER, text_poster)
    }
}
