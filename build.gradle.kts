plugins {
    alias( libs.plugins.kotlin.jvm )
    alias( libs.plugins.kotlin.serialization )

    `java-library`
}

repositories {
    mavenLocal()
    mavenCentral()
}

group = "me.knighthat"
version = "2025.11.04"

dependencies {
    api( libs.ktor.core )
    implementation( libs.kotlinx.serialization.json )

    testImplementation( libs.bundles.junit5 )
    testRuntimeOnly( libs.junit.platform )
    testImplementation( libs.kotlin.reflect )

    testImplementation( libs.bundles.ktor )
    testImplementation( libs.okhttp3.logging.interceptor )

    compileOnly( libs.lombok )
    annotationProcessor( libs.lombok )
    testCompileOnly( libs.lombok )
    testAnnotationProcessor( libs.lombok )

    compileOnly( libs.jetbrains.annotations )
    annotationProcessor( libs.jetbrains.annotations )
    testCompileOnly( libs.jetbrains.annotations )
    testAnnotationProcessor( libs.jetbrains.annotations )
}

tasks.test {
    useJUnitPlatform()
}
