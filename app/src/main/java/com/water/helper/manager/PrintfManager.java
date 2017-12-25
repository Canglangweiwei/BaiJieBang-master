package com.water.helper.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.android.print.sdk.PrinterConstants;
import com.android.print.sdk.PrinterInstance;
import com.github.promeg.pinyinhelper.Pinyin;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;
import com.water.helper.R;
import com.water.helper.base.AbsBaseApplication;
import com.water.helper.bean.GoodsModel;
import com.water.helper.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 打印机管理
 */
@SuppressWarnings("ALL")
public class PrintfManager {

    protected String TAG = "PrintfManager";
    public final static int WIDTH_PIXEL = 384;
    protected List<BluetoothChangLister> bluetoothChangListerList = new ArrayList<>();

    /**
     * 添加蓝牙改变监听
     */
    public void addBluetoothChangLister(BluetoothChangLister bluetoothChangLister) {
        bluetoothChangListerList.add(bluetoothChangLister);
    }

    protected Context context;

    protected PrinterInstance mPrinter;

    private PrintfManager() {
        super();
    }

    static class PrintfManagerHolder {
        private static PrintfManager instance = new PrintfManager();
    }

    public static PrintfManager getInstance() {
        return PrintfManagerHolder.instance;
    }

    public static PrintfManager getInstance(Context context) {
        if (PrintfManagerHolder.instance.context == null) {
            PrintfManagerHolder.instance.context = context;
        }
        return PrintfManagerHolder.instance;
    }

    public void setPrinter(PrinterInstance mPrinter) {
        this.mPrinter = mPrinter;
    }

    public void connection() {
        if (mPrinter != null) {
            mPrinter.openConnection();
        }
    }

