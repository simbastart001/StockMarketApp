package com.plcoding.stockmarketapp.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCompanyListings(companyListings: List<CompanyListingEntity>)

    //    clear all the data from the table
    @Query("DELETE FROM company_listings")
    suspend fun clearCompanyListings()

    @Query("SELECT * FROM company_listings WHERE LOWER(name) LIKE '%' || :query || '%' OR UPPER(:query) == symbol")
    suspend fun searchCompanyListings(query: String): List<CompanyListingEntity>


}