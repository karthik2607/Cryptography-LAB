import socket
import random

def generate_dh_keys(p, g):
    private_key = random.randint(1, p - 1)
    public_key = pow(g, private_key, p)
    return private_key, public_key

def main():
    p = 23
    g = 5

    alice_private, alice_public = generate_dh_keys(p, g)

    s = socket.socket()
    s.connect(('localhost', 12345))
    print(f"Alice sends public key: {alice_public}")

    s.send(str(alice_public).encode())

    bob_public = int(s.recv(1024).decode())
    print(f"Alice received public key (from Eve): {bob_public}")

    shared_secret = pow(bob_public, alice_private, p)
    print(f"Alice's shared secret key: {shared_secret}")
    s.close()

if __name__ == '__main__':
    main()
