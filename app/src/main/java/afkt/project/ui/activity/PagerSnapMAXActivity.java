package afkt.project.ui.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import afkt.project.R;
import afkt.project.base.app.BaseActivity;
import afkt.project.databinding.BaseViewRecyclerviewBinding;
import afkt.project.model.bean.ItemBean;
import afkt.project.ui.adapter.PagerSnapMAXAdapter;
import dev.engine.log.DevLogEngine;
import dev.utils.app.ListViewUtils;
import dev.utils.app.helper.ViewHelper;

/**
 * detail: PagerSnapHelper - 无限滑动
 * @author Ttt
 * <pre>
 *     PagerSnapHelper : 每次滑动一页居中显示, 类似 ViewPager
 * </pre>
 */
public class PagerSnapMAXActivity
        extends BaseActivity<BaseViewRecyclerviewBinding> {

    private PagerSnapMAXAdapter pagerSnapAdapter;

    @Override
    public int baseLayoutId() {
        return R.layout.base_view_recyclerview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup parent = (ViewGroup) binding.vidBvrRecy.getParent();
        // 根布局处理
        ViewHelper.get().setPadding(parent, 0);
    }

    @Override
    public void initValue() {
        super.initValue();

        List<ItemBean> lists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            lists.add(ItemBean.newItemBeanPager());
        }

        // 初始化布局管理器、适配器
        pagerSnapAdapter = new PagerSnapMAXAdapter(this, lists);
        binding.vidBvrRecy.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
//        binding.vidBvrRecy.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.vidBvrRecy.setAdapter(pagerSnapAdapter);

        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(binding.vidBvrRecy);

        int size = lists.size();
        // 滑动到中间 ( 无滑动过程 )
        ((LinearLayoutManager) binding.vidBvrRecy.getLayoutManager()).scrollToPositionWithOffset(size * 100 - 1, 10);
        // 复位到中间
        ListViewUtils.smoothScrollToPosition(binding.vidBvrRecy, size * 100 + 1);
    }

    @Override
    public void initListener() {
        super.initListener();

        binding.vidBvrRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(
                    @NonNull RecyclerView recyclerView,
                    int dx,
                    int dy
            ) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(
                    @NonNull RecyclerView recyclerView,
                    int newState
            ) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        // 获取最后一个可见 view 的位置
                        int lastItemPosition = linearManager.findLastVisibleItemPosition();
                        // 获取第一个可见 view 的位置
                        int firstItemPosition = linearManager.findFirstVisibleItemPosition();

                        // 获取居中索引
                        int currentPosition = (lastItemPosition + firstItemPosition) / 2;
                        // 真实索引
                        int index = pagerSnapAdapter.getRealIndex(currentPosition);

                        DevLogEngine.getEngine().dTag(TAG, "%s - %s 当前显示索引: %s - %s", lastItemPosition, firstItemPosition, currentPosition, index);
                    }
                }
            }
        });
    }
}