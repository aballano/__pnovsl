package com.aballano.cabishop.productlist.domain.usecase

import arrow.core.Either
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.either.fx.fx
import arrow.core.fix
import arrow.data.extensions.list.traverse.sequence
import arrow.data.fix
import com.aballano.cabishop.common.domain.model.Product
import com.aballano.cabishop.common.domain.model.decoder
import com.aballano.cabishop.productlist.domain.usecase.ParsingError.JsonMalformedError
import com.aballano.cabishop.productlist.domain.usecase.ParsingError.ModelParsingError
import com.aballano.cabishop.productlist.domain.usecase.ParsingError.UnknownJsonStructureError
import helios.core.DecodingError
import helios.core.Json

interface ParseProductUseCase {
    operator fun invoke(jsonString: String): Either<ParsingError, List<Product>>
}

class ParseProductUseCaseImpl : ParseProductUseCase {
    override operator fun invoke(jsonString: String): Either<ParsingError, List<Product>> = fx {
        val json = !Json.parseFromString(jsonString)
            .mapLeft(::JsonMalformedError)

        val productsArray = !json["products"].flatMap(Json::asJsArray)
            .toEither { UnknownJsonStructureError }

        val list = productsArray.value.map {
            Product.decoder().decode(it)
                .mapLeft(::ModelParsingError)
        }

        // Since we get a List<Either<Product>> we need to reverse it (and then fix the types).
        !list.sequence(Either.applicative()).fix().map { it.fix() }
    }
}

sealed class ParsingError {
    data class JsonMalformedError(val throwable: Throwable) : ParsingError()
    object UnknownJsonStructureError : ParsingError()
    data class ModelParsingError(val error: DecodingError) : ParsingError()
}