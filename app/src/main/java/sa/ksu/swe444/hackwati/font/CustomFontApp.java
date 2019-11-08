package sa.ksu.swe444.hackwati.font;

import android.app.Application;

public class CustomFontApp extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    FontsOverride.setDefaultFont(this, "DEFAULT", "font/ArbFONTS-GE-SS-Two-Light_28.otf");
    FontsOverride.setDefaultFont(this, "MONOSPACE", "font/ArbFONTS-GE-SS-Two-Light_28.otf");
    FontsOverride.setDefaultFont(this, "SERIF", "font/ArbFONTS-GE_SS_Two_Light-1.otf");
  }//onCreate
}//end class