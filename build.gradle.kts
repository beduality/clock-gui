plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.pluginYml)
}

group = "io.github.beduality"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(libs.paper.api)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

bukkit {
    name = "ClockTime"
    version = project.version.toString()
    main = "io.github.beduality.clock_time.ClockTimePlugin"
    description = "Adds a 12-hour display to Minecraft's clock with a quick-click chat message!"
    apiVersion = "1.20"
    author = "Luis Emidio, Block-Entity Duality Team and contributors"
    website = "https://github.com/beduality/clock-time"
    permissions {
        register("clock_time.use") {
            description = "Allows the player to right-click a clock to check the time."
            `default` = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveBaseName.set("ClockTime")
    archiveClassifier.set("")
    archiveVersion.set(project.version.toString())
}

val cleanPlugins by tasks.registering {
    doLast {
        delete(fileTree("../server/data/plugins") {
            include("ClockTime*.jar")
        })
    }
}

tasks.register<Copy>("buildmv") {
    dependsOn("clean", "shadowJar", cleanPlugins)
    from(layout.buildDirectory.dir("libs"))
    into("../server/data/plugins")
}
