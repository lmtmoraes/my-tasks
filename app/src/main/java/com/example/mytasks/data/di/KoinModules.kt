package com.example.mytasks.data.di

import android.net.ConnectivityManager
import androidx.room.Room
import com.example.mytasks.data.MyTasksDatabase
import com.example.mytasks.data.TaskRepository
import com.example.mytasks.ui.viewmodel.TaskFormViewModel
import com.example.mytasks.ui.viewmodel.TaskListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidModule = module {
    single {
        androidContext()
            .getSystemService(ConnectivityManager::class.java)
                as ConnectivityManager
    }
}

val appModule = module {
    viewModelOf(::TaskFormViewModel)
    viewModelOf(::TaskListViewModel)
}

val storageModule = module {
    singleOf(::TaskRepository)
    single {
        Room.databaseBuilder(
            androidContext(),
            MyTasksDatabase::class.java, "my-tasks.db"
        ).build()
    }
    single {
        get<MyTasksDatabase>().taskDao()
    }
}