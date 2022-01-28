package boozilla.houston.gradle.extension;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.inject.Inject;

@Data
@AllArgsConstructor
public class HoustonServer {
    private String name;
    private String runtimeSide;
    private String hostname;
    private int port;
    private String clientId;
    private String clientSecret;

    @Inject
    public HoustonServer(final String name)
    {
        this.name = name;
    }
}
