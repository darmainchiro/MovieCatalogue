package id.ajiguna.moviecatalogue5.ui.favorite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ajiguna.moviecatalogue5.ui.movies.DetailMovieActivity
import id.ajiguna.moviecatalogue5.ui.favorite.DetailFavoriteActivity
import id.ajiguna.moviecatalogue5.R
import id.ajiguna.moviecatalogue5.model.Movie
import kotlinx.android.synthetic.main.item_content.view.*
import java.util.ArrayList

class ListFavoriteAdapter(private val activity: FragmentActivity) : RecyclerView.Adapter<ListFavoriteAdapter.ListViewHolder>(){
//    var movieList : List<Movie> = listOf()

    var act: Context? = null
    var movieItem: List<Movie>? = null
    var items = ArrayList<Movie>()

    set(items) {
        if (items.size > 0) {
            this.items.clear()
        }
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(movie: Movie) {
        this.items.add(movie)
        notifyItemInserted(this.items.size - 1)
    }
    fun updateItem(position: Int, movie: Movie) {
        this.items[position] = movie
        notifyItemChanged(position, movie)
    }
    fun removeItem(position: Int) {
        this.items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = this.items.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView){
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500"+ movie.posterPath)
                    .into(img_photo)
                txt_name.text = movie.title
                txt_year.text = movie.releaseDate
                txt_description.text = movie.overview
                itemView.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, DetailMovieActivity::class.java)
                        intent.putExtra(DetailMovieActivity.EXTRA_POSITION, position)
                        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movie)
                        activity.startActivityForResult(intent, DetailMovieActivity.REQUEST_UPDATE)
                    }
                }))
            }
        }
    }

}