plugins {
    `java-library`
    id("de.eldoria.plugin-yml.bukkit") version "0.8.0"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
    withJavadocJar()
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven {
        url = uri("https://maven.pkg.github.com/aerulion/erenos")
        name = "github-packages"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    compileOnly("net.aerulion:erenos:3.10.0")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

group = "net.aerulion"
version = "1.7.1"

bukkit {
    name = "Shop"
    main = "net.aerulion.shop.Main"
    version = getVersion().toString()
    author = "aerulion"
    apiVersion = "1.21.10"
    depend = listOf("Erenos")
    softDepend = listOf("Multiverse-Core")
    commands {
        register("particleshop") {
            description = "Hiermit kann ein Shop erstellt werden."
        }
        register("openshop") {
            description = "Hiermit kann ein Shop für einen Spieler geöffnet werden."
        }
    }
    permissions {
        register("shop.use") {
            description = "Mit dieser Permission können Spieler den Shop benutzen."
            default = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.OP
        }
        register("shop.admin") {
            description = "Diese Permission ermöglicht es, Admin Funktionen des Shops zu nutzen."
            default = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.OP
        }
    }
}
