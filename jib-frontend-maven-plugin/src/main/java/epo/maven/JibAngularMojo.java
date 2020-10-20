package epo.maven;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.cloud.tools.jib.api.CacheDirectoryCreationException;
import com.google.cloud.tools.jib.api.Containerizer;
import com.google.cloud.tools.jib.api.InvalidImageReferenceException;
import com.google.cloud.tools.jib.api.Jib;
import com.google.cloud.tools.jib.api.RegistryException;
import com.google.cloud.tools.jib.api.RegistryImage;
import com.google.cloud.tools.jib.api.buildplan.AbsoluteUnixPath;
import com.google.cloud.tools.jib.plugins.common.HelpfulSuggestions;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;


@Mojo(
    name = JibAngularMojo.GOAL_NAME,
    defaultPhase = LifecyclePhase.PACKAGE
)
public class JibAngularMojo extends JibAngularPluginConfiguration {

  static final String GOAL_NAME = "containerize";

  public void execute() throws MojoExecutionException, MojoFailureException {

    getLog().info("Containerizing frontend application \"" + project.getName() + "\"");

    // Parses 'to' into image reference
    if (to.image == null) {
      throw new MojoFailureException(
          HelpfulSuggestions.forToNotConfigured(
              "Missing target image parameter",
              "<to><image>",
              "pom.xml",
              "mvn compile jib-frontend:containerize -Dimage=<your image name>"));
    }


    try {
      buildImage();
    } catch (IOException | InvalidImageReferenceException | InterruptedException |
        RegistryException | CacheDirectoryCreationException | ExecutionException e) {
      throw new MojoExecutionException(e.getMessage());
    }
  }


  private void buildImage() throws IOException, InvalidImageReferenceException, InterruptedException,
      RegistryException, CacheDirectoryCreationException, ExecutionException {

    Jib.from(from.image)
        .addLayer(collectFiles(container.appBuildDirectory, 1), AbsoluteUnixPath.get(container.appPath))
        .addLayer(collectFiles(container.serverConfigDirectory, 1), AbsoluteUnixPath.get(container.configPath))
        .containerize(
            Containerizer.to(RegistryImage.named(to.image)
                .addCredential(to.auth.getUsername(), to.auth.getPassword())));
  }


  private List<Path> collectFiles(String dir, int depth) throws IOException {
    try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
      return stream
          .filter(file -> !Files.isDirectory(file) && !file.getFileName().startsWith("."))
          .collect(Collectors.toList());
    }
  }

}
