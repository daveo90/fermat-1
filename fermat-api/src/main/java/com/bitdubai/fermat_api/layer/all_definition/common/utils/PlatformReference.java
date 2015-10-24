package com.bitdubai.fermat_api.layer.all_definition.common.utils;

import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;

/**
 * The class <code>com.bitdubai.fermat_api.layer.all_definition.common.utils.PlatformReference</code>
 * haves all the information of a Platform Reference.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 22/10/2015.
 */
public class PlatformReference {

    private static final int HASH_PRIME_NUMBER_PRODUCT = 1523;
    private static final int HASH_PRIME_NUMBER_ADD = 2819;

    private final Platforms platform;

    public PlatformReference(final Platforms platform) {

        this.platform = platform;
    }

    public Platforms getPlatform() {
        return platform;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlatformReference)) return false;

        PlatformReference that = (PlatformReference) o;

        return platform.equals(that.platform);
    }

    @Override
    public int hashCode() {
        int c = 0;
        c += platform.hashCode();
        return 	HASH_PRIME_NUMBER_PRODUCT * HASH_PRIME_NUMBER_ADD + c;
    }

    @Override
    public String toString() {
        return "PlatformReference{" +
                "platform=" + platform +
                '}';
    }

}