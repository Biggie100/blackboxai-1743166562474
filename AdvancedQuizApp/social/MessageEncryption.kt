package com.advancedquiz.social

import android.util.Base64
import com.advancedquiz.auth.WalletConnectManager
import java.security.*
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class MessageEncryption @Inject constructor(
    private val walletConnectManager: WalletConnectManager
) {
    private val keyPair: KeyPair by lazy { generateKeyPair() }
    private val keyFactory = KeyFactory.getInstance("EC")
    private val keyAgreement = KeyAgreement.getInstance("ECDH")
    private val cipher = Cipher.getInstance("AES/GCM/NoPadding")

    fun getPublicKey(): String {
        return Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
    }

    fun encryptMessage(message: String, recipientPublicKey: String): String {
        val sharedSecret = generateSharedSecret(recipientPublicKey)
        val secretKey = SecretKeySpec(sharedSecret, "AES")
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(message.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decryptMessage(encryptedMessage: String, senderPublicKey: String): String {
        val sharedSecret = generateSharedSecret(senderPublicKey)
        val secretKey = SecretKeySpec(sharedSecret, "AES")
        
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedMessage = Base64.decode(encryptedMessage, Base64.DEFAULT)
        return String(cipher.doFinal(decodedMessage))
    }

    private fun generateSharedSecret(publicKeyStr: String): ByteArray {
        val publicKeyBytes = Base64.decode(publicKeyStr, Base64.DEFAULT)
        val publicKey = keyFactory.generatePublic(X509EncodedKeySpec(publicKeyBytes))
        
        keyAgreement.init(keyPair.private)
        keyAgreement.doPhase(publicKey, true)
        return keyAgreement.generateSecret()
    }

    private fun generateKeyPair(): KeyPair {
        val keyGen = KeyPairGenerator.getInstance("EC")
        keyGen.initialize(256)
        return keyGen.generateKeyPair()
    }

    companion object {
        init {
            Security.addProvider(org.bouncycastle.jce.provider.BouncyCastleProvider())
        }
    }
}