package com.aballano.cabishop.productlist.domain

import com.aballano.cabishop.common.domain.model.Product
import com.aballano.cabishop.productlist.domain.usecase.ParseProductUseCaseImpl
import com.aballano.cabishop.productlist.domain.usecase.ParsingError
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ParseProductUseCaseTest {
    private val parseProduct = ParseProductUseCaseImpl()

    @Test fun `should properly parse json`() {
        parseProduct(
            """
            { "products":[
                {"code":"VOUCHER","name":"CabiShop Voucher","price":5},
                {"code":"TSHIRT","name":"CabiShop T-Shirt","price":20},
                {"code":"MUG","name":"CabiShop Coffee Mug","price":7.5}
            ] }
            """
        ).fold(
            ifLeft = {
                Assertions.fail("")
            }, ifRight = {
                assertThat(it).containsExactly(
                    Product("VOUCHER", "CabiShop Voucher", 5.0),
                    Product("TSHIRT", "CabiShop T-Shirt", 20.0),
                    Product("MUG", "CabiShop Coffee Mug", 7.5)
                )
            }
        )
    }

    @Test fun `should identify malformed json`() {
        parseProduct("not a json").fold(
            ifLeft = {
                assertThat(it).isInstanceOf(ParsingError.JsonMalformedError::class.java)
            }, ifRight = {
                Assertions.fail("")
            }
        )
    }

    @Test fun `should identify wrong json tag`() {
        parseProduct(
            """
            { "wrongproducts":[
                {"code":"VOUCHER","name":"CabiShop Voucher","price":5},
                {"code":"TSHIRT","name":"CabiShop T-Shirt","price":20},
                {"code":"MUG","name":"CabiShop Coffee Mug","price":7.5}
            ] }
            """
        ).fold(
            ifLeft = {
                assertThat(it).isInstanceOf(ParsingError.UnknownJsonStructureError::class.java)
            }, ifRight = {
                Assertions.fail("")
            }
        )
    }

    @Test fun `should identify wrong json structure`() {
        parseProduct(
            """
            {  "products": 
                {"code":"MUG","name":"CabiShop Coffee Mug","price":7.5}
            }
            """
        ).fold(
            ifLeft = {
                assertThat(it).isInstanceOf(ParsingError.UnknownJsonStructureError::class.java)
            }, ifRight = {
                Assertions.fail("")
            }
        )
    }

    @Test fun `should identify wrong json field`() {
        parseProduct(
            """
            { "products":[
                {"wrongcode":"VOUCHER","name":"CabiShop Voucher","price":5},
                {"wrongcode":"TSHIRT","name":"CabiShop T-Shirt","price":20},
                {"wrongcode":"MUG","name":"CabiShop Coffee Mug","price":7.5}
            ] }
            """
        ).fold(
            ifLeft = {
                assertThat(it).isInstanceOf(ParsingError.ModelParsingError::class.java)
            }, ifRight = {
                Assertions.fail("")
            }
        )
    }
}