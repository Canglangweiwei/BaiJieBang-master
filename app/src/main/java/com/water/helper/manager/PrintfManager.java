package com.water.helper.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.print.sdk.PrinterConstants;
import com.android.print.sdk.PrinterInstance;
import com.github.promeg.pinyinhelper.Pinyin;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.water.helper.R;
import com.water.helper.base.AbsBaseApplication;
import com.water.helper.bean.GoodsModel;
import com.water.helper.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public void printf(String hotelName, String floor,
                       String operator, String remark,
                       List<GoodsModel> modeList) {
        try {
            printTabSpace(5);
            printText("青岛柏洁洗涤有限公司收货单");
            printfWrap(2);
            printTwoColumn("宾\u3000馆：", hotelName);
            printTabSpace(5);
            printTwoColumn("楼层：", floor);
            printfWrap();
            printTwoColumn("操作人：", operator);
            printTabSpace(5);
            printTwoColumn("时间：", Util.stampToDate(System.currentTimeMillis()));
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
            printfWrap(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public interface BluetoothChangLister {
        void chang(String name);
    }

    public void removeAllMessage() {
        mHandler.removeMessages(0);
    }
}