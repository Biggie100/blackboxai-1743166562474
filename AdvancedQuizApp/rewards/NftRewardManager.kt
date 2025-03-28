package com.advancedquiz.rewards

import com.advancedquiz.auth.WalletConnectManager
import org.web3j.contracts.ERC721
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider

class NftRewardManager(
    private val walletConnectManager: WalletConnectManager
) {
    private val web3j: Web3j = Web3j.build(HttpService("https://mainnet.infura.io/v3/YOUR_INFURA_KEY"))
    private val contractAddress = "0xYOUR_NFT_CONTRACT_ADDRESS"

    suspend fun mintRewardNft(level: Int, score: Int): String? {
        return try {
            val credentials = walletConnectManager.getWalletCredentials()
            val nftContract = ERC721.load(
                contractAddress,
                web3j,
                credentials,
                DefaultGasProvider()
            )

            val tokenId = System.currentTimeMillis().toString()
            val transactionReceipt = nftContract
                .mint(walletConnectManager.session?.walletAddress, tokenId)
                .send()

            transactionReceipt.transactionHash
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getOwnedNfts(): List<NftReward> {
        return try {
            val nftContract = ERC721.load(
                contractAddress,
                web3j,
                walletConnectManager.getWalletCredentials(),
                DefaultGasProvider()
            )

            val balance = nftContract.balanceOf(walletConnectManager.session?.walletAddress).send()
            (0 until balance.toInt()).map { index ->
                val tokenId = nftContract.tokenOfOwnerByIndex(
                    walletConnectManager.session?.walletAddress,
                    index.toBigInteger()
                ).send()
                NftReward(
                    tokenId = tokenId.toString(),
                    imageUrl = "https://nft.storage/$tokenId.png"
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

data class NftReward(
    val tokenId: String,
    val imageUrl: String
)