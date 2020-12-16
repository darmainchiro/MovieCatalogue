package id.ajiguna.moviecatalogue5.ui.movies

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ajiguna.moviecatalogue5.R
import id.ajiguna.moviecatalogue5.model.Movie
import kotlinx.android.synthetic.main.item_content.view.*
import java.util.ArrayList
import id.ajiguna.moviecatalogue5.ui.favorite.CustomOnItemClickListener

class ListMovieAdapter : RecyclerView.Adapter<ListMovieAdapter.ListViewHolder>{
//    var movieList : List<Movie> = listOf()

    var act: Context? = null
    var movieItem: List<Movie>? = null
    private var items: ArrayList<Movie>? = null

    constructor(recyclerViewActivity: Context, movieList: List<Movie>) {
        act = recyclerViewActivity
        movieItem = movieList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = movieItem?.size!!

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        movieItem?.get(position)?.let { holder.bind(it) }
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView){
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500"+ movie.posterPath)
//                    .apply(RequestOptions().override(55, 55))
                    .into(img_photo)
                txt_name.text = movie.title
                txt_year.text = movie.releaseDate
                txt_description.text = movie.overview
                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(act, DetailMovieActivity::class.java)
                                intent.putExtra(DetailMovieActivity.EXTRA_POSITION, position)
                                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movie)
                                act?.startActivity(
                                    intent
                                )
                            }
                        })
                )
            }
        }
    }

}