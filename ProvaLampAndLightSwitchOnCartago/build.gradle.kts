plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.jena:apache-jena-libs:4.5.0")
    /*implementation ("net.sourceforge.owlapi:org.semanticweb.hermit:1.4.5.519")
    implementation("edu.stanford.protege:org.protege.editor.owl.codegeneration:1.0.2")*/
    implementation ("org.slf4j:slf4j-simple:1.7.9")
    implementation("org.apache.jena:jena-arq:4.4.0")

    implementation(files("src/main/resources/cartago.jar"))
    implementation(files("src/main/resources/c4jason.jar"))
}
