package hanu.a2_2001040205;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import hanu.a2_2001040205.database.ProductManager;
import hanu.a2_2001040205.fragment.CartFragment;
import hanu.a2_2001040205.fragment.ProductFragment;
import hanu.a2_2001040205.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity  {
    private final List<Product> shoppingItems = new ArrayList<>();
    private final String ProductsLink = "https://hanu-congnv.github.io/mpr-cart-api/products.json";
    public static int HIGH;
    public static int WIDE;
    private ProductManager manageProduct;
    ProductFragment productFragment;

    private String consequences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manageProduct = ProductManager.getInstance(this);
        initCartData();

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        HIGH = point.y;
        WIDE = point.x;

    }

    private void initCartData() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                URL link;
                HttpURLConnection urlConnection;
                try {
                    link = new URL(ProductsLink);
                    urlConnection = (HttpURLConnection) link.openConnection();
                    urlConnection.connect();
                    InputStream inputTheStream = urlConnection.getInputStream();
                    Scanner scanner = new Scanner(inputTheStream);
                    StringBuilder consequence = new StringBuilder();
                    String lineWord;
                    while (scanner.hasNextLine()) {
                        lineWord = scanner.nextLine();
                        consequence.append(lineWord);
                    }

                MainActivity.this.consequences = consequence.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray TheItems = new JSONArray(consequences);

                            for (int i = 0; i < TheItems.length(); i++) {
                                JSONObject product = TheItems.getJSONObject(i);
                                Product shoppingMoreItems = new Product();
                                shoppingMoreItems.setId(product.getInt("id"));
                                shoppingMoreItems.setName(product.getString("name"));
                                shoppingMoreItems.setPrice(product.getInt("unitPrice"));
//                                shoppingMoreItems.setCategory(product.getString("category"));
                                shoppingMoreItems.setImageUrl(product.getString("thumbnail"));


                                shoppingItems.add(shoppingMoreItems);
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                productFragment = new ProductFragment(shoppingItems, manageProduct);

                                fragmentManager.beginTransaction()
                                        .replace(R.id.TheFragmentContainer, productFragment)
                                        .commit();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.view_the_cart) {

            List<Product> TheCarts = manageProduct.all();
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new CartFragment(TheCarts, manageProduct);
            fragmentManager.beginTransaction()
                    .replace(R.id.TheFragmentContainer, fragment, "VIEW_CART")
                    .addToBackStack("back")
                    .commit();
            return true;
        } else if (menuItem.getItemId() == R.id.back_to_main_screen) {
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu and this will add more items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void searchProducts(){
        EditText search = findViewById(R.id.finding_text);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<Product> filterList = shoppingItems.stream().filter((shoppingItems)-> {
                    return shoppingItems.getName().toLowerCase().equals(charSequence.toString().toLowerCase());
                }).collect(Collectors.toList());
                if (charSequence.toString().trim().isEmpty()){
                    productFragment.setProduct(shoppingItems);
                } else {
                    productFragment.setProduct(filterList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}

//public class MainActivity extends AppCompatActivity implements AsyncResponse {
//    private List<Product> shoppingProducts = new ArrayList<>();
//    private final String productUrl = "https://mpr-cart-api.herokuapp.com/products";
//    public static int WIDTH;
//    public static int HEIGHT;
//    private ProductManager productManager;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        productManager = ProductManager.getInstance(this);
//
//        JsonTask jsonTask = new JsonTask();
//        jsonTask.task = this;
//        jsonTask.execute(productUrl);
//
//
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        WIDTH = size.x;
//        HEIGHT = size.y;
//    }
//
//    @Override
//    public void onTaskCompleted() {
//        FragmentManager manager = getSupportFragmentManager();
//        Fragment fragment = new ProductFragment(shoppingProducts, productManager);
//
//        manager.beginTransaction()
//                .replace(R.id.TheFragmentContainer, fragment)
//                .commit();
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.view_the_cart){
//            List<Product> carts = productManager.all();
//
//            FragmentManager manager = getSupportFragmentManager();
//            Fragment fragment = new CartFragment(carts, productManager);
//
//            manager.beginTransaction()
//                    .replace(R.id.TheFragmentContainer, fragment, "VIEW_CART" )
//                    .addToBackStack("back")
//                    .commit();
//
//            return true;
//        }
//
//        return false;
//    }
//
//    private class JsonTask extends AsyncTask<String, String, String> {
//        private AsyncResponse task;
//
//        @Override
//        protected String doInBackground(String... strings) {
//            URL url;
//            HttpURLConnection urlConnection;
//            try {
//                url = new URL(strings[0]);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.connect();
//                InputStream is = urlConnection.getInputStream();
//                Scanner sc = new Scanner(is);
//                StringBuilder result = new StringBuilder();
//                String line;
//                while (sc.hasNextLine()) {
//                    line = sc.nextLine();
//                    result.append(line);
//                }
//                Log.i("RESULT", "" + result);
//                return result.toString();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (result == null) {
//                return;
//            }
//            try {
//                JSONArray products = new JSONArray(result);
//
//                for(int i = 0; i < products.length(); i++){
//                    JSONObject product = products.getJSONObject(i);
//                    Product shoppingProduct = new Product();
//
//                    shoppingProduct.setId(product.getInt("id"));
//                    shoppingProduct.setImageUrl(product.getString("thumbnail"));
//                    shoppingProduct.setName(product.getString("name"));
//                    shoppingProduct.setPrice(product.getInt("unitPrice"));
//
//                    shoppingProducts.add(shoppingProduct);
//                }
//
//                task.onTaskCompleted();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}