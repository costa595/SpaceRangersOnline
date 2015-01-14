package threads;

import java.util.LinkedList;

/**
 * Created by surkov on 21.12.14.
 */
public class ThreadService {
    private final LinkedList queue;
    public ThreadService() {
        queue = new LinkedList();
    }
}
