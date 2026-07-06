plugins {
    id("fabric-loom") version "1.6-SNAPSHOT"
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.fabricmc.net/")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.6")
    mappings("net.fabricmc:yarn:1.20.6+build.3:v2")
    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.99.3+1.20.6")

    // Compile-time dependency on common domain logic
    implementation(project(":clock-time-common"))
    implementation("net.kyori:adventure-text-serializer-gson:4.17.0")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }

    from(project(":clock-time-common").sourceSets.main.get().resources) {
        include("languages/**")
    }
}
