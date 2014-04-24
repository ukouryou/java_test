/**
 *
 */
package reactor;

import java.io.IOException;

/**
 * Apr 11, 2014
 * @author andy
 */
public class Server {
    public static void main(String[] args) throws IOException {
        Reactor reactor  = new Reactor(9900, false);
        new Thread(reactor).start();
    }
}
