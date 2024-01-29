package com.plcoding.stockmarketapp.data.csv

import com.opencsv.CSVReader
import com.plcoding.stockmarketapp.domain.model.CompanyListing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyListingParser @Inject constructor() : CSvParser<CompanyListing> {
    override suspend fun parse(stream: InputStream): List<CompanyListing> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val symbol = line.getOrNull(0)    // 0 is the index of the symbol
                    val name = line.getOrNull(1)     // 1 is the index of the name
                    val exchange = line.getOrNull(2) // 2 is the index of the exchange
                    CompanyListing(
                        symbol = symbol ?: return@mapNotNull null,
                        name = name ?: return@mapNotNull null,
                        exchange = exchange ?: return@mapNotNull null
                    )
                }.also {
                    csvReader.close()
                }
        }
    }
}