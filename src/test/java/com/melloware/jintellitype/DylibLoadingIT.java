package com.melloware.jintellitype;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class DylibLoadingIT {
    private static final int ALT_SHIFT_B = 89;

    @Test
    public void testDylibLoading() {
        // skip test on non-mac platforms
        String osType = JIntellitype.getOsType();
        assumeTrue(osType.startsWith("mac"));
        assumeTrue(JIntellitype.isJIntellitypeSupported(osType));

        assertTrue(JIntellitype.isDylibLoaded);

        JIntellitype.getInstance().registerHotKey(ALT_SHIFT_B, JIntellitype.MOD_ALT_OR_OPTION + JIntellitype.MOD_SHIFT, 'B');
        JIntellitype.getInstance().unregisterHotKey(ALT_SHIFT_B);
    }
}
