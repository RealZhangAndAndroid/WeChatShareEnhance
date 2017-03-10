package real.com.wechatshareenhance;

//import android.app.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.platformtools.Util;

import java.io.File;


public class WeChatActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String APP_ID = "wx123456789b8a5a32c3";
    private static final int permission_read_extstorage = 007;
    private IWXAPI api;
    EditText editText;
    private static final int PHOTO_REQUEST_CAMERA = 1;
    private static final int THUMB_SIZE = 150;
    private Bundle bundle;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    ImageView imageView;
    ImageView imageView_camera;
    private Uri loacl_uri;
    private File camera_file = new File(Environment
            .getExternalStorageDirectory(), PHOTO_FILE_NAME);
    private ImageView[] imageViewGroup;

    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.share_text);
        imageView = (ImageView) findViewById(R.id.ivs);
        imageView_camera = (ImageView) findViewById(R.id.imageView_camera);

        // IWXAPI 是第三方app和微信通信的openapi接口
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
        //-----------------------
        if (isInstallWeiChat()) return;
        //----------------
        imageViewGroup = new ImageView[30];
        imageViewGroup[0] = (ImageView) findViewById(R.id.doc);
        imageViewGroup[1] = (ImageView) findViewById(R.id.montagne);
        imageViewGroup[2] = (ImageView) findViewById(R.id.rook);
        imageViewGroup[3] = (ImageView) findViewById(R.id.twitch);
        imageViewGroup[4] = (ImageView) findViewById(R.id.fuze);
        imageViewGroup[5] = (ImageView) findViewById(R.id.glaz);
        imageViewGroup[6] = (ImageView) findViewById(R.id.tachanka);
        imageViewGroup[7] = (ImageView) findViewById(R.id.glaz);
        imageViewGroup[8] = (ImageView) findViewById(R.id.sledge);
        imageViewGroup[9] = (ImageView) findViewById(R.id.smoke);
        imageViewGroup[10] = (ImageView) findViewById(R.id.thatcher);
        imageViewGroup[11] = (ImageView) findViewById(R.id.mute);
        imageViewGroup[12] = (ImageView) findViewById(R.id.ash);
        imageViewGroup[13] = (ImageView) findViewById(R.id.castle);
        imageViewGroup[14] = (ImageView) findViewById(R.id.pulse);
        imageViewGroup[15] = (ImageView) findViewById(R.id.thermite);
        imageViewGroup[16] = (ImageView) findViewById(R.id.bandit);
        imageViewGroup[17] = (ImageView) findViewById(R.id.blitz);
        imageViewGroup[18] = (ImageView) findViewById(R.id.iq);
        imageViewGroup[19] = (ImageView) findViewById(R.id.jager);
        imageViewGroup[20] = (ImageView) findViewById(R.id.buck);
        imageViewGroup[21] = (ImageView) findViewById(R.id.frost);
        imageViewGroup[22] = (ImageView) findViewById(R.id.blackbeard);
        imageViewGroup[23] = (ImageView) findViewById(R.id.valkyrie);
        imageViewGroup[24] = (ImageView) findViewById(R.id.capitao);
        imageViewGroup[25] = (ImageView) findViewById(R.id.caveira);
        imageViewGroup[26] = (ImageView) findViewById(R.id.echo);
        imageViewGroup[27] = (ImageView) findViewById(R.id.hibana);
        imageViewGroup[28] = (ImageView) findViewById(R.id.jackal);
        imageViewGroup[29] = (ImageView) findViewById(R.id.mira);

        for (int i = 0; i < imageViewGroup.length; i++) {
            imageViewGroup[i].setOnClickListener(this);
        }


//        LinearLayout ll = (LinearLayout)findViewById(R.id.LinearLayout1);
//        for(int i=0;i<20;i++)
//        {
//            ImageView imageView= new ImageView(this);
//            imageView.setImageResource(R.drawable.fuze);
//            imageView.setma(R.dimen.imageSzie_padding,R.dimen.imageSzie_padding,R.dimen.imageSzie_padding,R.dimen.imageSzie_padding);
//            imageView.setMinimumWidth(R.dimen.imageSzie);
//            imageView.setMaxHeight(R.dimen.imageSzie);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            ll.addView(imageView);
//        }


    }

    private boolean isInstallWeiChat() {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(WeChatActivity.this, getString(R.string.not_installed),
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void newPhoto(View view) {
        gallery();
    }

    public void newCamera(View view) {
        camera();
    }

    public void camera() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permission_read_extstorage);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(camera_file));
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    public void gallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void weichat_image_button(View view) {
        if (isInstallWeiChat()) return;

        if (loacl_uri == null) {
            return;
        }

        String path = getRealPathFromURI(this, loacl_uri);

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);


