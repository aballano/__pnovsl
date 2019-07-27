package com.aballano.cabishop.checkout.presentation.component

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.aballano.cabishop.R
import com.aballano.cabishop.checkout.presentation.model.ProductToCheckoutPresentationModel
import com.aballano.cabishop.checkout.presentation.presenter.ProductCheckoutRowPresenter
import com.aballano.cabishop.checkout.presentation.presenter.ProductCheckoutRowPresenter.RenderAction
import com.aballano.cabishop.checkout.presentation.presenter.ProductCheckoutRowPresenter.RenderAction.Discount
import com.aballano.cabishop.checkout.presentation.presenter.ProductCheckoutRowPresenter.RenderAction.SetProductTotal
import com.aballano.cabishop.common.exhaustive
import com.aballano.knex.KnexRenderer
import kotlinx.android.synthetic.main.checkout_item.view.*

class ProductCheckoutRowRenderer(
    private val presenter: ProductCheckoutRowPresenter
) : KnexRenderer<ProductToCheckoutPresentationModel>() {

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View =
        inflater.inflate(R.layout.checkout_item, parent, false)

    override fun render(content: ProductToCheckoutPresentationModel, position: Int, payloads: List<*>) {
        with(rootView) {
            productName.text = content.product.name
            productPrice.text = content.product.price.toString()
        }

        presenter.onStart(content).render()
    }

    private fun List<RenderAction>.render() {
        forEach { action ->
            when (action) {
                is SetProductTotal -> {
                    rootView.productTotal.text =
                        context.getString(
                            R.string.productTotal,
                            action.numItems, action.itemPrice, action.productTotal
                        )
                }
                Discount.NoDiscount -> {
                    rootView.productTotalDiscounted.visibility = GONE
                }
                is Discount.DiscountApplied -> {
                    with(rootView.productTotalDiscounted) {
                        visibility = VISIBLE
                        text = context.getString(
                            R.string.discountTotal,
                            action.discountApplied, action.discountedTotal
                        )
                    }
                }
            }.exhaustive
        }
    }
}
