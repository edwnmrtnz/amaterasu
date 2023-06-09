# amaterasu
Collection of android libraries or architecture components to kickstart android codebases.

## How to import
As of now, amaterasu libraries are published via Github Package Registry and as such, it needs authentication.

1. Generate github personal token with <i>read:packages</i> permission. Just go to account -> settings -> developer settings -> personal access token -> generate.
2. Create a properties file, place it in your project's root folder, and add it to .gitignore. e.g github.properties 
3. Inside build.gradle properties.
```groovy
def githubProperties = new Properties() githubProperties.load(new FileInputStream(rootProject.file("github.properties")))
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/edwnmrtnz/amaterasu")
        credentials {
            username = githubProperties['gpr.usr'] ?: System.getenv("GPR_USER")
            password = githubProperties['gpr.key'] ?: System.getenv("GPR_API_KEY")
        }
    }
}
```
4. Import it as dependency
```
dependencies {
    implementation ‘com.github.amaterasu.androidutils:{version}’
}
```

Note: 
I'm only trying github package registry now. The setup is too verbose for public library but I think this process will change in the future.

