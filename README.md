[![](https://jitpack.io/v/FutureMind/prefs-delegates.svg)](https://jitpack.io/#FutureMind/prefs-delegates)

# Prefs delegates
Yet one more library leveraging kotlin magic for elegant shared prefs handling. With some extra rx sugar on top.

## Features

- Handles all primitive types that Shared Preferences do.
- ...but adds hassle-free `enum` serialization.
- ...and any arbitrary `Serializable` structure via `moshi` json serializer.
- Takes care of proper handling of nullable and default values.
- Provides `RxJava` observability.

## Usage

### Basic usage

You can use the delegates directly like you would use a regular variable:

```kotlin
var someFlag by prefs.boolean("some_flag", false)

textView.text = "The flag is currently: $someFlag" //loaded from shared preferences

someFlag = true //saved to shared preferences
```

**The magic part is that every time you read or write the variable, it's loaded from shared prefs or saved to them.**

### Nullability and default values

```kotlin
var booleanImplicit by prefs.boolean("key1", false)
var booleanExplicit: Boolean by prefs.boolean("key2", false)
var booleanNullableImplicit by prefs.boolean("key3")
var booleanNullableExplicit: Boolean? by prefs.boolean("key4")
var booleanNullableExplicitDefault: Boolean? by prefs.boolean("key5", false)
```

### Observable

```kotlin
val someObservableInt = prefs.observableInt("some_observable_int")

someObservableInt.observable().subscribe {
    //will return the current value from shared prefs upon subscription
    //and then while it's subscribed, every value subsequently written to it.
}

//for the observable to work you need to save via the same instance
someObservableInt.save(21)
```

## Supported classes

* `Boolean`
* `Int`
* `Long`
* `Float`
* `Double`
* `String`
* `StringSet`
* `Enum` (saved as a string name of the enum)
* and other classes that can be serialized to json via `moshi`

## Installation

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