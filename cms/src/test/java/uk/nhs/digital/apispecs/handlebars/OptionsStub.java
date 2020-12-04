package uk.nhs.digital.apispecs.handlebars;

import static java.util.Collections.emptyList;
import static uk.nhs.digital.test.util.RandomTestUtils.randomString;

import com.github.jknack.handlebars.*;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

public class OptionsStub extends Options {

    public static final String TEMPLATE_CONTENT_FROM_MAIN_BLOCK = randomString("main_block_content_");
    public static final String TEMPLATE_CONTENT_FROM_INVERSE_BLOCK = randomString("inverse_block_content_");

    private Buffer buffer;

    private OptionsStub(final Buffer buffer, final Handlebars handlebars, final String helperName, final TagType tagType,
                        final Context context, final Template fn, final Template inverse, final Object[] params,
                        final Hash hash, final List<String> blockParams) {
        super(handlebars, helperName, tagType, context, fn, inverse, params, hash.hash(), blockParams);
        this.buffer = buffer;
    }

    public static Options with(final Buffer buffer, final Hash hash) {
        return new OptionsStub(buffer, null, null, null, null, null, null, null, hash, emptyList());
    }

    public static Options with(final Buffer buffer, final Data data) {
        final Context currentContext = Context.newContext("irrelevant model").data(data.getData());

        return new OptionsStub(buffer, null, null, null, currentContext, null, null, null, Hash.empty(), emptyList());
    }

    public static Options with(final Buffer buffer) {
        return new OptionsStub(buffer, null, null, null, null, null, null, null, Hash.empty(), emptyList());
    }

    public static Options with(final Context context) {
        return new OptionsStub(null, null, null, null, context, null, null, null, Hash.empty(), emptyList());
    }

    public static Options with(final Buffer buffer, final Object currentContextModel) {
        final Context currentContext = Context.newContext(currentContextModel);

        return new OptionsStub(buffer, null, null, null, currentContext, null, null, null, Hash.empty(), emptyList());
    }

    public static Options with(final Buffer buffer, final Object currentContextModel, final Object parentContextModel) {
        final Context parentContext = Context.newContext(parentContextModel);
        final Context currentContext = Context.newContext(parentContext, currentContextModel);

        return new OptionsStub(buffer, null, null, null, currentContext, null, null, null, Hash.empty(), emptyList());
    }

    @Override public CharSequence fn() {
        return TEMPLATE_CONTENT_FROM_MAIN_BLOCK;
    }

    @Override public CharSequence inverse() {
        return TEMPLATE_CONTENT_FROM_INVERSE_BLOCK;
    }

    @Override public Buffer buffer() {
        return buffer;
    }

    public static class Hash {
        private final Map<String, Object> hash;

        private Hash(final Map<String, Object> hash) {
            this.hash = hash;
        }

        public static Hash of(final String key, final Object value) {
            return new Hash(ImmutableMap.of(key, value));
        }

        public static Hash empty() {
            return new Hash(ImmutableMap.of());
        }

        public Map<String, Object> hash() {
            return hash;
        }
    }

    public static class Data {
        private final Map<String, Object> data;

        private Data(final Map<String, Object> data) {
            this.data = data;
        }

        public static Data of(final String key, final Object value) {
            return new Data(ImmutableMap.of(key, value));
        }

        private Map<String, Object> getData() {
            return data;
        }
    }
}
