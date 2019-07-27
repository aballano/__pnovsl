package com.aballano.cabishop.common.domain

import com.aballano.cabishop.common.domain.model.CartProduct
import com.aballano.cabishop.common.domain.model.Product
import io.reactivex.Completable
import io.reactivex.Single

interface CartRepository {
    // Here it would be nice to constraint the int to be only positive to prevent weird behaviours,
    // we could also benefit from some kind of simple validation.
    fun addToCart(product: Product, amount: Int): Completable
    fun getCartItems(): Single<List<CartProduct>>
}

class CartRepositoryImpl : CartRepository {
    private val cart by lazy { mutableMapOf<Product, CartProduct>() }

    override fun addToCart(product: Product, amount: Int): Completable = Completable.fromAction {
        cart[product].let { cartProduct ->
            cart[product] = when (cartProduct) {
                null -> CartProduct(product, amount)
                else -> cartProduct.copy(amount = cartProduct.amount + amount)
            }
        }
    }

    override fun getCartItems(): Single<List<CartProduct>> =
        Single.fromCallable { cart.values.toList() }
}