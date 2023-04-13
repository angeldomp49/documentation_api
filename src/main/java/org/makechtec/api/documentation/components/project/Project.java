package org.makechtec.api.documentation.components.project;

import org.makechtec.api.documentation.components.dependency_tag.DependencyTag;
import org.makechtec.api.documentation.components.version.Version;

import java.util.List;

public record Project(String name, DependencyTag dependencyTag, List<Version> versions) {
}
