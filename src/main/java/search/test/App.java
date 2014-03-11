package search.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App
{

     Parent parent = new Parent();

    public void testIterable(Iterable<Parent> iterable) {
        List<Parent> list = new ArrayList<Parent>();
        for (Parent parent : iterable) {
            System.out.println(parent.getMessage());
            list.add(parent);
        }

        for (Parent parent : list) {
            System.out.println("tt:" + parent.getMessage());
        }
    }
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        App app = new App();
        app.testSuite();

    }

    public void testSuite() {

        Iterable<Parent> iterable = new testIterable();
        testIterable(iterable);
    }

    public static class testIterable implements Iterable<Parent> {

        @Override
        public Iterator<Parent> iterator() {
            App app = new App();
            return app.new testIterator();
        }

    }

    public class testIterator implements Iterator<Parent> {



        int i = 0;

        /* (non-Javadoc)
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            parent.setMessage(i);
            i++;
            return i>3 ? false : true;
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#next()
         */
        @Override
        public Parent next() {
            return parent;
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() {
            // TODO Auto-generated method stub

        }

    }
}
