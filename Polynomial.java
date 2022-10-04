import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Polynomial {
    double[] coef;
    int[] exp;
    public Polynomial() {
        coef = new double[1];
        exp = new int[1];
        coef[0] = 0;
        exp[0] = 0;
    }
    public Polynomial(double[] poly, int[] deg) {
        int len = poly.length;
        coef = new double[len];
        exp = new int[len];
        System.arraycopy(poly, 0, coef,0, len);
        System.arraycopy(deg, 0, exp, 0, len);
    }

    public Polynomial(File file) {
        try {
            Scanner pfile = new Scanner(file);
            if (!(pfile.hasNextLine())) {           // Empty file case
                coef = new double[1];
                exp = new int[1];
                coef[0] = 0;
                exp[0] = 0;
                pfile.close();
                return;
            }
            String data = pfile.nextLine();
            int d_len = data.length();
            char[] input = new char[d_len];
            input = data.toCharArray();
            int len = 0;
            for (int i = 0; i < d_len; i++) {
                if (input[i] == '+' || input[i] == '-') len++; // Count the number of terms
            }
            if (input[0] != '-') len++;
            String[] terms = new String[len];
            int pos = 0;
            if (input[0] == '-') {
                terms[0] = "-";
                pos++;
            } else {
                terms[0] = "";
            }
            for (int i = 0; i < len; i++) {
                while (pos < d_len && input[pos] != '-' && input[pos] != '+') {
                    terms[i] += String.valueOf(input[pos]);
                    pos++;
                }
                if (pos < d_len) {
                    if (input[pos] == '-' && i+1 < len) {
                        terms[i+1] = "-";
                    } else if (input[pos] == '+' && i+1 < len) {
                        terms[i+1] = "";
                    }
                    pos++;
                }
            }            
            coef = new double[len];
            exp = new int[len];
            for (int i = 0; i < len; i++) {
                if (terms[i].contains("x")) { 
                    String[] args = terms[i].split("x");
                    for (int j = 0; j < args.length; j++) {
                    }
                    coef[i] = Double.parseDouble(args[0]);
                    exp[i] = Integer.parseInt(args[1]);
                } else {
                    coef[i] = Double.parseDouble(terms[i]);
                    exp[i] = 0;
                }
            }
            pfile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found.");
            e.printStackTrace();
        }
    }
    public Polynomial add(Polynomial poly) {
        if (this == null || poly == null) return null;
        int lenc = coef.length;
        int lenp = poly.coef.length;
        int degc = 0;                               // Get highest degree of calling poly
        for (int i = 0; i < lenc; i++) {
            if (degc < exp[i]) {
                degc = exp[i];
            }
        }
        int degp = 0;                               // Get highest degree of argument poly
        for (int i = 0; i < lenp; i++) {
            if (degp < poly.exp[i]) {
                degp = poly.exp[i];
            }
        }
        int mlen = Math.max(degc, degp) + 1;        // Get highest degree between the two (add 1 because I want to include degree 0 into the length)
        int[] terms = new int[mlen];                // Create array to track the terms (filled with 0s)
        for (int i = 0; i < lenc; i++) {            // Add the coefficients from calling poly
            terms[exp[i]] += coef[i];
        }
        for (int i = 0; i < lenp; i++) {            // Add the coefficients from arg poly
            terms[poly.exp[i]] += poly.coef[i];
        }
        int num_terms = 0;                          // Number of terms in the post-sum poly
        for (int i = 0; i < mlen; i++) {            // Count the number of non zero terms
            if (terms[i] != 0) 
                num_terms++;
        }
        double[] sumc = new double[num_terms];      // Array for the summed coefficients
        int[] sume = new int[num_terms];            // Array for the summed exponents
        int counter = 0;                            // Count the number of added terms
        for (int i = 0; i < mlen; i++) {            // Fill the new arrays with values
            if (terms[i] != 0) {                    // If this term is non-zero:
                sumc[counter] = terms[i];           // summed_coef_array[current number of terms added] = coef from degree i
                sume[counter] = i;                  // summed_exp_array[current number of terms added] = degree i
                counter++;
            }
        }
        return new Polynomial(sumc, sume);
    }

    public Polynomial multiply(Polynomial poly) {
        int lenc = coef.length;
        int lenp = poly.coef.length;
        Polynomial[] products = new Polynomial[lenp];   // Array of the polynomials made from multiplying poly1 (calling poly) by each of the terms of the poly2 (argument poly)
        for (int i = 0; i < lenp; i++) {                // Loop through the list of p1 multiplied by each of the terms of p2
            double[] pcoef = new double[lenc];          // Make the list to store multiplied coefficients
            int[] pexp = new int[lenc];                 // Make the list to store multiplied exponents
            System.arraycopy(coef, 0, pcoef,0, lenc);   // Fill the list with p1
            System.arraycopy(exp, 0, pexp, 0, lenc);    // Fill the list with p1
            for (int j = 0; j < lenc; j++) {            // Loop through the terms of p1 nad multiply each one by term i of p2
                pcoef[j] *= poly.coef[i];               // Multiply the coefficients
                pexp[j] += poly.exp[i];                 // Add the exponents (cuz you're multiplying them)
            }
            products[i] = new Polynomial(pcoef, pexp);
        }
        System.out.println();
        Polynomial result = new Polynomial();
        for (int i = 0; i < lenp; i++) {
            result = result.add(products[i]);
        }
        return result;
    }

    public double evaluate(double val) {
        int len = coef.length;              // Take length of our poly
        double sum = 0;                     // Sum will be added sequentially by terms of different degrees
        for (int x = 0; x < len; x++) {     // Loop through the terms
            double exp = 1;                 // Variable to calculate the val^(exponent)
            for (int y = 0; y < this.exp[x]; y++) {     // Loop to do the exponentiation
                exp *= val;
            }
            sum += coef[x]*exp;             // Add coefficient*exp to sum
        }
        return sum;
    }

    public boolean hasRoot(double x) {
        if (this.evaluate(x) == 0) return true;
        return false;
    }

    public void saveToFile(String name) {
        try {
            File file = new File(name);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            }
            FileWriter f = new FileWriter(name);
            String poly = "";
            for (int i = 0; i < coef.length; i++) {
                if (exp[i] == 0) {
                    if (i != 0 && coef[i] >= 0) {
                        poly += "+";
                    }
                    poly += Double.toString(coef[i]);
                } else {
                    if (i != 0 && coef[i] >= 0) {
                        poly += "+";
                    }
                    poly += Double.toString(coef[i]);
                    poly += "x";
                    poly += Integer.toString(exp[i]);
                }
            }
            f.write(poly);
            f.close();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }

    public void print_poly() {
        int len = coef.length;
        if (coef == null || len == 0) {
            System.out.print("Polynomial: 0");
            System.out.println("");
            return;
        }
        System.out.print("Polynomial: (" + coef[0] + ")x^" + exp[0]);
        for (int i = 1; i < len; i++) {
            System.out.print(" + (" + coef[i] + ")x^" + exp[i]);
        }
        System.out.println("");
    }
}