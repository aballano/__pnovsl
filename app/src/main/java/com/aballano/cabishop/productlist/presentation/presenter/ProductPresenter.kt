package com.aballano.cabishop.productlist.presentation.presenter

import androidx.lifecycle.MutableLiveData
import arrow.core.Either
import com.aballano.cabishop.common.DisposablePresenter
import com.aballano.cabishop.common.domain.CartRepository
import com.aballano.cabishop.common.postIfDistinct
import com.aballano.cabishop.productlist.domain.repository.LoadProductsRepository
import com.aballano.cabishop.productlist.domain.usecase.ParsingError
import com.aballano.cabishop.productlist.presentation.model.ProductPresentationModel
import com.aballano.cabishop.productlist.presentation.model.toPresentationModel
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.subscribeBy

class ProductListPresenter(
    private val loadProductsRepository: LoadProductsRepository,
    private val cartRepository: CartRepository,
    private val scheduler: Scheduler
) : DisposablePresenter() {
    private val items: MutableLiveData<Either<ParsingError, List<ProductPresentationModel>>> = MutableLiveData()

    // This is probably not the typical way of using LiveData or ViewModels, but it's an effective
    // anti-rotation cache.
    fun loadItems() = items.also {
        // Here we could have a more complicated logic to update the livedata
        // in order to automatically propagate changes.
        loadProductsRepository.loadProducts()
            .map { either -> either.map { list -> list.map { it.toPresentationModel() } } }
            .subscribeOn(scheduler)
            .subscribeBy(onSuccess = items::postIfDistinct)
            .trackDisposable()
    }

    fun onAddToCartClicked(product: ProductPresentationModel, amount: Int) {
        cartRepository.addToCart(product.product, amount)
            .subscribeOn(scheduler)
            .subscribeBy()
            .trackDisposable()
    }
}

