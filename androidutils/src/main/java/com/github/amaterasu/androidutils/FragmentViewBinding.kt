package com.github.amaterasu.androidutils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
Sample:

class MyFragment: Fragment(R.layout.my_fragment) {
    private val binding by viewBinding(MyFragmentBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
 */

class FragmentViewBinding<T : ViewBinding>(
    val fragment: Fragment,
    val viewBindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerObserver = Observer<LifecycleOwner?> { owner ->
                if (owner == null) {
                    binding = null
                }
            }
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerObserver)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerObserver)
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = binding

        if (binding != null && binding.root === thisRef.view) {
            return binding
        }

        val view = thisRef.view ?: throw IllegalStateException(ERROR_VIEW_ACCESS_NULL)

        return viewBindingFactory(view).also { this.binding = it }
    }
    companion object {
        private const val ERROR_VIEW_ACCESS_NULL = """
            Should not attempt to get bindings when the Fragment's view is null.
        """
    }
}

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBinding(this, viewBindingFactory)