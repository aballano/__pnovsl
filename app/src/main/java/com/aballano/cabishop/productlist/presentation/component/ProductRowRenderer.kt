package com.aballano.cabishop.productlist.presentation.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aballano.cabishop.R
import com.aballano.cabishop.common.exhaustive
import com.aballano.cabishop.productlist.presentation.model.ProductPresentationModel
import com.aballano.cabishop.productlist.presentation.presenter.ProductRowPresenter
import com.aballano.cabishop.productlist.presentation.presenter.ProductRowPresenter.RenderAction
import com.aballano.cabishop.productlist.presentation.presenter.ProductRowPresenter.RenderAction.SetProductCount
import com.aballano.knex.KnexRenderer
import kotlinx.android.synthetic.main.product_item.view.*

class ProductRowRenderer(
    private val presenter: ProductRowPresenter,
    private val onAddToCartClicked: (ProductPresentationModel, Int) -> Unit
) : KnexRenderer<ProductPresentationModel>() {

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View =
        inflater.inflate(R.layout.product_item, parent, false)

    override fun render(content: ProductPresentationModel, position: Int, payloads: List<*>) {
        with(rootView) {
            productName.text = content.name
            productPrice.text = content.price
            productCount.text = "0"
            addToCartButton.isEnabled = false

            addProductButton.setOnClickListener {
                presenter.onAddClicked(productCount.text.toString()).render()
            }
            removeProductButton.setOnClickListener {
                presenter.onRemoveClicked(productCount.text.toString()).render()
            }
            addToCartButton.setOnClickListener {
                presenter.onAddToCartClicked(content, productCount.text.toString(), onAddToCartClicked).render()
            }
        }
    }

    private fun List<RenderAction>.render() {
        forEach { action ->
            when (action) {
                is SetProductCount -> rootView.productCount.text = action.count
                RenderAction.EnableAddToCart -> rootView.addToCartButton.isEnabled = true
                RenderAction.DisableAddToCart -> rootView.addToCartButton.isEnabled = false
            }.exhaustive
        }
    }
}
