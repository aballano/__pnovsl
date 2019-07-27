package com.aballano.cabishop.checkout.presentation.model

import com.aballano.cabishop.checkout.domain.model.DiscountRule
import com.aballano.cabishop.checkout.domain.model.applyTo
import com.aballano.cabishop.common.domain.model.CartProduct
import com.aballano.cabishop.common.domain.model.Product

data class ProductToCheckoutPresentationModel(
    val product: Product,
    val originalTotalPrice: Double,
    val totalAmount: Int,
    val discountApplied: DiscountRule?,
    val discountedTotalPrice: Double
)

fun CartProduct.toPresentationModel(discountRule: DiscountRule?): ProductToCheckoutPresentationModel =
    ProductToCheckoutPresentationModel(
        product = this.product,
        originalTotalPrice = this.amount * this.product.price,
        totalAmount = this.amount,
        discountApplied = discountRule,
        discountedTotalPrice = discountRule.applyTo(this.amount, this.product.price)
    )