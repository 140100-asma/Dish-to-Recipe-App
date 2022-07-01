package app.ij.mlwithtensorflowlite;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import app.ij.mlwithtensorflowlite.adapter.ParseItemAdapter;
import app.ij.mlwithtensorflowlite.model.ParseItemModel;

public class ShowRecipe extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParseItemAdapter adapter;
    private ArrayList<ParseItemModel> parseItems = new ArrayList<>();
    private ProgressBar progressBar;
    String key=getIntent ().getStringExtra ("key");

    public String url;
    public String biryaniURL="https://www.allrecipes.com/search/results/?search=briyani";
    public String friedNoodlesURL="https://www.allrecipes.com/search/results/?search=fried+noodles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParseItemAdapter(parseItems, this);
        recyclerView.setAdapter(adapter);

        Content content = new Content();
        content.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        // Get the search view and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); //Do not iconfy the widget; expand it by default

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                ArrayList<ParseItemModel> newList = new ArrayList<>();
                for (ParseItemModel parseItem : parseItems) {
                    String title = parseItem.getTitle().toLowerCase();

                    // you can specify as many conditions as you like
                    if (title.contains(newText)) {
                        newList.add(parseItem);
                    }
                }
                // create method in adapter
                adapter.setFilter(newList);

                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);

        return true;

    }

    private class Content extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(ShowRecipe.this, android.R.anim.fade_in));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(ShowRecipe.this, android.R.anim.fade_out));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            switch(key){
                case "Biryani":
                    url=biryaniURL;
                    break;
                case "Fried noodles":
                    url=friedNoodlesURL;
            }

            try {

                Document doc = Jsoup.connect(url).get();

                Elements data = doc.select("div.card__detailsContainer");
                int size = data.size();
                Log.d("doc", "doc: "+doc);
                Log.d("data", "data: "+data);
                Log.d("size", ""+size);
                for (int i = 0; i < size; i++) {
                    String imgUrl = data.select("div.inner-container.js-inner-container")
                            .select("img")
                            .eq(i)
                            .attr("src");

                    String title = data.select("div.card__detailsContainer")
                            .select("h3")
                            .eq(i)
                            .text();

                    String detailUrl = data.select("div.card__detailsContainer")

                            .select("a")
                            .eq(i)
                            .attr("href");

                    parseItems.add(new ParseItemModel(imgUrl, title, detailUrl));
                    Log.d("items", "img: " + imgUrl + " . title: " + title);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}