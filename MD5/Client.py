import socket
import struct
import math

# Define the left rotation function
def left_rotate(x, amount):
    x &= 0xFFFFFFFF
    return ((x << amount) | (x >> (32 - amount))) & 0xFFFFFFFF

# MD5 Constants
s = [7, 12, 17, 22] * 4 + [5, 9, 14, 20] * 4 + [4, 11, 16, 23] * 4 + [6, 10, 15, 21] * 4
K = [int(abs(math.sin(i + 1)) * 2**32) & 0xFFFFFFFF for i in range(64)]

# Initialize variables
A0 = 0x67452301
B0 = 0xEFCDAB89
C0 = 0x98BADCFE
D0 = 0x10325476

# MD5 Padding
def md5_padding(message):
    message = bytearray(message, 'utf-8')
    original_length = len(message) * 8  # Length in bits
    message.append(0x80)
    
    while (len(message) * 8) % 512 != 448:
        message.append(0)
    
    message += struct.pack('<Q', original_length)
    return message

# Process MD5 Hash and Print Each Round
def md5_process(message):
    global A0, B0, C0, D0
    for chunk_index in range(0, len(message), 64):
        chunk = message[chunk_index:chunk_index + 64]
        M = struct.unpack('<16I', chunk)

        A, B, C, D = A0, B0, C0, D0

        for i in range(64):
            if i < 16:
                F = (B & C) | (~B & D)
                g = i
            elif 16 <= i < 32:
                F = (D & B) | (~D & C)
                g = (5 * i + 1) % 16
            elif 32 <= i < 48:
                F = B ^ C ^ D
                g = (3 * i + 5) % 16
            else:
                F = C ^ (B | ~D)
                g = (7 * i) % 16

            F = (F + A + K[i] + M[g]) & 0xFFFFFFFF
            A = D
            D = C
            C = B
            B = (B + left_rotate(F, s[i])) & 0xFFFFFFFF

        A0 = (A0 + A) & 0xFFFFFFFF
        B0 = (B0 + B) & 0xFFFFFFFF
        C0 = (C0 + C) & 0xFFFFFFFF
        D0 = (D0 + D) & 0xFFFFFFFF

# MD5 Hash Function
def md5_hash(message):
    global A0, B0, C0, D0
    A0, B0, C0, D0 = 0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476  # Reset initial values
    message = md5_padding(message)
    md5_process(message)
    final_hash = ''.join(f'{x:02x}' for x in struct.unpack('<4I', struct.pack('<4I', A0, B0, C0, D0)))
    return final_hash

# Client-side socket code
def client_send_mac(message, key, host, port):
    mac_input = key + message
    mac = md5_hash(mac_input)
    
    # Create socket connection to the server
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((host, port))
        data = f"{message}|{key}|{mac}"
        s.sendall(data.encode())
        response = s.recv(1024).decode()
        print("Server response:", response)

# Example usage
if __name__ == "__main__":
    message = input("Enter the message: ")
    key = input("Enter the key: ")
    client_send_mac(message, key, 'localhost', 65432)
