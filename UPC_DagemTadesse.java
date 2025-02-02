package HW1;

// This is the updated version of the UPC-A scanner
public class UPC_DagemTadesse {
	
		
	
    //--------------------------------------------
    // Scan in the bit pattern from the image
    // Takes the filename of the image
    // Returns an int array of the 95 scanned bits
    //--------------------------------------------
    public static int[] scanImage(String filename) {
        DUImage barCode = new DUImage(filename); // Load the image
        int[] bits = new int[95]; // Array which holds the scanned bits

        int middleY = barCode.getHeight() / 2; // Get the middle height of the image
        for (int i = 0; i < 95; i++) {
            int x = 5 + (i * 2); // Calculate x position for each bar
            int redValue = barCode.getRed(x, middleY); // Get the red value of the pixel
            bits[i] = (redValue < 128) ? 1 : 0; // Determines black (1) or white (0)
        }

        return bits; // Return the scanned bits
    }

    //--------------------------------------------
    // Finds the matching digit for the given pattern
    // This is a helper method for decodeScan
    //--------------------------------------------
    public static int matchPattern(int[] scanPattern, int startIndex, boolean left) {
        // Patterns for UPC digits
        int[][] digitPat = {
            {0, 0, 0, 1, 1, 0, 1},
            {0, 0, 1, 1, 0, 0, 1},
            {0, 0, 1, 0, 0, 1, 1},
            {0, 1, 1, 1, 1, 0, 1},
            {0, 1, 0, 0, 0, 1, 1},
            {0, 1, 1, 0, 0, 0, 1},
            {0, 1, 0, 1, 1, 1, 1},
            {0, 1, 1, 1, 0, 1, 1},
            {0, 1, 1, 0, 1, 1, 1},
            {0, 0, 0, 1, 0, 1, 1}
        };

        int[] segment = new int[7]; // Array to hold the current segment
        for (int i = 0; i < 7; i++) {
            segment[i] = scanPattern[startIndex + i]; //  7 bits for this segment
        }
        if (!left) { // If we are scanning the right side, flip the bits
            for (int i = 0; i < 7; i++) {
                segment[i] = segment[i] == 0 ? 1 : 0; // flip bits for right side
            }
        }

        // Compare segment with digit patterns
        for (int digit = 0; digit < digitPat.length; digit++) {
            boolean match = true; // Assume a match until proven differently
            for (int j = 0; j < 7; j++) {
                if (segment[j] != digitPat[digit][j]) { 
                    match = false; 
                    break;
                }
            }
            if (match) {
                return digit; // Return the matching digit
            }
        }
        return -1; // No match found
    }

    //--------------------------------------------
    // Performs a full scan decode that turns all 95 bits into 12 digits
    //--------------------------------------------
    public static int[] decodeScan(int[] scanPattern) {
        // Check start pattern (should be 1, 0, 1)
        if (scanPattern[0] != 1 || scanPattern[1] != 0 || scanPattern[2] != 1) {
            System.out.println("Error: Incorrect start pattern."); // Error message
            System.exit(1); // Exit if start pattern is incorrect
        }

        // Decode left side digits (positions 3 to 45)
        int[] digits = new int[12]; // Array for the 12 decoded digits
        for (int i = 0; i < 6; i++) {
            digits[i] = matchPattern(scanPattern, 3 + i * 7, true); // Match the left side patterns
            if (digits[i] == -1) {
                System.out.println("Error decoding left digits"); // Error message
                System.exit(1); // Exit if there's an error
            }
        }

        // Check middle pattern (should be 0, 1, 0, 1, 0)
        if (scanPattern[45] != 0 || scanPattern[46] != 1 || scanPattern[47] != 0 || scanPattern[48] != 1 || scanPattern[49] != 0) {
            System.out.println("Error: Incorrect middle pattern."); // Error message
            System.exit(1); // Exit if middle pattern is incorrect
        }

        // Decode right side digits (positions 50 to 92)
        for (int i = 0; i < 6; i++) {
            digits[i + 6] = matchPattern(scanPattern, 50 + i * 7, false); // Match right side patterns
            if (digits[i + 6] == -1) {
                System.out.println("Error decoding right digits"); // Error message
                System.exit(1); // Exit if there's an error
            }
        }

        // Check end pattern (should be 1, 0, 1)
        if (scanPattern[92] != 1 || scanPattern[93] != 0 || scanPattern[94] != 1) {
            System.out.println("Error: Incorrect end pattern."); // Error message
            System.exit(1); // Exit if end pattern is incorrect
        }

        return digits; // Return the decoded digits
    }

    //--------------------------------------------
    // Verify the checksum of the digits
    //--------------------------------------------
    public static boolean verifyCode(int[] digits) {
        int sumEven = 0, sumOdd = 0; // Initialize sums for even and odd indexed digits
        for (int i = 0; i < digits.length - 1; i++) { // Loop through all digits except the last
            if (i % 2 == 0) {
                sumEven += digits[i]; // Sum even indexed digits
            } else {
                sumOdd += digits[i]; // Sum odd indexed digits
            }
        }
        int total = (sumEven * 3) + sumOdd; // Calculate total using the checksum formula
        int mod = total % 10; // Get remainder
        int checkDigit = (mod == 0) ? 0 : 10 - mod; 
        return checkDigit == digits[11]; 
    }

    //--------------------------------------------
    // The main method scans the image, decodes it, and validates it
    //--------------------------------------------
    public static void main(String[] args) {
        // Default file name for the barcode image
        String barcodeFileName = "barcode1.png";
        // Check if a file name was provided as a command-line argument
        if (args.length == 1) {
            barcodeFileName = args[0]; // Use the provided file name
        }
        // Scan the image and get the pattern as an array of bits
        int[] scanPattern = scanImage(barcodeFileName);
        // Print the scanned bit pattern
        System.out.println("Original scan");
        for (int i = 0; i < scanPattern.length; i++) {
            System.out.print(scanPattern[i]); // Print each bit
        }
        System.out.println(""); // New line

        // Decode the scanned pattern into digits
        int[] digits = decodeScan(scanPattern);
        // Print the decoded digits
        boolean scanError = false; // Flag to check for scan errors
        System.out.println("Digits");
        for (int i = 0; i < 12; i++) {
            System.out.print(digits[i] + " "); // Print each digit
            if (digits[i] == -1) { // Check for errors
                scanError = true; // Set error flag
            }
        }
        System.out.println(""); // New line

        // Check for scan errors
        if (scanError) {
            System.out.println("Scan error"); // Print error message
        } else { // If no errors, check the checksum
            if (verifyCode(digits)) {
                System.out.println("Passed Checksum"); // Checksum if its valid 
            } else {
                System.out.println("Failed Checksum"); // Checksum if its invalid
            }
        }
    }
}



