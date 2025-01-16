buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.google.services)
    }
}

plugins {
    alias(libs.plugins.android.application) apply  false
    alias(libs.plugins.android.library) apply  false
    alias(libs.plugins.mapsplatform) apply false
    alias(libs.plugins.kotlin.android) apply  false
    alias(libs.plugins.compose.compiler) apply false
}
