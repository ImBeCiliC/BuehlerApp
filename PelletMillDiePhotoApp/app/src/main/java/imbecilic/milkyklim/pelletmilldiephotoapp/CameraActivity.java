package imbecilic.milkyklim.pelletmilldiephotoapp;

/**
 * Created by Tobi on 04.02.2017.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.TextView;

public class CameraActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera.newInstance())
                    .commit();
        }
    }

}
