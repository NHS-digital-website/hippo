package uk.nhs.digital.ps.chart.model;

public class MapNavigation {
    private final boolean enabled;
    private final ButtonOptions buttonOptions;

    public MapNavigation() {
        this.enabled = true;
        this.buttonOptions = new ButtonOptions("bottom");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ButtonOptions getButtonOptions() {
        return buttonOptions;
    }
}
