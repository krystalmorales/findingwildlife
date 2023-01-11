package learn.findingwildlife.domain;

import java.util.ArrayList;
import java.util.List;


public class Result<T> {

    private final ArrayList<String> messages = new ArrayList<>();
    private T payload;

    public List<String> getErrorMessages() {
        return new ArrayList<>(messages);
    }

    public void addErrorMessage(String message) {
        messages.add(message);
    }

    public boolean isSuccess() {
        return messages.size() == 0;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
