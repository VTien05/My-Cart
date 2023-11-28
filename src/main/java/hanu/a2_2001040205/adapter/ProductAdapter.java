package hanu.a2_2001040205.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import hanu.a2_2001040205.MainActivity;
import hanu.a2_2001040205.R;
import hanu.a2_2001040205.database.ProductManager;
import hanu.a2_2001040205.model.Product;
import hanu.a2_2001040205.util.CurrencyFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<Product> products;
    private final ProductManager manageTheProduct;

    public ProductAdapter(List<Product> products, ProductManager manageTheProduct) {
        this.products = products;
        this.manageTheProduct = manageTheProduct;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context cont = viewGroup.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(cont);

        View iView = layoutInflater.inflate(R.layout.product_recyclerview, viewGroup, false);

        return new ProductHolder(iView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder productHolder, int locate) {
        Product production = products.get(locate);

        productHolder.bind(production);
    }

    //count the items
    @Override
    public int getItemCount() {
        return products.size();
    }

    //recyclerView of the product holder
    public class ProductHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImage;
        private final TextView itemName;
        private final TextView itemPrice;
        private final ImageButton addToTheCart;


        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            addToTheCart = itemView.findViewById(R.id.send_to_cart);
        }

        public void bind(Product product) {
            new DownloadImageTask(itemImage).execute(product.getImageUrl());
            itemName.setText(product.getName());
            itemPrice.setText(CurrencyFormatter.format((long) product.getPrice()));
            itemPrice.setWidth(MainActivity.WIDE / 2 - 100);

            //handle click event later
            addToTheCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manageTheProduct.add(product);

                    Toast toast = Toast.makeText(itemView.getContext(), "Item has been add to the cart!!!!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    //update the list of the product
    public void updateList(List<Product> addProducts) {
        this.products = addProducts;
        notifyDataSetChanged();
    }

    //download the image in the url
//    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView imageView;
//
//        public DownloadImageTask(ImageView imageView) {
//            this.imageView = imageView;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String theUrldisplay = urls[0];
//            Bitmap something = null;
//            try {
//                InputStream insth = new java.net.URL(theUrldisplay).openStream();
//                something = BitmapFactory.decodeStream(insth);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return something;
//        }
//
//        protected void onPostExecute(Bitmap theResult) {
//            imageView.setImageBitmap(theResult);
//        }
//    }
    private static class DownloadImageTask {
        private final ImageView imageView;
        private final Executor executor;
        private final Handler handler;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
            this.executor = Executors.newSingleThreadExecutor();
            this.handler = new Handler(Looper.getMainLooper());
        }

        public void execute(String url) {
            executor.execute(() -> {
                Bitmap bitmap = null;
                try {
                    InputStream in = new URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap finalBitmap = bitmap;
                handler.post(() -> imageView.setImageBitmap(finalBitmap));
            });
        }
    }

}