    /**
     * 连接
     */
    public void openPrinter(final BluetoothDevice mDevice) {
        AbsBaseApplication.sApp.getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                setPrinter(new PrinterInstance(context, mDevice, mHandler));
                // default is gbk...
                connection();
                // 连接保存
                SharedPreferencesManager.updateBluetoothName(context, mDevice);
            }
        });
    }

    public PrinterInstance getPrinter() {
        return mPrinter;
    }

    private boolean isHasPrinter = false;

    public boolean isConnect() {
        return isHasPrinter;
    }

    public void disConnect(final String text) {
        AbsBaseApplication.sApp.getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                isHasPrinter = false;
                if (mPrinter != null) {
                    mPrinter.closeConnection();
                    mPrinter = null;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUitl.showShort(text);
                    }
                });
            }
        });
    }


    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String bluetoothName = context.getString(R.string.no_connect_blue_tooth);
            switch (msg.what) {
                case PrinterConstants.Connect.SUCCESS:// 成功
                    isHasPrinter = true;
                    ToastUitl.showShort(context.getString(R.string.connection_success));
                    bluetoothName = SharedPreferencesManager.getBluetoothName(context);
                    break;
                case PrinterConstants.Connect.FAILED:// 失败
                    disConnect(context.getString(R.string.connection_fail));
                    break;
                case PrinterConstants.Connect.CLOSED:// 关闭
                    disConnect(context.getString(R.string.bluetooth_disconnect));
                    break;
            }

            for (BluetoothChangLister bluetoothChangLister : bluetoothChangListerList) {
                if (bluetoothChangLister == null) {
                    continue;
                }
                bluetoothChangLister.chang(bluetoothName);
            }
        }
    };

    public void defaultConnection() {
        String bluetoothName = SharedPreferencesManager.getBluetoothName(context);
        if (bluetoothName == null) {
            return;
        }
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = defaultAdapter.getBondedDevices();
        for (BluetoothDevice device : bondedDevices) {
            if (device.getName().equals(bluetoothName)) {
                mPrinter = new PrinterInstance(context, device, mHandler);
                mPrinter.openConnection();
                return;
            }
        }
    }

    /**
     * @param hotelName : 宾馆名称
     * @param floor     ：楼层
     * @param operator  ： 操作人
     * @param remark    ：备注
     */
    public final void printf(final String hotelName, final String floor,
                       final String operator, final String remark,
                       final List<GoodsModel> modeList) {
        try {
            // 图片打印
            printLogoImage();
            printfWrap();
            printTabSpace(17);
            boldOn();// 加粗
            printLargeText("收货单");
            boldOff();// 取消加粗
            printfWrap(2);
            printTwoColumn("宾\u3000馆：", hotelName);
            printfWrap();
            printTwoColumn("楼\u3000层：", floor);
            printfWrap();
            printTwoColumn("操作人：", operator);
            printfWrap();
            printTwoColumn("时\u3000间：", Util.stampToDate(System.currentTimeMillis()));
            printfWrap();
            printPlusLine();
            printText("类型");
            printTabSpace(14);
            printText("数量（个）");
            printTabSpace(10);
            printText("重污（个）");
            for (int j = 0; j < modeList.size(); j++) {
                GoodsModel mode = modeList.get(j);
                String name = mode.getTitle();
                printText(name);
                StringBuilder nameSB = new StringBuilder();
                for (int i = 0; i < 18 - (name.length() * 2); i++) {
                    nameSB.append(" ");
                }
                printText(nameSB.toString());

                String number = String.valueOf(mode.getNum());
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 10 - number.length(); i++) {
                    sb.append(" ");
                }
                printText(number + sb.toString());
                printTabSpace(10);
                printText(String.valueOf(mode.getNum_wu()));
                printfWrap();
            }
            printPlusLine();
            printText("备注信息：");
            printText(remark);
            printfWrap();
            printText("地\u3000\u3000址：青岛市市北区兴隆一路6号甲");
            printfWrap();
            printText("客服电话：0532-83716111");
            printfWrap();
            printText("客户签字：");
            printfWrap(5);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LoadingDialog.cancelDialogForLoading();
        }
    }

    /**
     * 字体大小
     *
     * @param text
     * @throws IOException
     */
    public void printLargeText(String text) throws IOException {

        byte[] result1 = new byte[3];
        result1[0] = 0x1b;
        result1[1] = 0x21;
        result1[2] = 48;// 代表字体的大小

        mPrinter.sendByteData(result1);
        mPrinter.sendByteData(getGbk(text));

        byte[] result2 = new byte[3];
        result2[0] = 0x1b;
        result2[1] = 0x21;
        result2[2] = 0;// 代表字体的大小

        mPrinter.sendByteData(result2);
    }

    /**
     * 选择加粗模式
     *
     * @return
     */
    private void boldOn() throws IOException {
        byte[] result = new byte[3];
        result[0] = 27;
        result[1] = 69;
        result[2] = 0xF;

        mPrinter.sendByteData(result);
    }

    /**
     * 取消加粗模式
     *
     * @return
     */
    private void boldOff() throws IOException {
        byte[] result = new byte[3];
        result[0] = 27;
        result[1] = 69;
        result[2] = 0;

        mPrinter.sendByteData(result);
    }

    /**
     * 打印空白(一个Tab的位置，约4个汉字)
     *
     * @param length 需要打印空白的长度,
     * @throws IOException
     */
    private void printTabSpace(int length) throws IOException {
        StringBuilder space1 = new StringBuilder();
        for (int i = 0; i < length; i++) {
            space1.append(" ");
        }
        mPrinter.sendByteData(space1.toString().getBytes());
    }

    private void printTwoColumn(String title, String content) throws IOException {
        int iNum = 0;
        byte[] byteBuffer = new byte[100];
        byte[] tmp;

        tmp = getGbk(title);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = setLocation(getOffset(content));
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(content);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);

        mPrinter.sendByteData(byteBuffer);
    }

    private byte[] getGbk(String stText) throws IOException {
        return stText.getBytes("GBK");// 必须放在try内才可以
    }

    private void printfWrap() throws IOException {
        printfWrap(1);
    }

    private void printfWrap(int lineNum) throws IOException {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < lineNum; i++) {
            line.append(" \n");
        }
        mPrinter.sendByteData(line.toString().getBytes());
    }

    /**
     * 绝对打印位置
     */
    private byte[] setLocation(int offset) throws IOException {
        byte[] bs = new byte[4];
        bs[0] = 0x1B;
        bs[1] = 0x24;
        bs[2] = (byte) (offset % 256);
        bs[3] = (byte) (offset / 256);
        return bs;
    }

    private int getOffset(String str) {
        return WIDTH_PIXEL - getStringPixLength(str);
    }

    private int getStringPixLength(String str) {
        int pixLength = 0;
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (Pinyin.isChinese(c)) {
                pixLength += 24;
            } else {
                pixLength += 12;
            }
        }
        return pixLength;
    }

    /**
     * 注意：线条不能太长，不然会换出一行，如果决定长度不够，可以增城两个字符，但是得去掉换行符
     *
     * @throws IOException
     */
    private void printPlusLine() throws IOException {
        printText("- - - - - - - - - - - - - - - - - - - - - - -\n");
    }

    /**
     * 打印文字
     */
    private void printText(String text) throws IOException {
        mPrinter.sendByteData(getGbk(text));
    }

    /**
     * 打印图片
     */
    private void printLogoImage() throws IOException {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.printf_bitmap);
        byte[] bytes = bitmap2PrinterBytes(bitmap, 18);
        mPrinter.sendByteData(bytes);
    }

    private static byte[] bitmap2PrinterBytes(Bitmap bitmap, int left) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        byte[] imgbuf = new byte[(width / 8 + left + 4) * height];
        byte[] bitbuf = new byte[width / 8];
        int[] p = new int[8];
        int s = 0;

        for (int y = 0; y < height; ++y) {
            int n;
            for (n = 0; n < width / 8; ++n) {
                int value;
                for (value = 0; value < 8; ++value) {
                    int grey = bitmap.getPixel(n * 8 + value, y);
                    int red = ((grey & 0x00FF0000) >> 16);
                    int green = ((grey & 0x0000FF00) >> 8);
                    int blue = (grey & 0x000000FF);
                    int gray = (int) (0.29900 * red + 0.58700 * green + 0.11400 * blue); // 灰度转化公式
//                    int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                    if (gray <= 128) {
                        gray = 1;
                    } else {
                        gray = 0;
                    }
                    p[value] = gray;
//                    if(bitmap.getPixel(n * 8 + value, y) >128 ) {
//                        p[value] = 1;
//                    } else {
//                        p[value] = 0;
//                    }
                }
                value = p[0] * 128 + p[1] * 64 + p[2] * 32 + p[3] * 16 + p[4] * 8 + p[5] * 4 + p[6] * 2 + p[7];
                bitbuf[n] = (byte) value;
            }
            if (y != 0) {
                ++s;
                imgbuf[s] = 22;
            } else {
                imgbuf[s] = 22;
            }
            ++s;
            imgbuf[s] = (byte) (width / 8 + left);
            for (n = 0; n < left; ++n) {
                ++s;
                imgbuf[s] = 0;
            }
            for (n = 0; n < width / 8; ++n) {
                ++s;
                imgbuf[s] = bitbuf[n];
            }
            ++s;
            imgbuf[s] = 21;
            ++s;
            imgbuf[s] = 1;
        }
        return imgbuf;
    }

    public interface BluetoothChangLister {
        void chang(String name);
    }

    public void removeAllMessage() {
        mHandler.removeMessages(0);
    }
}
