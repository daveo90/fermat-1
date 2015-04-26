package com.bitdubai.fermat_core.layer._9_communication.cloud_server;

import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.layer._9_communication.CantStartSubsystemException;
import com.bitdubai.fermat_api.layer._9_communication.CommunicationSubsystem;
import com.bitdubai.fermat_dmp_plugin.layer._9_communication.cloud_server.developer.bitdubai.DeveloperBitDubai;

/**
 * Created by loui on 26/04/15.
 */
public class CloudServerSubsystem implements CommunicationSubsystem {

    private Plugin plugin;

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void start() throws CantStartSubsystemException {
        /**
         * I will choose from the different Developers available which implementation to use. Right now there is only
         * one, so it is not difficult to choose.
         */

        try {
            DeveloperBitDubai developerBitDubai = new DeveloperBitDubai();
            plugin = developerBitDubai.getPlugin();
        }
        catch (Exception e)
        {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException();
        }

    }


}
