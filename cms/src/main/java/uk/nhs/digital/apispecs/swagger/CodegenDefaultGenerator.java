package uk.nhs.digital.apispecs.swagger;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import io.swagger.codegen.v3.CodegenOperation;
import io.swagger.codegen.v3.DefaultGenerator;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.*;

/**
 * Some producer teams require ordering their endpoints in a non-lexographical order.
 * This requires a custom extension of the DefaultGenerator provided by CodeGen.
 *
 * This class CodegenDefaultGenerator extends DefaultGenerator class and sorts CodegenOperations before returning them.
 * See the method orderedByDeclaredTags.
 */
public class CodegenDefaultGenerator extends DefaultGenerator {

    @Override
    public Map<String, List<CodegenOperation>> processPaths(Paths paths) {
        Map<String, List<CodegenOperation>> ops = super.processPaths(paths);
        return orderedByDeclaredTags(ops);
    }

    private Map<String, List<CodegenOperation>> orderedByDeclaredTags(final Map<String, List<CodegenOperation>> operationsByTags) {
        final List<String> declaredTagNames = operationsByTags.containsKey("Default")
            ? new ArrayList<>(singletonList("Default"))
            : new ArrayList<>(operationsByTags.size());

        declaredTagNames.addAll(
            Optional.ofNullable(openAPI.getTags())
                .orElse(Collections.emptyList())
                .stream()
                .map(Tag::getName)
                .map(config::sanitizeTag)
                .collect(toList())
        );

        final Map<String, List<CodegenOperation>> orderedOperationsByTags = new LinkedHashMap<>(operationsByTags.size());

        declaredTagNames.stream()
            .filter(operationsByTags::containsKey)
            .forEachOrdered(tagName -> orderedOperationsByTags.put(tagName, operationsByTags.get(tagName)));

        return orderedOperationsByTags;
    }

}
