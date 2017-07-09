package cn.zucc.qwmcql.personalassistant.wechat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.util.logging.LogManager;

import cn.zucc.qwmcql.personalassistant.R;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;

public class ShareActivity extends Activity{
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    private IWXAPI api;
    private Button button;
    private static final int THUMB_SIZE = 150;
    private int mTargetScene =WXSceneTimeline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        regToWx();
        button=(Button)findViewById(R.id.buttonShare);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = "https://github.com/JohnChin/NoteMan/blob/master/README.md";
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = "个人助理Assis";
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
//                finish();
            }
        });
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
