import jQuery from "jquery";

export function initAria() {
    jQuery("details").each(function (i) {
        jQuery(this).find("summary").attr("aria-expanded", "false");
        jQuery(this).find("summary").attr("aria-controls", `details-content-${i}`);
        jQuery(this).find(".details-body").attr("aria-hidden", "true");
        jQuery(this).find(".details-body").attr("id", `details-content-${i}`);

        jQuery(this).find("summary").click(function () {
            jQuery(this).attr("aria-expanded", function (i, v) {
                return v !== "true"
            });
            jQuery(this).closest("details").find(".details-body").attr("aria-hidden", function (i, v) {
                return v !== "true"
            });
        });
    });
}
