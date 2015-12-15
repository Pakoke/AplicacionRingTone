package app.ringtone.functions.gallery;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.io.File;

import app.pacoke.aplicacionringtone.R;

public class DetailsActivity extends AppCompatActivity {
    private TextView titleTextView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_gallery);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        String title = getIntent().getStringExtra("title");
        String image = getIntent().getStringExtra("image");
        titleTextView = (TextView) findViewById(R.id.title);
        imageView = (ImageView) findViewById(R.id.grid_item_image);
        titleTextView.setText(Html.fromHtml(title));

        Picasso.with(this)
                .load(new File(image))
                .error(R.drawable.error_icon)
                .into(imageView);
    }
}
