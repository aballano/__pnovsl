package com.aballano.cabishop.checkout.presentation.presenter

import com.aballano.cabishop.checkout.presentation.model.ProductToCheckoutPresentationModel
import com.aballano.cabishop.checkout.presentation.presenter.ProductCheckoutRowPresenter.RenderAction.Discount.DiscountApplied
import com.aballano.cabishop.checkout.presentation.presenter.ProductCheckoutRowPresenter.RenderAction.Discount.NoDiscount
import com.aballano.cabishop.checkout.presentation.presenter.ProductCheckoutRowPresenter.RenderAction.SetProductTotal
import com.aballano.cabishop.common.DisposablePresenter

class ProductCheckoutRowPresenter : DisposablePresenter() {

    fun onStart(content: ProductToCheckoutPresentationModel): List<RenderAction> = listOf(
        SetProductTotal(content.totalAmount, content.product.price, content.originalTotalPrice),
        if (content.discountApplied != null) {
            DiscountApplied(content.discountApplied.name, content.discountedTotalPrice)
        } else {
            NoDiscount
        }
    )

    sealed class RenderAction {
        data class SetProductTotal(val numItems: Int, val itemPrice: Double, val productTotal: Double) : RenderAction()

        sealed class Discount : RenderAction() {
            object NoDiscount : Discount()
            data class DiscountApplied(val discountApplied: String, val discountedTotal: Double) : Discount()
        }
    }
}
