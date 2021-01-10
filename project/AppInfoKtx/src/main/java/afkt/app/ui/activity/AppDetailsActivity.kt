package afkt.app.ui.activity

import afkt.app.R
import afkt.app.databinding.ActivityAppDetailsBinding
import afkt.app.ui.adapter.KeyValueAdapter
import afkt.app.utils.ExportUtils
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import dev.utils.DevFinal
import dev.utils.app.AppUtils
import dev.utils.app.ResourceUtils
import dev.utils.app.helper.ViewHelper
import dev.utils.app.info.AppInfoItem
import dev.utils.app.info.AppInfoUtils
import dev.utils.app.info.KeyValueBean
import dev.utils.app.logger.DevLogger
import dev.utils.app.toast.ToastTintUtils

class AppDetailsActivity : AppCompatActivity(),
    View.OnClickListener {

    private lateinit var binding: ActivityAppDetailsBinding

    // = Object =

    private var appInfoItem: AppInfoItem? = null // APP 信息 Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        try {
            appInfoItem =
                AppInfoUtils.getAppInfoItem(getIntent().getStringExtra(DevFinal.PACKNAME))
        } catch (e: Exception) {
            DevLogger.e(e)
        }
        if (appInfoItem == null) {
            ToastTintUtils.warning(ResourceUtils.getString(R.string.str_get_appinfo_fail))
            finish()
            return
        }

        setSupportActionBar(binding.vidAadToolbar)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            // 给左上角图标的左边加上一个返回的图标
            actionBar.setDisplayHomeAsUpEnabled(true)
            // 对应 ActionBar.DISPLAY_SHOW_TITLE
            actionBar.setDisplayShowTitleEnabled(false)
            // 设置点击事件
            binding.vidAadToolbar.setNavigationOnClickListener { finish() }
        }

        // 获取 APP 信息
        val appInfoBean = appInfoItem!!.appInfoBean
        ViewHelper.get()
            .setImageDrawable(binding.vidAadAppIgview, appInfoBean.appIcon) // 设置 app 图标
            .setText(binding.vidAadNameTv, appInfoBean.appName) // 设置 app 名
            .setText(binding.vidAadVnameTv, appInfoBean.versionName) // 设置 app 版本

        var lists = appInfoItem!!.listKeyValues
        lists.add(
            0,
            KeyValueBean.get(
                R.string.str_app_market,
                ResourceUtils.getString(R.string.str_goto_app_market)
            )
        )
        lists.add(
            1,
            KeyValueBean.get(
                R.string.str_app_details_setting,
                ResourceUtils.getString(R.string.str_goto_app_details_setting)
            )
        )
        binding.vidAadRecy.adapter =
            KeyValueAdapter(lists).setListener(object : KeyValueAdapter.Listener {
                override fun onItemClick(
                    item: KeyValueBean,
                    position: Int
                ): Boolean {
                    return when (position) {
                        0 -> {
                            if (!AppUtils.launchAppDetails(appInfoBean.appPackName, "")) {
                                ToastTintUtils.error(ResourceUtils.getString(R.string.str_operate_fail))
                            }
                            true
                        }
                        1 -> {
                            if (AppUtils.isInstalledApp(appInfoBean.appPackName)) {
                                AppUtils.launchAppDetailsSettings(appInfoBean.appPackName)
                            } else {
                                ToastTintUtils.error(ResourceUtils.getString(R.string.str_app_not_exist))
                            }
                            true
                        }
                        else -> false
                    }
                }
            })
        binding.vidAadOpenAppTv.setOnClickListener(this)
        binding.vidAadUninstallTv.setOnClickListener(this)
    }

    // ===========
    // = OnClick =
    // ===========

    override fun onClick(v: View) {
        if (!AppUtils.isInstalledApp(appInfoItem!!.appInfoBean.appPackName)) {
            ToastTintUtils.error(ResourceUtils.getString(R.string.str_app_not_exist))
            return
        }
        when (v.id) {
            R.id.vid_aad_open_app_tv -> AppUtils.launchApp(appInfoItem!!.appInfoBean.appPackName)
            R.id.vid_aad_uninstall_tv -> AppUtils.uninstallApp(appInfoItem!!.appInfoBean.appPackName)
        }
    }

    // ========
    // = Menu =
    // ========

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bar_menu_app_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId != R.id.bmai_export_app_msg) {
            if (!AppUtils.isInstalledApp(appInfoItem!!.appInfoBean.appPackName)) {
                ToastTintUtils.error(ResourceUtils.getString(R.string.str_app_not_exist))
                return false
            }
        }
        when (item.itemId) {
            R.id.bmai_share -> {
                appInfoItem?.let { ExportUtils.shareApp(it) }
            }
            R.id.bmai_export_app -> {
                appInfoItem?.let { ExportUtils.exportApp(it) }
            }
            R.id.bmai_export_app_msg -> {
                appInfoItem?.let { ExportUtils.exportInfo(it) }
            }
        }
        return true
    }
}