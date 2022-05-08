plugins {
    `java-library`
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/aerulion/nucleus")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("net.aerulion:nucleus:2.0.1")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

group = "net.aerulion"
version = "1.4.0"

bukkit {
    name = "Shop"
    main = "net.aerulion.shop.Main"
    version = getVersion().toString()
    author = "aerulion"
    apiVersion = "1.18"
    depend = listOf("Nucleus", "Vault")
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
