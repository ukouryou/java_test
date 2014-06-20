/**
 *
 */

/**
 * Jun 13, 2014
 * @author andy
 */
public class Client {

    private Factory factory;

    /**
     *
     */
    public Client() {
        factory = Factory.getInstance();

    }

    public void process() {
        if (null == factory) {
            System.out.println("factory is null");
        } else {
            System.out.println("factory has been inalizationlized");
        }

        Factory.destroy();

        if (null == factory) {
            System.out.println("factory is null");
        } else {
            System.out.println("factory has been inalizationlized");
        }

    }
    /**
     * @param args
     */
    public static void main(String[] args) {
       Client client = new Client();
       client.process();

    }

}
