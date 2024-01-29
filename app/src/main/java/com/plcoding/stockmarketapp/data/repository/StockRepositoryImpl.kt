package com.plcoding.stockmarketapp.data.repository

import com.opencsv.CSVParser
import com.plcoding.stockmarketapp.data.csv.CSvParser
import com.plcoding.stockmarketapp.data.local.StockDatabase
import com.plcoding.stockmarketapp.data.mapper.toCompanyListing
import com.plcoding.stockmarketapp.data.mapper.toCompanyListingEntity
import com.plcoding.stockmarketapp.data.remote.StockApi
import com.plcoding.stockmarketapp.domain.model.CompanyListing
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import com.plcoding.stockmarketapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val stockApi: StockApi,
    private val stockDatabase: StockDatabase,
    val companyListingParser: CSvParser<CompanyListing>
) : StockRepository {

    private val stockDao = stockDatabase.stockDao
    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = stockDao.searchCompanyListings(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
//           Get the data from cache
            val shouldJustLoadFromCache = !fetchFromRemote && !isDbEmpty
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

//           Get the data from the API
            val remoteListings = try {
                val response = stockApi.getListings()
                companyListingParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
                null
            }

            remoteListings?.let { listings ->
                stockDao.clearCompanyListings()
                stockDao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() })
                emit(
                    Resource.Success(data =
                    stockDao.searchCompanyListings("")
                        .map { it.toCompanyListing() })
                )
                emit(Resource.Loading(false))
            }
        }
    }
}














