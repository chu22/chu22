
/**
 * This class implements operations on huge unsigned integers for the purposes
 * of the RSA algorithm
 * Lawrence & Ana
 */
public class HugeUInt implements Comparable<HugeUInt>{
    //ArrayList stores decimals
    private int[] array;


    HugeUInt(int [] arr){
    	array = arr;
    }
    
    /**
     * Constructor. Builds a HugeUInt from a String
     * @param str - a String containing the value
     */
    HugeUInt(String str) {
        array = new int[str.length()];
        int x = str.length() - 1;
        for (int i = x; i >= 0; i--) {
            array[x-i] = Character.getNumericValue(str.charAt(i));
        }
    }

    /**
     * Public constructor. Creates a HugeUint from an integer
     * @param arg
     * Lawrence
     */
    HugeUInt(int arg) {
        this(Integer.toString(arg));
    }

    /**
     * Copies the content of the argument into a new HugeUint
     * @param arg
     * Lawrence & Ana
     */
    HugeUInt(HugeUInt arg) {
        array = new int[arg.array.length];
        for (int i = 0; i < arg.array.length; i++) array[i] = arg.array[i];
    }

    /**
     * Returns the length of a HugeUint
     * @return
     * Ana
     */
    public int getSize() {
        return array.length;
    }

    /**
     * Returns a digit in position
     * @param position
     * @return
     * Lawrence
     */
    public int getDigit(int position) {
        return array[position];
    }

    /**
     * Changes the value of a digit
     * @param position
     * @param value
     * Lawrence & Ana
     */
    public void setDigit(int position, int value) {
        array[position] = value;
    }

    @Override
    /**
     * Implementation of the Comparable interface
     */
    public int compareTo(HugeUInt o) {
        //First we compare sizes
        if (getSize() < o.getSize()) return -1;
        if (getSize() > o.getSize()) return 1;
        //At this point we know that both instances have equal sizes, we have to compare each digit
        int max = getSize();
        for (int i = max - 1; i >= 0; i--) {
            if (getDigit(i) < o.getDigit(i)) return -1;
            if (getDigit(i) > o.getDigit(i)) return 1;
        }
        //The instances are equal
        return 0;
    }

    /**
     * Adds padding zeroes
     * @param positions
     * Lawrence
     */
    public void addPaddingZeros(int positions) {
        int add = positions - getSize();
        if (add <= 0) return;
        int[] tmp = new int[getSize()+add];
        for (int i = 0; i < getSize(); i++) tmp[i] = array[i];
        for (int i = getSize(); i < getSize()+add; i++) tmp[i] = 0;
        array = tmp;
    }

    /**
     * Trims extra zeros
     */
    public void trim() {
        if (getSize() == 0) return;
        int c = getSize() - 1;
        while (c>0&&array[c] == 0) {
            c--;
        }
        if(c == getSize()-1) return;
        int[] tmp = new int[c+1];
        while(c>=0){
        	tmp[c] = array[c];
        	c--;
        }
        array = tmp;
    }

    /**
     * Shifts the HugeUint right (which is equal to division by 10)
     * @param positions
     * @return
     * Lawrence & Ana
     */
    public HugeUInt shiftRight(int positions) {
        HugeUInt result = new HugeUInt(this);
        if(positions==0) return result;
        for (int i = 0; i < getSize(); i++) {
            if(i+positions>=getSize()){
            	result.array[i] = 0;
            }
            else result.array[i] = array[i+positions];
        }
        result.trim();
        return result;
    }

    /**
     * Shifts the HugeUint left (which is equal to multiplication by 10)
     * @param positions
     * @return
     * Lawrence & Ana
     */
    public HugeUInt shiftLeft(int positions) {
        if(positions==0) return new HugeUInt(this);
        int[] tmp = new int[getSize()+positions];
        for (int i = 0; i < positions; i++) tmp[i] = 0;
        for (int i = 0; i < getSize(); i++) tmp[i+positions] = array[i];
        HugeUInt result = new HugeUInt(tmp);
        return result;
    }

    /**
     * Adds another HugeUint
     * @param arg
     * @return
     * Lawrence
     */
    public HugeUInt add(HugeUInt arg) {
        int max = compareTo(arg) > 0 ? getSize() : arg.getSize();
        int min = compareTo(arg) < 0 ? getSize() : arg.getSize();
        int cur = 0;
        int[] tmp = new int[max+1];
        HugeUInt result = new HugeUInt(tmp);
        for (int i = 0; i < min; i++) {
            cur = cur + array[i] + arg.array[i];
            tmp[i] = cur % 10;
            cur = cur / 10;
        }
        if (arg.getSize() < getSize()) {
            for (int i = min; i < getSize(); i++) {
                cur = cur + array[i];
                tmp[i] = cur % 10;
                cur = cur / 10;
            }
            if (cur > 0) tmp[tmp.length-1] = cur;
        }
        else if (arg.getSize() > getSize()) {
            for (int i = min; i < arg.getSize(); i++) {
                cur = cur + arg.array[i];
                tmp[i] = cur % 10;
                cur = cur / 10;
            }
            if (cur > 0) tmp[tmp.length-1] = cur;
        }
        else{
            if (cur > 0) tmp[tmp.length-1] = cur;
        }
        result.trim();
        return result;
    }

