package uk.nhs.digital.common.components.apispecification.swagger;

import io.swagger.codegen.v3.CodegenOperation;
import io.swagger.codegen.v3.DefaultGenerator;
import io.swagger.v3.oas.models.Paths;

import java.util.*;

/**
 * Some producer teams require ordering their endpoints in a non-lexographical order.
 * This requires a custom extension of the DefaultGenerator provided by CodeGen.
 *
 * This class CodegenDefaultGenerator extends DefaultGenerator class and overrides processPaths to return
 * a sorted map of groups (keys) and lists of grouped operations (values).
 * See the method orderedByDeclaredTags.
 */
public class CodegenDefaultGenerator extends DefaultGenerator {
    private static CodegenOperationSorter codegenOperationSorter;

    CodegenDefaultGenerator(CodegenOperationSorter codegenOperationSorter) {
        super();
        this.codegenOperationSorter = codegenOperationSorter;
    }

    @Override
    public Map<String, List<CodegenOperation>> processPaths(Paths paths) {
        Map<String, List<CodegenOperation>> ops = super.processPaths(paths);
        return codegenOperationSorter.orderedByDeclaredTags(ops);
    }
}
