# Prefs delegates
One more Kotlin library for better usage of Android `SharedPreferences` with optionally reactive streams. 

## Usage:

First define your store class:

```kotlin
class TestStore(prefs: SharedPreferences, moshi: Moshi) {

    companion object {
        private const val PREF_BOOLEAN = "PREF_BOOLEAN"
        private const val PREF_ENUM = "PREF_ENUM"
        private const val PREF_JSON = "PREF_JSON"
        private const val PREF_OBSERVABLE_INT = "PREF_OBSERVABLE_INT"
    }

    enum class SomeEnum { DEFAULT, NICE }

    data class Person(val name: String, val age: Int)

    var booleanPref: Boolean by prefs.boolean(PREF_BOOLEAN)
    var enumPref: SomeEnum by prefs.enum(PREF_ENUM, SomeEnum.DEFAULT)
    var jsonPref: Person by prefs.json(PREF_JSON, moshi, Person("stranger", 23))
    val intObservablePref = prefs.observableInt(PREF_OBSERVABLE_INT)
}
```

And then you can use it that way:

```kotlin
val store = TestStore(prefs, moshi)

val currentEnumValue = store.enumPref
if (store.booleanPref) { /* do something */ }
store.jsonPref = Person("nice guy", 24)

store.intObservablePref.observer().subscribe {
    // new int received
}
val currentObservableValue = store.intObservablePref.get()
store.intObservablePref.save(25)
```

All shared preferences delegates and extension functions for `ObservablePreference<T>`
has `defaultValue: T` argument so you can also use your own default value, eg.

```kotlin
var stringPref by prefs.string(PREF_STRING, "Custom default value")
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

Add the dependency:

```groovy
dependencies {
    implementation 'com.github.FutureMind:prefs-delegates:0.9'
}
```
