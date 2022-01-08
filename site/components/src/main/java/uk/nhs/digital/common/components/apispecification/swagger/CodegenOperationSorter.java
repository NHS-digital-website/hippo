package uk.nhs.digital.common.components.apispecification.swagger;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import io.swagger.codegen.v3.CodegenOperation;
import io.swagger.v3.oas.models.OpenAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CodegenOperationSorter {
    private static Logger log = LoggerFactory.getLogger(CodegenOperationSorter.class);
    private OpenAPI openApi;
    private static final String OPERATION_ORDER_EXTENSION = "operation-order";

    CodegenOperationSorter(OpenAPI openApi) {
        this.openApi = openApi;
    }

    public LinkedHashMap<String, List<CodegenOperation>> orderedByDeclaredTags(final Map<String, List<CodegenOperation>> operationsByTags) {
        try {
            final Optional<List<Map<String, Object>>> orderedOperationGroups = Optional.ofNullable(openApi.getExtensions())
                .map(openApiExtensions -> openApiExtensions.get("x-spec-publication"))
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .map(xSpecPublicationExtensions -> xSpecPublicationExtensions.get(OPERATION_ORDER_EXTENSION))
                .filter(List.class::isInstance)
                .map(groups -> (List<Map<String, Object>>) groups);

            if (!orderedOperationGroups.isPresent()) {
                //no-op
                return new LinkedHashMap<>(operationsByTags);
            }

            final List<CodegenOperation> flattenedOpList = operationsByTags.values().stream().flatMap(Collection::stream).collect(toList());
            final LinkedHashMap<String, List<CodegenOperation>> orderedOperationsByTags = new LinkedHashMap<>(operationsByTags.size());
            for (Map<String, Object> group : orderedOperationGroups.orElse(emptyList())) {
                final String groupName = Optional.ofNullable(group.get("group")).map(String.class::cast).orElse("Default");
                final List<Map<String,String>> operations = (List<Map<String,String>>) group.get("operations");
                final List<CodegenOperation> operationsToAdd = getOperationsToAdd(flattenedOpList, operations);
                orderedOperationsByTags.put(groupName, operationsToAdd);
            }
            return orderedOperationsByTags;
        } catch (Exception e) {
            log.error("Could not parse custom order", e);
            return new LinkedHashMap<>(operationsByTags);
        }

    }

    private List<CodegenOperation> getOperationsToAdd(List<CodegenOperation> flattenedOpList, List<Map<String, String>> operations) {
        final List<CodegenOperation> operationsToAdd = new ArrayList<>();

        for (Map<String, String> opReference : operations) {
            String path = opReference.get("path");
            String httpMethod = opReference.get("method");
            CodegenOperation operation = flattenedOpList.stream()
                .filter(op -> op.getPath().equals(path) && op.getHttpMethod().equals(httpMethod))
                .findAny().orElse(null);
            if (operation == null) {
                throw new RuntimeException("Could not find operation that matches reference: " + httpMethod + " " + path);
            }
            operationsToAdd.add(operation);
        }
        return operationsToAdd;
    }
}