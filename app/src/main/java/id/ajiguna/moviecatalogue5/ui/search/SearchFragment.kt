package id.ajiguna.moviecatalogue5.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ajiguna.moviecatalogue5.model.Movie
import id.ajiguna.moviecatalogue5.model.MovieResponse
import id.ajiguna.moviecatalogue5.ui.movies.ListMovieAdapter
import id.ajiguna.moviecatalogue5.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import id.ajiguna.moviecatalogue5.R
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {
    private var list = ArrayList<Movie>()
    private lateinit var listMovieAdapter: ListMovieAdapter
    var query: String? = null
    var rv:RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        rv = root.findViewById<View>(R.id.rv_search) as RecyclerView
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        query = arguments?.getString("query")
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
                    rv?.layoutManager = LinearLayoutManager(activity)
//                    rv?.setLayoutManager(LinearLayoutManager(activity))
                    listMovieAdapter = activity?.let { ListMovieAdapter(it,list) }!!
//                    listMovieAdapter = ListMovieAdapter(activity!!,list)

                    rv?.adapter = listMovieAdapter
                    listMovieAdapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<MovieResponse>, t: Throwable){
                //Tulis code jika response fail
            }
        })
    }
}