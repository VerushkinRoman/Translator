import org.gradle.api.JavaVersion

object Config {
    const val compileSdk = 31
    const val minSdk = 23
    const val targetSdk = 31
    val javaVersion = JavaVersion.VERSION_1_8
}

object Modules {
    const val models = ":models"
    const val data = ":data"
    const val network = ":network"
    const val base = ":base"
    const val description = ":description"
    const val adapter = ":adapter"
    const val history = ":history"
    const val main = ":main"
}

object Versions {
    const val retrofit = "2.9.0"
    const val okhttpInterceptor = "5.0.0-alpha.2"

    const val coroutines = "1.5.2"

    const val room = "2.3.0"

    const val koin = "3.1.3"

    const val coil = "1.4.0"

    const val ktx = "1.7.0"
    const val appcompat = "1.3.1"
    const val material = "1.4.0"
    const val constraint = "2.1.1"
    const val jUnit = "4.12"
    const val testJUnit = "1.1.3"
    const val espresso = "3.4.0"

    const val binding = "1.5.0-beta01"
}

object Android {
    const val core = "androidx.core:core-ktx:${Versions.ktx}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraint = "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
    const val jUnit = "junit:junit:${Versions.jUnit}"
    const val testJUnit = "androidx.test.ext:junit:${Versions.testJUnit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}

object Retrofit {
    const val core = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val converterGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val okhttpInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okhttpInterceptor}"
}

object Coroutines {
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
}

object Room {
    const val runtime = "androidx.room:room-runtime:${Versions.room}"
    const val compiler = "androidx.room:room-compiler:${Versions.room}"
}

object Koin {
    const val core = "io.insert-koin:koin-core:${Versions.koin}"
    const val android = "io.insert-koin:koin-android:${Versions.koin}"
    const val compat = "io.insert-koin:koin-android-compat:${Versions.koin}"
}

object Coil {
    const val core = "io.coil-kt:coil:${Versions.coil}"
}

object BindingDelegate{
    const val core = "com.github.kirich1409:viewbindingpropertydelegate-noreflection:${Versions.binding}"
}