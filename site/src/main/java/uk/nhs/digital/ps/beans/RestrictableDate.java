package uk.nhs.digital.ps.beans;

import uk.nhs.digital.ps.site.exceptions.DataRestrictionViolationException;

import java.time.*;

/**
 * <p>
 * Represents date whose day should be restricted from being returned in certain conditions.
 * </p><p>
 * See {@linkplain #getDayOfMonth()}.
 * </p>
 */
public class RestrictableDate {

    private final Integer year;
    private final Month month;
    private final Integer dayOfMonth;

    private RestrictableDate(final Integer year, final Month month, final Integer dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public Integer getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    /**
     * @return Day of month when {@linkplain #isRestricted()} returns {@code false}; throws exception otherwise.
     * @throws DataRestrictionViolationException when {@linkplain #isRestricted()} returns {@code
     *                                                             true}.
     */
    public Integer getDayOfMonth() {
        if (isRestricted()) {
            throw new DataRestrictionViolationException(
                "Restricted date does not contain day of month component."
                + " Consult 'isRestricted()' before requesting day of month value."
            );
        }
        return dayOfMonth;
    }

    public boolean isRestricted() {
        return dayOfMonth == null;
    }

    public static RestrictableDate fullDateFrom(final LocalDate nominalPublicationDateInstant) {

        return new RestrictableDate(
            nominalPublicationDateInstant.getYear(),
            nominalPublicationDateInstant.getMonth(),
            nominalPublicationDateInstant.getDayOfMonth()
        );
    }

    public static RestrictableDate restrictedDateFrom(final LocalDate nominalPublicationDate) {

        return new RestrictableDate(
            nominalPublicationDate.getYear(),
            nominalPublicationDate.getMonth(),
            null
        );
    }

    @Override
    public String toString() {
        return "RestrictableDate{"
            + "year=" + year
            + ", month=" + month
            + ", dayOfMonth=" + dayOfMonth
            + '}';
    }
}
