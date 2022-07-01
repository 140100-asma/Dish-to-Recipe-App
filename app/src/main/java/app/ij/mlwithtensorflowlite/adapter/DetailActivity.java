package app.ij.mlwithtensorflowlite.adapter;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import app.ij.mlwithtensorflowlite.R;

public class DetailActivity extends AppCompatActivity {

    private WebView webView;



    //private ImageView imageView;
    //private TextView titleTExtView, detailTextView;
    //private String detailString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

       /* imageView = findViewById(R.id.imageView);
        titleTExtView = findViewById(R.id.textView);
        detailTextView = findViewById(R.id.detailTextView);

        titleTExtView.setText(getIntent().getStringExtra("title"));
        //Picasso.get().load(getIntent().getStringExtra("image")).into(imageView);
        Content content = new Content();
        content.execute();*/

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient ());
        webView.loadUrl("https://www.allrecipes.com/search/results/?search=biryani");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    /*private class Content extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            detailTextView.setText(detailString);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String baseUrl = "https://www.allrecipes.com/search/results/?search=briyani";
                String detailUrl = getIntent().getStringExtra("detailUrl");

                String url = baseUrl + detailUrl;

                Document doc = Jsoup.connect(url).get();

                //Elements data = doc.select("div.recipe-meta-item");

                Elements data = doc.select("div.two-col-content-wrapper");

                detailString = data.select("div.recipe-meta-item")
                        .text();


            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }*/
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
