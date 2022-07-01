package app.ij.mlwithtensorflowlite;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

//import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BlogActivity extends AppCompatActivity {
    TextView title, summary_txt, author;
    ImageView imageView;
    String title_txt, summary, author_txt, image, link;
    WebView webView;
    String blogDoc;
    Elements data;
    Document document;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        title = findViewById(R.id.title_txt_id);
        imageView = findViewById(R.id.imageView);
        author = findViewById(R.id.author_txt_id);
        summary_txt = findViewById(R.id.summary_txt_id);
        webView = findViewById(R.id.webView_id);

        title_txt = getIntent().getStringExtra("title");
        author_txt = getIntent().getStringExtra("author");
        summary = getIntent().getStringExtra("summary");
        image = getIntent().getStringExtra("image");
        link = getIntent().getStringExtra("postLink");

        title.setText(title_txt);
        author.setText(author_txt);
        summary_txt.setText(summary);
        Picasso.get().load(image).into(imageView);

        Content content = new Content();
        content.execute();



    }

    private class Content extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            webView.setWebChromeClient(new WebChromeClient());
            webView.loadDataWithBaseURL(null,blogDoc,"text/html","utf-8",null);
            webView.requestFocus();
            progressDialog.hide();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                document = Jsoup.connect(link).get();
                data = document.select("div.searchResults__contentContainer");

                int i = 0;
                blogDoc = data.select("div.entry-content.si-entry").eq(i).html();


            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.hide();
            }
            try {
                document = Jsoup.connect(link).get();
                data = document.select("div.post-category");




            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.hide();
            }


            return null;
        }


    }
}