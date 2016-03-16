/**
 * Class RSA implements the core functionality of the RSA algorithm
 */

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class RSA {

    /**
     * Nested class RSAKeys implements RSA keys (both private and public)
     */
    public static class RSAKeys {
        HugeUInt primeP;
        HugeUInt primeQ;
        HugeUInt phi;
        HugeUInt e;
        HugeUInt d;
        HugeUInt n;

        /**
         * Public constructor. The prime numbers are to be provided by the user
         * @param p
         * @param q
         */
        public RSAKeys(int p, int q) {
            primeP = new HugeUInt(p);
            primeQ = new HugeUInt(q);
            n = calculate_n(primeP, primeQ);
            phi = calculate_phi(primeP, primeQ);
            e = calculate_e(n, phi);
            d = caclculate_d(e, phi);
        }

        /**
         * Public constructor. Autogenerates all values depending on the generate value
         * @param generate
         */
        public RSAKeys(int decimals) {
            if (decimals>0) {
                primeP = generatePrimeNumber(decimals);
                primeQ = generatePrimeNumber(decimals);
                n = calculate_n(primeP, primeQ);
                phi = calculate_phi(primeP, primeQ);
                e = calculate_e(n, phi);
                d = caclculate_d(e, phi);
            }
            else {
                primeP = new HugeUInt(0);
                primeQ = new HugeUInt(0);
                n = new HugeUInt(0);
                phi = new HugeUInt(0);
                e = new HugeUInt(0);
                d = new HugeUInt(0);
            }
        }
    }

    static int MAX_DECIMALS = 2; //The block size

    public static void setBlockSize(int b){
    	MAX_DECIMALS = b;
    }
    
    /**
     * Verifies if a number is a prime number
     * @param a
     * @return
     * Lawrence & Ana
     */
    public static boolean isPrime(HugeUInt a) {
    	HugeUInt zero = new HugeUInt(0);
    	if(a.compareTo(new HugeUInt(1))<=0) return false;
    	else if(a.compareTo(new HugeUInt(2))==0) return true;
    	else if(a.mod(2).compareTo(zero)==0) return false;
    	HugeUInt i = new HugeUInt(3);
    	while (i.multiply(i).compareTo(a)<=0){
    		if(a.mod(i).compareTo(zero)==0) return false;
    		i = i.add(2);
    	}
        return true;
    }

    /**
     * The Eucleudean algorithm of finding GCD
     * @param a
     * @param b
     * @return
     * Lawrence
     */
    public static HugeUInt gcd(HugeUInt a, HugeUInt b) {
        if (b.compareTo(new HugeUInt(0)) == 0) return a;
        else return gcd(b, a.mod(b));
    }

    /**
     * Verifies if the two arguments are relatively prime
     * @param e
     * @param phi
     * @return
     * Lawrence
     */
    public static boolean areRelativelyPrime(HugeUInt e, HugeUInt phi) {
        HugeUInt gcd = gcd(e, phi);
        boolean result = gcd.compareTo(new HugeUInt(1)) == 0;
        return result;
    }

    /**
     * Produces a prime number
     * @return
     * Lawrence & Ana
     */
    public static HugeUInt generatePrimeNumber(int decimals) {
        while (true) {
            HugeUInt rnd = generateRandom(decimals);
            if (isPrime(rnd)) return rnd;
        }
    }


    /**
     * Generates a random HugeUInt number with decimals positions
     * @param decimals
     * @return
     * Ana
     */
    public static HugeUInt generateRandom(int decimals) {
        Random random = new Random();
        int arr[] = new int[decimals];
        HugeUInt result = new HugeUInt(arr);
        for (int i = 0; i < decimals-1; i++) arr[i] =(random.nextInt(10));
        arr[decimals-1] = random.nextInt(9)+1;
        return result;
    }
    
    /**
     * Calculates n
     * @param p
     * @param q
     * @return
     * Ana
     */
    public static HugeUInt calculate_n(HugeUInt p, HugeUInt q)  {
        return p.multiply(q);
    }

    /**
     * Calculates phi
     * @param p
     * @param q
     * @return
     * Lawrence
     */
    public static HugeUInt calculate_phi(HugeUInt p, HugeUInt q)  {
        return p.subtract(new HugeUInt(1)).multiply(q.subtract(new HugeUInt(1)));
    }

    /**
     * Calculates e
     * @param n
     * @param phi
     * @return
     * Ana
     */
    public static HugeUInt calculate_e(HugeUInt n, HugeUInt phi) {
        int deci;
    	while (true) {
        	Random rnd = new Random();
        	deci = rnd.nextInt(5)+1;
            HugeUInt e = generatePrimeNumber(deci);
            boolean statement1 = e.compareTo(n) < 0;
            boolean statement2 = areRelativelyPrime(e, phi);
            if (statement1 && statement2) return e;
        }
    }

    /**
     * Calculates d
     * @param e
     * @param phi
     * @return
     * Lawrence & Ana
     */
    public static HugeUInt caclculate_d(HugeUInt e, HugeUInt phi) {
        int k = 1;
        while (true) {
            HugeUInt value = phi.multiply(k);
            value = value.add(new HugeUInt(1));
            value = value.mod(e);
            if (value.compareTo(new HugeUInt(0)) == 0) {
                return phi.multiply(new HugeUInt(k)).add(new HugeUInt(1)).divide(e);
            }
            k++;
        }
    }

    /**
     * Encodes the ASCII code of a char according to the assignment
     * @param charASCII
     * @return
     * Ana
     */
    public static int encodeASCII(byte charASCII) {
        if (charASCII >= 32 && charASCII <= 126) return (byte) (charASCII - 27);
        if (charASCII == 0) return 0;
        if (charASCII == 11) return 1;
        if (charASCII == 9) return 2;
        if (charASCII == 10) return 3;
        if (charASCII == 13) return 4;
        else return 100;
    }

    /**
     * Decodes the ASCII code of a char according to the assignment
     * @param encASCII
     * @return
     * Ana
     */
    public static int decodeASCII(int encASCII) {
        switch (encASCII) {
            case 0: return 0;
            case 1: return 11;
            case 2: return 9;
            case 3: return 10;
            case 4: return 13;
            default:return (byte) (encASCII + 27);
        }
    }

    /**
     * Saves a public key (e, n) into an XML file according to the assignment
     * @param outputFile
     * @param publicKey
     * Lawrence & Ana
     */
    public static void savePublicKeyXML(File outputFile, RSAKeys publicKey) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            writer.println("<?xml version=\"1.0\"?>");
            writer.println("<rsakey>");
            writer.println("<evalue>" + publicKey.e + "</evalue>");
            writer.println("<nvalue>" + publicKey.n + "</nvalue>");
            writer.println("</rsakey>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a private key (d, n) into an XML file according to the assignment
     * @param outputFile
     * @param privateKey
     * Lawrence
     */
    public static void savePrivateKeyXML(File outputFile, RSAKeys privateKey) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            writer.println("<?xml version=\"1.0\"?>");
            writer.println("<rsakey>");
            writer.println("<dvalue>" + privateKey.d + "</dvalue>");
            writer.println("<nvalue>" + privateKey.n + "</nvalue>");
            writer.println("</rsakey>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a public key from an XML file
     * @param inputFile
     * @return
     * Ana
     */
    public static RSAKeys readPublicKeyXML(File inputFile) {
        String eValue = new String();
        String nValue = new String();
        try {
            DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document xmlDoc = docReader.parse(inputFile);
            Element rootElement = xmlDoc.getDocumentElement();
            NodeList nodeE = rootElement.getElementsByTagName("evalue");
            Element elE = (Element) nodeE.item(0);
            eValue = elE.getTextContent();
            NodeList nodeN = rootElement.getElementsByTagName("nvalue");
            Element elN = (Element) nodeN.item(0);
            nValue = elN.getTextContent();
            RSAKeys result = new RSAKeys(0);
            result.e = new HugeUInt(eValue);
            result.n = new HugeUInt(nValue);
            return result;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads a private key from an XML file
     * @param inputFile
     * @return
     * Lawrence & Ana
     */
    public static RSAKeys readPrivateKeyXML(File inputFile) {
        String dValue = new String();
        String nValue = new String();
        try {
            DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document xmlDoc = docReader.parse(inputFile);
            Element rootElement = xmlDoc.getDocumentElement();
            NodeList nodeD = rootElement.getElementsByTagName("dvalue");
            Element elD = (Element) nodeD.item(0);
            dValue = elD.getTextContent();
            NodeList nodeN = rootElement.getElementsByTagName("nvalue");
            Element elN = (Element) nodeN.item(0);
            nValue = elN.getTextContent();
            RSAKeys result = new RSAKeys(0);
            result.d = new HugeUInt(dValue);
            result.n = new HugeUInt(nValue);
            return result;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypts (or blocks a file if no key has been provided) a file
     * @param inputFile contains the source data
     * @param outputFile contains encrypted data
     * @param publicKey (e, n)
     * Lawrence
     */
    public static boolean encryptFile(File inputFile, File outputFile, RSAKeys publicKey) {
        ArrayList<HugeUInt> store = new ArrayList<>();
        try(FileInputStream inputStream = new FileInputStream(inputFile);
            PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            byte[] inputArray = new byte[MAX_DECIMALS];
            int[] encodedInputArray = new int[MAX_DECIMALS];
            while (inputStream.read(inputArray) != -1) {
                for (int i = 0; i < inputArray.length; i++) {
                    encodedInputArray[i] = encodeASCII(inputArray[i]);
                    inputArray[i] = 0;
                }
                HugeUInt value = new HugeUInt(0);
                for (int i = 0; i < inputArray.length; i++) {
                    HugeUInt mult = new HugeUInt(encodedInputArray[i]);
                    mult = mult.multiply(new HugeUInt(100).pow(i));
                    value = value.add(mult);
                }
                if (publicKey != null){
                	if(publicKey.n.compareTo(value)<0){
                		return false;
                	}
                	value = HugeUInt.modPow(value, publicKey.e, publicKey.n);
                }
                store.add(value);
            }
            for (HugeUInt value: store) {
            	if(publicKey==null){
            		value.addPaddingZeros(MAX_DECIMALS * 2);
            	}
                writer.println(value);
            }
            writer.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Decrypts (or simply unblocks a blocked file if no key has been provided) a file
     * @param inputFile contains the source encrypted data
     * @param outputFile writes decrypted data into this file
     * @param privateKey (d,n)
     * Lawrence & Ana
     */
    public static boolean decryptFile(File inputFile, File outputFile, RSAKeys privateKey) {
        String line;
        int symbol;
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            while ((line = reader.readLine()) != null) {
                HugeUInt value = new HugeUInt(line);
                value.trim();
                if (privateKey != null){
                	if(privateKey.n.compareTo(value)<0){
                		return false;
                	}
                	value = HugeUInt.modPow(value, privateKey.d, privateKey.n);
                }
                value.addPaddingZeros(MAX_DECIMALS * 2);
                for (int i = 0; i < MAX_DECIMALS; i++) {
                    symbol = value.getDigit(i * 2) + value.getDigit(i * 2 + 1) * 10;
                    symbol = decodeASCII(symbol);
                    outputStream.write((byte)symbol);
                }
                outputStream.flush();
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method takes a blocked file and encrypts it
     * @param inputFile
     * @param outputFile
     * @param publicKey
     */
    public static boolean encryptBlockedFile(File inputFile, File outputFile, RSAKeys publicKey) {
        String line;
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            while ((line = reader.readLine()) != null) {
                HugeUInt value = new HugeUInt(line);
                value.trim();
                if(publicKey.n.compareTo(value)<0){
            		return false;
            	}
                value = HugeUInt.modPow(value, publicKey.e, publicKey.n);
                writer.println(value);
            }
            writer.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method takes an ecncrypted file and decrypts it into a blocked file
     * @param inputFile
     * @param outputFile
     * @param privateKey
     */
    public static boolean decryptBlockedFile(File inputFile, File outputFile, RSAKeys privateKey) {
        String line;
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            while ((line = reader.readLine()) != null) {
                HugeUInt value = new HugeUInt(line);
                if(privateKey.n.compareTo(value)<0){
            		return false;
            	}
                value = HugeUInt.modPow(value, privateKey.d, privateKey.n);
                value.addPaddingZeros(MAX_DECIMALS * 2);
                writer.println(value);
            }
            writer.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Included for testing purposes
     * @param args
     */
    public static void main(String[] args) {
//        encryptFile(new File("test.txt"), new File("blocked.txt"), null);
//        encryptBlockedFile(new File("blocked.txt"), new File("blocked_enc.txt"), keys);
//        decryptBlockedFile(new File("blocked_enc.txt"), new File("blocked_dec.txt"), keys);
//        decryptFile(new File("blocked_dec.txt"), new File("decrypted.txt"), null);
    }
}
