package com.aballano.cabishop.productlist.data

import com.github.kittinunf.fuel.httpGet
import io.reactivex.Single

interface ProductsRemoteDataSource {
    // We normally would load directly the list of products with Retrofit/others directly,
    // but I wanted to try two new libs and I preferred to do the json parsing in the repository.
    fun loadProductsJson(): Single<String>
}

// I believe that the only test we could do at this point is a data validation one between
// the app and the backend.
class ProductsRemoteDataSourceImpl : ProductsRemoteDataSource {

    override fun loadProductsJson(): Single<String> = Single.fromPublisher { pub ->
        "https://api.myjson.com/bins/4bwec"
            .httpGet()
            .responseString()
            .third
            .fold(success = {
                pub.onNext(it)
                pub.onComplete()
            }, failure = {
                // An alternative way would be to return a Single<Either<Response, Error>>.
                pub.onError(it.exception)
            })
    }
}