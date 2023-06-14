package com.umc.oppla.repository

import com.umc.oppla.data.local.DataStore

class SearchRepository(private val datastore: DataStore) {

    suspend fun saveSearchHistory(token: String) = datastore.saveSearchHistory(token)

    suspend fun deleteSearchHistory(token:String) = datastore.deleteSearchHistory(token)

    fun getSearchHistory() = datastore.getSearchHistory()

}