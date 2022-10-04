import java.io.File;
public class Driver { 
 public static void main(String [] args) { 
    double[] a1 = {3, 2, -3, 5};
    int[] a2 = {7, 0, 1, 7};
    double[] b1 = {5, 9, -10, 6};
    int[] b2 = {2, 5, 0, 3};

    /* double[] a1 = {-1, -2};
    int[] a2 = {0, 1};
    double[] b1 = {1, 2};
    int[] b2 = {1, 0}; */
        
    Polynomial a = new Polynomial(a1, a2);
    Polynomial b = new Polynomial(b1, b2);
    int val = 2;
    System.out.println("Result for x = " + val + ": " + a.evaluate(val));
    Polynomial c = a.multiply(b);               // Multiply a*b
    c.print_poly();                             // Print a*b
    c.saveToFile("poly.txt");             // Save c to file
    File f = new File("poly.txt");    // Read file with c
    Polynomial d = new Polynomial(f);          // Construct d from file
    d.print_poly();                            // Print d
 } 
} 