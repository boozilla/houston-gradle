package boozilla.houston.gradle;

import boozilla.houston.gradle.extension.HoustonPluginExtension;
import boozilla.houston.gradle.extension.HoustonServer;
import boozilla.houston.gradle.task.SyncAssetProto;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class HoustonPlugin implements Plugin<Project> {
    @Override
    public void apply(final Project project)
    {
        final var servers = project.container(HoustonServer.class,
                name -> project.getObjects().newInstance(HoustonServer.class, name));
        project.getExtensions().create("houston", HoustonPluginExtension.class, servers);
        project.afterEvaluate(p -> {
            for(final var server : servers)
            {
                final var name = server.getName();
                final var task = "syncAssetProto" + Character.toUpperCase(name.charAt(0)) + name.substring(1);

                p.getTasks().create(task, SyncAssetProto.class, syncTask -> syncTask.environment = name);
            }
        });
    }
}
