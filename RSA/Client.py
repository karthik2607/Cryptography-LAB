import socket
import random
import secrets

# RSA functions

def gcd(a, b):
    while b != 0:
        a, b = b, a % b
    return a

def multiplicative_inverse(e, phi):
    d = 0
    x1 = 0
    x2 = 1
    y1 = 1
    temp_phi = phi

    while e > 0:
        temp1 = temp_phi // e
        temp2 = temp_phi - temp1 * e
        temp_phi = e
        e = temp2

        x = x2 - temp1 * x1
        y = d - temp1 * y1

        x2 = x1
        x1 = x
        d = y1
        y1 = y

    if temp_phi == 1:
        return d + phi

def is_prime(num):
    if num <= 1:
        return False
    if num <= 3:
        return True
    if num % 2 == 0 or num % 3 == 0:
        return False
    i = 5
    while i * i <= num:
        if num % i == 0 or num % (i + 2) == 0:
            return False
        i += 6
    return True

def generate_large_prime(bits=32):
    while True:
        num = secrets.randbits(bits)
        if is_prime(num):
            return num

def generate_key_pair(bits=32):
    p = generate_large_prime(bits)
    q = generate_large_prime(bits)

    if p == q:
        raise ValueError('p and q cannot be equal')

    n = p * q
    phi = (p - 1) * (q - 1)

    e = 65537  
    d = multiplicative_inverse(e, phi)

    return ((e, n), (d, n), p, q, n, phi)  

def encrypt(pk, plaintext):
    key, n = pk
    cipher = [pow(ord(char), key, n) for char in plaintext]
    return cipher

if __name__ == '__main__':
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect(('localhost', 12345))

    print(" - Generating your public / private key-pairs now . . .")
    public, private, p, q, n, phi = generate_key_pair(32)

    print(" - Your public key is ", public)
    print(" - Your private key is ", private)

    message = input(" - Enter a message to encrypt with your public key: ")
    encrypted_msg = encrypt(public, message)
    encrypted_msg_str = ','.join(map(str, encrypted_msg))

    data = {
        "encrypted_msg": encrypted_msg_str,
        "private_key": f"{private[0]},{private[1]}",
        "p": str(p),
        "q": str(q),
        "n": str(n),
        "phi": str(phi),
        "e": str(public[0])
    }

    data_str = ';'.join(f"{key}={value}" for key, value in data.items())

    print(" - Sending encrypted message and RSA details to server . . .")
    client_socket.send(data_str.encode())

    client_socket.close()
