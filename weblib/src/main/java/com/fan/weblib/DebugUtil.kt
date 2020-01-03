package com.fan.weblib

import android.app.Activity
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup


class DebugUtil {
    companion object {
        /**
         * 打印当前Activity里面所点击或所有的View，取决于 {@see printClickView}
         * @param activity 当前Activity
         * @param printInDebug debug模式打印
         * @param printClickView true：打印当前点击的View，false：打印所有
         * @param isShowComplete 显示完整的信息
         * @param isShowViewGroup 是否显示ViewGroup控件
         */
        fun printViewInfo(
            activity: Activity,
            printInDebug: Boolean = true,
            printClickView: Boolean = true, isShowComplete: Boolean = false,
            isShowViewGroup: Boolean = true,
            header: String = "控件信息----------------------"
        ) {
            if (!isDebuggable(activity) && printInDebug) {
                return
            }
            if (printClickView) {
                printClickView(activity, isShowComplete, isShowViewGroup, header)
            } else {
                printAllView(activity, isShowComplete, header)
            }
        }

        private fun isDebuggable(activity: Activity): Boolean {
            var debuggable = false
            val pm = activity.packageManager
            try {
                val app = pm.getApplicationInfo(activity.packageName, 0)
                debuggable = 0 != app.flags and ApplicationInfo.FLAG_DEBUGGABLE
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return debuggable
        }

        private fun printClickView(
            activity: Activity,
            isShowCompleteName: Boolean,
            isShowViewGroup: Boolean,
            header: String
        ) {
            forView(activity) {
                if (!isShowViewGroup && it is ViewGroup) {
                    return@forView
                }
                it.setOnClickListener { item ->
                    logViewInfo(item, isShowCompleteName, header)
                }
            }
        }

        private fun printAllView(
            activity: Activity,
            isShowCompleteName: Boolean,
            header: String
        ) {
            forView(activity) {
                val greenObserver = it.viewTreeObserver
                greenObserver.addOnPreDrawListener {
                    logViewInfo(it, isShowCompleteName, header)
                    return@addOnPreDrawListener true
                }
            }
        }

        private fun forView(activity: Activity, detail: (item: View) -> Unit) {
            val list = getAllChildViews(activity)
            for (item in list) {
                detail(item)
            }
        }

        private fun logViewInfo(item: View, isShowCompleteName: Boolean, header: String) {
            val id = "0x${Integer.toHexString(item.id)}"

            val basicEnd =
                "\n---------- 控件ID可从R文件对应找到XML写入的ID ----------" +
                        "\n(app -> build -> generated -> not_namespaced_r_class_sources -> debug" +
                        " -> processDebugResources -> r -> 包名 -> R.java)" +
                        "\n------------------------------ END ------------------------------"
            val basic: String = String.format(
                "$header\n" +
                        " 控件类型：%s; \n" +
                        " 控件ID：%s; \n ",
                if (isShowCompleteName)
                    item.javaClass.name
                else
                    item.javaClass.simpleName,
                if ("0xffffffff" == id) "未设置" else id
            )

            Log.e(
                "TAG",
                if (isShowCompleteName) {
                    basic + String.format(
                        "x轴开始坐标：%s y轴开始坐标：%s \n " +
                                "x轴结束坐标：%s y轴结束坐标：%s $basicEnd",
                        item.left, item.top,
                        item.left + item.width, item.top + item.height
                    )
                } else {
                    basic
                }
            )
        }


        /**
         * @note 获取该activity所有view
         * @author liuh
         */
        private fun getAllChildViews(activity: Activity): List<View> {
            val view = activity.window.decorView
            return getAllChildViews(view)
        }

        private fun getAllChildViews(view: View): List<View> {
            val allChildren = ArrayList<View>()
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val viewChild = view.getChildAt(i)
                    allChildren.add(viewChild)
                    allChildren.addAll(getAllChildViews(viewChild))
                }
            }
            return allChildren
        }
    }
}