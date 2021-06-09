package com.bighero.speaky.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bighero.speaky.data.source.FirebaseRepository
import com.bighero.speaky.domain.di.Injection
import com.bighero.speaky.ui.assessment.AssessmentViewModel
import com.bighero.speaky.ui.assessment.result.ResultViewModel
import com.bighero.speaky.ui.detail.assessment.DetailResultViewModel
import com.bighero.speaky.ui.detail.module.DetailModuleViewModel
import com.bighero.speaky.ui.detail.praktik.DetailPracticeViewModel
import com.bighero.speaky.ui.home.fragment.dashboard.DashboardViewModel
import com.bighero.speaky.ui.home.fragment.history.HistoryViewModel
import com.bighero.speaky.ui.home.fragment.module.ModuleViewModel
import com.bighero.speaky.ui.home.fragment.practice.PracticeViewModel


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
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
                ResultViewModel(mFirebaseRepository) as T
            }
            modelClass.isAssignableFrom(AssessmentViewModel::class.java) -> {
                AssessmentViewModel(mFirebaseRepository) as T
            }
            modelClass.isAssignableFrom(DetailModuleViewModel::class.java) -> {
                DetailModuleViewModel(mFirebaseRepository) as T
            }
            modelClass.isAssignableFrom(PracticeViewModel::class.java) -> {
                PracticeViewModel(mFirebaseRepository) as T
            }
            modelClass.isAssignableFrom(DetailPracticeViewModel::class.java) -> {
                DetailPracticeViewModel(mFirebaseRepository) as T
            }
            modelClass.isAssignableFrom(DetailResultViewModel::class.java) -> {
                DetailResultViewModel(mFirebaseRepository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(mFirebaseRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    }
}

