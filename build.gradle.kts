plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri( "https://repo.maven.apache.org/maven2/" )
    }
}

group = "me.knighthat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly( libs.ktor.serialization.json )

    testImplementation( libs.junit5.jupiter.api )
    testImplementation( libs.junit5.jupiter.engine )
    testImplementation( libs.okhttp3.okhttp )
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