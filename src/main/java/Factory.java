/**
 *
 */

/**
 * Jun 13, 2014
 * @author andy
 */
public class Factory {

    public static Factory factory ;

    public static synchronized Factory getInstance() {
        if (null == factory) {
            factory = new Factory();
        }

        return factory;
    }
    private Factory() {
    }


    public static synchronized void destroy() {
        factory = null;
        if (factory == null) {
            System.out.println("Factory.factory is null");
        }

    }

}
