# argshandle

Simplify fragments and activity arguments handling.


### Fragment

```kotlin
// Source Fragment
parentFragmentManager.commit {
    val fragment = DesetinationFragment.newInstance("Product 1", 50.0)
    replace(R.id.container, fragment)
    addToBackStack("tag_destination")
}

// DestinationFragment
class DestinationFragment : Fragment() {

    private var name : String by argument()
    // Sample for dealing with nullable
    private var amount : Double? by nullArgument()

    companion object {
        fun newInstance(name: String, amount: Double): SecondFragment {
            return DestinationFragment().apply {
                this.name = name
                this.amount = amount
            }
        }
    }
}

```

### Activity
Unlike fragment, we have to provide the key since we dont have access to activity instance when building the intent extras.

```kotlin

//Source activity
DestinationActivity.start(context, "Product 1", 50.0)

//DestinationActivity
class DestinationActivity : AppCompatActivity() {

    private val name by extras<String>(key = KEY_NAME)

    // Second argument for default value
    private val price by extras(key = KEY_PRICE, default = 0.0)

    ....
    companion object {
        private const val KEY_NAME = "key_name"
        private const val KEY_PRICE = "key_price"
        fun start(
            context : Context,
            name : String,
            price : Double
        ) {
            val intent = Intent(context, DestinationActivity::class.java)
            intent.putExtra(KEY_NAME, name)
            intent.putExtra(KEY_PRICE, price)
            context.startActivity(intent)
        }
    }
}
```

