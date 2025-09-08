plugins {
    java
    alias( libs.plugins.kotlin.jvm )
    alias( libs.plugins.kotlin.serialization )
}

repositories {
    mavenLocal()
    mavenCentral()
}

group = "me.knighthat"
version = "0.0.1"

dependencies {
    implementation( libs.kotlinx.coroutines )
    implementation( libs.ktor.serialization.json )

    testImplementation( libs.bundles.junit5 )
    testRuntimeOnly( libs.junit.platform )

    testImplementation( kotlin( "test" ) )
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
