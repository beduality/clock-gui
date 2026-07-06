plugins {
    id("fabric-loom") version "1.6-SNAPSHOT"
    id("com.gradleup.shadow") version "8.3.0"
    alias(libs.plugins.minotaur)
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.fabricmc.net/")
    }
}

val shadowImpl by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.6")
    mappings("net.fabricmc:yarn:1.20.6+build.3:v2")
    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.99.3+1.20.6")

    // Compile-time dependency
    implementation(project(":clock-time-common"))
    implementation("net.kyori:adventure-text-serializer-gson:4.17.0")

    // Add to shadow configuration to shade/merge at build time
    shadowImpl(project(":clock-time-common")) {
        isTransitive = true
    }
    shadowImpl("net.kyori:adventure-api:4.17.0") {
        isTransitive = true
    }
    shadowImpl("net.kyori:adventure-text-minimessage:4.17.0") {
        isTransitive = true
    }
    shadowImpl("net.kyori:adventure-text-serializer-gson:4.17.0") {
        isTransitive = true
    }
}

tasks.shadowJar {
    configurations = listOf(shadowImpl)
    archiveClassifier.set("shadow")

    // Do not include Minecraft or Loom's internal classes if transitive
    exclude("net/minecraft/**")
    exclude("com/mojang/**")
}

tasks.remapJar {
    inputFile.set(tasks.shadowJar.flatMap { it.archiveFile })
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

modrinth {
    token.set(providers.environmentVariable("MODRINTH_TOKEN"))
    projectId.set(System.getenv("MODRINTH_PROJECT_ID") ?: "clock-time")
    versionNumber.set(project.version.toString())
    versionName.set("ClockTime Fabric ${project.version}")

    val isPreRelease = project.version.toString().contains("-")
    versionType.set(if (isPreRelease) "beta" else "release")

    // Upload the final remapped mod jar for Fabric
    uploadFile.set(tasks.remapJar)
    gameVersions.set(listOf("1.20.5", "1.20.6", "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4"))
    loaders.set(listOf("fabric", "quilt"))
    changelog.set(System.getenv("RELEASE_CHANGELOG") ?: "No changelog provided.")
}

val isDryRun = System.getenv("DRY_RUN")?.toBoolean() == true || System.getenv("MODRINTH_DRY_RUN")?.toBoolean() == true

tasks.modrinth {
    onlyIf { !isDryRun }
}
