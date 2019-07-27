package com.aballano.cabishop.productlist.sl

import com.aballano.cabishop.productlist.data.ProductsRemoteDataSource
import com.aballano.cabishop.productlist.data.ProductsRemoteDataSourceImpl
import com.aballano.cabishop.productlist.domain.usecase.ParseProductUseCase
import com.aballano.cabishop.productlist.domain.usecase.ParseProductUseCaseImpl
import com.aballano.cabishop.productlist.domain.repository.LoadProductsRepository
import com.aballano.cabishop.productlist.domain.repository.LoadProductsRepositoryImpl
import com.aballano.cabishop.productlist.presentation.presenter.ProductListPresenter
import com.aballano.cabishop.productlist.presentation.presenter.ProductRowPresenter
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val productModule = module {

    // Data sources
    factory<ProductsRemoteDataSource> { ProductsRemoteDataSourceImpl() }

    // Usecases
    factory<ParseProductUseCase> { ParseProductUseCaseImpl() }

    // Repositories
    factory<LoadProductsRepository> { LoadProductsRepositoryImpl(get(), get()) }

    // Presenters
    viewModel { ProductRowPresenter() }
    viewModel { ProductListPresenter(get(), get(), Schedulers.io()) }
}
