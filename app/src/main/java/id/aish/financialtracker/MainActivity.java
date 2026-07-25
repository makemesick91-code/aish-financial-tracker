package id.aish.financialtracker;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Aish Financial Tracker — native WebView wrapper.
 *
 * Memuat aplikasi HTML offline dari folder assets dan mengaktifkan
 * penyimpanan lokal (localStorage) agar seluruh data pengguna
 * tersimpan permanen di perangkat.
 */
public class MainActivity extends Activity {

    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        web = new WebView(this);
        web.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);      // localStorage — data tersimpan permanen
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(false);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setMediaPlaybackRequiresUserGesture(false);

        web.setWebViewClient(new WebViewClient());

        // WebChromeClient wajib ada agar dialog JavaScript (alert/confirm) berfungsi.
        // Tanpa ini, confirm() selalu mengembalikan "false" di WebView.
        web.setWebChromeClient(new WebChromeClient());

        // Jembatan untuk menyimpan file backup ke perangkat.
        web.addJavascriptInterface(new AppBridge(), "AishApp");

        web.loadUrl("file:///android_asset/index.html");

        setContentView(web);
    }

    /** Dipanggil dari JavaScript: window.AishApp.saveBackup(json, namaFile) */
    public class AppBridge {
        @JavascriptInterface
        public String saveBackup(String json, String filename) {
            try {
                byte[] data = json.getBytes(StandardCharsets.UTF_8);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10+ : simpan ke folder Download tanpa perlu izin apa pun.
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "application/json");
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                    Uri uri = getContentResolver().insert(
                            MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                    if (uri == null) return "";

                    OutputStream out = getContentResolver().openOutputStream(uri);
                    if (out == null) return "";
                    out.write(data);
                    out.flush();
                    out.close();
                    return "folder Download";
                } else {
                    // Android 7–9 : simpan ke folder khusus aplikasi (tanpa izin).
                    File dir = getExternalFilesDir(null);
                    if (dir == null) dir = getFilesDir();
                    File file = new File(dir, filename);
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(data);
                    out.flush();
                    out.close();
                    return file.getAbsolutePath();
                }
            } catch (Exception e) {
                return "";
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && web != null && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
