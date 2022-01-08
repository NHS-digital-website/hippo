package uk.nhs.digital.common.components.apispecification.swagger;

import io.swagger.codegen.v3.CodegenOperation;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class OperationOrderMatcher extends BaseMatcher<LinkedHashMap<String, List<CodegenOperation>>> {
    private final LinkedHashMap<String, List<CodegenOperation>> operationOrder;

    OperationOrderMatcher(LinkedHashMap<String, List<CodegenOperation>> operationOrder) {
        this.operationOrder = operationOrder;
    }

    @Override
    public boolean matches(Object o) {
        if (!(o instanceof LinkedHashMap<?,?>)) {
            return false;
        }
        List theseEntries = new ArrayList(operationOrder.entrySet());
        List thoseEntries = new ArrayList(((LinkedHashMap) o).entrySet());
        return theseEntries.equals(thoseEntries);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(this.operationOrder);
        return;
    }

    public static OperationOrderMatcher matchInOrder(LinkedHashMap<String, List<CodegenOperation>> expected) {
        return new OperationOrderMatcher(expected);
    }
}
