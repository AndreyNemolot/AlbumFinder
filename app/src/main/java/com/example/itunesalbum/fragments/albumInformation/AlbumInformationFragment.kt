package com.example.itunesalbum.fragments.albumInformation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesalbum.R
import com.example.itunesalbum.databinding.AlbumInformationFragmentBinding
import com.example.itunesalbum.model.album.AlbumResult
import com.example.itunesalbum.model.song.SongListHeader
import com.example.itunesalbum.model.song.SongResult
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook


class AlbumInformationFragment : Fragment() {

    companion object {
        fun newInstance(bundleKey: String, result: AlbumResult): AlbumInformationFragment {
            val fragment = AlbumInformationFragment()
            val args = Bundle()
            args.putParcelable(bundleKey, result)
            fragment.arguments = args
            return fragment
        }
    }

    private val headerAdapter = ItemAdapter<SongListHeader>()
    private val itemAdapter = GenericItemAdapter()
    private val fastAdapter: GenericFastAdapter =
        FastAdapter.with(listOf(headerAdapter, itemAdapter))
    private lateinit var viewModel: AlbumInformationViewModel
    private lateinit var binding: AlbumInformationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AlbumInformationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setViewModel()
        setBinding()
        subscribeOnSongList()
        setEventHookForHeaderItem()
        setEventHookForSongItem()
    }

    private fun setViewModel() {
        val album = getResult()
        viewModel = ViewModelProviders.of(
            this, AlbumInfoModelFactory(album)
        ).get(AlbumInformationViewModelImpl::class.java)
    }

    private fun setBinding() {
        binding.viewModel = viewModel as AlbumInformationViewModelImpl
        binding.recyclerView.adapter = fastAdapter
    }

    private fun subscribeOnSongList() {
        viewModel.subscribeOnSongsList().observe(this, Observer {
            headerAdapter.set(listOf(viewModel.headerItem))
            itemAdapter.set(it)
        })
    }

    private fun setEventHookForHeaderItem() {
        fastAdapter.addEventHook(object : ClickEventHook<SongListHeader>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return if (viewHolder is SongListHeader.ViewHolder) {
                    viewHolder.itemView
                } else {
                    null
                }
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<SongListHeader>,
                item: SongListHeader
            ) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.collectionViewUrl))
                startActivity(browserIntent)
            }
        })
    }

    private fun setEventHookForSongItem() {
        fastAdapter.addEventHook(object : ClickEventHook<SongResult>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return if (viewHolder is SongResult.ViewHolder) {
                    viewHolder.itemView
                } else {
                    null
                }
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<SongResult>,
                item: SongResult
            ) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.trackViewUrl))
                startActivity(browserIntent)
            }
        })
    }

    private fun getResult(): AlbumResult {
        val bundle = arguments
        if (bundle != null) {
            val rssLink =
                bundle.getParcelable<AlbumResult>(getString(R.string.bundle_result_key))
            if (rssLink != null) {
                return rssLink
            }
        }
        return AlbumResult()
    }

}
