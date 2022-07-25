package uk.nhs.digital.toolbox.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ExceptionUtilsTest {

    @Test(expected = ExceptionUtils.UncheckedWrappingException.class)
    public void wrapCheckedException_rethrowsSuppliersCheckedExceptionAsUncheckedException() {

        // given
        final Exception checkedExceptionThrownBySupplier = new Exception();

        final ExceptionUtils.CheckedExceptionThrowingSupplier<String> checkedExceptionThrowingSupplier = () -> {
            throw checkedExceptionThrownBySupplier;
        };

        // when
        ExceptionUtils.wrapCheckedException(checkedExceptionThrowingSupplier);
        fail("Exception thrown " + sameInstance(checkedExceptionThrownBySupplier));

        // then
        // expectations set up in 'given' are satisfied
    }

    @Test(expected = RuntimeException.class)
    public void wrapCheckedException_rethrowsSuppliersOriginalUncheckedException() {

        // given
        final RuntimeException uncheckedExceptionThrownBySupplier = new RuntimeException();

        final ExceptionUtils.CheckedExceptionThrowingSupplier<String> checkedExceptionThrowingSupplier = () -> {
            throw uncheckedExceptionThrownBySupplier;
        };

        // when
        ExceptionUtils.wrapCheckedException(checkedExceptionThrowingSupplier);

        fail("Exception thrown " + sameInstance(uncheckedExceptionThrownBySupplier));

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

    @Test(expected = ExceptionUtils.UncheckedWrappingException.class)
    public void wrapCheckedException_rethrowsConsumersCheckedExceptionAsUncheckedException() {

        // given
        final Exception checkedExceptionThrownByConsumer = new Exception();

        final ExceptionUtils.CheckedExceptionThrowingConsumer checkedExceptionThrowingConsumer = () -> {
            throw checkedExceptionThrownByConsumer;
        };

        // when
        ExceptionUtils.wrapCheckedException(checkedExceptionThrowingConsumer);
        fail("Exception thrown " + sameInstance(checkedExceptionThrownByConsumer));

        // then
        // expectations set up in 'given' are satisfied
    }

    @Test(expected = RuntimeException.class)
    public void wrapCheckedException_rethrowsConsumersOriginalUncheckedException() {

        // given
        final RuntimeException uncheckedExceptionThrownByConsumer = new RuntimeException();

        final ExceptionUtils.CheckedExceptionThrowingSupplier<String> checkedExceptionThrowingConsumer = () -> {
            throw uncheckedExceptionThrownByConsumer;
        };

        // when
        ExceptionUtils.wrapCheckedException(checkedExceptionThrowingConsumer);
        fail("Exception thrown " + sameInstance(uncheckedExceptionThrownByConsumer));

        // then
        // expectations set up in 'given' are satisfied
    }
}