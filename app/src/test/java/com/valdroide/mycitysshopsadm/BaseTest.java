package com.valdroide.mycitysshopsadm;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import java.io.IOException;

/**
 * Created by LEO on 22/2/2017.
 */

public abstract class BaseTest {
    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        if (RuntimeEnvironment.application != null) {
            ShadowApplication shadowApplication = Shadows.shadowOf(RuntimeEnvironment.application);
            shadowApplication.grantPermissions("android.permission.INTERNET");
        }
    }
}
