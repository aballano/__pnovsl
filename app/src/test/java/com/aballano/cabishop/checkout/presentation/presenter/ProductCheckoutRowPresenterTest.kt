package com.aballano.cabishop.checkout.presentation.presenter

import com.aballano.cabishop.checkout.domain.model.DiscountRule
import com.aballano.cabishop.checkout.domain.model.XForYDiscount
import com.aballano.cabishop.checkout.presentation.model.ProductToCheckoutPresentationModel
import com.aballano.cabishop.checkout.presentation.presenter.ProductCheckoutRowPresenter.RenderAction.Discount
import com.aballano.cabishop.checkout.presentation.presenter.ProductCheckoutRowPresenter.RenderAction.SetProductTotal
import com.aballano.cabishop.common.domain.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ProductCheckoutRowPresenterTest {

    private val presenter = ProductCheckoutRowPresenter()

    @Test fun `onStart should set product total price`() {
        assertThat(presenter.onStart(givenModel())).contains(
            SetProductTotal(7, 5.0, 125.0),
            Discount.NoDiscount
        )
    }

    @Test fun `onStart should set no discount`() {
        assertThat(presenter.onStart(givenModel())).contains(
            Discount.NoDiscount
        )
    }

    @Test fun `onStart should set discount`() {
        val discount = XForYDiscount(1, 1, "VOUCHER2")
        assertThat(presenter.onStart(givenModel(withDiscount = discount))).contains(
            Discount.DiscountApplied(discount.name, 99.0)
        )
    }

    private fun givenModel(
        withDiscount: DiscountRule? = null,
        discountedTotalPrice: Double = 99.0
    ) = ProductToCheckoutPresentationModel(
        Product("VOUCHER", "CabiShop Voucher", 5.0),
        125.0,
        7,
        withDiscount,
        discountedTotalPrice
    )
}