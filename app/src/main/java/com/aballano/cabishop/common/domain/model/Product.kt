package com.aballano.cabishop.common.domain.model

import helios.json

// An alternative way would be declaring a data model for the
// remote response and then convert to domain model.
@json
data class Product(
    // Right now the code is a string, in order to gain benefit
    // from types this should be an entity on it's own.
    val code: String,
    val name: String,
    val price: Double
) {
    companion object
}
