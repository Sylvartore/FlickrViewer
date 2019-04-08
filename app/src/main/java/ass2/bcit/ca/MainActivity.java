package ass2.bcit.ca;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String source = "https://www.flickr.com/services/feeds/photos_public.gne?tags=";
    static final String format = "&format=json";
    static final String defaultTag = "NHL";
    private String tag;
    private ProgressDialog pDialog;
    private DatabaseReference db;
    private List<Item> items;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        db = FirebaseDatabase.getInstance().getReference("database");
        tag = defaultTag;
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                DataSnapshot ds = dataSnapshot.child(tag);
                for (DataSnapshot snapshot : ds.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    items.add(item);
                }
                if (items.isEmpty()) {
                    new Fetch().execute(tag);
                } else {
                    ItemAdapter adapter = new ItemAdapter(MainActivity.this, items);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);

                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void onClickAddTag(View view) {
        EditText input = findViewById(R.id.tag);
        String newTag = input.getText().toString();
        if (!newTag.trim().isEmpty()) tag = newTag;
        else tag = defaultTag;
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                DataSnapshot ds = dataSnapshot.child(tag);
                for (DataSnapshot snapshot : ds.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    items.add(item);
                }
                if (items.isEmpty()) {
                    new Fetch().execute(tag);
                } else {
                    ItemAdapter adapter = new ItemAdapter(MainActivity.this, items);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);

                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void onClickFilter(View view) {
        EditText input = findViewById(R.id.keyword);
        String keyword = input.getText().toString();
        List<Item> res = items;
        if (!keyword.trim().isEmpty()) {
            res = new ArrayList<>();
            keyword = keyword.toLowerCase();
            for (Item item : items) {
                if (item.author.toLowerCase().contains(keyword) ||
                        item.title.toLowerCase().contains(keyword)) {
                    res.add(item);
                }
            }
        }
        ItemAdapter adapter = new ItemAdapter(MainActivity.this, res);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class Fetch extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... arg) {
            String topic = defaultTag;
            if (arg.length > 0) topic = arg[0];
            String url = source + topic + format;
            items = new HttpHandler().makeServiceCall(url);
            db.child(topic).setValue(items);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing()) pDialog.dismiss();

            ItemAdapter adapter = new ItemAdapter(MainActivity.this, items);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

}
