package com.bighero.speaky.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bighero.speaky.data.source.FirebaseRepository
import com.bighero.speaky.domain.di.Injection
import com.bighero.speaky.ui.home.fragment.history.HistoryViewModel
import com.bighero.speaky.ui.home.fragment.module.ModuleViewModel


class ViewModelFactory private constructor(private val mFirebaseRepository: FirebaseRepository) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context)).apply {
                    instance = this
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(mFirebaseRepository) as T
            }
            modelClass.isAssignableFrom(ModuleViewModel::class.java) -> {
                ModuleViewModel(mFirebaseRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    }
}