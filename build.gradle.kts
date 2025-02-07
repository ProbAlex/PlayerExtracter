plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1" // Apply Shadow plugin correctly
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.eclipse.org/content/repositories/ee4j/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.sparkjava:spark-core:2.9.4")
    implementation("org.json:json:20210307")


    implementation("jakarta.websocket:jakarta.websocket-api:2.1.0")
    implementation("org.eclipse.jetty.websocket:websocket-api:9.4.43.v20210629")  // WebSocket API
    implementation("org.eclipse.jetty.websocket:websocket-server:9.4.43.v20210629")  // WebSocket Server
    implementation("org.eclipse.jetty:jetty-server:9.4.43.v20210629")  // Jetty Server
    implementation("org.eclipse.jetty:jetty-servlet:9.4.43.v20210629")  // Jetty Servlet support

    tasks.jar {
        manifest {
            attributes["Main-Class"] = "org.example.Main"
        }
    }

    tasks.shadowJar {
        archiveClassifier.set("") // Ensures the shadow JAR replaces the normal JAR
    }

    tasks.test {
        useJUnitPlatform()
    }
}