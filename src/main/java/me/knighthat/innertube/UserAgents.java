package me.knighthat.innertube;

import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAgents {

// START: Static fields/functions
    @NotNull
    public static final String CHROME_WINDOWS = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.3";

    @NotNull
    public static final String IOS = "com.google.ios.youtube/20.14.2 (iPhone12,1; U; CPU iOS 17_4_1 like Mac OS X;)";

    @NotNull
    public static final String TVHTML5_SIMPLY_EMBEDDED_PLAYER = "Mozilla/5.0 (PlayStation; PlayStation 4/12.02) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.4 Safari/605.1.15";

    @NotNull
    public static final String ANDROID = "com.google.android.youtube/20.10.38 (Linux; U; Android 11) gzip";

    @NotNull
    public static final String ANDROID_VR = "com.google.android.apps.youtube.vr.oculus/1.61.48 (Linux; U; Android 12; en_US; Oculus Quest 3; Build/SQ3A.220605.009.A1; Cronet/132.0.6808.3)";
// END: Static fields/functions
}
