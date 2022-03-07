package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * Supporting helper for {@linkplain TypeAnyHelper},
 * further sanitising values of type 'Any' for fields
 * such as {@linkplain io.swagger.v3.oas.models.media.Schema Schema}'s
 * '{@code example}'.
 * <p>
 * See:
 * <ul>
 *     <li><a href="https://spec.openapis.org/oas/v3.0.3#schema-object">Schema Object's 'example' and 'default' fields</a></li>
 *     <li><a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#section-6.2">JSON Schema's 'default' field</a></li>
 * </ul>
 */
public class TypeAnySanitisingHelper implements Helper<Object> {

    public static final TypeAnySanitisingHelper INSTANCE = new TypeAnySanitisingHelper();

    public static final String NAME = "sanitiseTypeAny";

    private TypeAnySanitisingHelper() {
        // private to encourage use of INSTANCE
    }

    @Override public String apply(final Object model, final Options options) {

        return renderAccordingToType(model);
    }

    private String renderAccordingToType(final Object model) {

        if (model instanceof Date) {
            return renderDateAsFullDate((Date) model);
        }

        return String.valueOf(model);
    }

    /**
     * <p>
     * Renders {@linkplain Date}s using '{@code yyyy-MM-dd}' pattern.
     * <p>
     * For <a href="https://spec.openapis.org/oas/v3.0.3#schema-object">fields with format 'date'</a>,
     * Codegen emits value of type {@linkplain Date} whose '{@linkplain Date#toString()}' emits
     * value with pattern '{@code dow mon dd hh:mm:ss zzz yyyy}' even when the original value in JSON
     * is specified as '{@code yyyy-MM-dd}'.
     * <p>
     * <a href="https://spec.openapis.org/oas/v3.0.3#dataTypeFormat">OAS' definition's</a>
     * format '{@code date}' translates to <a href="https://tools.ietf.org/html/rfc3339">RFC 3339's</a>
     * '{@code full-date}' which means strictly '{@code yyyy-MM-dd}' pattern.
     */
    private String renderDateAsFullDate(final Date date) {
        return fullDateFormat().format(date);
    }

    private SimpleDateFormat fullDateFormat() {
        // SimpleDateFormat is not thread safe hence the need for new instance on each call
        return new SimpleDateFormat("yyyy-MM-dd");
    }
}
