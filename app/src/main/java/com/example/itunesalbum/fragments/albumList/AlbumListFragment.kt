package com.example.itunesalbum.fragments.albumList

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.itunesalbum.CustomApplication
import com.example.itunesalbum.MainActivity
import com.example.itunesalbum.R
import com.example.itunesalbum.databinding.AlbumListFragmentBinding
import com.example.itunesalbum.fragments.Screens
import com.example.itunesalbum.model.album.AlbumResult
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class AlbumListFragment : Fragment(), CoroutineScope {

    companion object {
        fun newInstance() =
            AlbumListFragment()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO
    private val itemAdapter = GenericItemAdapter()
    private val fastAdapter: GenericFastAdapter = FastAdapter.with(itemAdapter)
    private val autocompleteDebounce = 400L
    private val autocompleteThreshold = 2
    private lateinit var viewModel: AlbumListViewModel
    private lateinit var binding: AlbumListFragmentBinding
    private lateinit var suggestionAdapter: ArrayAdapter<AlbumResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = SearchView(
            (context as MainActivity)
                .supportActionBar?.themedContext ?: context
        )
        val searchAutoComplete =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as SearchAutoComplete
        searchAutoComplete.apply {
            setDropDownBackgroundResource(R.color.md_white_1000)
            threshold = autocompleteThreshold
        }
        searchAutoComplete.setAdapter(suggestionAdapter)
        searchAutoComplete.onItemClickListener =
            OnItemClickListener { adapterView, _, itemIndex, _ ->
                val query =
                    adapterView.getItemAtPosition(itemIndex).toString()
                searchView.setQuery(query, true)
            }

        menu.findItem(R.id.searchView).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        val searchMenuItem = menu.findItem(R.id.searchView)
        if (viewModel.searchQuery.isNotEmpty()) {
            searchMenuItem.expandActionView()
            searchView.setQuery(viewModel.searchQuery, false)
            searchView.clearFocus()
        }


        val closeButton: ImageView = searchView.findViewById(R.id.search_close_btn) as ImageView
        closeButton.setOnClickListener {
            viewModel.searchQuery = ""
            searchView.onActionViewCollapsed()
            searchMenuItem.collapseActionView()

        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchByQuery(query)
                return false
            }

            private var searchFor = ""
            override fun onQueryTextChange(newText: String): Boolean {
                val searchText = newText.trim()
                if (searchText == searchFor || searchText == "" ||
                    searchText.toCharArray().size < autocompleteThreshold
                ) return false

                searchFor = searchText

                launch {
                    delay(autocompleteDebounce)
                    if (searchText != searchFor)
                        return@launch
                    searchByAutocomplete(newText)
                }
                return false
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AlbumListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewModel()
        setBinding()
        setUpRecyclerViewAdapter()
        setArrayAdapter()
        subscribeOnAlbumsList()
        subscribeOnAutocompleteAlbumsList()

    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this).get(AlbumListViewModelImpl::class.java)
    }

    private fun setBinding() {
        binding.viewModel = viewModel as AlbumListViewModelImpl
    }

    private fun setArrayAdapter() {
        suggestionAdapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_dropdown_item_1line
        )
    }

    private fun subscribeOnAlbumsList() {
        viewModel.subscribeOnAlbumsList().observe(this, Observer {
            addDataToAdapter(it)
        })
    }

    private fun subscribeOnAutocompleteAlbumsList() {
        viewModel.subscribeOnAutocompleteAlbumsList().observe(this, Observer {
            setAutocompleteData(it)
        })
    }


    private fun setAutocompleteData(albumList: List<AlbumResult>) {
        suggestionAdapter.clear()
        suggestionAdapter.addAll(albumList)
        suggestionAdapter.notifyDataSetChanged()
        Log.e("autoTag", "SETDATA: ${albumList.size}")
    }

    private fun setUpRecyclerViewAdapter() {
        binding.recyclerView.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, item, _ ->
            openInformationFragment(item as AlbumResult)
            false
        }
    }

    private fun openInformationFragment(item: AlbumResult) {
        val router = CustomApplication.instance.getRouter()
        router.navigateTo(
            Screens.Companion.InformationFragment(
                getString(R.string.bundle_result_key),
                item
            )
        )
    }

    private fun addDataToAdapter(albumList: List<AlbumResult>) {
        itemAdapter.set(albumList)
    }

    private fun searchByQuery(query: String) {
        viewModel.loadAlbumsByName(query)
    }

    private fun searchByAutocomplete(query: String) {
        viewModel.searchQuery = query
        Log.e("autoTag", "AUTOCOMPLETE: $query")
        viewModel.loadAlbumsForAutocompleteByName(query)
    }


}