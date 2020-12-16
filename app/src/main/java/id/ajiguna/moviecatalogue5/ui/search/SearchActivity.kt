package id.ajiguna.moviecatalogue5.ui.search

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.ajiguna.moviecatalogue5.R
import id.ajiguna.moviecatalogue5.api.ApiClient
import id.ajiguna.moviecatalogue5.model.Movie
import id.ajiguna.moviecatalogue5.model.MovieResponse
import id.ajiguna.moviecatalogue5.ui.movies.ListMovieAdapter
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.pb_movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity(){

    var query: String? = null
    private var list = ArrayList<Movie>()
    private lateinit var listMovieAdapter: ListMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_search)

        supportActionBar?.title = getString(R.string.search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        query = intent?.getStringExtra("query")
        getMovie()
    }

    private fun getMovie() {
        val apiKey = getString(R.string.api_key)
        ApiClient().services.searchMovies(apiKey,query).enqueue(object :
            Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                //Tulis code jika response sukses
                if(response.code() == 200) {
                    list = response.body()?.movies as ArrayList<Movie>
                    if (list != null && list.size > 0) {
                        rv_search.layoutManager = LinearLayoutManager(this@SearchActivity)
                        listMovieAdapter = this@SearchActivity?.let { ListMovieAdapter(it, list) }!!
                        rv_search.adapter = listMovieAdapter
                        listMovieAdapter.notifyDataSetChanged()
                        pb_movie.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(this@SearchActivity, R.string.not_found, Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onFailure(call: Call<MovieResponse>, t: Throwable){
                //Tulis code jika response fail
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)  {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}