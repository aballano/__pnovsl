package com.aballano.cabishop.checkout.domain.model

sealed class DiscountRule {
    // This name should technically belong to the presentation layer as how we're using it right now.
    abstract val name: String
}

sealed class SameProductDiscount(
    open val targetItemNumber: Int,
    open val targetProductCode: String
) : DiscountRule()

data class XForYDiscount(
    override val targetItemNumber: Int,
    val paidItems: Int,
    override val targetProductCode: String
) : SameProductDiscount(targetItemNumber, targetProductCode) {
    override val name: String
        get() = "$targetItemNumber-per-$paidItems"
}

data class BulkDiscount(
    override val targetItemNumber: Int,
    val reducedPrice: Double,
    override val targetProductCode: String
) : SameProductDiscount(targetItemNumber, targetProductCode) {
    override val name: String
        get() = "+$targetItemNumber-for-$reducedPrice"
}

fun DiscountRule.appliesTo(
    productCode: String,
    amount: Int
): Boolean = when (this) {
    is SameProductDiscount -> amount >= targetItemNumber &&
        productCode == targetProductCode
}

fun DiscountRule?.applyTo(
    amount: Int,
    productPrice: Double
): Double = when (this) {
    // We assume here the order of discounts, this might be better controlled
    // by a priority param from the remote response
    is XForYDiscount -> (amount / targetItemNumber + amount % targetItemNumber) * paidItems * productPrice
    is BulkDiscount -> amount * reducedPrice
    null -> amount * productPrice
}