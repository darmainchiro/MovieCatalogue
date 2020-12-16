package id.ajiguna.moviecatalogue5.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.Toast
import androidx.core.os.bundleOf
import id.ajiguna.moviecatalogue5.R
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import id.ajiguna.moviecatalogue5.db.FavoriteHelper
import id.ajiguna.moviecatalogue5.helper.MappingHelper
import id.ajiguna.moviecatalogue5.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


internal class StackRemoteViewsFactorys(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory{

    private lateinit var favoriteHelper: FavoriteHelper
    private var list = ArrayList<Movie>()
    private lateinit var bitmap: Bitmap

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        favoriteHelper = mContext?.applicationContext?.let { FavoriteHelper.getInstance(it) }!!
        favoriteHelper.open()

        list.clear()
        list = favoriteHelper.getListFavoriteMovie()

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {
        favoriteHelper.close()
    }

    override fun getCount(): Int = list.size

    override fun getViewAt(position: Int): RemoteViews {

        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        bitmap = Glide.with(mContext)
            .asBitmap()
            .load("https://image.tmdb.org/t/p/w500" + list[position].posterPath)
            .apply(RequestOptions().fitCenter())
            .submit(800, 550)
            .get()
        rv.setImageViewBitmap(R.id.imageView, bitmap)

        val extras = bundleOf(
            StackWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}