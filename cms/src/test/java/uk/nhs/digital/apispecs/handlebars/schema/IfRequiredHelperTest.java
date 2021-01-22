package uk.nhs.digital.apispecs.handlebars.schema;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.apispecs.handlebars.OptionsStub.TEMPLATE_CONTENT_FROM_INVERSE_BLOCK;
import static uk.nhs.digital.apispecs.handlebars.OptionsStub.TEMPLATE_CONTENT_FROM_MAIN_BLOCK;

import com.github.jknack.handlebars.Options;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import uk.nhs.digital.apispecs.handlebars.OptionsStub;

import java.io.IOException;

public class IfRequiredHelperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private Options.Buffer buffer;

    private final IfRequiredHelper ifRequiredHelper = new IfRequiredHelper(
        new ContextModelsStack.Factory(new UniqueModelStackExtractor())
    );

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void rendersMainBlock_whenParentObjectRequiresCurrentOne() throws IOException {

        // given
        final Schema<?> schemaWithPropertyRequired = new ObjectSchema();
        schemaWithPropertyRequired.required(singletonList("required-object"));

        final Schema<?> requiredSchema = new ObjectSchema();
        schemaWithPropertyRequired.addProperties("required-object", requiredSchema);

        final Options options = OptionsStub.with(
            buffer,
            requiredSchema,
            schemaWithPropertyRequired
        );

        // when
        final Options.Buffer actualBuffer = ifRequiredHelper.apply(requiredSchema, options);

        // then
        assertThat("Returns buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK);
    }

    @Test
    public void rendersInverseBlock_whenParentObjectDoesNotRequireCurrentOne() throws IOException {

        // given
        final Schema<?> schemaWithPropertyRequired = new ObjectSchema();
        schemaWithPropertyRequired.required(singletonList("required-object"));

        final Schema<?> notRequiredSchema = new ObjectSchema();
        schemaWithPropertyRequired.addProperties("not-required-object", notRequiredSchema);

        final Options options = OptionsStub.with(
            buffer,
            notRequiredSchema,
            schemaWithPropertyRequired
        );

        // when
        final Options.Buffer actualBuffer = ifRequiredHelper.apply(notRequiredSchema, options);

        // then
        assertThat("Returns buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_INVERSE_BLOCK);
    }

    @Test
    public void rendersInverseBlock_whenNoParentModel() throws IOException {

        // given
        final Schema<?> currentSchemaModel = new ObjectSchema();

        final Options options = OptionsStub.with(
            buffer,
            currentSchemaModel
        );

        // when
        final Options.Buffer actualBuffer = ifRequiredHelper.apply(currentSchemaModel, options);

        // then
        assertThat("Returns buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_INVERSE_BLOCK);
    }

    @Test
    public void rendersInverseBlock_whenParentModelIsNotSchema() throws IOException {

        // given
        final Object parentModelNotSchema = new Object();

        final Schema<?> currentSchemaModel = new ObjectSchema();

        final Options options = OptionsStub.with(
            buffer,
            currentSchemaModel,
            parentModelNotSchema
        );

        // when
        final Options.Buffer actualBuffer = ifRequiredHelper.apply(currentSchemaModel, options);

        // then
        assertThat("Returns buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_INVERSE_BLOCK);
    }

    @Test
    public void throwsException_onAnyFailure() throws IOException {

        // given
        final RuntimeException expectedCause = new RuntimeException();
        given(buffer.append(any())).willThrow(expectedCause);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Failed to render 'required' status.");
        expectedException.expectCause(sameInstance(expectedCause));

        final Schema<?> irrelevantModel = new ObjectSchema();

        final Options options = OptionsStub.with(buffer, irrelevantModel);

        // when
        ifRequiredHelper.apply(irrelevantModel, options);

        // then
        // expectations in 'given' are satisfied
    }

}