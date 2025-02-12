package in.jgssolution.study;

import android.view.View;

public class VersionActivity {
    // Make StatusBar static so it can be called from Activity
    public static void StatusBar(android.view.Window window) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Set the status bar color and make it light
            window.setStatusBarColor(window.getContext().getColor(R.color.appbar));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
