plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.vstu.energywatertracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vstu.energywatertracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Добавьте это для Room
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-Xjvm-default=all"
        )
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
}

// Конфигурация kapt
kapt {
    correctErrorTypes = true
    useBuildCache = true
    javacOptions {
        option("-Xmaxerrs", 1000)
    }
}

dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Compose
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.material:material-icons-extended")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")

    // Для Room (решает проблему с SQLite на Windows)
    implementation("androidx.sqlite:sqlite:2.4.0")
    implementation("androidx.sqlite:sqlite-framework:2.4.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    // Hilt DI
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}
//plugins {
//    id("com.android.application")
//    id("org.jetbrains.kotlin.android")
//    id("com.google.dagger.hilt.android")
//    id("kotlin-kapt")
//}
//
//android {
//    namespace = "com.vstu.energywatertracker"
//    compileSdk = 34
//
//    defaultConfig {
//        applicationId = "com.vstu.energywatertracker"
//        minSdk = 26
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables {
//            useSupportLibrary = true
//        }
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//
//    kotlinOptions {
//        jvmTarget = "17"
//    }
//
//    buildFeatures {
//        compose = true
//    }
//
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.4"
//    }
//
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
//}
//
//dependencies {
//    // Core
//    implementation("androidx.core:core-ktx:1.12.0")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
//
//    // Compose
//    implementation("androidx.activity:activity-compose:1.8.1")
//    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3:1.1.2")
//    implementation("androidx.compose.material:material-icons-extended")
//    implementation("androidx.compose.material:material:1.5.4") // Для дополнительных компонентов
//
//    // Navigation
//    implementation("androidx.navigation:navigation-compose:2.7.5")
//
//    // Room Database
//    implementation("androidx.room:room-runtime:2.6.0")
//    implementation("androidx.room:room-ktx:2.6.0")
//    kapt("androidx.room:room-compiler:2.6.0")
//
//    // Hilt DI
//    implementation("com.google.dagger:hilt-android:2.48")
//    kapt("com.google.dagger:hilt-compiler:2.48")
//    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
//
//    // CameraX (опционально, если нужно)
//    implementation("androidx.camera:camera-camera2:1.3.1")
//    implementation("androidx.camera:camera-lifecycle:1.3.1")
//    implementation("androidx.camera:camera-view:1.3.1")
//
//    // WorkManager для уведомлений
//    implementation("androidx.work:work-runtime-ktx:2.9.0")
//
//    // Coil для загрузки изображений
//    implementation("io.coil-kt:coil-compose:2.5.0")
//
//    // DataStore для настроек
//    implementation("androidx.datastore:datastore-preferences:1.0.0")
//
//    // Простая библиотека для диаграмм
//    implementation("com.github.madrapps:plot:0.1.2")
//
//    // Testing
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
//}