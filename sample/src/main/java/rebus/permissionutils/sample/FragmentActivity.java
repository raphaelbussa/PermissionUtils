package rebus.permissionutils.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class FragmentActivity extends AppCompatActivity {

    private final static String SECOND_FRAGMENT = "SECOND_FRAGMENT";
    private final static String SECOND_FRAGMENT_V4 = "SECOND_FRAGMENT_V4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        boolean isFragmentV4 = getIntent().getExtras().getBoolean("IS_FRAGMENT_V4");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setSubtitle(isFragmentV4 ? "Fragment V4" : "Fragment");
        }

        if (isFragmentV4) {
            if (savedInstanceState == null) {
                android.support.v4.app.Fragment fragment = android.support.v4.app.Fragment.instantiate(this, SecondFragmentV4.class.getName());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, fragment, SECOND_FRAGMENT_V4)
                        .commit();
            }
        } else {
            if (savedInstanceState == null) {
                android.app.Fragment fragment = android.app.Fragment.instantiate(this, SecondFragment.class.getName());
                getFragmentManager().beginTransaction()
                        .add(R.id.container, fragment, SECOND_FRAGMENT)
                        .commit();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
