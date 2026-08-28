plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.0" apply false
}

task clean(type: Delete) {
    delete rootProject.buildDir
}

task exportFullProject(type: Copy) {
    description = 'Exports the complete ODIN project structure to the Windows Downloads folder.'
    group = 'distribution'

    def userHome = System.getProperty("user.home")
    def downloadsDir = file("${userHome}/Downloads/odin")

    doFirst {
        if (!downloadsDir.exists()) {
            if (!downloadsDir.mkdirs()) {
                throw new GradleException("No se pudo crear el directorio: ${downloadsDir}")
            }
        }
        if (!downloadsDir.canWrite()) {
            downloadsDir.setWritable(true, false)
        }
        println "\n>>> Iniciando exportación de proyecto completo a: ${downloadsDir}"
    }

    from rootProject.projectDir
    into downloadsDir

    exclude '**/.gradle/**'
    exclude '**/build/**'
    exclude '.idea/**'
    exclude '.git/**'
    exclude '.gitignore'
    exclude '.DS_Store'
    exclude '**/*.iml'
    exclude '.classpath'
    exclude '.project'
    exclude '.settings/**'
    exclude 'local.properties'

    doLast {
        if (!file("${downloadsDir}/settings.gradle.kts").exists()) {
            throw new GradleException("No se pudo verificar la copia del proyecto en: ${downloadsDir}")
        }
        println "\n>>> Exportación completada exitosamente en: ${downloadsDir}"
        println ">>> Estructura de directorios copiada:\n"
        println "   - app/"
        println "   - src/"
        println "   - build.gradle.kts (raíz)"
        println "   - settings.gradle.kts"
        println "   - [todos los archivos de configuración y fuentes]\n"
    }
}
