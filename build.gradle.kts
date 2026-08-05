plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "2.0.0-SNAPSHOT"
    id("de.eldoria.plugin-yml.bukkit") version "0.9.0"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven {
        url = uri("https://maven.pkg.github.com/aerulion/erenos")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: "aerulion"
            password = project.findProperty("gpr.key") as String? ?: System.getenv("token_erenos")
        }
    }
}

dependencies {
    paperweight.paperDevBundle("26.2.build.+")
    compileOnly("net.aerulion:erenos:4.+")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(25)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

group = "net.aerulion"
version = "1.7.6"

bukkit {
    name = "Shop"
    main = "net.aerulion.shop.Main"
    version = getVersion().toString()
    author = "aerulion"
    apiVersion = "26.2"
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
