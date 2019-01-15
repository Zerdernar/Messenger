package com.example.schooluser.messenger

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

// TODO форматирование cmd + alt + l
val AppModule = module {
    viewModel { ConversationViewModel() }
}