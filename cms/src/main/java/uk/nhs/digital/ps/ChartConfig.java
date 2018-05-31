package uk.nhs.digital.ps;

import java.util.Objects;
import javax.jcr.Binary;
import javax.jcr.RepositoryException;

public class ChartConfig {

    private String type;
    private String title;
    private String yTitle;
    private Binary inputFileContent;

    public ChartConfig(final String type,
                       final String title,
                       final String yTitle,
                       final Binary inputFileContent
    ) {
        this.type = type;
        this.title = title;
        this.yTitle = yTitle;
        this.inputFileContent = inputFileContent;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getYTitle() {
        return yTitle;
    }

    public Binary getInputFileContent() {
        return inputFileContent;
    }

    public boolean noInputFileContent() {
        try {
            return inputFileContent == null || inputFileContent.getSize() == 0;
        } catch (final RepositoryException ex) {
            throw new RuntimeException("Failed to read binary size.", ex);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChartConfig that = (ChartConfig) o;
        return Objects.equals(type, that.type)
            && Objects.equals(title, that.title)
            && Objects.equals(yTitle, that.yTitle)
            && Objects.equals(inputFileContent, that.inputFileContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, title, yTitle, inputFileContent);
    }
}
