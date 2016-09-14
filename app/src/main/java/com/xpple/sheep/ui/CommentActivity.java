package com.xpple.sheep.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.xpple.sheep.R;
import com.xpple.sheep.adapter.CommentAdapter;
import com.xpple.sheep.base.BaseListActivity;
import com.xpple.sheep.bean.Comment;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.proxy.CommentProxy;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.view.dialog.ActionSheetDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论页
 *
 * @author nEdAy
 */
public class CommentActivity extends BaseListActivity implements CommentProxy.IQueryItemListener, CommentProxy.IQueryCountItemListener, CommentProxy.IQueryMoreItemListener, CommentProxy.IAddItemListener, CommentProxy.IDeleteItemListener {
    private CommentProxy queryProxy;
    private CommentAdapter adapter;
    private ArrayList<Comment> items = new ArrayList<>();
    private boolean isOrder;//true - 升序 ； false - 降序
    private String itemObjectId;
    private ImageView iv_comment;
    private EditText et_comment;
    private String parentCommentObjectId, parentCommentAuthorNickname;
    private static String mLastContent;
    private ImageView mRightImageButton;
    private TextView mRightTextButton;
    private SVProgressHUD svProgressHUD;

    @Override
    public int bindLayout() {
        return R.layout.activity_comment;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        mRightTextButton = (TextView) findViewById(R.id.right_text_btn);
        mRightImageButton = (ImageView) findViewById(R.id.right_image_btn);
        initTopBarForBoth("评论", "返回", R.mipmap.ic_sort_ascending, () -> {
            if (isOrder) {
                mRightImageButton.setImageResource(R.mipmap.ic_sort_ascending);
                CommonUtils.showToast("最新发表的评论在最上面");
                isOrder = false;
                QueryItem();
            } else {
                mRightImageButton.setImageResource(R.mipmap.ic_ascending_sort);
                CommonUtils.showToast("最先发表的评论在最上面");
                isOrder = true;
                QueryItem();
            }
        });
        initListView();
        iv_comment = (ImageView) findViewById(R.id.iv_comment);
        et_comment = (EditText) findViewById(R.id.et_comment);
        et_comment.addTextChangedListener(mCommentWatcher);
        svProgressHUD = new SVProgressHUD(mContext);
        itemObjectId = getIntent().getStringExtra("itemObjectId");
        adapter = new CommentAdapter(mContext, items);
        listView.setAdapter(adapter);
        queryProxy = new CommentProxy();
        queryProxy.setQueryItemListener(this);
        queryProxy.setQueryCountItemListener(this);
        queryProxy.setQueryMoreItemListener(this);
        queryProxy.setDeleteItemListener(this);
        // 根据项目ID 非刷新主动查询 评论 按时间降序
        // 主动查询
        listView.refresh();
        iv_comment.setOnClickListener(v -> {
            User currentUser = getUserProxy().getCurrentUser();
            if (currentUser == null) {
                CommonUtils.showToast("请先登录");
                getOperation().startActivity(LoginActivity.class);
            } else {
                String mContent = et_comment.getText().toString().trim();
                if (mContent.equals(mLastContent)) {
                    CommonUtils.showToast("请勿重复提交相同评论，谢谢^_^");
                } else if (TextUtils.isEmpty(mContent)) {
                    CommonUtils.showToast("请勿提交空白评论，谢谢^_^");
                } else {
                    queryProxy.setAddItemListener(this);
                    JsonObject author = new JsonObject();
                    author.addProperty("__type", "Pointer");
                    author.addProperty("className", "_User");
                    author.addProperty("objectId", currentUser.getObjectId());
                    JsonObject comment = new JsonObject();
                    comment.add("author", author);
                    if (parentCommentObjectId == null) {
                        comment.addProperty("itemObjectId", itemObjectId);
                        comment.addProperty("content", mContent);
                    } else {
                        comment.addProperty("itemObjectId", parentCommentObjectId);
                        comment.addProperty("content", "回复 " + parentCommentAuthorNickname + " 的评论： " + mContent);
                    }
                    queryProxy.addItem(comment, mContent);
                    /**隐藏软键盘**/
                    View _view = getWindow().peekDecorView();
                    if (_view != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(_view.getWindowToken(), 0);
                    }
                    svProgressHUD.showWithStatus("发送中...");
                }
            }
        });
    }


    @Override
    protected void QueryItem() {
        queryProxy.QueryItem(true, isOrder, itemObjectId);
    }

    @Override
    protected void QueryCountItem() {
        queryProxy.QueryCountItem(itemObjectId);
    }

