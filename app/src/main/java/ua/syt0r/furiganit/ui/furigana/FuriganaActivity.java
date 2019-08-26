package ua.syt0r.furiganit.ui.furigana;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import ua.syt0r.furiganit.R;

public class FuriganaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furigana);
        overridePendingTransition(R.anim.activity_start,R.anim.activity_finish);

        String data = getIntent().getStringExtra("data");
        WebView webView = findViewById(R.id.webview);
        webView.loadData(data,"text/html; charset=utf-8", "utf-8");
    }

    public void Click(View v){
        overridePendingTransition(R.anim.activity_start,R.anim.activity_finish);
        finish();
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.activity_start,R.anim.activity_finish);
        super.onBackPressed();
    }
}
