package com.zsb.lostandfound.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zsb.lostandfound.R;
import com.zsb.lostandfound.i.IPopupItemClick;

/**
 * Created by zsb on 2015/3/25.
 */
public class EditPopupWindow extends BasePopupWindow implements View.OnClickListener {
    private TextView mEdit;
    private TextView mDelete;
    private IPopupItemClick mOnPopupItemClickListner;

    public EditPopupWindow(Context context, int width, int height) {
        super(LayoutInflater.from(context).inflate(R.layout.pop_device, null),
                dpToPx(context, width), dpToPx(context, height));
        setAnimationStyle(R.style.PopupAnimation);
    }

    /**
     * 像素转化: dp转为px
     *
     * @param context
     * @param dp
     * @return px
     */
    private static int dpToPx(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    /**
     * 添加确认单击监听
     *
     * @param l
     */
    @Override
    public void setOnSubmitClickListener(onSubmitClickListener l) {
        super.setOnSubmitClickListener(l);
    }

    @Override
    public void init() {

    }

    @Override
    public void initEvents() {
        mEdit.setOnClickListener(this);
        mDelete.setOnClickListener(this);
    }

    @Override
    public void initViews() {
        mEdit = (TextView) findViewById(R.id.tv_pop_edit);
        mDelete = (TextView) findViewById(R.id.tv_pop_delete);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pop_edit:
                if (mOnPopupItemClickListner != null)
                    mOnPopupItemClickListner.onEdit(v);
                break;
            case R.id.tv_pop_delete:
                if (mOnPopupItemClickListner != null)
                    mOnPopupItemClickListner.onDelete(v);
                break;
        }
        dismiss();
    }

    public void setOnPopupItemClickListner(IPopupItemClick iPopupItemClick) {
        mOnPopupItemClickListner = iPopupItemClick;
    }
}
