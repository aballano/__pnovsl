package com.aballano.cabishop.productlist.presentation.presenter

import com.aballano.cabishop.common.DisposablePresenter
import com.aballano.cabishop.common.intOrZero
import com.aballano.cabishop.productlist.presentation.model.ProductPresentationModel
import com.aballano.cabishop.productlist.presentation.presenter.ProductRowPresenter.RenderAction.DisableAddToCart
import com.aballano.cabishop.productlist.presentation.presenter.ProductRowPresenter.RenderAction.EnableAddToCart
import com.aballano.cabishop.productlist.presentation.presenter.ProductRowPresenter.RenderAction.SetProductCount

class ProductRowPresenter : DisposablePresenter() {
    fun onAddClicked(currentAmount: String): List<RenderAction> = listOf(
        SetProductCount("${currentAmount.intOrZero().coerceIn(0, 98) + 1}"),
        EnableAddToCart
    )

    fun onRemoveClicked(currentAmount: String): List<RenderAction> {
        val coercedAmount = currentAmount.intOrZero().coerceIn(1, 99)
        val newAmount = coercedAmount - 1

        return listOf(
            SetProductCount("$newAmount")
        ).let {
            if (newAmount == 0) it + listOf(DisableAddToCart)
            else it
        }
    }

    fun onAddToCartClicked(
        content: ProductPresentationModel,
        currentAmount: String,
        onAddToCartClicked: (ProductPresentationModel, Int) -> Unit
    ): List<RenderAction> {
        onAddToCartClicked(content, currentAmount.intOrZero())
        return listOf(
            SetProductCount("0"),
            DisableAddToCart
        )
    }

    sealed class RenderAction {
        data class SetProductCount(val count: String) : RenderAction()
        object EnableAddToCart : RenderAction()
        object DisableAddToCart : RenderAction()
    }
}
