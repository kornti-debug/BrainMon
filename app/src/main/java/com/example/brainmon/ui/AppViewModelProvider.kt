package com.example.brainmon.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.brainmon.MonsterApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for the Home Screen ViewModel
        initializer {
            MonstersViewModel(
                monsterApplication().repository
            )
        }

    }
}


fun CreationExtras.monsterApplication(): MonsterApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MonsterApplication)