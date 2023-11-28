package hanu.a2_2001040205.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import hanu.a2_2001040205.MainActivity;
import hanu.a2_2001040205.R;
import hanu.a2_2001040205.database.ProductManager;
import hanu.a2_2001040205.model.Product;
import hanu.a2_2001040205.util.CurrencyFormatter;
import hanu.a2_2001040205.util.RecyclerViewItemClick;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder>{

    private List<Product> items;
    private RecyclerViewItemClick myOnClickListener;
    private ProductManager productManager;

    public CartAdapter(List<Product> products, ProductManager productManager) {
        this.items = products;
        this.productManager = productManager;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup par, int viewType) {
        Context cont = par.getContext();

        LayoutInflater inflaterr = LayoutInflater.from(cont);

        View iView = inflaterr.inflate(R.layout.cart_recycleview, par, false );

        return new CartHolder(iView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holding, int location) {
        Product products = items.get(location);

        holding.bind(products);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class CartHolder extends RecyclerView.ViewHolder{
        private ImageView itemImage;
        private TextView itemName;
        private TextView itemPrice;
        private TextView itemQuantity;
        private ImageButton raiseQuantity;
        private ImageButton decreaseQuantity;
        private TextView sumMoney;



        public CartHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemName.setWidth(MainActivity.WIDE /3);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemQuantity = itemView.findViewById(R.id.quantity);
            raiseQuantity = itemView.findViewById(R.id.raise_quantity);
            decreaseQuantity = itemView.findViewById(R.id.decrease_quantity);
            sumMoney = itemView.findViewById(R.id.sum_money);

        }

        public void bind(Product product){
            new DownloadImageTask(itemImage).execute(product.getImageUrl());
            itemName.setText(product.getName());
            itemPrice.setText(CurrencyFormatter.format((long) product.getPrice()));
            itemQuantity.setText(String.valueOf(product.getQuantity()));
            itemPrice.setText(CurrencyFormatter.format((long) product.getPrice() * product.getQuantity()));
            //handle click event
            raiseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int nowQuantity = product.getQuantity();
                    product.setQuantity(nowQuantity + 1);
                    productManager.update(product);
                    myOnClickListener.onProductClick(getAdapterPosition(), view);
                    notifyDataSetChanged();
                }
            });
            decreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int nowQuantity = product.getQuantity();
                    if (nowQuantity - 1 == 0){
                        productManager.delete((long) product.getId());
                        items.remove(product);

                    }else {
                        product.setQuantity(nowQuantity - 1);
                        productManager.update(product);
                    }

                    myOnClickListener.onProductClick(getAdapterPosition(), view);
                    notifyDataSetChanged();
                }
            });
        }
    }

        public void onItemClick(RecyclerViewItemClick clickOnItems){
        this.myOnClickListener = clickOnItems;
    }

        //Update the list
    public void updateTheList(List<Product> productss){
        this.items = productss;
        notifyDataSetChanged();
    }

//    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView downloadTheImage;
//
//        public DownloadImageTask(ImageView downloadTheImage) {
//            this.downloadTheImage = downloadTheImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String theUrlDisplay = urls[0];
//
//            Bitmap something = null;
//
//            try {
//                InputStream insth = new java.net.URL(theUrlDisplay).openStream();
//                something = BitmapFactory.decodeStream(insth);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//
//            return something;
//        }
//
//        protected void onPostExecute(Bitmap theResult) {
//            downloadTheImage.setImageBitmap(theResult);
//        }
//    }
private static class DownloadImageTask {
    private final ImageView downloadTheImage;
    private final Executor executor;
    private final Handler handler;

    public DownloadImageTask(ImageView downloadTheImage) {
        this.downloadTheImage = downloadTheImage;
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
            handler.post(() -> downloadTheImage.setImageBitmap(finalBitmap));
        });
    }
}

}