    /**
     * Implementation of addition for integers
     * @param intArg
     * @return
     * Ana
     */
    public HugeUInt add(int intArg) {
        HugeUInt arg = new HugeUInt(intArg);
        return new HugeUInt(this.add(arg));
    }


    /**
     * Substracts one (smaller) number from another
     * @param arg
     * @return
     * Lawrence & Ana
     */
    public HugeUInt subtract(HugeUInt arg) {
        //We only subtract smaller numbers
        HugeUInt result = new HugeUInt(this);
        int cur = 0;
        for (int i = 0; i < arg.getSize(); i++) {
            cur = cur + 10 + result.array[i] - arg.array[i];
            result.array[i] = cur % 10;
            cur = cur < 10 ? -1 : 0;
        }
        for (int i = arg.getSize(); i < getSize(); i++) {
            cur = cur + 10 + result.array[i];
            result.array[i] = cur % 10;
            cur = cur < 10 ? -1 : 0;
        }
        result.trim();
        return result;
    }

    /**
     * Multiplies two HugeUints
     * @param arg
     * @return
     * Lawrence
     */
    public HugeUInt multiply(HugeUInt arg) {
        int[] tmp = new int[arg.getSize()+getSize()];
        HugeUInt result = new HugeUInt(tmp);
        int cur, k;
        for (int i = 0; i < getSize(); i++){
            for (int j = 0; j < arg.getSize(); j++) {
                cur = array[i] * arg.array[j];
                k = i + j;
                while (cur > 0) {
                    cur += tmp[k];
                    tmp[k] = cur % 10;
                    cur /= 10;
                    k++;
                }
            }
        }
        result.trim();
        return result;
    }

    /**
     * Multiplies a HugeUint by integer
     * @param arg
     * @return
     * Lawrence & Ana
     */
    public HugeUInt multiply(int arg) {
        HugeUInt result = new HugeUInt(this);
        int cur = 0;
        for (int i = 0; i < getSize(); i++) {
            int m = array[i] * arg + cur;
            cur = m / 10;
            result.array[i]  = m % 10;
        }
        int[] tmp = new int[10];
        int ctr = 0;
        while (cur > 0) {
            tmp[ctr] = cur % 10;
            cur /= 10;
            ctr++;
        }
        int[] mult = new int[ctr+getSize()];
        if(ctr > 0){
        	for(int i = 0;i<getSize();i++){
        		mult[i] = result.array[i];
        	}
        	for(int i = 0;i<ctr;i++){
        		mult[i+getSize()] = tmp[i];
        	}
        	result.array = mult;
        }
        result.trim();
        return result;
    }

    /**
     * Divides two HugeUints
     * @param b
     * @return
     * Ana
     */
    public HugeUInt divide(HugeUInt b) {
        HugeUInt q = new HugeUInt(this);
        HugeUInt t;
        HugeUInt m;
        HugeUInt p = new HugeUInt(new int[getSize()]);
        int lenA = q.getSize();
        int lenB = b.getSize();
        int sh = lenA - lenB;
        int d;     
        while (b.compareTo(q) <= 0) {
        	sh = lenA - lenB;
            t = q.shiftRight(sh);
            if (t.compareTo(b) < 0) {
                sh--;
            }
            d = 9;
            m = b.multiply(d).shiftLeft(sh);
            while (q.compareTo(m) < 0) {
                d--;
                m = b.multiply(d).shiftLeft(sh);
            }
            q = q.subtract(m);
            p.array[sh] = d;
            q.trim();
            lenA = q.getSize();
        }
        p.trim();
        return p;
    }

    /**
     * Implementation of the modulus
     * @param b
     * @return
     * Lawrence & Ana
     */
    public HugeUInt mod(HugeUInt b) {
    	 HugeUInt q = new HugeUInt(this);
         HugeUInt t;
         HugeUInt m;
         int lenA = q.getSize();
         int lenB = b.getSize();
         int sh = lenA - lenB;
         int d;     
         while (b.compareTo(q) <= 0) {
         	sh = lenA - lenB;
             t = q.shiftRight(sh);
             if (t.compareTo(b) < 0) {
                 sh--;
             }
             d = 9;
             m = b.multiply(d).shiftLeft(sh);
             while (q.compareTo(m) < 0) {
                 d--;
                 m = b.multiply(d).shiftLeft(sh);
             }
             q = q.subtract(m);
             q.trim();
             lenA = q.getSize();
         }
         q.trim();
         return q;
    }

    /**
     * Implementation of the modulus for an integer
     * @param b
     * @return
     * Ana
     */
    public HugeUInt mod(int b) {
        return mod(new HugeUInt(b));
    }

