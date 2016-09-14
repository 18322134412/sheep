package com.xpple.sheep.ui;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.util.CommonUtils;

/**
 * Vip说明页
 *
 * @author nEdAy
 */
public class VipHelpActivity extends BaseActivity {
    private static final String[] vipList = {"0-4", "5-11", "12-22", "23-46",
            "47-88", "89-154", "155-250", "251-382", "383-556", "557-778",
            "779-1054"};
    private static final int[] vipImageList = {R.mipmap.level_0, R.mipmap.level_1,
            R.mipmap.level_2, R.mipmap.level_3, R.mipmap.level_4, R.mipmap.level_5, R.mipmap.level_6,
            R.mipmap.level_7, R.mipmap.level_8, R.mipmap.level_9, R.mipmap.level_10};

    @Override
    public int bindLayout() {
        return R.layout.activity_vip_help;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("等级说明", "返回");
        // 新建TableLayout01的实例
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        // 全部列自动填充空白处
        tableLayout.setStretchAllColumns(true);
        // 生成10行，8列的表格
        for (int row = 0; row < 11; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER_VERTICAL);
            int px = CommonUtils.dip2px(mContext, 20F);
            tableRow.setPadding(px, px, px, px / 2);
            // tv用于显示
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(vipImageList[row]);
            tableRow.addView(imageView);
            TextView textView = new TextView(mContext);
            textView.setText(vipList[row]);
            textView.setTextColor(getBaseContext().getResources().getColor(
                    R.color.global_red_click_color));
            tableRow.addView(textView);
            // 新建的TableRow添加到TableLayout
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            View line = new View(mContext);
            line.setBackgroundColor(getBaseContext().getResources().getColor(
                    R.color.global_line_color));
            LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(mContext, 1));
            line.setLayoutParams(l);
            tableLayout.addView(line);
        }
    }

}
