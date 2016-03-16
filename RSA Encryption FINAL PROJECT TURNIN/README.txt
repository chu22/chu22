This is the readme for the RSA Encryption Program written by Ana Salinas and Lawrence Chu. Below you will find instructions on how the GUI works, as well as details on how some operations were implemented.

The GUI has 12 buttons:

1) Enter a key
This button allows for useres to enter 2 primes for key generation. It will generate both keys and then store them in the program's memory, which allows the user to immediately use them for any of the encryption/decryption functionalities.
However, the button does NOT save the keys to files. If the user generates new keys in anyway, the currently generated/loaded keys will disappear. The user must save use the save options in order to create the key files.
The option does error check for primality of the inputs before proceding with generation.

2) Generate a key
This option prompts the user for a prime number length and then generates 2 random primes of that length, which it uses to generate 2 keys. The randomization is done by simply continuously generating random numbers of the specified length and testing for primality 
Similar to the first option, the button does NOT save the keys to files. If the user generates new keys in anyway, the currently generated/loaded keys will disappear.The user must save use the save options in order to create the key files.

3/4) Save a public/private key
These 2 options save the public/private key currently stored in memory to a file, which the user will input in the filechooser. They will create a new file if the specified file does not exist.

5/6) Load a private/public key
These 2 buttons prompts the user for a public/private key file and loads it into memory.
The program does NOT error check for key file validity, and it is up to the user to ensure the key file is in the proper format and contains valid numbers for RSA encryption.

7/8) Block/Unblock
These buttons will execute message blocking after prompting the user for a block size, input message/blocked file, and output blocked/message file. They will create a new file if the specified output file does not exist.

9) Encrypt Blocked
This button will execute the encrypt algorithms on a user specified blocked file and public key file, outputting it to a user specified output file. It will create a new file if the output file currently does not exist.
The option will NOT error check for blockfile format validity and the user must ensure that the blockfile is in a valid format.

10) Decrypt Blocked
This button will execute the decrytion algorithm on a user specified encrypted file and private key file, outputting a blocked file of some user specified blocking size. It will create a new file if the user specified output file currently does not exist.

11) Encrypt/Decrypt
These options simply combines the options blocking/encryption or decryption/unblocking functionalities in one execution, allowing a user to create an encrypted file from a message file, or create a message file from an encrypted file, skipping the intermmediate creation of a blocked file.


Some Algorithm Implementations:

1) basic +,-,*, and / operations were done using the basic long addition, subtraction, multiplication, and division algorithms.

2) exponentiation and modular exponentiation was done with exponentiation by squaring methods.

3) primality testing was implemented using the naive algorithm, shown here: http://www.algolist.net/Algorithms/Number_theoretic/Primality_test_naive