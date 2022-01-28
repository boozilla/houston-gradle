package boozilla.houston.gradle.task;

import boozilla.houston.AdminGrpc;
import boozilla.houston.AdminService;
import boozilla.houston.RuntimeSideOuterClass;
import boozilla.houston.gradle.extension.HoustonPluginExtension;
import io.grpc.Metadata;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Getter
public class SyncAssetProto extends DefaultTask {
    @Input
    public String environment;

    @TaskAction
    public void download() throws IOException
    {
        final var houstonExt = getProject().getExtensions().findByType(HoustonPluginExtension.class);
        final var server = houstonExt.getServers().stream()
                .filter(it -> it.getName().contentEquals(environment))
                .findAny()
                .orElseThrow();

        final var metadata = new Metadata();
        metadata.put(Metadata.Key.of("x-houston-client-id", Metadata.ASCII_STRING_MARSHALLER), server.getClientId());
        metadata.put(Metadata.Key.of("x-houston-client-secret", Metadata.ASCII_STRING_MARSHALLER), server.getClientSecret());

        final var channel = NettyChannelBuilder.forAddress(server.getHostname(), server.getPort())
                .intercept(MetadataUtils.newAttachHeadersInterceptor(metadata))
                .negotiationType(NegotiationType.PLAINTEXT)
                .build();

        try
        {
            final var adminStub = AdminGrpc.newBlockingStub(channel);
            final var protobufResp = adminStub.protobuf(AdminService.AdminProtobufRequest.newBuilder()
                    .setSide(RuntimeSideOuterClass.RuntimeSide.valueOf(server.getRuntimeSide().toUpperCase()))
                    .build());

            final var downloadPath = Path.of(getProject().getProjectDir().getAbsolutePath(), houstonExt.getPath());
            if(!downloadPath.toFile().exists())
            {
                throw new RuntimeException("The directory set on 'path' does not exist");
            }

            if(houstonExt.isTruncate())
            {
                for(final var file : downloadPath.toFile().listFiles((dir, name) -> name.endsWith(".proto")))
                {
                    Files.delete(file.toPath());
                }

                System.out.println("Truncate asset proto directory");
            }

            for(final var proto : protobufResp.getListList())
            {
                final var path = Path.of(downloadPath.toString(), proto.getName() + ".proto");
                Files.writeString(path, proto.getSchema(),
                        StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);

                System.out.println("Download proto = " + path);
            }
        }
        finally
        {
            channel.shutdown();
        }
    }
}
