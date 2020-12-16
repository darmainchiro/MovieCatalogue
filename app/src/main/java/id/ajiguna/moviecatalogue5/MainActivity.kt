package id.ajiguna.moviecatalogue5

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.ajiguna.moviecatalogue5.ui.movies.MoviesFragment
import id.ajiguna.moviecatalogue5.ui.search.SearchFragment
import id.ajiguna.moviecatalogue5.ui.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import id.ajiguna.moviecatalogue5.ui.search.SearchActivity


class MainActivity : AppCompatActivity() {

    private var searchView: SearchView? = null
    private var navController: NavController? = null
    private var appBarConfiguration: AppBarConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_movies, R.id.navigation_tvshows, R.id.navigation_favorite
            )
        )
        setupActionBarWithNavController(navController!!, appBarConfiguration!!)
        nav_view.setupWithNavController(navController!!)
    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchSetup()

        return super.onCreateOptionsMenu(menu)
    }

    private fun searchSetup() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView?.queryHint = resources.getString(R.string.search)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val i = Intent(this@MainActivity, SearchActivity::class.java)
                i.putExtra("query", query)
                startActivity(i)

                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
//                if (newText.length > 0) {
//                    val transaction = supportFragmentManager.beginTransaction()
//                    val bundle = Bundle()
//                    bundle.putString("query", newText)
//                    val fragment = SearchFragment()
//                    fragment.setArguments(bundle)
//                    transaction.replace(R.id.nav_host_fragment, fragment)
//                    transaction.commit()
//                    nav_view.getMenu().setGroupCheckable(0, false, true)
//                }
//                else {
//                    nav_view.getMenu().setGroupCheckable(0, true, true)
//                    nav_view.getMenu().getItem(0).setChecked(true)
////                    setupActionBarWithNavController(navController!!, appBarConfiguration!!)
////                    nav_view.setupWithNavController(navController!!)
//                    navController?.navigate(R.id.navigation_movies)
//
//                }
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }else if(item.itemId == R.id.action_settings){
            val mIntent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}
