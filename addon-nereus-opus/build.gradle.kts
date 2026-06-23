import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.ow2.asm:asm:9.7.1")
        classpath("org.ow2.asm:asm-commons:9.7.1")
    }
}

plugins {
    java
    id("org.jetbrains.kotlin.jvm")
}

val recoveredCompileClasses = layout.buildDirectory.dir("recovered-compile-classes")
fun relocateTabooLibReference(bytes: ByteArray): ByteArray {
    val reader = org.objectweb.asm.ClassReader(bytes)
    val writer = org.objectweb.asm.ClassWriter(0)
    val remapper = object : org.objectweb.asm.commons.Remapper() {
        override fun map(internalName: String): String {
            return if (internalName == "taboolib" || internalName.startsWith("taboolib/")) {
                "cc/polarastrum/aiyatsbus/$internalName"
            } else {
                internalName
            }
        }
    }
    reader.accept(org.objectweb.asm.commons.ClassRemapper(writer, remapper), 0)
    return writer.toByteArray()
}

fun relocateTabooLibReferences(jarFile: File) {
    val tmpFile = jarFile.resolveSibling("${jarFile.name}.relocating")
    val seen = linkedSetOf<String>()

    JarFile(jarFile).use { input ->
        JarOutputStream(BufferedOutputStream(FileOutputStream(tmpFile))).use { output ->
            val entries = input.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                if (!seen.add(entry.name)) {
                    continue
                }

                val bytes = input.getInputStream(entry).use { it.readBytes() }
                val relocated = entry.name.endsWith(".class") &&
                    !entry.name.startsWith("hamsteryds/nereusopus/library/") &&
                    !entry.name.startsWith("license/")
                val newEntry = JarEntry(entry.name).also {
                    it.comment = entry.comment
                    it.extra = entry.extra
                    it.time = entry.time
                }

                output.putNextEntry(newEntry)
                output.write(if (relocated) relocateTabooLibReference(bytes) else bytes)
                output.closeEntry()
            }
        }
    }

    Files.move(
        tmpFile.toPath(),
        jarFile.toPath(),
        StandardCopyOption.REPLACE_EXISTING,
    )
}

val kotlinRecoveredClassExcludes = providers.provider {
    val generated = fileTree(layout.projectDirectory.dir("src/main/kotlin")) {
        include("**/*.kt")
    }.files.flatMap { file ->
        val path = file.relativeTo(layout.projectDirectory.dir("src/main/kotlin").asFile)
            .invariantSeparatorsPath
            .removeSuffix(".kt")

        listOf(
            "$path.class",
            "$path${'$'}*.class",
        )
    }

    generated + listOf(
        "nereusopuscore/ListenerRegisterer\$1.class",
    )
}
val sourceRecoveredClassExcludes = providers.provider {
    val generated = listOf("src/main/kotlin", "src/main/java").flatMap { sourceRoot ->
        fileTree(layout.projectDirectory.dir(sourceRoot)) {
            include("**/*.kt")
            include("**/*.java")
        }.files.flatMap { file ->
            val path = file.relativeTo(layout.projectDirectory.dir(sourceRoot).asFile)
                .invariantSeparatorsPath
                .removeSuffix(".kt")
                .removeSuffix(".java")

            listOf(
                "$path.class",
                "$path${'$'}*.class",
            )
        }
    }

    generated + listOf(
        "nereusopuscore/ListenerRegisterer\$1.class",
    )
}
val sourceRecoveredSourceExcludes = providers.provider {
    val generated = listOf("src/main/kotlin", "src/main/java").flatMap { sourceRoot ->
        fileTree(layout.projectDirectory.dir(sourceRoot)) {
            include("**/*.kt")
            include("**/*.java")
        }.files.map { file ->
            file.relativeTo(layout.projectDirectory.dir(sourceRoot).asFile)
                .invariantSeparatorsPath
                .removeSuffix(".kt")
                .removeSuffix(".java") + ".java"
        }
    }

    generated + listOf(
        "nereusopuscore/ListenerRegisterer\$1.java",
    )
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.tabooproject.org/repository/releases/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

tasks.register<Sync>("prepareRecoveredCompileClasses") {
    from(layout.projectDirectory.dir("src/main/recovered-classes")) {
        exclude("kotlin/**")
        exclude("META-INF/**")
        exclude("module-info.class")
        exclude(kotlinRecoveredClassExcludes.get())
    }
    into(recoveredCompileClasses)
}

dependencies {
    compileOnly(files(recoveredCompileClasses))
    compileOnly(files("libs/ParrotX.jar"))
    compileOnly(project(":project:common"))
    compileOnly(project(":project:common-impl"))
    compileOnly("io.izzel.taboolib:common:6.2.4-3d34097")
    compileOnly("io.izzel.taboolib:basic-configuration:6.2.4-3d34097")
    compileOnly("io.izzel.taboolib:common-util:6.2.4-3d34097")
    compileOnly("io.izzel.taboolib:bukkit-nms:6.2.4-3d34097")
    compileOnly("io.izzel.taboolib:bukkit-util:6.2.4-3d34097")
    compileOnly("io.izzel.taboolib:platform-bukkit-impl:6.2.4-3d34097")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.2-beta-r3-b")
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly(kotlin("stdlib"))
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn(tasks.named("prepareRecoveredCompileClasses"))
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xjvm-default=all", "-Xextended-compiler-checks")
    }
}

tasks.jar {
    archiveBaseName.set("NereusOpus")
    archiveVersion.set("3.3.3")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    from(layout.projectDirectory.dir("src/main/recovered-classes")) {
        exclude(sourceRecoveredClassExcludes.get())
    }
    from(sourceSets.main.get().output)
    eachFile {
        if (path == "module-info.class") {
            exclude()
        }
    }

    doLast {
        relocateTabooLibReferences(archiveFile.get().asFile)
    }
}

tasks.named<Jar>("sourcesJar") {
    archiveBaseName.set("NereusOpus")
    archiveVersion.set("3.3.3")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(layout.projectDirectory.dir("src/recovered/java")) {
        exclude(sourceRecoveredSourceExcludes.get())
    }
    from(sourceSets.main.get().allSource)
    from(layout.projectDirectory.dir("src/main/resources"))
}
