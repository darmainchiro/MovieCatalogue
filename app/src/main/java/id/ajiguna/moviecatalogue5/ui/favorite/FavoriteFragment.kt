package id.ajiguna.moviecatalogue5.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import id.ajiguna.moviecatalogue5.db.FavoriteHelper
import id.ajiguna.moviecatalogue5.model.Movie
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import id.ajiguna.moviecatalogue5.R
import id.ajiguna.moviecatalogue5.helper.MappingHelper

class FavoriteFragment : Fragment() {

    private lateinit var favoriteHelper: FavoriteHelper
    private var list = ArrayList<Movie>()
    private lateinit var listFavoriteAdapter: ListFavoriteAdapter
    companion object {
        private const val STATE_LIST = "state_list"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteHelper = activity?.applicationContext?.let { FavoriteHelper.getInstance(it) }!!
        favoriteHelper.open()

        rv_favorite.layoutManager = LinearLayoutManager(activity)
        listFavoriteAdapter = activity?.let { ListFavoriteAdapter(it) }!!
        rv_favorite.adapter = listFavoriteAdapter


        if (savedInstanceState == null) {
            getMovie()
        } else {
            val stateList = savedInstanceState.getParcelableArrayList<Movie>(STATE_LIST)
            if (stateList != null) {
                listFavoriteAdapter.items = stateList
            }
            pb_favorite.visibility = View.INVISIBLE
        }
    }

    private fun getMovie(){
        GlobalScope.launch(Dispatchers.Main) {
            pb_favorite.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            pb_favorite.visibility = View.INVISIBLE
            val item = deferredNotes.await()
//            Toast.makeText(activity, "total "+item.size, Toast.LENGTH_SHORT).show()
            if (item.size > 0) {
                listFavoriteAdapter.items = item
            } else {
                listFavoriteAdapter.items = ArrayList()
                showSnackbarMessage(getString(R.string.no_data))
            }
        }
        listFavoriteAdapter.notifyDataSetChanged()
        pb_favorite.setVisibility(View.GONE);

    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_favorite, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_LIST, listFavoriteAdapter.items)
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

}