package com.example.itunesalbum.fragments

import androidx.fragment.app.Fragment
import com.example.itunesalbum.fragments.albumInformation.AlbumInformationFragment
import com.example.itunesalbum.fragments.albumList.AlbumListFragment
import com.example.itunesalbum.model.album.AlbumResult
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    companion object {
        class ListFragment : SupportAppScreen() {
            override fun getFragment(): Fragment {
                return AlbumListFragment.newInstance()
            }
        }

        class InformationFragment(private val bundleKey: String, private val result: AlbumResult) :
            SupportAppScreen() {
            override fun getFragment(): Fragment {
                return AlbumInformationFragment.newInstance(bundleKey, result)
            }
        }
    }

}