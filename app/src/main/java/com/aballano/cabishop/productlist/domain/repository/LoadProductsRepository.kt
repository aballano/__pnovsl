package com.aballano.cabishop.productlist.domain.repository

import arrow.core.Either
import com.aballano.cabishop.common.domain.model.Product
import com.aballano.cabishop.productlist.data.ProductsRemoteDataSource
import com.aballano.cabishop.productlist.domain.usecase.ParseProductUseCase
import com.aballano.cabishop.productlist.domain.usecase.ParsingError
import io.reactivex.Single

interface LoadProductsRepository {
    fun loadProducts(): Single<Either<ParsingError, List<Product>>>
}

// Here we could have a local data source such as a DB combined with a `lastUpdate`
// kind of field in the call to remote to avoid unnecessary network requests.
// For such cases a test would be needed, but given the nature of the params used here
// I don't think there's any value in testing the impl wiring when the function signature is explicit enough.
class LoadProductsRepositoryImpl(
    private val remoteDataSource: ProductsRemoteDataSource,
    private val parseProduct: ParseProductUseCase
) : LoadProductsRepository {

    override fun loadProducts() =
        remoteDataSource.loadProductsJson()
            .map(parseProduct::invoke)
}