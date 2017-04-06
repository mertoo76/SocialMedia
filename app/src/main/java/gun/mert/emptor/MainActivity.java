package gun.mert.emptor;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.*;
import com.facebook.CallbackManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LoginButton loginButton;
   Button login,gmail;
     CallbackManager callbackManager;
    TextView user_name;
FacebookActivity facebookActivity;
    AccessTokenTracker accessTokenTracker;

//


    //
    private static final String TWITTER_KEY = "yourkey";
    private static final String TWITTER_SECRET = "yoursecret";

    //Tags to send the username and image url to next activity using intent
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_IMAGE_URL = "image_url";

    //Twitter Login Button
    TwitterLoginButton twitterLoginButton;
    //

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//twit
        //Initializing TwitterAuthConfig, these two line will also added automatically while configuration we did
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));


        //
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

// twit


//Initializing twitter login button
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitterLogin);

        //Adding callback to the button
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //If login succeeds passing the Calling the login method and passing Result object
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                //If failure occurs while login handle it here
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });


        //

        //gmail
gmail = (Button)findViewById(R.id.auth);

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),gmailActivity.class);
                startActivity(i);
            }
        });


        //gmail



        //sil

        //

       Button ınsta = (Button)findViewById(R.id.ınsta);

        ınsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),InstActivity.class);
                startActivity(i);
            }
        });




// face
       user_name=(TextView)findViewById(R.id.face_name);
        login = (Button) findViewById(R.id.button);


        if (AccessToken.getCurrentAccessToken() != null) {
            updateWithToken(AccessToken.getCurrentAccessToken());
        }

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("FaceLog", "Success");
            }

            @Override
            public void onCancel() {
                Log.i("FaceLog", "Cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i("FaceLog", "Error");
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                updateWithToken(currentAccessToken);
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
            }
        });


// face



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    //twit
    public void login(Result<TwitterSession> result) {

        //Creating a twitter session with result's data
        TwitterSession session = result.data;

        //Getting the username from session
        final String username = session.getUserName();

        //This code will fetch the profile image URL
        //Getting the account service of the user logged in

      // burdaki verifyCredential iki parametre aldığı için aşağıdaki çözüme yöneldim.
        TwitterSession   sessionnew = result.data;
        Twitter          twitternew = Twitter.getInstance();
        TwitterApiClient api     = twitternew.core.getApiClient(sessionnew);
        AccountService servicenew = api.getAccountService();
        Call<User> usernew    = servicenew.verifyCredentials(true, true);
        usernew.enqueue(new Callback<User>() {
                    @Override
                    public void failure(TwitterException e) {
                        //If any error occurs handle it here
                    }

                    @Override
                    public void success(Result<User> userResult) {
                        //If it succeeds creating a User object from userResult.data
                        User user = userResult.data;

                        //Getting the profile image url
                        String profileImage = user.profileImageUrl.replace("_normal", "");

                        //Creating an Intent
                        Intent intent = new Intent(MainActivity.this, twitAcivity.class);

                        //Adding the values to intent
                        intent.putExtra(KEY_USERNAME,username);
                        intent.putExtra(KEY_PROFILE_IMAGE_URL, profileImage);

                        //Starting intent
                        startActivity(intent);
                    }
                });
    }


    //



    //



    //
    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
// face
    private void updateWithToken(AccessToken currentAccessToken) {
        if (currentAccessToken != null) {
            Log.i("FaceLog", "Already Logged.");

            GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                    Log.i("FaceLog", jsonObject.toString());
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        AppController.getInstance().profilImageUrl = profile.getProfilePictureUri(200, 200);
                        AppController.getInstance().profilName = profile.getName();
                        AppController.getInstance().profilLink = profile.getLinkUri();
                        try {
                            AppController.getInstance().profilGender = jsonObject.getString("gender");
                            AppController.getInstance().profilEmail = jsonObject.getString("email");
                            AppController.getInstance().profilLocale = jsonObject.getString("locale");
                            AppController.getInstance().profilTimezone = jsonObject.getString("timezone");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + "me" + "/friends", null, HttpMethod.GET, new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse graphResponse) {
                            Log.i("FaceLog", graphResponse.getJSONObject().toString());
                            try {
                                AppController.getInstance().profilFriendsCount = graphResponse.getJSONObject().getJSONObject("summary").getString("total_count");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(new Intent(getApplicationContext(), LoggedActivity.class));
                            finish();
                        }
                    }).executeAsync();
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,locale,timezone");
            graphRequest.setParameters(parameters);
            graphRequest.executeAsync();

        } else {
            Log.i("FaceLog", "Not Logged.");

        }
    }
// face
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
