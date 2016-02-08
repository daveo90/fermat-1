package com.bitdubai.fermat_cbp_plugin.layer.actor.crypto_customer.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.DeviceDirectory;
import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantDeleteRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginBinaryFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_api.layer.world.interfaces.Currency;
import com.bitdubai.fermat_cbp_api.all_definition.identity.ActorIdentity;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantCreateNewActorExtraDataException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantCreateNewCustomerIdentityWalletRelationshipException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantGetListActorExtraDataException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantGetListCustomerIdentityWalletRelationshipException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantUpdateActorExtraDataException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.interfaces.ActorExtraData;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.interfaces.CustomerIdentityWalletRelationship;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.interfaces.QuotesExtraData;
import com.bitdubai.fermat_cbp_api.layer.negotiation.exceptions.CantGetListClauseException;
import com.bitdubai.fermat_cbp_plugin.layer.actor.crypto_customer.developer.bitdubai.version_1.exceptions.CantGetCryptoCustomerActorProfileImageException;
import com.bitdubai.fermat_cbp_plugin.layer.actor.crypto_customer.developer.bitdubai.version_1.exceptions.CantInitializeCryptoCustomerActorDatabaseException;
import com.bitdubai.fermat_cbp_plugin.layer.actor.crypto_customer.developer.bitdubai.version_1.exceptions.CantPersistProfileImageExtraDataException;
import com.bitdubai.fermat_cbp_plugin.layer.actor.crypto_customer.developer.bitdubai.version_1.structure.ActorExtraDataIdentity;
import com.bitdubai.fermat_cbp_plugin.layer.actor.crypto_customer.developer.bitdubai.version_1.structure.ActorExtraDataInformation;
import com.bitdubai.fermat_cbp_plugin.layer.actor.crypto_customer.developer.bitdubai.version_1.structure.CustomerIdentityWalletRelationshipInformation;
import com.bitdubai.fermat_cbp_plugin.layer.actor.crypto_customer.developer.bitdubai.version_1.structure.QuotesExtraDataInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by angel on 19/11/15.
 */
public class CryptoCustomerActorDao {

    private Database database;
    private PluginDatabaseSystem pluginDatabaseSystem;
    private PluginFileSystem pluginFileSystem;
    private UUID pluginId;

    /*
        Builders
    */

        public CryptoCustomerActorDao(PluginDatabaseSystem pluginDatabaseSystem, PluginFileSystem pluginFileSystem, UUID pluginId) {
            this.pluginDatabaseSystem = pluginDatabaseSystem;
            this.pluginFileSystem = pluginFileSystem;
            this.pluginId = pluginId;
        }

    /*
        Public methods
     */

        public void initializeDatabase() throws CantInitializeCryptoCustomerActorDatabaseException {
            try {
                database = this.pluginDatabaseSystem.openDatabase(pluginId, pluginId.toString());
            } catch (CantOpenDatabaseException cantOpenDatabaseException) {
                throw new CantInitializeCryptoCustomerActorDatabaseException(cantOpenDatabaseException.getMessage());
            } catch (DatabaseNotFoundException e) {
                CryptoCustomerActorDatabaseFactory CustomerActorDatabaseFactory = new CryptoCustomerActorDatabaseFactory(pluginDatabaseSystem);
                try {
                    database = CustomerActorDatabaseFactory.createDatabase(pluginId, pluginId.toString());
                } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                    throw new CantInitializeCryptoCustomerActorDatabaseException(cantCreateDatabaseException.getMessage());
                }
            }

