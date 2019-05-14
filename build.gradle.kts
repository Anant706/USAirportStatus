plugins {
  java
  jacoco
}       

repositories {
	mavenCentral()
}

dependencies {
  testCompile("org.junit.jupiter:junit-jupiter-api:5.2.0")
  testRuntime("org.junit.jupiter:junit-jupiter-engine:5.2.0")
  testRuntime("org.junit.platform:junit-platform-console:1.2.0")
  testCompile("org.mockito:mockito-core:1.+")
  implementation("com.google.code.gson:gson:2.8.5") 
}
 
sourceSets {
  main {
    java.srcDirs("AirportStatus/src")
    resources.srcDirs("AirportStatus/src")
  }
  test {
    java.srcDirs("AirportStatus/test")
  }
}

val test by tasks.getting(Test::class) {
	useJUnitPlatform {}
}

tasks {
  val treatWarningsAsError =
    listOf("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")

  getByName<JavaCompile>("compileJava") {
    options.compilerArgs = treatWarningsAsError      
  }

  getByName<JavaCompile>("compileTestJava") {
    options.compilerArgs = treatWarningsAsError
  }

  getByName<JacocoReport>("jacocoTestReport") {
    afterEvaluate {
      getClassDirectories().setFrom(files(classDirectories.files.map {
        fileTree(it) { exclude("**/AirportConsoleApplication.class") }
      }))
    }
  }
}

task("runnoerror", JavaExec::class) {
   main = "airports.AirportConsoleApplication"
   classpath = sourceSets["main"].runtimeClasspath
   args(listOf("AirportStatus/src/resources/airport_list.txt"))
}
task("runwitherror", JavaExec::class) {
   main = "airports.AirportConsoleApplication"
   classpath = sourceSets["main"].runtimeClasspath
   args(listOf("AirportStatus/src/resources/airport_list_with_error.txt"))
}

task("run") {
   dependsOn("runnoerror", "runwitherror")
}

defaultTasks("clean", "test", "jacocoTestReport")
