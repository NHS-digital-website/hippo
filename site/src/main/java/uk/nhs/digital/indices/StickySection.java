package uk.nhs.digital.indices;

public interface StickySection {

    /**
     * The StickySection's Title.
     *
     * @return A String that can be empty.
     */
    String getHeading();

    /**
     * The type of heading.
     *
     * @return The name of the heading level.
     */
    String getHeadingLevel();

    /**
     * Is the heading intended to be a main heading.
     *
     * @return true if the StickySection is intended as a main heading.
     */
    default boolean isMainHeading() {
        return this.getHeadingLevel().equalsIgnoreCase("Main heading") && this.getHeading() != null && !this.getHeading().isEmpty();
    }

}
