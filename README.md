[houston Asset Server](https://github.com/boozilla/houston) Gradle Plugin is a convenient tool for automating synchronization through the build script. It facilitates the extraction of schema data in protobuf format from an Excel file uploaded to the 'houston Asset Server.'

## Getting Started

### Gradle Plugin Configuration

Add the houston Gradle Plugin to your project by including the following code in your `build.gradle` file:

```groovy
plugins {
    id 'io.github.boozilla.houston' version '1.0.5'
}
```

### Plugin Configuration

Configure the houston plugin in your `build.gradle` to suit your project's needs. Below is an example demonstrating different settings for 'dev' and 'prod' environments. The example utilizes all available settings within the plugin:

```groovy
houston {
    dev {
        scope = 'SERVER'
        url = 'dev-houston.hostname.com:443'
        tls = true
        path = 'src/main/proto/asset'
        token = '''
            [REDACTED]
            '''
        verifier = project(':your-verifier-module')
    }
}
```

Below is a detailed explanation of each setting:

| Name  | Description                                                                                                |
|-------|------------------------------------------------------------------------------------------------------------|
| scope | The scope value of the asset you want to sync. The values `CLIENT` and `SERVER` are allowed.               |
| url   | The hostname of the houston Asset Server you want to synchronize.                                          |
| token | This is the token with Admin privileges set in the houston Asset Server.                                     |
| path  | This is where the synced proto schema files are stored. The default base path is `'src/main/proto/asset'`. |
| tls   | Whether to use TLS to communicate with the server.                                                         |

### Token Generation

To generate the token with Admin privileges, please refer to the [Generate admin token documentation](https://github.com/boozilla/houston/wiki/Generate-admin-token) for detailed instructions.

### Usage

Once configured, you can utilize the plugin to automate synchronization tasks. Specific tasks and commands will depend on your project's requirements and the plugin's capabilities.

Please refer to the plugin documentation for more detailed instructions and examples.

### RunVerifier Task

The `RunVerifier` task allows you to execute an implementation of `AssetSheetConstraints` through the houston server. The corresponding code for `AssetSheetConstraints` can be found [here](https://github.com/boozilla/houston/blob/main/api/src/main/java/boozilla/houston/asset/constraints/AssetSheetConstraints.java).

#### Usage

To run the verifier task, you can include it in your Gradle build script:

```shell
./gradlew [ENVIRONMENT_NAME].runVerifier
```

Execute the `dev` environment RunVerifier Task:

```shell
./gradlew dev.runVerifier
```

This task provides a way to test and execute the constraints defined in `AssetSheetConstraints` using the houston server.

### SyncSchema Task

The `SyncSchema` task is designed to synchronize table schema proto files uploaded to the houston server.

To execute the synchronization task, use the following Gradle command:

```shell
./gradlew [ENVIRONMENT_NAME].syncSchema
```

Execute the `prod` environment syncSchema Task:

```shell
./gradlew prod.syncSchema
```

This task ensures that the table schema proto files uploaded to the houston server are synchronized with your local project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
