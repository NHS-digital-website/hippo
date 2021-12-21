package uk.nhs.digital.arc.process;

public class ExternalStorageReference {
    private final String nodePath;
    private final String propValue;

    public ExternalStorageReference(String nodePath, String propValue) {
        this.nodePath = nodePath;
        this.propValue = propValue;
    }

    public String getNodePath() {
        return nodePath;
    }

    public String getPropValue() {
        return propValue;
    }

    @Override
    public String toString() {
        return "[ nodePath: " + nodePath + " propValue: " + propValue + "]";
    }
}
