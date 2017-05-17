package imbecilic.milkyklim.pelletmilldiephotoapp;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Tobias on 10.02.2017.
 */

public class Heveticaneue {
    private static boolean fontsLoaded = false;

    private static Typeface[] fonts = new Typeface[1];

    private static String fontPath = "fonts/helveticaneue.ttf";

    public static Typeface getTypeface(Context context){
        if(!fontsLoaded){
            loadFonts(context);
        }
        return fonts[0];
    }

    private static void loadFonts(Context context){
        fonts[0] = Typeface.createFromAsset(context.getAssets(), fontPath);
        fontsLoaded = true;
    }
}
