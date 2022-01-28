package boozilla.houston.gradle.extension;

import lombok.Data;
import org.gradle.api.NamedDomainObjectContainer;

@Data
public class HoustonPluginExtension {
    private final NamedDomainObjectContainer<HoustonServer> servers;

    private String path;
    private boolean truncate = false;

    public HoustonPluginExtension(final NamedDomainObjectContainer<HoustonServer> servers)
    {
        this.servers = servers;
    }
}
