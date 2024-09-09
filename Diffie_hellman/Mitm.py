import socket

def mitm_server():
    s = socket.socket()
    s.bind(('localhost', 12345))
    s.listen(5)

    alice_conn, addr = s.accept()
    print("Connection established with Alice")

    alice_public = int(alice_conn.recv(1024).decode())
    print(f"Eve intercepted Alice's public key: {alice_public}")

    bob_conn = socket.socket()
    bob_conn.connect(('localhost', 12346))
    print("Eve connected to Bob")

    eve_private, eve_public = 6, 8
    print(f"Eve sends her own public key to Bob: {eve_public}")
    bob_conn.send(str(eve_public).encode())

    bob_public = int(bob_conn.recv(1024).decode())
    print(f"Eve intercepted Bob's public key: {bob_public}")

    print(f"Eve forwards Bob's public key to Alice: {bob_public}")
    alice_conn.send(str(bob_public).encode())

    shared_secret_alice = pow(alice_public, eve_private, 23)
    shared_secret_bob = pow(bob_public, eve_private, 23)
    print(f"Eve's shared secret with Alice: {shared_secret_alice}")
    print(f"Eve's shared secret with Bob: {shared_secret_bob}")

    alice_conn.close()
    bob_conn.close()
    s.close()

if __name__ == '__main__':
    mitm_server()
