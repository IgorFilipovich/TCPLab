package security

import java.security.*
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.spec.SecretKeySpec
import java.security.spec.X509EncodedKeySpec

import java.security.KeyFactory

import java.security.PublicKey
import javax.crypto.interfaces.DHPublicKey

class DiffieHellmanEncryptor {
    lateinit var publicKey: PublicKey
    private lateinit var keyAgreement: KeyAgreement
    private lateinit var secret: ByteArray
    private val cipherAlgo: String = "AES"

    init {
        exchangeParams()
    }

    fun getPublicKeyString(): String {
        val bytePublicKey = publicKey.encoded
        return Base64.getEncoder().encodeToString(bytePublicKey)
    }

    private fun exchangeParams() {
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance("DH")
        kpg.initialize(512)
        val kp = kpg.generateKeyPair()
        publicKey = kp.public
        keyAgreement = KeyAgreement.getInstance("DH")
        keyAgreement.init(kp.private)
    }

    private fun generateCipherKey(): Key {
        return SecretKeySpec(secret, 0, 32, cipherAlgo)
    }

    fun setReceiverPublicKey(key: String) {
        val bytePublicKey = Base64.getDecoder().decode(key)
        val factory = KeyFactory.getInstance("DH")
        val anotherKey = factory.generatePublic(X509EncodedKeySpec(bytePublicKey)) as DHPublicKey
        keyAgreement.doPhase(anotherKey, true);
        secret = keyAgreement.generateSecret();
    }

    fun encrypt(message: String): String {
        val key: Key = generateCipherKey()
        val cipher: Cipher = Cipher.getInstance(cipherAlgo)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encrypted = cipher.doFinal(message.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    fun decrypt(message: String): String {
        val key: Key = generateCipherKey()
        val cipher: Cipher = Cipher.getInstance(cipherAlgo)
        cipher.init(Cipher.DECRYPT_MODE, key)
        val decodedValue: ByteArray = Base64.getDecoder().decode(message)
        val decrypted: ByteArray = cipher.doFinal(decodedValue)
        return String(decrypted)
    }
}

