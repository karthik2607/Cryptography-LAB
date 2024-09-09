import socket
import random

def generate_dh_keys(p, g):
    private_key = random.randint(1, p - 1)
    public_key = pow(g, private_key, p)
    return private_key, public_key

def main():
    p = 23
    g = 5

    bob_private, bob_public = generate_dh_keys(p, g)

    s = socket.socket()
    s.bind(('localhost', 12346))
    s.listen(5)

    conn, addr = s.accept()
    print("Connection established with Eve")

    alice_public = int(conn.recv(1024).decode())
    print(f"Bob received public key (from Eve): {alice_public}")

    print(f"Bob sends public key: {bob_public}")
    conn.send(str(bob_public).encode())

    shared_secret = pow(alice_public, bob_private, p)
    print(f"Bob's shared secret key: {shared_secret}")
    conn.close()

if __name__ == '__main__':
    main()