            new pruebaExtraData(this);
        }

        public CustomerIdentityWalletRelationship createNewCustomerIdentityWalletRelationship(ActorIdentity identity, String walletPublicKey) throws CantCreateNewCustomerIdentityWalletRelationshipException {

            try {
                DatabaseTable RelationshipTable = this.database.getTable(CryptoCustomerActorDatabaseConstants.CRYPTO_CUSTOMER_ACTOR_RELATIONSHIP_TABLE_NAME);
                DatabaseTableRecord recordToInsert   = RelationshipTable.getEmptyRecord();
                UUID relationshipId = UUID.randomUUID();
                loadRecordAsNew(
                        recordToInsert,
                        relationshipId,
                        identity.getPublicKey(),
                        walletPublicKey
                );
                RelationshipTable.insertRecord(recordToInsert);
                return constructCryptoCustomerActorRelationshipFromRecord(recordToInsert);
            } catch (CantInsertRecordException e) {
                throw new CantCreateNewCustomerIdentityWalletRelationshipException(e.DEFAULT_MESSAGE, e, "", "");
            } catch (InvalidParameterException e) {
                throw new CantCreateNewCustomerIdentityWalletRelationshipException(e.DEFAULT_MESSAGE, e, "", "");
            } catch (CantGetListClauseException e) {
                throw new CantCreateNewCustomerIdentityWalletRelationshipException(e.DEFAULT_MESSAGE, e, "", "");
            }
        }

        public Collection<CustomerIdentityWalletRelationship> getAllCustomerIdentityWalletRelationship() throws CantGetListCustomerIdentityWalletRelationshipException {
            try {
                DatabaseTable RelationshipTable = this.database.getTable(CryptoCustomerActorDatabaseConstants.CRYPTO_CUSTOMER_ACTOR_RELATIONSHIP_TABLE_NAME);
                RelationshipTable.loadToMemory();
                List<DatabaseTableRecord> records = RelationshipTable.getRecords();
                RelationshipTable.clearAllFilters();
                Collection<CustomerIdentityWalletRelationship> resultados = new ArrayList<CustomerIdentityWalletRelationship>();
                for (DatabaseTableRecord record : records) {
                    resultados.add(constructCryptoCustomerActorRelationshipFromRecord(record));
                }
                return resultados;
            } catch (CantLoadTableToMemoryException e) {
                throw new CantGetListCustomerIdentityWalletRelationshipException(e.DEFAULT_MESSAGE, e, "", "");
            } catch (InvalidParameterException e) {
                throw new CantGetListCustomerIdentityWalletRelationshipException(e.DEFAULT_MESSAGE, e, "", "");
            } catch (CantGetListClauseException e) {
                throw new CantGetListCustomerIdentityWalletRelationshipException(e.DEFAULT_MESSAGE, e, "", "");
            }
        }

        public CustomerIdentityWalletRelationship getCustomerIdentityWalletRelationshipByIdentity(String publicKey) throws CantGetListCustomerIdentityWalletRelationshipException {
            try {
                DatabaseTable RelationshipTable = this.database.getTable(CryptoCustomerActorDatabaseConstants.CRYPTO_CUSTOMER_ACTOR_RELATIONSHIP_TABLE_NAME);
                RelationshipTable.addStringFilter(CryptoCustomerActorDatabaseConstants.CRYPTO_CUSTOMER_ACTOR_RELATIONSHIP_CUSTOMER_PUBLIC_KEY_COLUMN_NAME, publicKey, DatabaseFilterType.EQUAL);
                RelationshipTable.loadToMemory();
                List<DatabaseTableRecord> records = RelationshipTable.getRecords();
                RelationshipTable.clearAllFilters();
                for (DatabaseTableRecord record : records) {
                    return  constructCryptoCustomerActorRelationshipFromRecord(record);
                }
                return null;
            } catch (CantLoadTableToMemoryException e) {
                throw new CantGetListCustomerIdentityWalletRelationshipException(e.DEFAULT_MESSAGE, e, "", "");
            } catch (InvalidParameterException e) {
                throw new CantGetListCustomerIdentityWalletRelationshipException(e.DEFAULT_MESSAGE, e, "", "");
            } catch (CantGetListClauseException e) {
                throw new CantGetListCustomerIdentityWalletRelationshipException(e.DEFAULT_MESSAGE, e, "", "");
            }
        }

        public CustomerIdentityWalletRelationship getCustomerIdentityWalletRelationshipByWallet(String walletPublicKey) throws CantGetListCustomerIdentityWalletRelationshipException {
            try {
                DatabaseTable RelationshipTable = this.database.getTable(CryptoCustomerActorDatabaseConstants.CRYPTO_CUSTOMER_ACTOR_RELATIONSHIP_TABLE_NAME);
                RelationshipTable.addStringFilter(CryptoCustomerActorDatabaseConstants.CRYPTO_CUSTOMER_ACTOR_RELATIONSHIP_WALLET_COLUMN_NAME, walletPublicKey, DatabaseFilterType.EQUAL);
                RelationshipTable.loadToMemory();
                List<DatabaseTableRecord> records = RelationshipTable.getRecords();
                RelationshipTable.clearAllFilters();
                for (DatabaseTableRecord record : records) {
                    return  constructCryptoCustomerActorRelationshipFromRecord(record);
                }
                return null;
            } catch (CantLoadTableToMemoryException e) {
                throw new CantGetListCustomerIdentityWalletRelationshipException(e.DEFAULT_MESSAGE, e, "", "");
            } catch (InvalidParameterException e) {
                throw new CantGetListCustomerIdentityWalletRelationshipException(e.DEFAULT_MESSAGE, e, "", "");
            } catch (CantGetListClauseException e) {
                throw new CantGetListCustomerIdentityWalletRelationshipException(e.DEFAULT_MESSAGE, e, "", "");
            }
        }

    /*
        Private methods
     */

        private void loadRecordAsNew(
                DatabaseTableRecord databaseTableRecord,
                UUID   relationshipId,
                String publicKeyCustomer,
                String walletPublicKey
        ) {
            databaseTableRecord.setUUIDValue(CryptoCustomerActorDatabaseConstants.CRYPTO_CUSTOMER_ACTOR_RELATIONSHIP_RELATIONSHIP_ID_COLUMN_NAME, relationshipId);
            databaseTableRecord.setStringValue(CryptoCustomerActorDatabaseConstants.CRYPTO_CUSTOMER_ACTOR_RELATIONSHIP_CUSTOMER_PUBLIC_KEY_COLUMN_NAME, publicKeyCustomer);
            databaseTableRecord.setStringValue(CryptoCustomerActorDatabaseConstants.CRYPTO_CUSTOMER_ACTOR_RELATIONSHIP_WALLET_COLUMN_NAME, walletPublicKey);
        }

        private CustomerIdentityWalletRelationship newCryptoCustomerActorRelationship(
                UUID   relationshipId,
                String publicKeyCustomer,
                String   walletId
        ){
            return new CustomerIdentityWalletRelationshipInformation(relationshipId, publicKeyCustomer, walletId);
        }

        private CustomerIdentityWalletRelationship constructCryptoCustomerActorRelationshipFromRecord(DatabaseTableRecord record) throws InvalidParameterException, CantGetListClauseException {
            UUID    relationshipId       = record.getUUIDValue(CryptoCustomerActorDatabaseConstants.CRYPTO_CUSTOMER_ACTOR_RELATIONSHIP_RELATIONSHIP_ID_COLUMN_NAME);
            String  publicKeyCustomer     = record.getStringValue(CryptoCustomerActorDatabaseConstants.CRYPTO_CUSTOMER_ACTOR_RELATIONSHIP_CUSTOMER_PUBLIC_KEY_COLUMN_NAME);
            String    walletId            = record.getStringValue(CryptoCustomerActorDatabaseConstants.CRYPTO_CUSTOMER_ACTOR_RELATIONSHIP_WALLET_COLUMN_NAME);
            return newCryptoCustomerActorRelationship(relationshipId, publicKeyCustomer, walletId);
        }


    /*==============================================================================================
    *
    *   Actor Extra Data
    *
    *==============================================================================================*/


        public void createCustomerExtraData(ActorExtraData actorExtraData) throws CantCreateNewActorExtraDataException {
            try {
                DatabaseTable table = this.database.getTable(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_TABLE_NAME);
                DatabaseTableRecord record   = table.getEmptyRecord();
                record.setStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_ID_COLUMN_NAME, UUID.randomUUID().toString());
                record.setStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME, actorExtraData.getBrokerIdentity().getPublicKey());
                record.setStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_ALIAS_COLUMN_NAME, actorExtraData.getBrokerIdentity().getAlias());
                record.setStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_CUSTOMER_PUBLIC_KEY_COLUMN_NAME, actorExtraData.getCustomerPublicKey());
                table.insertRecord(record);
                if(actorExtraData.getBrokerIdentity().getProfileImage() != null) {
                    try {
                        persistNewCryptoCustomerIdentityProfileImage(actorExtraData.getBrokerIdentity().getPublicKey(), actorExtraData.getBrokerIdentity().getProfileImage());
                    } catch (CantPersistProfileImageExtraDataException e) {
                        // TODO: manejar las excepciones
                    }
                }
            } catch (CantInsertRecordException e) {
                throw new CantCreateNewActorExtraDataException(e.DEFAULT_MESSAGE, e, "", "");
            }
        }

        public void createActorQoutes(ActorExtraData actorExtraData) throws CantCreateNewActorExtraDataException{
            try {
                DatabaseTable table = this.database.getTable(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_TABLE_NAME);
                DatabaseTableRecord record;
                for(QuotesExtraData quote : actorExtraData.getQuotes()){
                    record = table.getEmptyRecord();
                    record.setUUIDValue(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_QUOTE_ID_COLUMN_NAME, UUID.randomUUID());
                    record.setStringValue(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME, actorExtraData.getBrokerIdentity().getPublicKey());
                    record.setStringValue(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_MERCHANDISE_COLUMN_NAME, quote.getMerchandise().getCode());
                    record.setStringValue(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_PAYMENT_CURRENCY_COLUMN_NAME, quote.getPaymentCurrency().getCode());
                    record.setFloatValue(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_PRICE_COLUMN_NAME, quote.getPrice());
                    table.insertRecord(record);
                }
            } catch (CantInsertRecordException e) {
                throw new CantCreateNewActorExtraDataException(e.DEFAULT_MESSAGE, e, "", "");
            }
        }

        public void updateCustomerExtraData(ActorExtraData actorExtraData) throws CantUpdateActorExtraDataException {
            try {
                DatabaseTable table = this.database.getTable(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_TABLE_NAME);
                DatabaseTableRecord record = table.getEmptyRecord();
                table.addStringFilter(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME, actorExtraData.getBrokerIdentity().getPublicKey(), DatabaseFilterType.EQUAL);
                record.setStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_ALIAS_COLUMN_NAME, actorExtraData.getBrokerIdentity().getAlias());
                table.updateRecord(record);
                updateQuotes(actorExtraData);
                //updatePlasforms(actorExtraData);
                if(actorExtraData.getBrokerIdentity().getProfileImage() != null) {
                    try {
                        updateCryptoBrokerIdentityProfileImage(actorExtraData.getBrokerIdentity().getPublicKey(), actorExtraData.getBrokerIdentity().getProfileImage());
                    } catch (CantPersistProfileImageExtraDataException e) {
                    }
                }
            } catch (CantUpdateRecordException e) {
                throw new CantUpdateActorExtraDataException(e.DEFAULT_MESSAGE, e, "", "");
            }
        }

        public void updateQuotes(ActorExtraData actorExtraData) throws CantUpdateActorExtraDataException {
            try {
                DatabaseTable table = this.database.getTable(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_TABLE_NAME);
                table.addStringFilter(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME, actorExtraData.getBrokerIdentity().getPublicKey(), DatabaseFilterType.EQUAL);

                DatabaseTableRecord record = table.getEmptyRecord();
                record.setStringValue(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME, actorExtraData.getBrokerIdentity().getPublicKey());
                table.deleteRecord(record);

                this.createActorQoutes(actorExtraData);
            } catch (CantDeleteRecordException e) {
                throw new CantUpdateActorExtraDataException(e.DEFAULT_MESSAGE, e, "", "");
            } catch (CantCreateNewActorExtraDataException e) {
                throw new CantUpdateActorExtraDataException(e.DEFAULT_MESSAGE, e, "", "");
            }
        }

        public Collection<ActorExtraData> getAllActorExtraData() throws CantGetListActorExtraDataException {
            try {
                DatabaseTable table = this.database.getTable(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_TABLE_NAME);
                table.loadToMemory();
                List<DatabaseTableRecord> records = table.getRecords();
                table.clearAllFilters();
                if (records.isEmpty() ){
                     return null;
                }else {
                    Collection<ActorExtraData> actoresExtraDatas = new ArrayList<ActorExtraData>();
                    for (DatabaseTableRecord record : records) {
                        String alias = record.getStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_ALIAS_COLUMN_NAME);
                        String brokerPublicKey = record.getStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME);
                        String customerPublicKey = record.getStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_CUSTOMER_PUBLIC_KEY_COLUMN_NAME);
                        byte[] image = null;
                        try {
                            image = getCryptoCustomerIdentityProfileImagePrivateKey(brokerPublicKey);
                        } catch (CantGetCryptoCustomerActorProfileImageException e) {
                            // TODO: manejar las excepciones
                        }
                        ActorIdentity identity = new ActorExtraDataIdentity(alias, brokerPublicKey, image);
                        Collection<QuotesExtraData> quotes = this.getQuotesByIdentity(brokerPublicKey, customerPublicKey);
                        Map<Currency, Collection<Platforms>> currencies = null;
                        ActorExtraData data = new ActorExtraDataInformation(
                                customerPublicKey,
                                identity,
                                quotes,
                                currencies
                        );
                        actoresExtraDatas.add(data);
                    }
                    return actoresExtraDatas;
                }
            } catch (CantLoadTableToMemoryException e) {
                throw new CantGetListActorExtraDataException(e.DEFAULT_MESSAGE, e, "", "");
            }
        }

        public ActorExtraData getActorExtraDataByPublicKey(String _publicKey) throws CantGetListActorExtraDataException {
            try {
                DatabaseTable table = this.database.getTable(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_TABLE_NAME);
                table.addStringFilter(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME, _publicKey, DatabaseFilterType.EQUAL);
                table.loadToMemory();
                List<DatabaseTableRecord> records = table.getRecords();
                table.clearAllFilters();
                if (records.isEmpty() ){
                    return null;
                }else {
                    for (DatabaseTableRecord record : records) {
                        String alias = record.getStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_ALIAS_COLUMN_NAME);
                        String publicKey = record.getStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME);
                        String customerPublicKey = record.getStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_CUSTOMER_PUBLIC_KEY_COLUMN_NAME);
                        byte[] image = null;
                        try {
                            image = getCryptoCustomerIdentityProfileImagePrivateKey(publicKey);
                        } catch (CantGetCryptoCustomerActorProfileImageException e) {
                            // TODO: manejar las excepciones
                        }
                        ActorIdentity identity = new ActorExtraDataIdentity(alias, publicKey, image);
                        Collection<QuotesExtraData> quotes = this.getQuotesByIdentity(publicKey, customerPublicKey);
                        Map<Currency, Collection<Platforms>> currencies = null;
                        ActorExtraData data = new ActorExtraDataInformation(
                                customerPublicKey,
                                identity,
                                quotes,
                                currencies
                        );
                        return data;
                    }
                    return null;
                }
            } catch (CantLoadTableToMemoryException e) {
                throw new CantGetListActorExtraDataException(e.DEFAULT_MESSAGE, e, "", "");
            }
        }

        public ActorIdentity getActorInformationByPublicKey(String _publicKey) throws CantGetListActorExtraDataException {
            try {
                DatabaseTable table = this.database.getTable(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_TABLE_NAME);
                table.addStringFilter(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME, _publicKey, DatabaseFilterType.EQUAL);
                table.loadToMemory();
                List<DatabaseTableRecord> records = table.getRecords();
                table.clearAllFilters();
                if (records.isEmpty() ){
                    return null;
                }else {
                    for (DatabaseTableRecord record : records) {
                        String alias = record.getStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_ALIAS_COLUMN_NAME);
                        String publicKey = record.getStringValue(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME);
                        byte[] image = null;
                        try {
                            image = getCryptoCustomerIdentityProfileImagePrivateKey(publicKey);
                        } catch (CantGetCryptoCustomerActorProfileImageException e) {
                            // TODO: manejar las excepciones
                        }
                        return new ActorExtraDataIdentity(alias, publicKey, image);
                    }
                    return null;
                }
            } catch (CantLoadTableToMemoryException e) {
                throw new CantGetListActorExtraDataException(e.DEFAULT_MESSAGE, e, "", "");
            }
        }

        private Collection<QuotesExtraData> getQuotesByIdentity(String brokerPublicKey, String customerPublicKey) throws CantGetListActorExtraDataException {
            DatabaseTable table = this.database.getTable(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_TABLE_NAME);
            table.addStringFilter(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME, brokerPublicKey, DatabaseFilterType.EQUAL);
            table.addStringFilter(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_CUSTOMER_PUBLIC_KEY_COLUMN_NAME, customerPublicKey, DatabaseFilterType.EQUAL);
            try {
                table.loadToMemory();
            } catch (CantLoadTableToMemoryException e) {
                throw new CantGetListActorExtraDataException(e.DEFAULT_MESSAGE, e, "", "");
            }
            List<DatabaseTableRecord> records = table.getRecords();
            table.clearAllFilters();
            if (records.isEmpty() ){
                return null;
            }else {
                Collection<QuotesExtraData> quotes = new ArrayList<>();
                for (DatabaseTableRecord record : records) {
                    Currency mer = null;
                    Currency pay = null;
                    try {
                        mer = FiatCurrency.getByCode(record.getStringValue(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_MERCHANDISE_COLUMN_NAME));
                    } catch (InvalidParameterException e) {
                        try {
                            mer = CryptoCurrency.getByCode(record.getStringValue(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_MERCHANDISE_COLUMN_NAME));
                        } catch (InvalidParameterException e1) {
                            throw new CantGetListActorExtraDataException(e.DEFAULT_MESSAGE, e1, "", "");
                        }
                    }
                    try {
                        pay = FiatCurrency.getByCode(record.getStringValue(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_PAYMENT_CURRENCY_COLUMN_NAME));
                    } catch (InvalidParameterException e) {
                        try {
                            pay = CryptoCurrency.getByCode(record.getStringValue(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_PAYMENT_CURRENCY_COLUMN_NAME));
                        } catch (InvalidParameterException e1) {
                            throw new CantGetListActorExtraDataException(e.DEFAULT_MESSAGE, e1, "", "");
                        }
                    }
                    Float pri = record.getFloatValue(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_PRICE_COLUMN_NAME);
                    QuotesExtraData quote = new QuotesExtraDataInformation(UUID.randomUUID(), mer, pay, pri);
                    quotes.add(quote);
                }
                return quotes;
            }
        }

        public boolean existBrokerExtraData(String publicKey){
            try {
                DatabaseTable table = this.database.getTable(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_TABLE_NAME);
                table.addStringFilter(CryptoCustomerActorDatabaseConstants.ACTOR_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME, publicKey, DatabaseFilterType.EQUAL);
                table.loadToMemory();
                List<DatabaseTableRecord> records = table.getRecords();
                table.clearAllFilters();
                if (records.isEmpty() ){
                    return false;
                }else {
                    return true;
                }
            } catch (CantLoadTableToMemoryException e) {
                return false;
            }
        }

        public boolean existBrokerExtraDataQuotes(String brokerPublicKey, String customerPublicKey){
            try {
                DatabaseTable table = this.database.getTable(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_TABLE_NAME);
                table.addStringFilter(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_BROKER_PUBLIC_KEY_COLUMN_NAME, brokerPublicKey, DatabaseFilterType.EQUAL);
                table.addStringFilter(CryptoCustomerActorDatabaseConstants.QUOTE_EXTRA_DATA_CUSTOMER_PUBLIC_KEY_COLUMN_NAME, customerPublicKey, DatabaseFilterType.EQUAL);
                table.loadToMemory();
                List<DatabaseTableRecord> records = table.getRecords();
                table.clearAllFilters();
                if (records.isEmpty() ){
                    return false;
                }else {
                    return true;
                }
            } catch (CantLoadTableToMemoryException e) {
                return false;
            }
        }

    /*
        FileSystem
     */

        public static final String CRYPTO_CUSTOMER_ACTOR_PROFILE_IMAGE_FILE_NAME = "cryptoCustomerActorProfileImage";

        private void persistNewCryptoCustomerIdentityProfileImage(String publicKey,byte[] profileImage) throws CantPersistProfileImageExtraDataException {
            try {
                PluginBinaryFile file = this.pluginFileSystem.createBinaryFile(pluginId,
                        DeviceDirectory.LOCAL_USERS.getName(),
                        this.CRYPTO_CUSTOMER_ACTOR_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                        FilePrivacy.PRIVATE,
                        FileLifeSpan.PERMANENT
                );
                file.setContent(profileImage);
                file.persistToMedia();
            } catch (CantPersistFileException e) {
                throw new CantPersistProfileImageExtraDataException("CAN'T PERSIST PROFILE IMAGE ", e, "Error persist file.", null);
            } catch (CantCreateFileException e) {
                throw new CantPersistProfileImageExtraDataException("CAN'T PERSIST PROFILE IMAGE ", e, "Error creating file.", null);
            } catch (Exception e) {
                throw  new CantPersistProfileImageExtraDataException("CAN'T PERSIST PROFILE IMAGE ", FermatException.wrapException(e),"", "");
            }
        }

        private byte[] getCryptoCustomerIdentityProfileImagePrivateKey(String publicKey) throws CantGetCryptoCustomerActorProfileImageException {
            byte[] profileImage;
            try {
                PluginBinaryFile file = this.pluginFileSystem.getBinaryFile(pluginId,
                        DeviceDirectory.LOCAL_USERS.getName(),
                        this.CRYPTO_CUSTOMER_ACTOR_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                        FilePrivacy.PRIVATE,
                        FileLifeSpan.PERMANENT
                );
                file.loadFromMedia();
                profileImage = file.getContent();
            } catch (CantLoadFileException e) {
                throw new CantGetCryptoCustomerActorProfileImageException("CAN'T GET IMAGE PROFILE ", e, "Error loaded file.", null);
            } catch (FileNotFoundException | CantCreateFileException e) {
                throw new CantGetCryptoCustomerActorProfileImageException("CAN'T GET IMAGE PROFILE ", e, "Error getting developer identity private keys file.", null);
            } catch (Exception e) {
                throw new CantGetCryptoCustomerActorProfileImageException("CAN'T GET IMAGE PROFILE ",FermatException.wrapException(e),"", "");
            }
            return profileImage;
        }

        private void updateCryptoBrokerIdentityProfileImage(String publicKey, byte[] profileImage) throws CantPersistProfileImageExtraDataException {
            try {
                this.pluginFileSystem.deleteBinaryFile(pluginId,
                        DeviceDirectory.LOCAL_USERS.getName(),
                        this.CRYPTO_CUSTOMER_ACTOR_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                        FilePrivacy.PRIVATE,
                        FileLifeSpan.PERMANENT);
                PluginBinaryFile file = this.pluginFileSystem.createBinaryFile(pluginId,
                        DeviceDirectory.LOCAL_USERS.getName(),
                        this.CRYPTO_CUSTOMER_ACTOR_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                        FilePrivacy.PRIVATE,
                        FileLifeSpan.PERMANENT
                );
                file.setContent(profileImage);
                file.persistToMedia();
            } catch (CantPersistFileException e) {
                throw new CantPersistProfileImageExtraDataException("CAN'T PERSIST PROFILE IMAGE ", e, "Error persist file.", null);
            } catch (CantCreateFileException e) {
                throw new CantPersistProfileImageExtraDataException("CAN'T PERSIST PROFILE IMAGE ", e, "Error creating file.", null);
            } catch (FileNotFoundException e) {
                throw new CantPersistProfileImageExtraDataException("CAN'T PERSIST PROFILE IMAGE ", e, "Error removing file.", null);
            }
        }

}