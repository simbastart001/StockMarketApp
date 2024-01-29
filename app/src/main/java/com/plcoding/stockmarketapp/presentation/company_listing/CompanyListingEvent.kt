package com.plcoding.stockmarketapp.presentation.company_listing

sealed class CompanyListingEvent {
    object Refresh : CompanyListingEvent()
    data class onSearchQueryChange(val query: String) : CompanyListingEvent()

}