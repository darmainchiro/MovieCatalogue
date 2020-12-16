package id.ajiguna.moviecatalogue5.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.ajiguna.moviecatalogue5.R
import id.ajiguna.moviecatalogue5.api.ApiClient
import id.ajiguna.moviecatalogue5.model.TVShow
import id.ajiguna.moviecatalogue5.model.TVShowResponse
import kotlinx.android.synthetic.main.fragment_tvshows.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TVShowFragment : Fragment() {

    private var list = ArrayList<TVShow>()
    companion object {
        private const val STATE_LIST = "state_list"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tvshows, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            getTVShow()
        } else {
            val stateList = savedInstanceState.getParcelableArrayList<TVShow>(STATE_LIST)
            if (stateList != null) {
                list.addAll(stateList)
            }
            rv_tvshow.layoutManager = LinearLayoutManager(activity)
            val listTVAdapter = activity?.let {
                stateList?.let { it1 ->
                    ListTVShowAdapter(
                        it,
                        it1
                    )
                }
            }
            rv_tvshow.adapter = listTVAdapter
            pb_tvshow.visibility = View.GONE
        }
    }

    private fun getTVShow(){
        val apiKey = getString(R.string.api_key)
        val language = getString(R.string.language)
        ApiClient().services.getTV(apiKey,language).enqueue(object :
            Callback<TVShowResponse> {
            override fun onResponse(call: Call<TVShowResponse>, response: Response<TVShowResponse>) {
                //Tulis code jika response sukses
                if(response.code() == 200) {
                    list = response.body()?.tvshow as ArrayList<TVShow>
                    rv_tvshow.layoutManager = LinearLayoutManager(activity)
                    val listTVAdapter = activity?.let { ListTVShowAdapter(
                            it,
                            list
                        )
                    }
                    rv_tvshow.adapter = listTVAdapter
                    pb_tvshow.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<TVShowResponse>, t: Throwable){
            }
        })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_LIST, list)
    }
}