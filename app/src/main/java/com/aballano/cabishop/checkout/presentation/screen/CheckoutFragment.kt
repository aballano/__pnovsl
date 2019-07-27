package com.aballano.cabishop.checkout.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aballano.cabishop.R
import com.aballano.cabishop.checkout.presentation.component.ProductCheckoutRowRenderer
import com.aballano.cabishop.checkout.presentation.presenter.CheckoutListPresenter
import com.aballano.cabishop.checkout.presentation.presenter.ProductCheckoutRowPresenter
import com.aballano.knex.KnexBuilder
import kotlinx.android.synthetic.main.list_screen.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutFragment : Fragment() {

    private val presenter: CheckoutListPresenter by viewModel()
    private val adapter by lazy {
        val productCheckoutRowPresenter: ProductCheckoutRowPresenter by viewModel()
        KnexBuilder.create { ProductCheckoutRowRenderer(productCheckoutRowPresenter) }
            .build()
            .into(recyclerView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.list_screen, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.setHasFixedSize(true)
        presenter.loadItems()
            .observe(this, Observer { items ->
                adapter.addAllAndNotify(items)
            })

        //TODO add total row
    }


    companion object {
        fun newInstance() = CheckoutFragment()
    }
}
