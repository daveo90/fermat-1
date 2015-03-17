package com.bitdubai.fermat_api.layer._7_world;

import com.bitdubai.fermat_api.layer._1_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer._1_definition.money.CryptoAddress;

import java.util.UUID;

/**
 * Created by ciencias on 3/12/15.
 */
public interface CryptoWallet {

    public long getWalletBalance(CryptoCurrency cryptoCurrency,UUID walletId);
    
    public long getAddressBalance(CryptoAddress cryptoAddress);
    
    public void sendCrypto (UUID walletId,CryptoCurrency cryptoCurrency, long amount, CryptoAddress cryptoAddress);
    
    
}
