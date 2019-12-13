package com.example.itunesalbum

import android.app.Application
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router


class CustomApplication : Application() {

    private lateinit var cicerone: Cicerone<Router>

    companion object {
        lateinit var instance: CustomApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        cicerone = Cicerone.create()
    }

    fun getNavigatorHolder(): NavigatorHolder {
        return cicerone.navigatorHolder
    }

    fun getRouter(): Router {
        return cicerone.router
    }
}