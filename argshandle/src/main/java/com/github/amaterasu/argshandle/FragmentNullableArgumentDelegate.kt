package com.github.amaterasu.argshandle

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class FragmentNullableArgumentDelegate<T : Any?> : ReadWriteProperty<Fragment, T?> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(
        thisRef: Fragment,
        property: KProperty<*>
    ): T? {
        val key = property.name
        return thisRef.arguments?.get(key) as? T
    }

    override fun setValue(
        thisRef: Fragment,
        property: KProperty<*>, value: T?
    ) {
        val args = thisRef.arguments ?: Bundle()
        val key = property.name
        value?.let {
            args.put(key, value)
        } ?: args.remove(key)
        thisRef.arguments = args
    }
}

fun <T : Any> nullableArgument(): ReadWriteProperty<Fragment, T?> =
    FragmentNullableArgumentDelegate()