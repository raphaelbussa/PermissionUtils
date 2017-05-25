/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Raphaël Bussa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
