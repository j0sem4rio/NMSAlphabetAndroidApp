package cafe.adriel.nmsalphabet.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import cafe.adriel.nmsalphabet.App;
import cafe.adriel.nmsalphabet.R;
import cafe.adriel.nmsalphabet.util.AnalyticsUtil;
import cafe.adriel.nmsalphabet.util.Util;

public class VersionActivity extends BaseActivity {

    @BindView(R.id.bg)
    ImageView bgView;
    @BindView(R.id.app_version)
    TextView appVersionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        tintManager.setTintColor(getResources().getColor(R.color.bg_version));
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void init() {
        adjustMarginAndPadding();
        Glide.with(this)
                .load(R.drawable.bg_version)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(bgView);
        appVersionView.setText(Util.getAppVersionName(this));
        AnalyticsUtil.sawEastereggEvent(App.getUser());
    }
}