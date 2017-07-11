package cn.zucc.qwmcql.personalassistant;

/**
 * Created by My PC on 2017/5/22.
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.zucc.qwmcql.personalassistant.anime.DepthPageTransformer;
import cn.zucc.qwmcql.personalassistant.wechat.Constants;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String[] mTitle = new String[]{"笔记列表", "日程规划", "收支管理"};
    private List<Fragment> mFragments;
    private long exitTime = 0;
    private FloatingActionButton fabNote;
    private RelativeLayout friend,timeLine;
    private IWXAPI api;
    private static final int THUMB_SIZE = 200;
    private int mTargetScene =SendMessageToWX.Req.WXSceneSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("记录人");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        initView();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mFragments = new ArrayList<>();
        mFragments.add(new FragmentNoteList());
        mFragments.add(new FragmentSchedulePlan());
        mFragments.add(new FragmentIncomeCost());
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mTitle.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle[position];
            }

        });
        mTabLayout.setupWithViewPager(mViewPager);
        fabNote = (FloatingActionButton) findViewById(R.id.fab_note);
        fabNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
                View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.bottom_sheet_noteplus, null);
                TextView tvText = (TextView) dialogView.findViewById(R.id.tv_textNote);
                TextView tvPic = (TextView) dialogView.findViewById(R.id.tv_picNote);
                tvText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                            intent.putExtra("flag",1);
                        startActivity(intent);
                    }
                });
                tvPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                        intent.putExtra("flag",2);
                        startActivity(intent);
                    }
                });
                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        final FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fab_incomeCost);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switch (arg0){
                    case 0:
                        fabNote.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.GONE);
                        break;
                    case 1:
                        fabNote.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                        break;
                    case 2:
                        fabNote.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_settings:
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    finish();
                    break;

            }
            return true;
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            mViewPager.setCurrentItem(1);
        } else if (id == R.id.nav_gallery) {
            mViewPager.setCurrentItem(0);
        } else if (id == R.id.nav_slideshow) {
            mViewPager.setCurrentItem(3);
        } else if (id == R.id.nav_manage) {

            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_share) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialog = inflater.inflate(R.layout.share_dialog, (ViewGroup) findViewById(R.id.shareLiner));
            friend=(RelativeLayout) dialog.findViewById(R.id.share_friends);
            timeLine=(RelativeLayout) dialog.findViewById(R.id.share_timeline);
            regToWx();
            friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTargetScene = SendMessageToWX.Req.WXSceneSession;
                    shareWeChatWeb();
                }
            });
            timeLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
                    shareWeChatWeb();
                }
            });
            builder.setTitle("分享到");
            builder.setView(dialog);
            builder.show();
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();
        }else if(id==R.id.nav_location){
            Intent intent = new Intent(MainActivity.this, LocationActivity.class);
            startActivity(intent);
        }
        else  if(id==R.id.nav_interest){
            Intent intent = new Intent(MainActivity.this, InterestActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void shareWeChatWeb(){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "https://github.com/JohnChin/NoteMan/blob/master/README.md";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "个人助理Assis,一个关于生活的记录人";
        msg.description = "个人助理App";
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bookshelf);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp,THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }
    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);
    }
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
