package id.ajiguna.moviecatalogue5.ui.tvshows

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ajiguna.moviecatalogue5.ui.tvshows.DetailTVActivity
import id.ajiguna.moviecatalogue5.R
import id.ajiguna.moviecatalogue5.model.TVShow
import id.ajiguna.moviecatalogue5.ui.favorite.CustomOnItemClickListener
import kotlinx.android.synthetic.main.item_content.view.*

class ListTVShowAdapter: RecyclerView.Adapter<ListTVShowAdapter.ListViewHolder>{

    var act: Context? = null
    var tvItem: List<TVShow>? = null

    constructor(recyclerViewActivity: Context, tvList: List<TVShow>) {
        act = recyclerViewActivity
        tvItem = tvList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = tvItem?.size!!

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        tvItem?.get(position)?.let { holder.bind(it) }
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvShow: TVShow) {
            with(itemView){
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500"+ tvShow.posterPath)
                    .into(img_photo)
                txt_name.text = tvShow.title
                txt_year.text = tvShow.releaseDate
                txt_description.text = tvShow.overview
                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(
                                    act,
                                    DetailTVActivity::class.java
                                )
                                intent.putExtra(
                                    DetailTVActivity.EXTRA_POSITION,
                                    position
                                )
                                intent.putExtra(
                                    DetailTVActivity.EXTRA_MOVIE,
                                    tvShow
                                )
                                act?.startActivity(
                                    intent
//                                    DetailMovieActivity.REQUEST_UPDATE
                                )
                            }
                        })
                )
            }
        }
    }

}