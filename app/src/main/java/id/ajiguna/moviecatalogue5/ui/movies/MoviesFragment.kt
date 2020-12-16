package id.ajiguna.moviecatalogue5.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.ajiguna.moviecatalogue5.R
import id.ajiguna.moviecatalogue5.api.ApiClient
import id.ajiguna.moviecatalogue5.model.Movie
import id.ajiguna.moviecatalogue5.model.MovieResponse
import kotlinx.android.synthetic.main.fragment_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesFragment : Fragment() {

    private var list = ArrayList<Movie>()
    private lateinit var listMovieAdapter: ListMovieAdapter
    companion object {
        private const val STATE_LIST = "state_list"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movies, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            getMovie()
        } else {
            val stateList = savedInstanceState.getParcelableArrayList<Movie>(STATE_LIST)
            if (stateList != null) {
                list.addAll(stateList)
            }
            rv_movie.layoutManager = LinearLayoutManager(activity)
            listMovieAdapter = activity?.let { ListMovieAdapter(it, stateList!!) }!!
            rv_movie.adapter = listMovieAdapter
            listMovieAdapter.notifyDataSetChanged()
            pb_movie.setVisibility(View.GONE);
        }
    }

    private fun getMovie(){
        val apiKey = getString(R.string.api_key)
        val language = getString(R.string.language)
        ApiClient().services.getMovie(apiKey,language).enqueue(object :
            Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                //Tulis code jika response sukses
                if(response.code() == 200) {
                    list = response.body()?.movies as ArrayList<Movie>
                    rv_movie.layoutManager = LinearLayoutManager(activity)
                    listMovieAdapter = activity?.let { ListMovieAdapter(it,list) }!!
                    rv_movie.adapter = listMovieAdapter
                    listMovieAdapter.notifyDataSetChanged()
                    pb_movie.setVisibility(View.GONE);
                }
            }
            override fun onFailure(call: Call<MovieResponse>, t: Throwable){
                //Tulis code jika response fail
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_LIST, list)
    }

}