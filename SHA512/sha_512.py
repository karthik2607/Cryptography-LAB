def rotr(x, n):
    return ((x >> n) | (x << (64 - n))) & 0xFFFFFFFFFFFFFFFF

def sha512_preprocess(message):
    bit_length = len(message) * 8
    message += b"\x80"
    while (len(message) + 16) % 128 != 0:
        message += b"\x00"
        message += bit_length.to_bytes(16, "big")
        return message
    
def sha512_process_block(block, hash_values):
    w = [0] * 80
    for i in range(16):
        w[i] = int.from_bytes(block[i * 8 : (i + 1) * 8], "big")
        for i in range(16, 80):
            s0 = rotr(w[i - 15], 1) ^ rotr(w[i - 15], 8) ^ (w[i - 15] >> 7)
            s1 = rotr(w[i - 2], 19) ^ rotr(w[i - 2], 61) ^ (w[i - 2] >> 6)
            w[i] = (w[i - 16] + s0 + w[i - 7] + s1) & 0xFFFFFFFFFFFFFFFF
            a, b, c, d, e, f, g, h = hash_values
            for i in range(80):
                S1 = rotr(e, 14) ^ rotr(e, 18) ^ rotr(e, 41)
                ch = (e & f) ^ ((~e) & g)
                temp1 = h + S1 + ch + round_constants[i] + w[i]
                S0 = rotr(a, 28) ^ rotr(a, 34) ^ rotr(a, 39)
                maj = (a & b) ^ (a & c) ^ (b & c)
                temp2 = S0 + maj
                h = g
                g = f
                f = e
                e = (d + temp1) & 0xFFFFFFFFFFFFFFFF
                d = c
                c = b
                b = a
                a = (temp1 + temp2) & 0xFFFFFFFFFFFFFFFF
                if 0 <= i <= 10 or 70 <= i <= 79:
                    print(f"Round {i}")
                    print(f"a: {a:016x}")
                    print(f"b: {b:016x}")
                    print(f"c: {c:016x}")
                    print(f"d: {d:016x}")
                    print(f"e: {e:016x}")
                    print(f"f: {f:016x}")
                    print(f"g: {g:016x}")
                    print(f"h: {h:016x}")
                    print()
                    return [
(hash_values[0] + a) & 0xFFFFFFFFFFFFFFFF,
(hash_values[1] + b) & 0xFFFFFFFFFFFFFFFF,
(hash_values[2] + c) & 0xFFFFFFFFFFFFFFFF,
(hash_values[3] + d) & 0xFFFFFFFFFFFFFFFF,
(hash_values[4] + e) & 0xFFFFFFFFFFFFFFFF,
(hash_values[5] + f) & 0xFFFFFFFFFFFFFFFF,
(hash_values[6] + g) & 0xFFFFFFFFFFFFFFFF,
(hash_values[7] + h) & 0xFFFFFFFFFFFFFFFF,
]
                    
                    
def sha512(message):
    message = sha512_preprocess(message)
    hash_values = initial_hash_values.copy()
    for i in range(0, len(message), 128):
        block = message[i : i + 128]
        print(f"\nProcessing block {i//128 + 1}:")
        hash_values = sha512_process_block(block, hash_values)
        return "".join(f"{x:016x}" for x in hash_values)
    
    # Initial hash values and round constants for SHA-512
initial_hash_values = [
0x6A09E667F3BCC908,
0xBB67AE8584CAA73B,
0x3C6EF372FE94F82B,
0xA54FF53A5F1D36F1,
0x510E527FADE682D1,
0x9B05688C2B3E6C1F,
0x1F83D9ABFB41BD6B,
0x5BE0CD19137E2179,
]
round_constants = [
0x428A2F98D728AE22,
0x7137449123EF65CD,
0xB5C0FBCFEC4D3B2F,
0xE9B5DBA58189DBBC,
0x3956C25BF348B538,
0x59F111F1B605D019,
0x923F82A4AF194F9B,
0xAB1C5ED5DA6D8118,
0xD807AA98A3030242,
0x12835B0145706FBE,
0x243185BE4EE4B28C,
0x550C7DC3D5FFB4E2,
0x72BE5D74F27B896F,
0x80DEB1FE3B1696B1,
0x9BDC06A725C71235,
0xC19BF174CF692694,
0xE49B69C19EF14AD2,
0xEFBE4786384F25E3,
0x0FC19DC68B8CD5B5,
0x240CA1CC77AC9C65,
0x2DE92C6F592B0275,
0x4A7484AA6EA6E483,
0x5CB0A9DCBD41FBD4,
0x76F988DA831153B5,
0x983E5152EE66DFAB,
0xA831C66D2DB43210,
0xB00327C898FB213F,
0xBF597FC7BEEF0EE4,
0xC6E00BF33DA88FC2,
0xD5A79147930AA725,
0x06CA6351E003826F,
0x142929670A0E6E70,
0x27B70A8546D22FFC,
0x2E1B21385C26C926,
0x4D2C6DFC5AC42AED,
0x53380D139D95B3DF,
0x650A73548BAF63DE,
0x766A0ABB3C77B2A8,
0x81C2C92E47EDAEE6,
0x92722C851482353B,
0xA2BFE8A14CF10364,
0xA81A664BBC423001,
0xC24B8B70D0F89791,
0xC76C51A30654BE30,
0xD192E819D6EF5218,
0xD69906245565A910,
0xF40E35855771202A,
0x106AA07032BBD1B8,
0x19A4C116B8D2D0C8,
0x1E376C085141AB53,
0x2748774CDF8EEB99,
0x34B0BCB5E19B48A8,
0x391C0CB3C5C95A63,
0x4ED8AA4AE3418ACB,
0x5B9CCA4F7763E373,
0x682E6FF3D6B2B8A3,
0x748F82EE5DEFB2FC,
0x78A5636F43172F60,
0x84C87814A1F0AB72,
0x8CC702081A6439EC,
0x90BEFFFA23631E28,
0xA4506CEBDE82BDE9,
0xBEF9A3F7B2C67915,
0xC67178F2E372532B,
0xCA273ECEEA26619C,
0xD186B8C721C0C207,
0xEADA7DD6CDE0EB1E,
0xF57D4F7FEE6ED178,
0x06F067AA72176FBA,
0x0A637DC5A2C898A6,
0x113F9804BEF90DAE,
0x1B710B35131C471B,
0x28DB77F523047D84,
0x32CAAB7B40C72493,
0x3C9EBE0A15C9BEBC,
0x431D67C49C100D4C,
0x4CC5D4BECB3E42B6,
0x597F299CFC657E2A,
0x5FCB6FAB3AD6FAEC,
0x6C44198C4A475817,
]
# Test cases
print("Test case 1: message length less than 896 bits")
message1 = b"Hello, World!"
print("Input:", message1)
print("SHA-512 hash:")
hash1 = sha512(message1)
print("Hash Value")
print(hash1)
print("\n" + "=" * 50 + "\n")
print("Test case 2: message length equal to 896 bits")
message2 = b"A" * 112 # 896 bits = 112 bytes
print("Input:", message2)
print("SHA-512 hash:")
hash2 = sha512(message2)
print("Hash Value")
print(hash2)