//系统分享功能
//        Uri uriToImage = loacl_uri;
//        Intent shareIntent = new Intent();
//        //发送图片到朋友圈
//        //ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//        //发送图片给好友。
//        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
//        shareIntent.setComponent(comp);
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
//        shareIntent.setType("image/jpeg");
//        startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }

    public void weichat_camera_button(View view) {
        if (isInstallWeiChat()) return;

        if (camera_file == null) {
            return;
        }

        String path = camera_file.getAbsolutePath();

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);


//系统分享功能
//        Uri uriToImage = loacl_uri;
//        Intent shareIntent = new Intent();
//        //发送图片到朋友圈
//        //ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//        //发送图片给好友。
//        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
//        shareIntent.setComponent(comp);
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
//        shareIntent.setType("image/jpeg");
//        startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }

    public void weichat_emotion_button(View View) {

    }

    private void emotion_send(Bitmap bmp) {
        if (isInstallWeiChat()) return;


        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    public void weichat_share_button(View View) {
        if (isInstallWeiChat()) return;

        WXTextObject textObj = new WXTextObject();
        textObj.text = editText.getText().toString();
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        // msg.title = "Will be ignored";
        msg.description = editText.getText().toString();
        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = mTargetScene;
        // 调用api接口发送数据到微信
        api.sendReq(req);

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void onRadioButtonClicked(View view) {
        if (!(view instanceof RadioButton)) {
            return;
        }

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.target_scene_session:
                if (checked) {
                    mTargetScene = SendMessageToWX.Req.WXSceneSession;
                }
                break;
            case R.id.target_scene_timeline:
                if (checked) {
                    mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                try {
                    Uri uri = data.getData();
                    loacl_uri = uri;
                    Glide.with(this).load(uri).crossFade().into(imageView);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            Glide.with(this).load(camera_file).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).crossFade().into(imageView_camera);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (isInstallWeiChat()) return;

        Bitmap bmp;
        int id = view.getId();
        switch (id) {
            case R.id.doc:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.doc);
                emotion_send(bmp);
                break;
            case R.id.montagne:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.montagne);
                emotion_send(bmp);
                break;
            case R.id.rook:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.rook);
                emotion_send(bmp);
                break;
            case R.id.twitch:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.twitch);
                emotion_send(bmp);
                break;
            case R.id.fuze:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.fuze);
                emotion_send(bmp);
                break;
            case R.id.glaz:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.glaz);
                emotion_send(bmp);
                break;
            case R.id.tachanka:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.tachanka);
                emotion_send(bmp);
                break;
            case R.id.kapkan:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.kapkan);
                emotion_send(bmp);
                break;
            case R.id.sledge:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sledge);
                emotion_send(bmp);
                break;
            case R.id.smoke:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.smoke);
                emotion_send(bmp);
                break;
            case R.id.thatcher:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.thatcher);
                emotion_send(bmp);
                break;
            case R.id.ash:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ash);
                emotion_send(bmp);
                break;
            case R.id.castle:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.castle);
                emotion_send(bmp);
                break;
            case R.id.thermite:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.thermite);
                emotion_send(bmp);
                break;
            case R.id.pulse:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pulse);
                emotion_send(bmp);
                break;
            case R.id.bandit:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bandit);
                emotion_send(bmp);
                break;
            case R.id.blitz:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.blitz);
                emotion_send(bmp);
                break;
            case R.id.iq:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.iq);
                emotion_send(bmp);
                break;
            case R.id.jager:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.jager);
                emotion_send(bmp);
                break;
            case R.id.buck:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.buck);
                emotion_send(bmp);
                break;
            case R.id.frost:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.frost);
                emotion_send(bmp);
                break;
            case R.id.valkyrie:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.valkyrie);
                emotion_send(bmp);
                break;
            case R.id.blackbeard:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.blackbeard);
                emotion_send(bmp);
                break;
            case R.id.capitao:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.capitao);
                emotion_send(bmp);
                break;
            case R.id.caveira:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.caveira);
                emotion_send(bmp);
                break;
            case R.id.echo:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.echo);
                emotion_send(bmp);
                break;
            case R.id.hibana:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hibana);
                emotion_send(bmp);
                break;
            case R.id.jackal:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.jackal);
                emotion_send(bmp);
                break;
            case R.id.mira:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mira);
                emotion_send(bmp);
                break;


            default:
                break;
        }
    }
}
