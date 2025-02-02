This Java program scans a UPC-A barcode from an image, extracts the 12-digit number, and performs error checking using the standard UPC checksum algorithm. The project demonstrates image processing, bit pattern recognition, and algorithmic validation techniques.

Features

✅ Reads a PNG image of a barcode
✅ Extracts binary bit patterns from the barcode
✅ Converts bit patterns into numeric digits
✅ Validates the barcode using the UPC checksum formula
✅ Detects invalid or corrupted barcodes

How It Works

Load Image: Reads the barcode image using a helper class (DUImage).
Scan Barcode: Extracts 95-bit sequence from the middle row of the image.
Decode Digits: Maps the bit patterns to their corresponding numerical values using predefined left-side patterns (right-side patterns are inverted automatically).
Verify Checksum: Uses the UPC-A checksum algorithm to ensure barcode validity.
Output Result: Displays the extracted UPC number and indicates whether it passed or failed the checksum test.
Technologies Used

Java (Object-Oriented Programming)
Image Processing (Pixel scanning using RGB values)
Algorithm Implementation (Pattern matching & checksum validation)
Data Structures (Arrays & loops for efficient processing)
Example Output

Scanning barcode from barcode1.png...
Extracted UPC Code: 036000291452
Checksum Valid: ✅ (Barcode is correct)
Why This Project?

This project showcases low-level image processing, pattern recognition, and algorithmic problem-solving—all valuable skills in computer vision and data validation
