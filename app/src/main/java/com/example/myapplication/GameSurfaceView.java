package com.example.myapplication;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



import static android.content.Context.WINDOW_SERVICE;
import static android.view.MotionEvent.ACTION_UP;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    private static final long INTERVAL_PERIOD = 16;
    private ScheduledExecutorService scheduledExecutorService;
    private static final float FONT_SIZE = 24f;
    private Paint paintCircle, paintFps;
    private float x, y, r;
    private ArrayList<Long> intervalTime = new ArrayList<Long>(20);


    Resources res;
    ///AnalogStick analogStick;
    //AimStick aimStick;
    ///PushButton pushButton;
    public static WindowManager windowMgr;
    public static Display display;
    /**StageMgr stageMgr;
    MultiTouchMgr mtmgr;
    Player player;
    Drone drone;
    Zombie zombie1,zombie2,zombie3,zombie4,zombie5;
    Camera camera;**/
    public static int frame;

    int width,height;



    /***public LinkedList<Character> characters;*/


    public GameSurfaceView(Context context) {
        super(context);
        windowMgr = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        display = windowMgr.getDefaultDisplay();
        init();
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /*
     * コンストラクター引数が1〜3個の場合のすべてで共通となる初期化ルーチン
     */
    private void init() {
        /*
         * このクラス（SurfaceViewTest）では、SurfaceView の派生クラスを定義するだけでなく、
         * SurfaceHolder.Callback インターフェイスのコールバックも実装（implement）しているが、
         * SurfaceHolder であるこのクラスのインスタンスの呼び出し元のアクティビティ（通常はMainActivity）
         * に対して、関連するコールバック（surfaceChanged, surfaceCreated, surfaceDestroyed）
         * の呼び出し先がこのクラスのインスタンス（this）であることを呼出元アクティビティに登録する。
         */
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // fps 計測用の設定値の初期化
        for (int i = 0; i < 19; i++) {
            intervalTime.add(System.currentTimeMillis());
        }

        // 描画に関する各種設定
        paintCircle = new Paint();
        ///paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setColor(Color.RED);
        paintCircle.setAntiAlias(true);
        paintFps = new Paint();
        paintFps.setTypeface(Typeface.DEFAULT);
        paintFps.setTextSize(FONT_SIZE);
        paintFps.setColor(Color.GRAY);
        paintFps.setAntiAlias(true);

        /**
         * 間違ってもインスタンス生成前代入をしないこと！！
         * バグの特定が難しくなります
         * この辺の設定順序はいつか
         * TODO:設定順序を決める
         */


        /***mtmgr = new MultiTouchMgr();
        analogStick = new AnalogStick(210,870);
        pushButton = new PushButton(1500,800);***/
        /**
         * 3/24
         * aimStick Test
         * todo:調整
         */
        //aimStick = new AimStick(1500,870);


        ///camera = new Camera(500,700);
        res = this.getContext().getResources();
        ///stageMgr = new StageMgr(res);

        //camera = new Camera(500,500,display.getWidth(),display.getHeight());

        ///player = new Player(res,3600,3600);
        ///player.setAnalogStick(analogStick);
        //player.setAimStick(aimStick);
        ///player.setButton(pushButton);
        ///player.setCamera(camera);
        ///StageMgr.setPlayer(player);
        ///stageMgr.setDynamicObject(player);
        ///camera.setPlayer(player);

        ///drone = new Drone(res,player.getX()+40,player.getY()+40);
        ///drone.setPlayer(player);



        //zombie1 = new Zombie(res,1313,1511);
        ///zombie2 = new Zombie(res,player.getX(),player.getY()-600);
        //zombie3 = new Zombie(res,player.getX()+600,player.getY()-600);
        //zombie4 = new Zombie(res,player.getX()-600,player.getY()+600);
        //zombie5 = new Zombie(res,player.getX()+600,player.getY()+600);

        ///DemoStage.initEnemies();

        frame = 0;

        /***characters.add(new Apple(500,500,50,Color.RED));
        characters.add(new Apple(100,500,30,Color.BLUE));
        characters.add(new Apple(200,500,50,Color.GREEN));
        characters.add(new Apple(400,500,30,Color.YELLOW));

        characters.get(0).setLimit(10);
**/
    }

    // コールバック内容の定義 (1/3)
    @Override
    public void surfaceCreated(final SurfaceHolder surfaceHolder) {
        x = getWidth() / 2;
        y = getHeight() / 2;
        width = getWidth();
        height = getHeight();


        Point realSize = new Point();
        display.getRealSize(realSize);



        /**camera.setDisplaySize(realSize.x,realSize.y);*/

        // SingleThreadScheduledExecutor による単一 Thread のインターバル実行
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // fps（実測値）の計測
                intervalTime.add(System.currentTimeMillis());
                float fps = 20000 / (intervalTime.get(19) - intervalTime.get(0));
                intervalTime.remove(0);

                // ロックした Canvas の取得
                Canvas canvas = surfaceHolder.lockCanvas();

                onUpdateGSV();
                onDrawGSV(canvas);



                canvas.drawText(String.format("%.1f fps\n%d frame", fps,frame), 200, FONT_SIZE, paintFps);
                // ロックした Canvas の解放
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }, 100, INTERVAL_PERIOD, TimeUnit.MILLISECONDS);
    }

    // コールバック内容の定義 (2/3)
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    }

    // コールバック内容の定義 (3/3)
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // SingleThreadScheduledExecutor を停止する
        scheduledExecutorService.shutdown();

        // 呼出元アクティビティ側のこのクラスのインスタンスに対するコールバック登録を解除する
        surfaceHolder.removeCallback(this);
    }

    // タッチイベントに対応する処理
    /**@Override
    public boolean onTouchEvent(MotionEvent event){
        //完全に荒治療
        //index>=2で二本目が触れてないときエラーを返すので
        //catchしてやる。一本の時はアクションうっぷで
        mtmgr.fingers = event.getPointerCount();

        for (int i=0; i<2; i++) {
            if(i>=2) break;
            mtmgr.finger.get(i).index = event.findPointerIndex(i);


            try {
                mtmgr.finger.get(i).x = event.getX(mtmgr.finger.get(i).index);
                mtmgr.finger.get(i).y = event.getY(mtmgr.finger.get(i).index);
                mtmgr.finger.get(i).paint.setColor(Color.RED);
                mtmgr.finger.get(i).state = 1;
                if(event.getAction()==ACTION_UP){
                    mtmgr.finger.get(i).state = 0;
                    mtmgr.finger.get(i).paint.setColor(Color.BLACK);
                }
                //event.getAction(i);
                //int hsize = event.getHistorySize();
            }catch(Exception e){
                mtmgr.finger.get(i).state = 0;
                mtmgr.finger.get(i).paint.setColor(Color.BLACK);
            }

        }

        return true;
    }**/

    public static int getFrame(){
        return frame;
    }

    public int onUpdateGSV(){
        /**stageMgr.onUpdate();
        analogStick.onUpdate(mtmgr.finger);
        pushButton.onUpdate(mtmgr.finger);
        //aimStick.onUpdate(mtmgr.finger);
        //cameraは必ず最後で
        camera.onUpdate(stageMgr.getStaticObjectLists(),stageMgr.getDynamicObjectLists());**/
        frame = frame%60+1;

        return 0;
    }

    public int onDrawGSV(final Canvas c){
        if(c == null){
            return 0;
        }



        //ここにゲームの描画処理を書く
        //この前に書くとカンバスが破棄されぬるぽでエラー落ち

        c.drawColor(Color.WHITE);


        /**
        mtmgr.onDraw(c);

        camera.onDraw(c);
        analogStick.onDraw(c);
        pushButton.onDraw(c);*/
        return 0;
    }

}
