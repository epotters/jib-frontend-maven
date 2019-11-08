package epo.maven;


import java.util.Collections;
import java.util.List;

import com.google.cloud.tools.jib.maven.JibPluginConfiguration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;


abstract class JibAngularPluginConfiguration extends AbstractMojo {

  private static final String BASE_IMAGE = "nginx:alpine";
  private static final String CONTAINER_APP_PATH = "/usr/share/nginx/html";
  private static final String CONTAINER_CONFIG_PATH = "/etc/nginx/conf.d";


  @Parameter(defaultValue = "${project}", readonly = true)
  MavenProject project;

  @Parameter
  FromConfiguration from = new FromConfiguration();

  @Parameter
  ToConfiguration to = new ToConfiguration();

  @Parameter
  AngularContainerParameters container = new AngularContainerParameters();


  /**
   * Configuration for {@code from} parameter
   */
  public static class FromConfiguration {

    @Parameter
    String image;

    @Parameter
    JibPluginConfiguration.FromAuthConfiguration auth = new JibPluginConfiguration.FromAuthConfiguration();

    public FromConfiguration() {
    }
  }

  public static class ToConfiguration {

    @Parameter
    String image;

    @Parameter
    List<String> tags = Collections.emptyList();

    @Parameter
    JibPluginConfiguration.ToAuthConfiguration auth = new JibPluginConfiguration.ToAuthConfiguration();

    public ToConfiguration() {
    }

    public void set(String image) {
      this.image = image;
    }
  }


  /**
   * Configuration for {@code container} parameter
   */
  public static class AngularContainerParameters {

    //    @Parameter(defaultValue = CONTAINER_APP_PATH)
    String appPath = CONTAINER_APP_PATH;

    //    @Parameter(defaultValue = CONTAINER_CONFIG_PATH)
    String configPath = CONTAINER_CONFIG_PATH;

    @Parameter(required = true)
    String appBuildDirectory;

    @Parameter(required = true)
    String serverConfigDirectory;
  }

}
