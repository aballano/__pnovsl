package com.aballano.cabishop.common.domain

import com.aballano.cabishop.common.domain.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CartRepositoryTest {

    @Test fun `repository should store given amount or products`() {
        with(givenRepository()) {
            val product = Product(
                code = "VOUCHER",
                name = "Cabishop Voucher",
                price = 5.0
            )
            val amount = 1

            addToCart(product, amount).test()
                .assertComplete()

            val list = getCartItems().test().values()[0]

            assertThat(list.size).isEqualTo(1)
            assertThat(list[0]).satisfies { (storedProduct, storedAmount) ->
                assertThat(storedProduct).isEqualTo(product)
                assertThat(storedAmount).isEqualTo(amount)

            }
        }
    }

    @Test fun `repository should increment amount or equal products`() {
        with(givenRepository()) {
            val product1 = Product(
                code = "VOUCHER",
                name = "Cabishop Voucher",
                price = 15.0
            )
            val product2 = Product(
                code = "TSHIRT",
                name = "Cabishop TSHIRT",
                price = 52.0
            )
            val product3 = Product(
                code = "VOUCHER",
                name = "Cabishop Voucher",
                price = 15.0
            )

            addToCart(product1, 1).test().assertComplete()
            addToCart(product2, 2).test().assertComplete()
            addToCart(product3, 3).test().assertComplete()

            val list = getCartItems().test().values()[0]

            assertThat(list.size).isEqualTo(2)
            assertThat(list[0]).satisfies { (storedProduct, storedAmount) ->
                assertThat(storedProduct).isEqualTo(product1)
                assertThat(storedAmount).isEqualTo(4)

            }
            assertThat(list[1]).satisfies { (storedProduct, storedAmount) ->
                assertThat(storedProduct).isEqualTo(product2)
                assertThat(storedAmount).isEqualTo(2)

            }
        }
    }

    fun givenRepository() = CartRepositoryImpl()
}