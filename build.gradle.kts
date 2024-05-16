import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "2.2.3"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "com.kalimero2.team"
version = "1.1.0"
description = "Annoys players with a message when they join the server on an outdated version."

repositories {
    maven("https://repo.viaversion.com/")
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api", "1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.viaversion","viaversion-api","4.10.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

bukkit {
    main = "com.kalimero2.team.pleaseUpdate.PleaseUpdatePlugin"
    apiVersion = "1.19"
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    authors = listOf("byquanton")
    softDepend = listOf("ViaVersion")
}

tasks {
    runServer {
        minecraftVersion("1.19.3")
    }
}
