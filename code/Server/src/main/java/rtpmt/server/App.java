package rtpmt.server;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * App!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            
            int port;
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            } else {
                port = 8080;
            }
            System.out.println( "Starting Server at port " + port );
            new Server(port).run();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
