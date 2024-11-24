plugins {
    `java-library`
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://jitpack.io")
    maven {
        url = uri("https://maven.pkg.github.com/aerulion/erenos")
        name = "github-packages"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("net.aerulion:erenos:3.0.2:dev")
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
version = "1.5.0"

bukkit {
    name = "Shop"
    main = "net.aerulion.shop.Main"
    version = getVersion().toString()
    author = "aerulion"
    apiVersion = "1.20"
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
