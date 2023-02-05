plugins {
    kotlin("js") version "1.8.10"
}

group = "me.admin"
version = "dev"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.206-kotlin-1.5.10")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.206-kotlin-1.5.10")
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {

        }
    }
}