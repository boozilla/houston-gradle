# Houston Asset Server Gradle plugin
It is a helper that helps achieve synchronization automation through the build script of the schema serialized in protobuf format from the data of the excel file uploaded to ‘Houston Asset Server’ through this plug-in.

## Configuration
First, an example using all the setting items available in this plugin.

```
houston {
    servers {
        dev {
            runtimeSide 'CLIENT'
            hostname 'localhost'
            port 8080
            clientId 'rh4NkSh6exxsFFbj9WMv8fcEyNRxV86P'
            clientSecret 'x9y5HYTz7xaxmRCSb3bJtQXryKU7PTxWdauVBsxfHKWMHpqLVWuRYevpbFxpNe8P'
            negotiationType NegotiationType.PLAINTEXT
        }
        
        prod {
            runtimeSide 'CLIENT'
            hostname 'prod-houston.hostname.com'
            port 8080
            clientId 'rh4NkSh6exxsFFbj9WMv8fcEyNRxV86P'
            clientSecret 'x9y5HYTz7xaxmRCSb3bJtQXryKU7PTxWdauVBsxfHKWMHpqLVWuRYevpbFxpNe8P'
            negotiationType NegotiationType.TLS
        }
    }

    path 'src/main/proto/asset'
    truncate true
}
```

Below is a detailed description of each setting.

| Name            | Description                                                                                                           |
|-----------------|-----------------------------------------------------------------------------------------------------------------------|
| runtimeSide     | The scope value of the asset you want to sync. The values `CLIENT` and `SERVER` are allowed.                          |
| hostname        | The hostname of the Houston Asset Server you want to synchronize.                                                     |
| port            | port of Houston Asset Server.                                                                                         |
| clientId        | This is the Client ID with Admin privileges set in Houston Asset Server.                                              |
| clientSecret    | Client Secret with Admin privileges                                                                                   |
| path            | This is where the synced proto schema files are stored. `Base path` is `Project root`                                 |
| truncate        | Whether to always empty and save .proto files to the location where proto schema files are stored. Default is `false` |
| negotiationType | Server transport negotiation type. `io.grpc.netty.NegotiationType`                                                    |

## Task definition
First, import the `SyncAssetProto` class in the build.gradle file.

```
import boozilla.houston.gradle.task.SyncAssetProto
```

Write a task that synchronizes for each environment defined in the `houston { servers {} }` syntax.

```
// Synchronize task with development environment

task syncAssetProtoDev(type: SyncAssetProto) {
    environment = 'dev'
}
```
```
// Synchronize task with Production environment

task syncAssetProtoDev(type: SyncAssetProto) {
    environment = 'prod'
}
```

Now, when you run the task in the desired environment, the Houston Asset Server and protobuf schema synchronization work.