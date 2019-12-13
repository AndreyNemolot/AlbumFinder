package com.example.itunesalbum

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.itunesalbum.fragments.Screens
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppNavigator


class MainActivity : AppCompatActivity() {

    private var navigator: Navigator = SupportAppNavigator(this, R.id.fragmentContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val router = CustomApplication.instance.getRouter()
            router.newRootScreen(Screens.Companion.ListFragment())
        }
    }

    override fun onResume() {
        super.onResume()
        CustomApplication.instance.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        CustomApplication.instance.getNavigatorHolder().removeNavigator()
    }

}
