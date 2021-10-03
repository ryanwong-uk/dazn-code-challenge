/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ryanwong.dazn.codechallenge.base.BaseRepository

@Suppress("UNCHECKED_CAST")
class ScheduleViewModelFactory(
    private val baseRepository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (ScheduleViewModel(baseRepository) as T)
}

