plugins {
    alias(libs.plugins.spotless)
}

repositories {
    mavenCentral()
}

spotless {
    java {
        googleJavaFormat("1.22.0")
        target("**/src/*/java/**/*.java")
    }
    kotlinGradle {
        target("*.gradle.kts", "**/*.gradle.kts")
        ktlint()
    }
}

subprojects {
    apply(plugin = "java")

    group = "io.github.beduality"

    repositories {
        mavenCentral()
        maven {
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
