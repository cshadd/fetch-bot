import org.junit.Test;
import io.github.cshadd.fetch_bot.controllers.*;
import io.github.cshadd.fetch_bot.io.*;
import io.github.cshadd.fetch_bot.util.*;

// Main
public class AppTest {
    @Test
    public void test() {
        System.out.println("Tested.");
        
        
        final PathfindController p = new PathfindControllerImpl();
        p.reset();
        System.out.println(p);

        p.goNext();
        System.out.println(p);
        p.blockNext();
        System.out.println(p);
        p.rotateLeft();
        System.out.println(p);
        p.rotateLeft();
        p.goNext();
        System.out.println(p);
        
    }
}
