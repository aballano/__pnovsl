package com.aballano.cabishop.productlist.presentation.model

import com.aballano.cabishop.common.domain.model.Product

data class ProductPresentationModel(
    val product: Product
) {
    val name = product.name
    val price = product.price.toString()
}

fun Product.toPresentationModel() = ProductPresentationModel(
    this
)