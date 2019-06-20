package com.melloware.jintellitype;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;

public class DllLoadingIT {

    private static final int ALT_SHIFT_B = 89;
    
    @Test
    public void testDllLoading() {
        // skip test on non-windows platforms
        assumeTrue(JIntellitype.isJIntellitypeSupported());
        
        assertTrue(JIntellitype.isDllLoaded);
        
        JIntellitype.getInstance().registerHotKey(ALT_SHIFT_B, JIntellitype.MOD_ALT + JIntellitype.MOD_SHIFT, 'B');
        JIntellitype.getInstance().unregisterHotKey(ALT_SHIFT_B);
    }
}
