package com.github.amaterasu.argshandle

import android.app.Activity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class ActivityArgumentDelegate<T : Any?> (
    private val key : String,
    private val default : T?
) : ReadOnlyProperty<Activity, T?> {

    override fun getValue(thisRef: Activity, property: KProperty<*>): T? {
        return thisRef.intent
            ?.extras
            ?.get(key) as? T
            ?: default
    }
}

fun <T : Any?> extras(
    key : String,
    default : T? = null
): ReadOnlyProperty<Activity, T?> =
    ActivityArgumentDelegate(key, default)