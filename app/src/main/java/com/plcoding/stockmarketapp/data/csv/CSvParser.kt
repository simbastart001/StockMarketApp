package com.plcoding.stockmarketapp.data.csv

import java.io.InputStream

interface CSvParser<T> {
    suspend fun parse(stream: InputStream): List<T>
}