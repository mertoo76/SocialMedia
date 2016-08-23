package gun.mert.emptor;
// face için
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class LoggedActivity extends AppCompatActivity {
    ImageView profilImg;
    TextView profilName, profilEmail, profilLink, profilGender, profilLocale, profilTimezone, profilFriendsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);


        profilImg = (ImageView)findViewById(R.id.imgUrl);
        profilName = (TextView)findViewById(R.id.tvName);
        profilEmail = (TextView)findViewById(R.id.tvEmail);
     //   profilLink = (TextView)findViewById(R.id.tvLink);
        profilGender = (TextView)findViewById(R.id.tvGender);
       // profilLocale = (TextView)findViewById(R.id.tvLocale);
     //   profilTimezone = (TextView)findViewById(R.id.tvTimezone);
      //  profilFriendsCount = (TextView)findViewById(R.id.tvCount);

        profilName.setText(AppController.getInstance().profilName);
        profilEmail.setText(AppController.getInstance().profilEmail);
       // profilLink.setText(AppController.getInstance().profilLink.toString());
        profilGender.setText(AppController.getInstance().profilGender);
     //   profilLocale.setText(AppController.getInstance().profilLocale);
       // profilTimezone.setText(AppController.getInstance().profilTimezone);
       // profilFriendsCount.setText(AppController.getInstance().profilFriendsCount);
        Picasso.with(getApplicationContext()).load(AppController.getInstance().profilImageUrl).into(profilImg);

    }
}
