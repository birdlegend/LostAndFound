package com.zsb.lostandfound;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zsb.lostandfound.adapter.BaseAdapterHelper;
import com.zsb.lostandfound.adapter.QuickAdapter;
import com.zsb.lostandfound.base.EditPopupWindow;
import com.zsb.lostandfound.bean.Found;
import com.zsb.lostandfound.bean.Lost;
import com.zsb.lostandfound.config.Constants;
import com.zsb.lostandfound.i.IPopupItemClick;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * 主界面
 * Created by zsb on 2015/3/25
 */
public class MainActivity extends BaseActivity
        implements View.OnClickListener, IPopupItemClick,
        AdapterView.OnItemLongClickListener {
    RelativeLayout layout_action;//标题栏上下文菜单
    LinearLayout layout_all;  //标题栏
    TextView tv_lost;   //标题栏中间文本框
    ListView listview;
    Button btn_add;  //添加信息按钮

    protected QuickAdapter<Lost> lostAdapter;// 失物

    protected QuickAdapter<Found> foundAdapter;// 招领

    private Button layout_found;  //标题栏中间found
    private Button layout_lost;
    PopupWindow morePop;

    RelativeLayout progress;
    LinearLayout layout_no;
    TextView tv_no;

    EditPopupWindow mPopupWindow;
    int position;


    /**
     * 设置布局文件
     */
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    /**
     * 初始化布局文件中控件
     */
    @Override
    public void initViews() {
        //下载失物招领信息列表时显示
        progress = (RelativeLayout) findViewById(R.id.progress);
        //无失物信息时显示
        layout_no = (LinearLayout) findViewById(R.id.layout_no);
        tv_no = (TextView) findViewById(R.id.tv_no);

        //标题栏中间上下文菜单(Lost和Found)
        layout_action = (RelativeLayout) findViewById(R.id.layout_action);
        //标题栏中间文本加图片
        layout_all = (LinearLayout) findViewById(R.id.layout_all);
        // 默认是失物界面, 标题栏中间文本
        tv_lost = (TextView) findViewById(R.id.tv_lost);
        tv_lost.setTag("Lost");
        listview = (ListView) findViewById(R.id.list_lost);
        btn_add = (Button) findViewById(R.id.btn_add);

        //初始化长按弹窗
        initEditPop();
    }


    /**
     * 触屏标题栏文本显示列表选项
     */
//    @SuppressWarnings("deprecation")
    private void showListPop() {
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_lost, null);
            // 注入
            layout_found = (Button) view.findViewById(R.id.layout_found);
            layout_lost = (Button) view.findViewById(R.id.layout_lost);
            layout_found.setOnClickListener(this);
            layout_lost.setOnClickListener(this);

            morePop = new PopupWindow(view, mScreenWidth, 300);
            morePop.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        morePop.dismiss();
                        return true;
                    }
                    return false;
                }
            });


            morePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            morePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            morePop.setTouchable(true);
            morePop.setFocusable(true);
            morePop.setOutsideTouchable(true);
            morePop.setBackgroundDrawable(new BitmapDrawable());

            // 动画效果 从顶部弹下
            morePop.setAnimationStyle(R.style.MenuPop);
            morePop.showAsDropDown(layout_all, 0, -dip2px(this, 2.0F));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEditPop() {
        mPopupWindow = new EditPopupWindow(this, 200, 48);
        mPopupWindow.setOnPopupItemClickListner(this);
    }

    /**
     * 初始化控件监听
     */
    @Override
    public void initListeners() {
        listview.setOnItemLongClickListener(this);
        btn_add.setOnClickListener(this);
        layout_all.setOnClickListener(this);
    }

    /**
     * 进行数据初始化
     */
    @Override
    public void initData() {
        if (lostAdapter == null) {
            lostAdapter = new QuickAdapter<Lost>(this, R.layout.item_list) {
                @Override
                protected void convert(BaseAdapterHelper helper, Lost lost) {
                    helper.setText(R.id.tv_title, lost.getTitle())
                            .setText(R.id.tv_describe, lost.getDescribe())
                            .setText(R.id.tv_time, lost.getCreatedAt())
                            .setText(R.id.tv_photo, lost.getPhone());
                }
            };
        }

        if (foundAdapter == null) {
            foundAdapter = new QuickAdapter<Found>(this, R.layout.item_list) {
                @Override
                protected void convert(BaseAdapterHelper helper, Found found) {
                    helper.setText(R.id.tv_title, found.getTitle())
                            .setText(R.id.tv_describe, found.getDescribe())
                            .setText(R.id.tv_time, found.getCreatedAt())
                            .setText(R.id.tv_photo, found.getPhone());
                }
            };
        }

        //默认加载失物界面
        listview.setAdapter(lostAdapter);
        queryLosts();
    }

    //设置标题栏文字显示和tag
    private void changeTextView(View v) {
        if (v == layout_found) {
            tv_lost.setTag("Found");
            tv_lost.setText("Found");
        } else {
            tv_lost.setTag("Lost");
            tv_lost.setText("Lost");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == layout_all) {
            showListPop();
        } else if (v == btn_add) {
            Intent intent = new Intent(this, AddActivity.class);
            intent.putExtra("from", tv_lost.getTag().toString());
            startActivityForResult(intent, Constants.REQUESTCODE_ADD);
        } else if (v == layout_found) {
            changeTextView(v);
            morePop.dismiss();
            queryFounds();
        } else if (v == layout_lost) {
            changeTextView(v);
            morePop.dismiss();
            queryLosts();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int[] location = new int[2]; //保存x,y坐标
        view.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(view, Gravity.RIGHT | Gravity.TOP,
                location[0], getStateBar() + location[1]);
        return false;
    }

    //添加成功后返回本页面,重新刷新列表
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUESTCODE_ADD:// 添加成功之后的回调
                String tag = tv_lost.getTag().toString();
                if (tag.equals("Lost")) {
                    queryLosts();
                } else {
                    queryFounds();
                }
                break;
        }
    }

    /**
     * 检索更新lost列表
     */
    private void queryLosts() {
        showView();
        BmobQuery<Lost> query = new BmobQuery<Lost>();
        query.order("-createAt"); //按时间排序
        query.findObjects(this, new FindListener<Lost>() {
            @Override
            public void onSuccess(List<Lost> losts) {
                lostAdapter.clear();
                foundAdapter.clear();
                if (losts == null || losts.size() == 0) {
                    showErrorView(1);
                    foundAdapter.notifyDataSetChanged(); //通知更新数据
                    return;
                }

                lostAdapter.addAll(losts);
                listview.setAdapter(lostAdapter);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onError(int i, String s) {
                showErrorView(1);
            }
        });
    }

    /**
     * 检索更新found列表
     */
    public void queryFounds() {
        showView();
        BmobQuery<Found> query = new BmobQuery<Found>();
        query.order("-createAt"); //按时间排序
        query.findObjects(this, new FindListener<Found>() {
            @Override
            public void onSuccess(List<Found> founds) {
                lostAdapter.clear();
                foundAdapter.clear();
                if (founds == null || founds.size() == 0) {
                    showErrorView(1);
                    foundAdapter.notifyDataSetChanged(); //通知更新数据
                    return;
                }

                foundAdapter.addAll(founds);
                listview.setAdapter(foundAdapter);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onError(int i, String s) {
                showErrorView(1);
            }
        });
    }

    /**
     * 请求出错或者无数据时候显示的界面 showErrorView
     *
     * @return void
     * @throws
     */
    private void showErrorView(int tag) {
        progress.setVisibility(View.GONE);
        listview.setVisibility(View.GONE);
        layout_no.setVisibility(View.VISIBLE);
        if (tag == 0)
            tv_no.setText(getResources().getText(R.string.list_no_data_lost));
        else
            tv_no.setText(getResources().getText(R.string.list_no_data_found));
    }

    /**
     * 有数据时显示列表
     */
    private void showView() {
        listview.setVisibility(View.VISIBLE);
        layout_no.setVisibility(View.GONE);
    }

    @Override
    public void onEdit(View v) {
        String tag = tv_lost.getTag().toString();
        Intent intent = new Intent(this, AddActivity.class);
        String title = "";
        String phone = "";
        String describe = "";
        String objectId = "";

        if (tag.equals("Lost")) {
            title = lostAdapter.getItem(position).getTitle();
            describe = lostAdapter.getItem(position).getDescribe();
            phone = lostAdapter.getItem(position).getPhone();
        } else {
            title = foundAdapter.getItem(position).getTitle();
            describe = foundAdapter.getItem(position).getDescribe();
            phone = foundAdapter.getItem(position).getPhone();
            objectId = foundAdapter.getItem(position).getObjectId();
        }

        intent.putExtra("objectId", objectId);
        intent.putExtra("describe", describe);
        intent.putExtra("phone", phone);
        intent.putExtra("title", title);
        intent.putExtra("from", tag);

        startActivityForResult(intent, Constants.REQUESTCODE_ADD);
    }

    @Override
    public void onDelete(View v) {
        String tag = tv_lost.getTag().toString();
        if (tag.equals("Lost"))
            deleteLost();
        else
            deleteFound();

    }

    private void deleteLost() {
        Lost lost = new Lost();
        lost.setObjectId(lostAdapter.getItem(position).getObjectId());
        lost.delete(this, new DeleteListener() {
            @Override
            public void onSuccess() {
                lostAdapter.remove(position);
                showToast("删除成功");
            }

            @Override
            public void onFailure(int i, String s) {
                showToast("删除失败");
            }
        });
    }

    private void deleteFound() {
        Found found = new Found();
        found.setObjectId(foundAdapter.getItem(position).getObjectId());
        found.delete(this, new DeleteListener() {
            @Override
            public void onSuccess() {
                foundAdapter.remove(position);
                showToast("删除成功");
            }

            @Override
            public void onFailure(int i, String s) {
                showToast("删除失败");
            }
        });
    }
}


