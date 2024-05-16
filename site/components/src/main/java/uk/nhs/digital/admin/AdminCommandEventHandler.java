package uk.nhs.digital.admin;

public interface AdminCommandEventHandler {

    boolean supports(final AdminCommand adminCommand);

    void execute(final AdminCommand adminCommand);
}
