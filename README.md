# Encryption & Decryption

A Java project exploring byte-level encryption, image processing, and least-significant-bit (LSB) steganography. Built from the EPFL CS-107 course scaffold, it shows how text becomes bytes and bits, how simple ciphers transform a message, and how an image can carry hidden data.

The repository contains utility classes and a manual demonstration runner. It is an educational implementation with some unfinished functionality; see [Current limitations](#current-limitations) before using it.

## What's inside

| Area | Functionality |
| --- | --- |
| Cryptography | Caesar shifts, single-byte XOR, Vigenere, one-time-pad XOR, and a simplified XOR-based CBC exercise, with corresponding decryption methods. |
| Bit manipulation | Read or update individual bits and convert between bytes and boolean arrays. |
| Text conversion | Convert UTF-8 strings to bytes or bits and back. |
| Image processing | Pack and unpack ARGB channels; convert between color, grayscale, and black-and-white representations. |
| Steganography | Embed and extract binary images or bit arrays using one bit per cover pixel, with additional text and color-image helpers. |
| Demonstrations | Assertion-based checks in `Main`, plus Swing dialogs and image previews. |

These algorithms are learning exercises and are not suitable for protecting sensitive data. In particular, the CBC exercise chains XOR operations without an underlying block cipher.

## Getting started

### Requirements

- **JDK 21 or later**, matching the Java 21 source and target settings in `pom.xml`.
- **Apache Maven**, available as `mvn` on your path. No Maven wrapper is included.
- A graphical desktop session to run `Main`, which opens Swing dialogs.

The Java sources use the standard library only. Maven needs to resolve its build plugins, including the CS-107 submission packager configured in `pom.xml`.

### Clone and compile

```sh
git clone https://github.com/augustinmonpays/Project-Encryption-Decryption.git
cd Project-Encryption-Decryption
mvn compile
```

Run all commands from the repository root. To check that Maven uses the intended JDK, run `mvn -version`.

### Run the demonstrations

```sh
java -ea -cp target/classes ch.epfl.cs107.Main
```

The `-ea` option enables Java assertions. **The test calls in `Main.main` are currently commented out**, so the default run only displays status dialogs. Those dialogs alone do not indicate that the implementations have passed their checks.

To exercise a feature:

1. Open [`Main.java`](src/main/java/ch/epfl/cs107/Main.java).
2. Uncomment the relevant assertion or `testCrypto(message, key)` call in `main`.
3. Run `mvn compile` again, then launch `Main` with `-ea`.
4. Inspect any `AssertionError` and its stack trace to identify the failing check.

There is no automated test suite under `src/test`; `mvn test` does not execute the checks in `Main`. The supplied checks are non-exhaustive, and some currently expose implementation issues.

### IntelliJ IDEA

Open `pom.xml` as a Maven project and select an installed JDK 21 or later. The checked-in IDE settings reference JDK 22, so you may need to update the project SDK. The included `Main` run configuration already supplies `-ea`; use the repository root as its working directory.

## Usage examples

After compiling, start JShell to try the following snippets without opening the demonstration dialogs:

```sh
jshell --class-path target/classes
```

### Encrypt and decrypt a message

```java
import ch.epfl.cs107.crypto.Encrypt;
import ch.epfl.cs107.crypto.Decrypt;
import ch.epfl.cs107.utils.Text;
import java.util.Base64;

byte[] message = Text.toBytes("Hello, cryptography!");
byte[] key = Text.toBytes("secret");

byte[] encrypted = Encrypt.vigenere(message.clone(), key);
System.out.println(Base64.getEncoder().encodeToString(encrypted));

byte[] decrypted = Decrypt.vigenere(encrypted.clone(), key);
System.out.println(Text.toString(decrypted));
// Hello, cryptography!
```

The Caesar, XOR, and Vigenere methods modify their input arrays. Pass a clone when you want to retain the original plaintext or ciphertext. Ciphertext is binary data; the example uses Base64 to display it as text.

### Hide text through the bit-array API

```java
import ch.epfl.cs107.stegano.TextSteganography;
import ch.epfl.cs107.utils.Text;
import java.util.Arrays;

int[][] cover = new int[8][8];
for (int[] row : cover) {
    Arrays.fill(row, 0xFF808080);
}

boolean[] bits = Text.toBitArray("Hello");
int[][] encoded = TextSteganography.embedBitArray(cover, bits);
boolean[] extracted = TextSteganography.revealBitArray(encoded);

System.out.println(Text.toString(Arrays.copyOf(extracted, bits.length)));
// Hello
```

An image with `height * width` pixels can hold that many bits, or `floor(height * width / 8)` complete bytes. This example embeds 40 bits in a 64-pixel cover. Check capacity before embedding: an oversized payload is silently truncated.

Extraction reads every pixel's LSB, and the image does not store the payload length. Keep that length separately to trim the extracted data, as shown above. This example uses the bit-array API because the `embedText` helper has a known conversion issue.

## Images and local resources

`Helper.read`, `Helper.write`, `Helper.readImage`, and `Helper.writeImage` resolve relative paths under `src/main/resources/`. Create that directory and any required subdirectories before using file-based examples.

The resource directory is excluded by `.gitignore`, and sample assets are not included. The optional file-based checks in `Main` expect:

```text
src/main/resources/
|-- text_one.txt
|-- image-formats/
|   |-- argb.png
|   |-- gray.png
|   |-- binary100.png
|   |-- binary150.png
|   |-- binary200.png
|   `-- binary220.png
`-- the-starry-night/
    |-- image.png
    |-- cover.png
    `-- hidden.png
```

The reference images must match the expected transformations for those comparisons to pass. The in-memory examples above need no resource files.

`Helper.writeImage` writes PNG files. Preserve hidden data in a lossless format: JPEG compression or subsequent image edits can change the pixel bits carrying the payload.

## Project structure

```text
.
|-- pom.xml                     Maven configuration; Java 21 target
|-- .idea/                      Shared IntelliJ settings and run configuration
`-- src/main/java/ch/epfl/cs107/
    |-- Main.java               Manual checks and demonstrations
    |-- Helper.java             File I/O, image I/O, dialogs, and random bytes
    |-- SignatureChecks.java    Course API signature checks
    |-- Challenge.java          Unfinished challenge entry point
    |-- crypto/
    |   |-- Encrypt.java
    |   `-- Decrypt.java
    |-- stegano/
    |   |-- ImageSteganography.java
    |   `-- TextSteganography.java
    `-- utils/
        |-- Bit.java
        |-- Image.java
        `-- Text.java
```

## Current limitations

- `Challenge.challenge()` still throws a `NOT IMPLEMENTED` exception.
- `Image.toGray` and `Image.toBinary` use swapped row/column indices and can fail on non-square images. `ImageSteganography.embedARGB` processes a new zero-filled array instead of the supplied image content.
- `TextSteganography.embedText` uses the message length instead of eight when copying each byte's bits. `revealText` can fail when the cover's pixel count is not divisible by eight.
- The CBC exercise requires a nonempty IV and a message length divisible by the IV length; it does not implement padding. Vigenere requires a nonempty key, and the one-time-pad methods expect a pad matching the message length.
- The three-argument `Encrypt.oneTimePad` overload mutates the plaintext and does not fill the caller's `result` array. Use the two-argument overload shown by the course API.
- Input validation relies heavily on assertions and is incomplete. `Helper.generateRandomBytes` uses `java.util.Random`, which is not a cryptographic random-number generator.

## Acknowledgments

Based on the EPFL CS-107 cryptography and steganography course scaffold. The supplied source files credit Hamza Remmal and Mehdi Alaoui; their original attribution is retained in the code.
