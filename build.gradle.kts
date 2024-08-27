plugins {
    id("java")
}

group = "com.qt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // servlet
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    // 日志
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.slf4j:slf4j-api:2.0.7")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}