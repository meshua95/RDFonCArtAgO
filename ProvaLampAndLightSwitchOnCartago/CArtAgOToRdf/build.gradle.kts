plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.jena:apache-jena-libs:4.5.0")
    implementation ("org.slf4j:slf4j-simple:1.7.9")
    implementation("org.apache.jena:jena-arq:4.4.0")

    //implementation("org.jacamo:jacamo:1.1")

    implementation(files("src/main/resources/cartago.jar"))
    implementation(files("src/main/resources/c4jason.jar"))
}