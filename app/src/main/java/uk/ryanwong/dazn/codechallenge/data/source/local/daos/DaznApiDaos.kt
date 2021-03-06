/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local.daos

import uk.ryanwong.dazn.codechallenge.data.source.local.daos.EventsDao
import uk.ryanwong.dazn.codechallenge.data.source.local.daos.ScheduleDao

/***
 * This data class is designed to make a balance between splitting Daos for easier maintenance
 * and avoiding a need to pass a long list of DAOs through the constructor of DaznRoomDbDataSource.
 */
data class DaznApiDaos(
    val eventsDao: EventsDao,
    val scheduleDao: ScheduleDao
)