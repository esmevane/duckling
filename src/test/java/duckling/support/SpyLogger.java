package duckling.support;

import duckling.Logger;

import java.util.ArrayList;
import java.util.Collections;

public class SpyLogger extends Logger {
    public ArrayList<String> messages = new ArrayList<>();

    @Override
    public void info(String... givenMessages) {
        Collections.addAll(messages, givenMessages);
    }
}
