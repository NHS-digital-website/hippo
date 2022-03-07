package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import static java.util.Collections.emptyList;

import com.github.jknack.handlebars.Context;

import java.util.List;
import java.util.Optional;

public class ContextModelsStack {

    private final List<?> modelStack;

    ContextModelsStack(final List<?> modelStack) {
        this.modelStack = Optional.ofNullable(modelStack).orElse(emptyList());
    }

    public Optional<Object> currentModel() {
        return Optional.ofNullable(modelStack.isEmpty() ? null : modelStack.get(0));
    }

    public Optional<Object> parentModel() {
        return Optional.ofNullable(modelStack.size() > 1 ? modelStack.get(1) : null);
    }

    public int ancestorModelsCount() {
        return modelStack.size() > 1 ? modelStack.size() - 1 : 0;
    }

    public static class Factory {

        private final UniqueModelStackExtractor uniqueModelStackExtractor;

        public Factory(final UniqueModelStackExtractor uniqueModelStackExtractor) {
            this.uniqueModelStackExtractor = uniqueModelStackExtractor;
        }

        public ContextModelsStack from(final Context context) {

            final List<?> uniqueModelStack = uniqueModelStackExtractor.uniqueModelsStackFrom(context);

            return new ContextModelsStack(uniqueModelStack);
        }
    }
}
