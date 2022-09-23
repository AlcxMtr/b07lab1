class Polynomial {
    double[] coef;
    public Polynomial() {
        coef = new double[1];
        coef[0] = 0;
    }
    public Polynomial(double[] poly) {
        coef = new double[poly.length];
        System.arraycopy(poly, 0, coef, 0, poly.length);
    }
    public Polynomial add(Polynomial poly) {
        int lenc = coef.length;
        int lenp = poly.coef.length;
        double[] sum;
        if (lenc > lenp) { // If the calling polynomial is of higher degree
            sum = new double[lenc];
            System.arraycopy(coef, 0, sum, 0, lenc);
            for (int x = 0; x < lenp; x++) {
                sum[x] += poly.coef[x];
            }
        } else { // If the poly taken as argument is higher or equal degree
            sum = new double[lenp];
            System.arraycopy(poly.coef, 0, sum, 0, lenp);
            for (int x = 0; x < lenc; x++) {
                sum[x] += coef[x];
            }
        }
        return new Polynomial(sum);
    }

    public double evaluate(double val) {
        int len = coef.length;
        double sum = 0;
        for (int x = 0; x < len; x++) {
            double exp = 1;
            for (int y = 0; y < x; y++) {
                exp *= val;
            }
            sum += coef[x]*exp;
        }
        return sum;
    }

    public boolean hasRoot(double x) {
        if (this.evaluate(x) == 0) return true;
        return false;
    }
}