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
        print(f"\nProcessing Block {chunk_index // 64 + 1}:\n")

        for i in range(64):
            if i < 16:
                F = (B & C) | (~B & D)
                g = i
                round_num = 1
            elif 16 <= i < 32:
                F = (D & B) | (~D & C)
                g = (5 * i + 1) % 16
                round_num = 2
            elif 32 <= i < 48:
                F = B ^ C ^ D
                g = (3 * i + 5) % 16
                round_num = 3
            else:
                F = C ^ (B | ~D)
                g = (7 * i) % 16
                round_num = 4

            F = (F + A + K[i] + M[g]) & 0xFFFFFFFF
            A = D
            D = C
            C = B
            B = (B + left_rotate(F, s[i])) & 0xFFFFFFFF

            # Print the status after each round
            print(f"Round {round_num}, Step {i + 1}: A = {A:08x}, B = {B:08x}, C = {C:08x}, D = {D:08x}")

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
    print(f"\nFinal Hash Value: {final_hash}")
    return final_hash

# Server-side socket code
def server_receive_mac(host, port):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((host, port))
        s.listen()
        print("Server listening for connections...")

        conn, addr = s.accept()
        with conn:
            print(f"Connected by {addr}")
            data = conn.recv(1024).decode()
            if data:
                message, key, received_mac = data.split('|')
                print(f"Message: {message}, Key: {key}, Received MAC: {received_mac}\n")
                
                # Recalculate the MAC
                recalculated_mac = md5_hash(key + message)
                
                # Compare received MAC with recalculated MAC
                if recalculated_mac == received_mac:
                    response = "MAC verification successful. Message is authentic."
                else:
                    response = "MAC verification failed. Message is not authentic."

                conn.sendall(response.encode())

# Example usage
if __name__ == "__main__":
    server_receive_mac('localhost', 65432)
