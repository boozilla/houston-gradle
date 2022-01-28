package boozilla.houston.gradle;

import boozilla.houston.gradle.extension.HoustonPluginExtension;
import boozilla.houston.gradle.extension.HoustonServer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class HoustonPlugin implements Plugin<Project> {
    @Override
    public void apply(final Project project)
    {
        final var servers = project.container(HoustonServer.class,
                name -> project.getObjects().newInstance(HoustonServer.class, name));
        project.getExtensions().create("houston", HoustonPluginExtension.class, servers);
    }
}
