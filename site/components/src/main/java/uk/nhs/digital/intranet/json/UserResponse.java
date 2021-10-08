package uk.nhs.digital.intranet.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserResponse {

    @JsonProperty("@odata.count")
    private int count;

    private List<User> value;

    public List<User> getValue() {
        return value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setValue(List<User> value) {
        this.value = value;
    }
}
