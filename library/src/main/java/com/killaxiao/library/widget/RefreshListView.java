package com.killaxiao.library.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;


import com.killaxiao.library.R;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by KillaXiao on 2016/7/25.
 * 加入了下拉刷新 和上拉加载的ListView
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = "RefreshListView";
    private int firstVisibleItemPosition; // 屏幕显示在第一个的item的索引
    private int downY; // 按下时y轴的偏移量
    private int headerViewHeight; // 头布局的高度
    private View headerView; // 头布局的对象

    private final int DOWN_PULL_REFRESH = 0; // 下拉刷新状态
    private final int RELEASE_REFRESH = 1; // 松开刷新
    private final int REFRESHING = 2; // 正在刷新中
    private int currentState = DOWN_PULL_REFRESH; // 头布局的状态: 默认为下拉刷新状态

    private Animation upAnimation; // 向上旋转的动画

    private ImageView ivRotate; // 头布局的旋转图标


    private OnRefreshListener mOnRefershListener;
    private boolean isScrollToBottom; // 是否滑动到底部
    private View footerView; // 脚布局的对象
    private int footerViewHeight; // 脚布局的高度
    private boolean isLoadingMore = false; // 是否正在加载更多中
    private Context mContext;
    private ImageView foot_img2,foot_img3; //底部布局的两个图标
    private boolean img2Visible = false;
    private boolean img3Visible = false;
    private boolean isCanDownPullRefresh = true; // 是否可以进行下拉刷新
    private boolean isCanLoadingMore = true; // 是否可以进行上拉加载
    private LayoutInflater mInflater;

    public RefreshListView(Context context) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        initHeaderView();
        initFooterView();
        this.setOnScrollListener(this);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        initHeaderView();
        initFooterView();
        this.setOnScrollListener(this);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        initHeaderView();
        initFooterView();
        this.setOnScrollListener(this);
    }

    public interface OnRefreshListener {

        /**
         * 下拉刷新
         */
        void onDownPullRefresh();

        /**
         * 上拉加载更多
         */
        void onLoadingMore();
    }

    /**
     * 设置是否可以进行下拉刷新
     * @param b
     */
    public void setCanDownPullRefresh(boolean b){
        this.isCanDownPullRefresh = b;
    }

    /**
     * 设置是否可以进行上拉加载
     * @param b
     */
    public void setCanLoadingMore(boolean b){
        this.isCanLoadingMore = b;
    }


    /**
     * 初始化脚布局
     */
    private void initFooterView() {
        footerView =mInflater.inflate(R.layout.listview_foot,null);
        foot_img2 = (ImageView)footerView.findViewById(R.id.foot_img2);
        foot_img3 = (ImageView)footerView.findViewById(R.id.foot_img3);
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        this.addFooterView(footerView);
        timer.schedule(task, new Date(), 500);
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        headerView = mInflater.inflate(R.layout.listview_header,null);
        ivRotate = (ImageView)headerView.findViewById(R.id.pullup_img);
        headerView.measure(0, 0); // 系统会帮我们测量出headerView的高度
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        this.addHeaderView(headerView); // 向ListView的顶部添加一个view对象
        initAnimation();
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        upAnimation = new RotateAnimation(0f,-360f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        upAnimation.setDuration(1000);
        upAnimation.setRepeatCount(-1);

    }

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if(!img2Visible){
                handler.sendEmptyMessage(0);
            }else{
                if(!img3Visible) {
                    handler.sendEmptyMessage(1);
                }else{
                    handler.sendEmptyMessage(-1);
                }
            }
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0: //显示第二个加载图标
                    foot_img2.setVisibility(View.VISIBLE);
                    img2Visible =true;
                    break;
                case 1: //显示第三个加载图标
                    foot_img3.setVisibility(View.VISIBLE);
                    img3Visible = true;
                    break;
                case -1: //隐藏第二个和第三个图标
                    foot_img2.setVisibility(View.GONE);
                    foot_img3.setVisibility(View.GONE);
                    img2Visible = false;
                    img3Visible = false;
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isCanDownPullRefresh) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = (int) ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveY = (int) ev.getY();
                    // 移动中的y - 按下的y = 间距.
                    int diff = (moveY - downY) / 3;
                    // -头布局的高度 + 间距 = paddingTop
                    int paddingTop = -headerViewHeight + diff;
                    // 如果: -头布局的高度 > paddingTop的值 执行super.onTouchEvent(ev);
                    if (firstVisibleItemPosition == 0
                            && -headerViewHeight < paddingTop) {
                        if (paddingTop > 0 && currentState == DOWN_PULL_REFRESH) { // 完全显示了.
                            Log.e(TAG, "松开刷新");
                            currentState = RELEASE_REFRESH;
                            refreshHeaderView();
                        } else if (paddingTop < 0
                                && currentState == RELEASE_REFRESH) { // 没有显示完全
                            Log.e(TAG, "下拉刷新");
                            currentState = DOWN_PULL_REFRESH;
                            refreshHeaderView();
                        }
                        // 下拉头布局
                        headerView.setPadding(0, 0, 0, 0);
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // 判断当前的状态是松开刷新还是下拉刷新
                    if (currentState == RELEASE_REFRESH) {
                        Log.e(TAG, "刷新数据.");
                        // 把头布局设置为完全显示状态
                        headerView.setPadding(0, 0, 0, 0);
                        // 进入到正在刷新中状态
                        currentState = REFRESHING;
                        refreshHeaderView();

                        if (mOnRefershListener != null) {
                            mOnRefershListener.onDownPullRefresh(); // 调用使用者的监听方法
                        }
                        return true;
                    } else if (currentState == DOWN_PULL_REFRESH) {
                        // 隐藏头布局
                        headerView.setPadding(0, -headerViewHeight, 0, 0);
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据currentState刷新头布局的状态
     */
    private void refreshHeaderView() {
        switch (currentState) {
            case DOWN_PULL_REFRESH : // 下拉刷新状态
                ivRotate.startAnimation(upAnimation);
                break;
            case RELEASE_REFRESH : // 松开刷新状态
                ivRotate.startAnimation(upAnimation);
                break;
            case REFRESHING : // 正在刷新中状态
                ivRotate.startAnimation(upAnimation);
                break;
            default :
                break;
        }
    }


    /**
     * 当滚动状态改变时回调
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE
                || scrollState == SCROLL_STATE_FLING) {
            // 判断当前是否已经到了底部
            if (isScrollToBottom && !isLoadingMore) {
                isLoadingMore = true;
                // 当前到底部
                Log.e(TAG, "加载更多数据");
                footerView.setPadding(0, 0, 0, 0);
                this.setSelection(this.getCount());
                if (mOnRefershListener != null) {
                    mOnRefershListener.onLoadingMore();
                }
            }
        }
    }

    /**
     * 当滚动时调用
     *
     * @param firstVisibleItem
     *            当前屏幕显示在顶部的item的position
     * @param visibleItemCount
     *            当前屏幕显示了多少个条目的总数
     * @param totalItemCount
     *            ListView的总条目的总数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        firstVisibleItemPosition = firstVisibleItem;
        if (getLastVisiblePosition() == (totalItemCount - 1)) {
            isScrollToBottom = true;
        } else {
            isScrollToBottom = false;
        }
    }

    /**
     * 设置刷新监听事件
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefershListener = listener;
    }

    /**
     * 隐藏头布局
     */
    public void hideHeaderView() {
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        ivRotate.setVisibility(View.VISIBLE);
        currentState = DOWN_PULL_REFRESH;
    }

    /**
     * 隐藏脚布局
     */
    public void hideFooterView() {
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        isLoadingMore = false;
    }
}