    /**
     * Implementation of the power
     * @param p
     * @return
     * Lawrence
     */
    public HugeUInt pow(int p) {
    	HugeUInt one = new HugeUInt(1);
    	HugeUInt acc = one;
    	if(p==0){
    		return one;
    	}
    	HugeUInt c = one.multiply(this);
    	while(p>1){
    		if(p%2==0){
    			c = c.multiply(c);
    		}
    		else if(p%2==1){
    			acc = acc.multiply(c);
    			c = c.multiply(c);
    		}
    		p/=2;
    	}
    	return c.multiply(acc);
    }

    /**
     * Implementation of the algorithm for large exponentiation
     * @param m
     * @param e
     * @param n
     * @return
     * Lawrence & Ana
     */
    public static HugeUInt modPow(HugeUInt m, HugeUInt e, HugeUInt n) {
    	HugeUInt two = new HugeUInt(2);
    	HugeUInt one = new HugeUInt(1);
    	HugeUInt zero = new HugeUInt(0);
    	HugeUInt acc = one;
    	if(e.compareTo(zero)==0){
    		return one;
    	}
    	HugeUInt c = one.multiply(m);
    	while(e.compareTo(one)>0){
    		if(e.mod(two).compareTo(zero)==0){
    			c = c.multiply(c).mod(n);
    		}
    		else if(e.mod(two).compareTo(one)==0){
    			acc = acc.multiply(c).mod(n);
    			c = c.multiply(c).mod(n);
    		}
    		e = e.divide(two);
    	}
    	return c.multiply(acc).mod(n);
    }

    /**
     * Produces a string representation of the the number
     * @return
     * Ana
     */
    public String toString() {
        String result = new String();
        for (int i = getSize() - 1; i >= 0; i--) result+=getDigit(i);
        return result;
    }

    /**
     * Main is only for testing.
     * @param args
     * Lawrence
     */
    public static void main(String[] args) {
        HugeUInt n1 = new HugeUInt("523");
        HugeUInt n2 = new HugeUInt("92009");
        HugeUInt n3 = new HugeUInt(n2);
        //comparison
        if (n1.compareTo(n2) < 0) {}
        //assignment
        System.out.println(n2 + " : " + n3);
        //left shift
        System.out.print(n1 + " <- left shift, 5 positions: ");
        n3 = n1.shiftLeft(5);
        System.out.println(n3);
        //right shift
        System.out.print(n2 + " -> right shift, 2 positions: ");
        n3 = n2.shiftRight(2);
        System.out.println(n3);
        //addition
        n1 = new HugeUInt("1199");
        n2 = new HugeUInt("57");
        System.out.print(n1 + " + " + n2 + " = ");
        n3 = n1.add(n2);
        System.out.println(n3);
        //subtraction
        n1 = new HugeUInt("1199");
        n2 = new HugeUInt("1190");
        System.out.print(n1 + " - " + n2 + " = ");
        n3 = n1.subtract(n2);
        System.out.println(n3);
        //multiplication
        n1 = new HugeUInt("13199");
        n2 = new HugeUInt("523447");
        System.out.print(n1 + " * " + n2 + " = ");
        n3 = n1.multiply(n2);
        System.out.println(n3);
        System.out.print(n1 + " * (int)" + 15 + " = ");
        n3 = n1.multiply(15);
        System.out.println(n3);

        //division
        n1 = new HugeUInt("42469");
        n2 = new HugeUInt("10265");
        System.out.print(n1 + " / " + n2 + " = ");
        n3 = n1.divide(n2);
        System.out.println(n3);

        n1 = new HugeUInt("1200004");
        n2 = new HugeUInt("3");
        System.out.print(n1 + " / " + n2 + " = ");
        n3 = n1.divide(n2);
        System.out.println(n3);

        n1 = new HugeUInt("14382");
        n2 = new HugeUInt("131");
        System.out.print(n1 + " / " + n2 + " = ");
        n3 = n1.divide(n2);
        System.out.println(n3);

        n1 = new HugeUInt("29356");
        n2 = new HugeUInt("1442");
        System.out.print(n1 + " / " + n2 + " = ");
        n3 = n1.divide(n2);
        System.out.println(n3);

        //modulus
        n1 = new HugeUInt("7407");
        n2 = new HugeUInt("2");
        System.out.print(n1 + " % " + n2 + " = ");
        n3 = n1.mod(n2);
        System.out.println(n3);

        //random numbers
        while (true) {
            HugeUInt rnd = RSA.generateRandom(5);
            if (RSA.isPrime(rnd)){
                System.out.println(rnd + " is prime: " + RSA.isPrime(rnd));
                break;
            }
        }
        n1 = new HugeUInt("8481817445");
        HugeUInt e = new HugeUInt("709");
        HugeUInt n = new HugeUInt("111723990469193");
        n2 = modPow(n1,e,n);
        System.out.println("Encrypt: " + n2);
        HugeUInt d = new HugeUInt("42704084186509");
        n3 = modPow(n2,d,n);
        System.out.println("Decrypt: " + n3);
    }
}
