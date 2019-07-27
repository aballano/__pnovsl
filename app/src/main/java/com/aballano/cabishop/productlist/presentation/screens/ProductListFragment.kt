package com.aballano.cabishop.productlist.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aballano.cabishop.R
import com.aballano.cabishop.productlist.presentation.component.ProductRowRenderer
import com.aballano.cabishop.productlist.presentation.model.ProductPresentationModel
import com.aballano.cabishop.productlist.presentation.presenter.ProductListPresenter
import com.aballano.cabishop.productlist.presentation.presenter.ProductRowPresenter
import com.aballano.knex.KnexBuilder
import kotlinx.android.synthetic.main.list_screen.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductListFragment : Fragment() {

    private val presenter: ProductListPresenter by viewModel()
    private val adapter by lazy {
        val productRowPresenter: ProductRowPresenter by viewModel()
        KnexBuilder.create { ProductRowRenderer(productRowPresenter, this::onAddToCartClicked) }
            .build()
            .into(recyclerView)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.list_screen, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.setHasFixedSize(true)
        presenter.loadItems()
            .observe(this, Observer { result ->
                result.fold(
                    ifLeft = {
                        Toast.makeText(context, "Error:\n$it", Toast.LENGTH_LONG).show()
                    }, ifRight = { items ->
                        adapter.addAllAndNotify(items)
                    }
                )
            })
    }

    private fun onAddToCartClicked(product: ProductPresentationModel, amount: Int) {
        presenter.onAddToCartClicked(product, amount)
    }

    companion object {
        fun newInstance() = ProductListFragment()
    }
}
