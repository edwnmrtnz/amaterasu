package com.github.amaterasu.scopey

import androidx.lifecycle.get
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

inline fun <reified T> ViewModelStoreOwner.scopey(noinline creator: () -> T): Lazy<T> {
    return LazyScopedValue({ viewModelStore }, { ScopeyViewModel.Factory(creator) })
}

class ScopeyViewModel<V>(
    val value: V
) : ViewModel() {
    class Factory<V>(val valueFactory: () -> V) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ScopeyViewModel(valueFactory()) as? T
                ?: throw java.lang.IllegalArgumentException("Unknown type")
    }
}

class LazyScopedValue<T : Any>(
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory
) : Lazy<T> {
    private var cached: Any = NotSet

    @Suppress("UNCHECKED_CAST")
    override val value: T
        get() {
            val value = cached
            return if (value == NotSet) {
                val factory = factoryProducer()
                val store = storeProducer()
                val viewModel = ViewModelProvider(store, factory).get<ScopeyViewModel<T>>()

                viewModel.value.also { cached = it }
            } else {
                value as T
            }
        }

    override fun isInitialized() = cached != NotSet

    companion object {
        private val NotSet = Any()
    }
}
