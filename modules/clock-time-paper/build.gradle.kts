plugins {
    java
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.pluginYml)
    alias(libs.plugins.hangar)
    alias(libs.plugins.minotaur)
}

dependencies {
    implementation(project(":clock-time-common"))
    compileOnly(libs.paper.api)
    implementation(libs.configurate.yaml)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockbukkit)
    testImplementation("org.mockito:mockito-core:5.11.0")
}

tasks.processResources {
    from(project(":clock-time-common").sourceSets.main.get().resources) {
        include("languages/**")
    }
}

bukkit {
    name = "ClockTime"
    version = project.version.toString()
    main = "io.github.beduality.clock_time.ClockTimePlugin"
    description = "Adds a quick-click chat message to clocks with the current in-game time in the player's locale."
    apiVersion = "1.20"
    author = "Luis Emidio, Block-Entity Duality Team and contributors"
    website = "https://github.com/beduality/clock-time"
    permissions {
        register("clock_time.use") {
            description = "Allows the player to right-click a clock to check the time."
            `default` = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.TRUE
        }
        register("clock_time.place") {
            description = "Allows the player to place clocks on blocks."
            `default` = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.TRUE
        }
        register("clock_time.break") {
            description = "Allows the player to break placed clocks."
            `default` = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}

tasks.jar {
    enabled = false
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    archiveBaseName.set("ClockTime")
    archiveClassifier.set("")
    archiveVersion.set(project.version.toString())
    relocate("org.spongepowered.configurate", "io.github.beduality.clock_time.libs.configurate")
    relocate("io.leangen.geantyref", "io.github.beduality.clock_time.libs.geantyref")
}

val cleanPlugins by tasks.registering {
    doLast {
        delete(
            fileTree("../server/data/plugins") {
                include("ClockTime*.jar")
            },
        )
    }
}

tasks.register<Copy>("buildmv") {
    dependsOn("clean", "shadowJar", cleanPlugins)
    from(layout.buildDirectory.dir("libs"))
    into("../server/data/plugins")
}

tasks.javadoc {
    exclude("**/infrastructure/**")
    exclude("**/ClockTimePlugin.java")
}

hangarPublish {
    publications.register("plugin") {
        version.set(project.version.toString())
        id.set("clock-time")
        val isPreRelease = project.version.toString().contains("-")
        channel.set(if (isPreRelease) "Beta" else "Release")
        changelog.set(System.getenv("RELEASE_CHANGELOG") ?: "No changelog provided.")
        apiKey.set(providers.environmentVariable("HANGAR_API_TOKEN"))

        platforms {
            paper {
                jar.set(tasks.shadowJar.flatMap { it.archiveFile })
                platformVersions.set(listOf("1.20.5", "1.20.6", "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4"))
            }
        }
    }
}

modrinth {
    token.set(providers.environmentVariable("MODRINTH_TOKEN"))
    projectId.set(System.getenv("MODRINTH_PROJECT_ID") ?: "clock-time")
    versionNumber.set(project.version.toString())
    versionName.set("ClockTime ${project.version}")

    val isPreRelease = project.version.toString().contains("-")
    versionType.set(if (isPreRelease) "beta" else "release")

    uploadFile.set(tasks.shadowJar)
    gameVersions.set(listOf("1.20.5", "1.20.6", "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4"))
    loaders.set(listOf("paper", "folia"))
    changelog.set(System.getenv("RELEASE_CHANGELOG") ?: "No changelog provided.")
}

val isDryRun = System.getenv("DRY_RUN")?.toBoolean() == true || System.getenv("MODRINTH_DRY_RUN")?.toBoolean() == true

tasks.modrinth {
    onlyIf { !isDryRun }
}

tasks.named("publishPluginPublicationToHangar") {
    onlyIf { !isDryRun }
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            groupId = "com.github.beduality"
            artifactId = "clock-time"
            version = project.version.toString()

            artifact(tasks.shadowJar)
            artifact(sourcesJar)
        }
    }
}
