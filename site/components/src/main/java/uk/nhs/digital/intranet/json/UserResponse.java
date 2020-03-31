package uk.nhs.digital.intranet.json;

import java.util.List;

public class UserResponse {

    private List<User> value;

    public List<User> getValue() {
        return value;
    }

    public void setValue(List<User> value) {
        this.value = value;
    }
}
