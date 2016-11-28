package duckling.support;

import duckling.Logger;

import java.util.ArrayList;

public class SpyLogger extends Logger {
    public ArrayList<String> messages = new ArrayList<>();

    @Override
    public void info(String... givenMessages) {
        for (String message : givenMessages) messages.add(message);
    }
}
