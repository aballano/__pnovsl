package com.aballano.cabishop.checkout.data

import com.aballano.cabishop.checkout.domain.model.BulkDiscount
import com.aballano.cabishop.checkout.domain.model.DiscountRule
import com.aballano.cabishop.checkout.domain.model.XForYDiscount
import io.reactivex.Single

interface DiscountsRemoteDataSource {
    fun loadDiscounts(): Single<List<DiscountRule>>
}

class DiscountsRemoteDataSourceImpl : DiscountsRemoteDataSource {

    override fun loadDiscounts(): Single<List<DiscountRule>> =
        Single.fromCallable {
            listOf(
                XForYDiscount(2, 1, "VOUCHER"),
                BulkDiscount(3, 19.0, "TSHIRT")
            )
        }
}