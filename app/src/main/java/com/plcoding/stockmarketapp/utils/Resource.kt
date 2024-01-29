package com.plcoding.stockmarketapp.utils

/***
 * @DrStart:            Resource.kt is responsible for handling the state of the data
 *                      that we are fetching from the API.
 *                      We have three states: Success, Error, and Loading.
 * */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(val isLoading: Boolean = true): Resource<T>(null)
}