    @Override
    protected void OnItemClickListener(int position) {
        Comment comment = items.get(position);
        User user = getUserProxy().getCurrentUser();
        if (user == null) {//先检查登录状态
            getOperation().startActivity(LoginActivity.class);
            CommonUtils.showToast("请先登录");
            return;
        }
        if (comment.getAuthor().getObjectId().equals(user.getObjectId())) {//登录用户Id和发布回复者Id相同
            new ActionSheetDialog(mContext).builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拷贝", ActionSheetDialog.SheetItemColor.Blue,
                            which -> copyComment(comment.getContent()))
                    .addSheetItem("删除", ActionSheetDialog.SheetItemColor.Blue,
                            which -> {
                                queryProxy.deleteItem(items.get(position).getObjectId(), user.getSessionToken());
                                svProgressHUD.showWithStatus("删除中...");
                            })
                    .show();
        } else {
            new ActionSheetDialog(mContext).builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拷贝", ActionSheetDialog.SheetItemColor.Blue,
                            which -> copyComment(comment.getContent()))
                    .addSheetItem("回复评论", ActionSheetDialog.SheetItemColor.Blue,
                            which -> {
                                parentCommentObjectId = comment.getObjectId();
                                parentCommentAuthorNickname = comment.getAuthor().getNickname();
                                et_comment.setHint("回复 " + parentCommentAuthorNickname + " 的评论：");
                                mRightTextButton.setVisibility(View.VISIBLE);
                                mRightTextButton.setText("取消回复");
                                mRightTextButton.setOnClickListener(v -> {
                                    parentCommentObjectId = null;
                                    parentCommentAuthorNickname = null;
                                    mRightTextButton.setVisibility(View.GONE);
                                    et_comment.setHint(null);
                                });
                            })
                    .show();
        }
    }

    private void copyComment(String content) {
        ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Comment", content);
        mClipboardManager.setPrimaryClip(mClipData);
        CommonUtils.showToast("已复制内容到剪切板");
    }

    @Override
    public void onQueryItemSuccess(List<Comment> object, boolean isUpdate) {
        curPage = 0;//重置页码
        if (CommonUtils.isNotNull(object)) {
            rl_no_network.setVisibility(View.GONE);
            rl_no_data.setVisibility(View.GONE);
            if (isUpdate) {
                items.clear();
            }
            adapter.addAll(object);
            if (object.size() < 10) {
                listView.setRefreshSuccess("加载完成"); // 通知加载完成
            } else {
                listView.setRefreshSuccess(); // 通知刷新成功
                listView.startLoadMore(); // 开启LoadingMore功能
            }
        } else {
            listView.setRefreshSuccess("暂无数据");
            rl_no_network.setVisibility(View.GONE);
            rl_no_data.setVisibility(View.VISIBLE);
            rl_no_data.setOnClickListener(v -> QueryItem());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onQueryItemFailure() {
        rl_no_network.setVisibility(View.VISIBLE);
        rl_no_network.setOnClickListener(v -> QueryItem());
        listView.setRefreshFail();// 通知刷新失败
    }

    /**
     * 查询更多-1
     */

    @Override
    public void onQueryCountItemSuccess(String count) {
        try {
            if (Integer.parseInt(count) > items.size()) {
                curPage++;
                queryMoreNearList(curPage);
            } else {
                listView.stopLoadMore();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onQueryCountItemFailure() {
        listView.stopLoadMore();
    }

    /**
     * 查询更多-2
     */

    private void queryMoreNearList(int page) {
        queryProxy.QueryMoreItem(page, isOrder, itemObjectId);
    }

    @Override
    public void onQueryMoreItemSuccess(List<Comment> object) {
        if (CommonUtils.isNotNull(object)) {
            adapter.addAll(object);
        }
        adapter.notifyDataSetChanged();
        listView.setLoadMoreSuccess();
    }

    @Override
    public void onQueryMoreItemFailure() {
        listView.stopLoadMore();
    }

    TextWatcher mCommentWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                iv_comment.setImageResource(R.mipmap.comment_send_pressed);
                iv_comment.setEnabled(true);
            }
        }
    };

    @Override
    public void onAddItemSuccess(String mContent) {
        svProgressHUD.showSuccessWithStatus("评论成功");
        et_comment.setText(null);
        mLastContent = mContent;
        QueryItem();
    }

    @Override
    public void onAddItemFailure() {
        svProgressHUD.showErrorWithStatus("发送失败");
    }

    @Override
    public void onDeleteItemSuccess() {
        svProgressHUD.showSuccessWithStatus("删除成功");
        QueryItem();
    }

    @Override
    public void onDeleteItemFailure() {
        svProgressHUD.showErrorWithStatus("删除失败");
    }


}
