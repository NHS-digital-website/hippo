package uk.nhs.digital.toolbox.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.nhs.digital.toolbox.exception.ExceptionUtils;

public class ExceptionUtilsTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void wrapCheckedException_rethrowsSuppliersCheckedExceptionAsUncheckedException() {

        // given
        final Exception checkedExceptionThrownBySupplier = new Exception();

        final ExceptionUtils.CheckedExceptionThrowingSupplier<String> checkedExceptionThrowingSupplier = () -> {
            throw checkedExceptionThrownBySupplier;
        };

        expectedException.expect(ExceptionUtils.UncheckedWrappingException.class);
        expectedException.expectCause(sameInstance(checkedExceptionThrownBySupplier));

        // when
        ExceptionUtils.wrapCheckedException(checkedExceptionThrowingSupplier);

        // then
        // expectations set up in 'given' are satisfied
    }

    @Test
    public void wrapCheckedException_rethrowsSuppliersOriginalUncheckedException() {

        // given
        final RuntimeException uncheckedExceptionThrownBySupplier = new RuntimeException();

        final ExceptionUtils.CheckedExceptionThrowingSupplier<String> checkedExceptionThrowingSupplier = () -> {
            throw uncheckedExceptionThrownBySupplier;
        };

        expectedException.expect(sameInstance(uncheckedExceptionThrownBySupplier));

        // when
        ExceptionUtils.wrapCheckedException(checkedExceptionThrowingSupplier);

        // then
        // expectations set up in 'given' are satisfied
    }

    @Test
    public void wrapCheckedException_returnsValueProducedBySupplierWhereSupplierThrowsNoException() {

        // given
        final String expectedValueToReturn = "a test string";

        final ExceptionUtils.CheckedExceptionThrowingSupplier<String> valueReturningSupplier =
            () -> expectedValueToReturn;

        // when
        final String actualReturnedValue = ExceptionUtils.wrapCheckedException(valueReturningSupplier);

        // then
        assertThat("Value returned by the method is the same as returned by the supplier.",
            actualReturnedValue,
            sameInstance(expectedValueToReturn)
        );
    }

    @Test
    public void wrapCheckedException_rethrowsConsumersCheckedExceptionAsUncheckedException() {

        // given
        final Exception checkedExceptionThrownByConsumer = new Exception();

        final ExceptionUtils.CheckedExceptionThrowingConsumer checkedExceptionThrowingConsumer = () -> {
            throw checkedExceptionThrownByConsumer;
        };

        expectedException.expect(ExceptionUtils.UncheckedWrappingException.class);
        expectedException.expectCause(sameInstance(checkedExceptionThrownByConsumer));

        // when
        ExceptionUtils.wrapCheckedException(checkedExceptionThrowingConsumer);

        // then
        // expectations set up in 'given' are satisfied
    }

    @Test
    public void wrapCheckedException_rethrowsConsumersOriginalUncheckedException() {

        // given
        final RuntimeException uncheckedExceptionThrownByConsumer = new RuntimeException();

        final ExceptionUtils.CheckedExceptionThrowingSupplier<String> checkedExceptionThrowingConsumer = () -> {
            throw uncheckedExceptionThrownByConsumer;
        };

        expectedException.expect(sameInstance(uncheckedExceptionThrownByConsumer));

        // when
        ExceptionUtils.wrapCheckedException(checkedExceptionThrowingConsumer);

        // then
        // expectations set up in 'given' are satisfied
    }
}