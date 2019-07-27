package com.aballano.cabishop.checkout.sl

import com.aballano.cabishop.checkout.data.DiscountsRemoteDataSource
import com.aballano.cabishop.checkout.data.DiscountsRemoteDataSourceImpl
import com.aballano.cabishop.checkout.presentation.presenter.CheckoutListPresenter
import com.aballano.cabishop.checkout.presentation.presenter.ProductCheckoutRowPresenter
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cartModule = module {

    factory<DiscountsRemoteDataSource> { DiscountsRemoteDataSourceImpl() }

    // Presenters
    viewModel { ProductCheckoutRowPresenter() }
    viewModel { CheckoutListPresenter(get(), get(), Schedulers.io()) }
}