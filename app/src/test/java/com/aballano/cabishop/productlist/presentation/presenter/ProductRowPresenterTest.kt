package com.aballano.cabishop.productlist.presentation.presenter

import com.aballano.cabishop.common.domain.model.Product
import com.aballano.cabishop.productlist.presentation.model.ProductPresentationModel
import com.aballano.cabishop.productlist.presentation.presenter.ProductRowPresenter.RenderAction
import com.aballano.cabishop.productlist.presentation.presenter.ProductRowPresenter.RenderAction.DisableAddToCart
import com.aballano.cabishop.productlist.presentation.presenter.ProductRowPresenter.RenderAction.EnableAddToCart
import com.aballano.cabishop.productlist.presentation.presenter.ProductRowPresenter.RenderAction.SetProductCount
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.concurrent.atomic.AtomicReference

@RunWith(Enclosed::class)
class ProductRowPresenterTest {

    // Part of this is technically testing the `intOrZero` function.
    @RunWith(Parameterized::class)
    class OnAddClickedCheck(
        private val amount: String,
        private val expectedActions: List<RenderAction>
    ) {

        companion object {
            @JvmStatic @Parameterized.Parameters(name = "for {0} should be {1}")
            fun data(): Collection<Array<Any>> = listOf(
                arrayOf("0", listOf(SetProductCount("1"))),
                arrayOf("-1", listOf(SetProductCount("1"))),
                arrayOf("99", listOf(SetProductCount("99"))),
                arrayOf("anything else", listOf(SetProductCount("1")))
            )
        }

        private val presenter = ProductRowPresenter()

        @Test fun `onAddClicked should coerce `() {
            assertThat(presenter.onAddClicked(amount))
                .isEqualTo(expectedActions + EnableAddToCart)
        }
    }

    @RunWith(Parameterized::class)
    class OnRemoveClickedCheck(
        private val amount: String,
        private val expectedActions: List<RenderAction>
    ) {

        companion object {
            @JvmStatic @Parameterized.Parameters(name = "for {0} should be {1}")
            fun data(): Collection<Array<Any>> = listOf(
                arrayOf("1", listOf(SetProductCount("0"), DisableAddToCart)),
                arrayOf("-1", listOf(SetProductCount("0"), DisableAddToCart)),
                arrayOf("0", listOf(SetProductCount("0"), DisableAddToCart)),
                arrayOf("2", listOf(SetProductCount("1"))),
                arrayOf("anything else", listOf(SetProductCount("0"), DisableAddToCart))
            )
        }

        private val presenter = ProductRowPresenter()

        @Test fun `onRemoveClicked should coerce `() {
            assertThat(presenter.onRemoveClicked(amount))
                .isEqualTo(expectedActions)
        }
    }

    private val presenter = ProductRowPresenter()

    @Test fun `onAddToCartClicked should call lambda`() {
        val model = ProductPresentationModel(
            Product("1", "name", 123.0)
        )
        val amount = "8"

        // Here in order to verify the lambda is called we could either use mocks or something custom
        val ref: AtomicReference<Pair<ProductPresentationModel, Int>> = AtomicReference()
        val lambda = { a: ProductPresentationModel, b: Int ->
            ref.set(a to b)
        }

        presenter.onAddToCartClicked(model, amount, lambda)

        assertThat(ref.get()).isEqualTo(model to amount.toInt())
    }

    @Test fun `onAddToCartClicked should set new state`() {
        val model = ProductPresentationModel(
            Product("1", "name", 123.0)
        )
        val amount = "8"

        assertThat(presenter.onAddToCartClicked(model, amount) {_, _ -> })
            .isEqualTo(
                listOf(
                    SetProductCount("0"),
                    DisableAddToCart
                )
            )
    }
}