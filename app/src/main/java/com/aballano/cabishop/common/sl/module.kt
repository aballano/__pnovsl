package com.aballano.cabishop.common.sl

import com.aballano.cabishop.common.domain.CartRepository
import com.aballano.cabishop.common.domain.CartRepositoryImpl
import org.koin.dsl.module

val commonModule = module {

    single<CartRepository> { CartRepositoryImpl() }
}