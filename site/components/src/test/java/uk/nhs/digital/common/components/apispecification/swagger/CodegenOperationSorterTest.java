package uk.nhs.digital.common.components.apispecification.swagger;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.nhs.digital.common.components.apispecification.swagger.OperationOrderMatcher.matchInOrder;

import com.google.common.collect.ImmutableMap;
import io.swagger.codegen.v3.CodegenOperation;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class CodegenOperationSorterTest {
    private OpenAPI openApi;
    private CodegenOperationSorter codegenOperationSorter;
    private static final Map<String, List<CodegenOperation>> operationsByTags = getOperations();

    @Before
    public void setUp() {
        this.openApi = new OpenAPI();
        this.codegenOperationSorter = new CodegenOperationSorter(openApi);
    }

    @Test
    public void returnsGroupedAndOrderedOperations_accordingToMetadataStructure() {
        // Arrange
        Map<String, Object> defaultOps = ImmutableMap.of(
            "operations", Arrays.asList(
                operationReferenceWithPathAndMethod("/some-path", "GET"),
                operationReferenceWithPathAndMethod("/some-path", "POST")
            )
        );
        Map<String, Object> groupAOps = ImmutableMap.of(
            "group", "GroupA",
            "operations", Arrays.asList(
                operationReferenceWithPathAndMethod("/some-other-path", "POST"),
                operationReferenceWithPathAndMethod("/some-other-path-with-a#suffix", "POST"),
                operationReferenceWithPathAndMethod("/some-path-in-multiple-groups", "POST")
            )
        );
        Map<String, Object> groupBOps = ImmutableMap.of(
            "group", "GroupB",
            "operations", Arrays.asList(
                operationReferenceWithPathAndMethod("/some-path-in-multiple-groups", "POST")
            )
        );

        List<Map<String, Object>> operationOrder = Arrays.asList(
            defaultOps, groupAOps, groupBOps
        );
        setOrder(operationOrder);

        LinkedHashMap<String, List<CodegenOperation>> expected = new LinkedHashMap<>();
        expected.put("Default", Arrays.asList(
            operationWithPathAndMethod("/some-path", "GET"),
            operationWithPathAndMethod("/some-path", "POST")
        ));
        expected.put("GroupA", Arrays.asList(
            operationWithPathAndMethod("/some-other-path", "POST"),
            operationWithPathAndMethod("/some-other-path-with-a#suffix", "POST"),
            operationWithPathAndMethod("/some-path-in-multiple-groups", "POST")
        ));
        expected.put("GroupB", Arrays.asList(
            operationWithPathAndMethod("/some-path-in-multiple-groups", "POST")
        ));

        // Act
        LinkedHashMap<String, List<CodegenOperation>> actual = this.codegenOperationSorter.orderedByDeclaredTags(operationsByTags);

        // Assert
        assertThat(actual, matchInOrder(expected));

    }


    @Test
    public void returnsOperationsAccordingToDefaultOrdering_whenNoExtensionSupplied() {
        // Arrange
        LinkedHashMap<String, List<CodegenOperation>> expected = new LinkedHashMap<>();
        expected.put("Default", Arrays.asList(
            operationWithPathAndMethod("/some-path", "GET"),
            operationWithPathAndMethod("/some-path", "POST"),
            operationWithPathAndMethod("/some-other-path", "POST"),
            operationWithPathAndMethod("/some-other-path-with-a#suffix", "POST"),
            operationWithPathAndMethod("/some-path-in-multiple-groups", "POST")
        ));

        // Act
        this.codegenOperationSorter = new CodegenOperationSorter(openApi);
        LinkedHashMap<String, List<CodegenOperation>> actual = this.codegenOperationSorter.orderedByDeclaredTags(operationsByTags);

        // Assert
        assertThat(actual, matchInOrder(expected));
    }

    @Test
    public void returnsOperationsAccordingToDefaultOrdering_whenMalformedExtensionSupplied() {
        // Arrange
        Map<String, Object> malformedOps = ImmutableMap.of(
            "operations", Arrays.asList(
                operationReferenceWithPathAndMethod("/some-path-that-does-not-exist", "GET")
            )
        );
        setOrder(Arrays.asList(malformedOps));
        LinkedHashMap<String, List<CodegenOperation>> expected = new LinkedHashMap<>();
        expected.put("Default", Arrays.asList(
            operationWithPathAndMethod("/some-path", "GET"),
            operationWithPathAndMethod("/some-path", "POST"),
            operationWithPathAndMethod("/some-other-path", "POST"),
            operationWithPathAndMethod("/some-other-path-with-a#suffix", "POST"),
            operationWithPathAndMethod("/some-path-in-multiple-groups", "POST")
        ));

        // Act
        this.codegenOperationSorter = new CodegenOperationSorter(openApi);
        LinkedHashMap<String, List<CodegenOperation>> actual = this.codegenOperationSorter.orderedByDeclaredTags(operationsByTags);

        // Assert
        assertThat(actual, matchInOrder(expected));
    }


    private static final Map<String, List<CodegenOperation>> getOperations() {
        return ImmutableMap.of(
            // grouping does not matter here as grouping is defined by OAS extension
            "Default", Arrays.asList(
                operationWithPathAndMethod("/some-path", "GET"),
                operationWithPathAndMethod("/some-path", "POST"),
                operationWithPathAndMethod("/some-other-path", "POST"),
                operationWithPathAndMethod("/some-other-path-with-a#suffix", "POST"),
                operationWithPathAndMethod("/some-path-in-multiple-groups", "POST")
            )
        );
    }

    private static CodegenOperation operationWithPathAndMethod(String path, String httpMethod) {
        CodegenOperation op = new CodegenOperation();
        op.path = path;
        op.httpMethod = httpMethod;
        return op;
    }

    private Map<String, String> operationReferenceWithPathAndMethod(String path, String httpMethod) {
        return ImmutableMap.of(
            "method", httpMethod,
            "path", path
        );
    }

    private void setOrder(List<Map<String, Object>> operationOrder) {
        openApi.setExtensions(
            ImmutableMap.of("x-spec-publication",
                ImmutableMap.of("operation-order", operationOrder)
            )
        );
    }

}
