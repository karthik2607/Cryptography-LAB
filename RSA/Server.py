import socket

# RSA functions

def decrypt(pk, ciphertext):
    key, n = pk
    aux = [str(pow(int(char), key, n)) for char in ciphertext]
    plain = [chr(int(char2)) for char2 in aux]
    return ''.join(plain)

if __name__ == '__main__':
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind(('localhost', 12345))
    server_socket.listen(1)
    print("Server is listening on port 12345...")

    conn, addr = server_socket.accept()
    print(f"Connection from {addr} has been established.")

    # Receive the data from the client
    data_str = conn.recv(4096).decode()
    data = dict(item.split("=") for item in data_str.split(";"))

    encrypted_msg = list(map(int, data["encrypted_msg"].split(',')))
    d, n = map(int, data["private_key"].split(','))
    p = int(data["p"])
    q = int(data["q"])
    n = int(data["n"])
    phi = int(data["phi"])
    e = int(data["e"])

    print(f"p value: {p}")
    print(f"q value: {q}")
    print(f"n = p * q: {n}")
    print(f"phi(n): {phi}")
    print(f"e: {e}")
    print(f"d: {d}")

    print(" - Decrypting message with private key . . .")
    decrypted_msg = decrypt((d, n), encrypted_msg)

    print(f" - The decrypted message is: {decrypted_msg}")

    conn.close()
