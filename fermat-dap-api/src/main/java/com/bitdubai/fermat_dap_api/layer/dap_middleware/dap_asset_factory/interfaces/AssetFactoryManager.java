package com.bitdubai.fermat_dap_api.layer.dap_middleware.dap_asset_factory.interfaces;

import com.bitdubai.fermat_dap_api.layer.all_definition.enums.State;
import com.bitdubai.fermat_dap_api.layer.dap_middleware.dap_asset_factory.exceptions.CantCreateAssetFactoryException;
import com.bitdubai.fermat_dap_api.layer.dap_middleware.dap_asset_factory.exceptions.CantCreateEmptyAssetFactoryException;
import com.bitdubai.fermat_dap_api.layer.dap_middleware.dap_asset_factory.exceptions.CantDeleteAsserFactoryException;
import com.bitdubai.fermat_dap_api.layer.dap_middleware.dap_asset_factory.exceptions.CantGetAssetFactoryException;
import com.bitdubai.fermat_dap_api.layer.dap_middleware.dap_asset_factory.exceptions.CantSaveAssetFactoryException;

import java.util.List;

/**
 * Created by franklin on 07/09/15.
 */
public interface AssetFactoryManager {
    //Getters
    /**
     * This method returns the information stored about the Asset Factory
     */
    AssetFactory getAssetFactoryByPublicKey(String assetPublicKey) throws CantGetAssetFactoryException;

    /**
     * This method returns the information stored about the all Asset Factory by issuerIdentityKey.
     */
    List<AssetFactory> getAssetFactoryByIssuer(String issuerIdentityPublicKey) throws CantGetAssetFactoryException;

    /**
     * This method returns the information stored about the all Asset Factory by state
     */
    List<AssetFactory> getAssetFactoryByState(State state) throws CantGetAssetFactoryException;

    //CRUD
    /**
     * This method create an empty object AssetFactory
     */
    AssetFactory createEmptyAssetFactory() throws CantCreateEmptyAssetFactoryException;

    /**
     * This method save object AssetFactory in database
     */
    void saveAssetFactory(AssetFactory assetFactory) throws CantSaveAssetFactoryException;

    /**
     * This method mark object AssetFactory in database with close
     */
    void markAssetFactoryInFinal(AssetFactory assetFactory) throws CantSaveAssetFactoryException;

    /**
     * This method remove object AssetFactory in database
     */
    void removeAssetFactory(AssetFactory assetFactory) throws CantDeleteAsserFactoryException;

    /**
     * This method retrieves the bitcoin wallet and check if you have available balance
     */
    long getAvailableBalance(long amount);

    /**
     * TThis method publishes the asset digital object with the number and amount of Asset
     */
    void publishAsset(AssetFactory assetFactory);
}