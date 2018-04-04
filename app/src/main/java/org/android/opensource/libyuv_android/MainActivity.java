package org.android.opensource.libyuv_android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import org.android.opensource.libraryyuv.Libyuv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    private ImageView imgShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgShow = findViewById(R.id.img);
        init();
    }


    private void init(){
        try {
            InputStream is = getResources().openRawResource(R.raw.test);
            Bitmap image = BitmapFactory.decodeStream(is);
//            BitmapFactory.Options opts = new BitmapFactory.Options();
//            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            Bitmap image = BitmapFactory.decodeStream(is, null, opts);
            int h = image.getHeight();
            int w = image.getWidth();



            //将位图资源转为二进制数据，数据大小为w*h*4
            int bytes = image.getByteCount();
            ByteBuffer buf = ByteBuffer.allocate(bytes);
            image.copyPixelsToBuffer(buf);
            byte[] byteArray = buf.array();

            byte[]  ybuffer=new byte[w*h];//用于保存y分量数据
            byte[]  uvbuffer=new byte[w*h/2];//用于保存uv分量数据

//            byte[] argbBuffer = new byte[w*h*4];
//            Libyuv.RGBAToARGB(byteArray, w*4, argbBuffer, w*4, w, h);

            //使用libyuv库，ARGB转NV21 格式
            Libyuv.ARGBToNV21(byteArray, w*4, w, h, ybuffer, uvbuffer);

            byte[] frameBuffer=new byte[w*h*3/2];
            System.arraycopy(ybuffer,0,frameBuffer,0,w*h);
            System.arraycopy(uvbuffer,0,frameBuffer,w*h,w*h/2);


            //保存成jpg图片
            String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.jpg";
            File file = new File(imagePath);
            FileOutputStream image_os = new FileOutputStream(file);
            YuvImage yuvImage = new YuvImage(frameBuffer, ImageFormat.NV21, w, h, null);
            yuvImage.compressToJpeg(new android.graphics.Rect(0, 0, w, h), 80, image_os);

            image_os.flush();
            image_os.close();


            //用于保存将yuv数据转成argb数据
            byte[] rgbbuffer=new byte[w*h*4];
            Libyuv.NV21ToARGB(ybuffer, w, uvbuffer, w, rgbbuffer, w*4, w, h);
            //还原成位图
            Bitmap stitchBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rgbbuffer));

            //显示还原的位图
            imgShow.setImageBitmap(stitchBmp);

        } catch (Exception e){
             e.printStackTrace();
        }

    }
}
