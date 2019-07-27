package com.aballano.cabishop.checkout.presentation.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aballano.cabishop.checkout.data.DiscountsRemoteDataSource
import com.aballano.cabishop.checkout.domain.model.DiscountRule
import com.aballano.cabishop.checkout.domain.model.appliesTo
import com.aballano.cabishop.checkout.presentation.model.ProductToCheckoutPresentationModel
import com.aballano.cabishop.checkout.presentation.model.toPresentationModel
import com.aballano.cabishop.common.DisposablePresenter
import com.aballano.cabishop.common.domain.CartRepository
import com.aballano.cabishop.common.domain.model.CartProduct
import com.aballano.cabishop.common.postIfDistinct
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy

class CheckoutListPresenter(
    private val cartRepository: CartRepository,
    // The reason for using a component from the data source in a presentation component is due to the fact that
    // discounts should normally be calculated in the server-side to avoid possible hacks in the clients. And therefore
    // I didn't put much more effort here as I would first expose the problem and discuss the technical implications.
    private val discountsRemoteDataSource: DiscountsRemoteDataSource,
    private val scheduler: Scheduler
) : DisposablePresenter() {
    private val items: MutableLiveData<List<ProductToCheckoutPresentationModel>> = MutableLiveData()

    fun loadItems(): LiveData<List<ProductToCheckoutPresentationModel>> =
        items.also {
            cartRepository.getCartItems()
                .calculateCheckout()
                .subscribeOn(scheduler)
                .subscribeBy(onSuccess = items::postIfDistinct)
                .trackDisposable()
        }

    private fun Single<List<CartProduct>>.calculateCheckout(): Single<List<ProductToCheckoutPresentationModel>> =
        this.flatMap { products ->
            discountsRemoteDataSource.loadDiscounts().map { discounts ->
                products.apply(discounts)
            }
        }

    // I would open for discussion if this kind of function should be tested. I'm unsure if we would be
    // either trying to test each function, when all of them should have their dedicated tests; or we would
    // be willing to tests the wiring itself or the right pass of params.
    private fun List<CartProduct>.apply(
        discounts: List<DiscountRule>
    ): List<ProductToCheckoutPresentationModel> =
        map { cartProduct ->
            cartProduct.toPresentationModel(discounts.find { discount ->
                discount.appliesTo(cartProduct.product.code, cartProduct.amount)
            })
        }
}






