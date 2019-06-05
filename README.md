[![](https://jitpack.io/v/FutureMind/prefs-delegates.svg)](https://jitpack.io/#FutureMind/prefs-delegates)

# Prefs delegates
One more Kotlin library for better usage of Android `SharedPreferences` with optionally reactive streams. 

## Usage:
### `Shared Preferences` delegates
All `Shared Preferences` delegates has possibility to declare a nullable and non nullable delegates:
```kolin
var booleanImplicit by prefs.boolean("key1", false)
var booleanExplicit: Boolean by prefs.boolean("key2", false)
var booleanNullableImplicit by prefs.boolean("key3")
var booleanNullableExplicit: Boolean? by prefs.boolean("key4")
var booleanNullableExplicitDefault: Boolean? by prefs.boolean("key5", false)
```

### `Observable`
All extension functions for `ObservablePreference<T>`
has `defaultValue: T` argument so you can also use your own default value, eg.

```kotlin
var stringPref by prefs.string("key", "Custom default value")
```

### Example

First declare your store class:

```kotlin
class TestStore(prefs: SharedPreferences, moshi: Moshi) {

    enum class SomeEnum { DEFAULT, NICE }

    data class Person(val name: String, val age: Int)

    var booleanPref: Boolean by prefs.boolean("prefBoolean", false)
    var enumPref: SomeEnum by prefs.enum("prefEnum", SomeEnum.DEFAULT)
    var jsonPref: Person? by prefs.json("prefJson", moshi)
    val intObservablePref = prefs.observableInt("prefObservableInt")
}
```

And then you can use it in that way:

```kotlin
val store = TestStore(prefs, moshi)

val currentEnumValue = store.enumPref
if (store.booleanPref) { /* do something */ }
store.jsonPref = Person("nice guy", 24)

store.intObservablePref.observer().subscribe {
    // do something with new int value
}
val currentObservableValue = store.intObservablePref.get()
store.intObservablePref.save(25)
```

## Supported classes:

* `Boolean`
* `Int`
* `Long`
* `Float`
* `Double`
* `String`
* `StringSet`
* `Enum`
* and other classes that can be serialized to json 

## Installation:

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency with current JitPack version: [![](https://jitpack.io/v/FutureMind/prefs-delegates.svg)](https://jitpack.io/#FutureMind/prefs-delegates)

```groovy
dependencies {
    implementation 'com.github.FutureMind:prefs-delegates:0.10.0'
    
    // or if you need only one of the library modules:
    implementation 'com.github.FutureMind.prefs-delegates:preferencesdelegates:0.10.0'
    implementation 'com.github.FutureMind.prefs-delegates:rxpreferencesdelegates:0.10.0'
}
```
