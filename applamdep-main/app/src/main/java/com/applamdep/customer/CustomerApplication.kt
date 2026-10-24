package com.applamdep.customer

import android.app.Application
import com.applamdep.customer.core.di.AppContainer
import com.applamdep.customer.core.di.DefaultAppContainer

/**
 * Application class khởi tạo Container Dependency Injection cho toàn app
 */
class CustomerApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
