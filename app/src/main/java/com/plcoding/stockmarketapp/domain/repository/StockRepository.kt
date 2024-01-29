package com.plcoding.stockmarketapp.domain.repository

import com.plcoding.stockmarketapp.domain.model.CompanyListing
import com.plcoding.stockmarketapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    /**
     * @DrStart:            This function is responsible for fetching the data from the API. We are
     *                     using the Flow API to emit the data to the UI. We are also using the
     *                     Resource class to handle the state of the data that we are fetching.
     * */
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

}