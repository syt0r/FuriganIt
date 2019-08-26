package ua.syt0r.furiganit.model.repository.overlay;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;

/*
* Saves and restores position of overlay window
* */
public class OverlayDataRepository {

    private static final String PREF_NAME = "overlay_window_pos";
    private static final String X_POS = "xPos";
    private static final String Y_POS = "yPos";
    private static final String TIMEOUT = "timeout";

    private SharedPreferences sharedPreferences;

    public OverlayDataRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public Point getPosition() {
        int x = sharedPreferences.getInt(X_POS, 0);
        int y = sharedPreferences.getInt(Y_POS, 0);
        return new Point(x, y);
    }

    public void setPosition(int xPos, int yPos) {
        sharedPreferences.edit()
                .putInt(X_POS, xPos)
                .putInt(Y_POS, yPos)
                .apply();
    }

    public long getTimeout() {
        return sharedPreferences.getLong("timeout",5);
    }

    public void setTimeout(long value) {
        sharedPreferences.edit().putLong(TIMEOUT, value).apply();
    }


}
