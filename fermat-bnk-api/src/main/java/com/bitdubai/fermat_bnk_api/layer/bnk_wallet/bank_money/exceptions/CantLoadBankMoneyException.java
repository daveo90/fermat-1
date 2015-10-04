package com.bitdubai.fermat_bnk_api.layer.bnk_wallet.bank_money.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by Yordin Alayn on 18.09.15.
 */
public class CantLoadBankMoneyException extends FermatException{
    public static final String DEFAULT_MESSAGE = "Falled To Load Bank Money Wallet.";
    public CantLoadBankMoneyException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }
}
