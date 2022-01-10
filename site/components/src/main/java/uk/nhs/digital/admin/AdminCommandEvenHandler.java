package uk.nhs.digital.admin;

public interface AdminCommandEvenHandler {

    boolean supports(final AdminCommand adminCommand);

    void execute(final AdminCommand adminCommand);
}
