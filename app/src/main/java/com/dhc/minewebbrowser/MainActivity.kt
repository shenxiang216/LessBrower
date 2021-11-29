package com.dhc.minewebbrowser

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var editText: EditText
    private lateinit var progressBar: ProgressBar

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 隐藏状态栏并全屏
        val lp: WindowManager.LayoutParams = this.getWindow().getAttributes()
        lp.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        this.getWindow().setAttributes(lp)

        setContentView(R.layout.activity_main)

        initView()
        initListener()
        loadDefaultUrl()
    }

    private fun loadDefaultUrl() {
        //访问网页
        webView.loadUrl("https://www.bing.com")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack()
                return true;
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initView() {
        webView = findViewById(R.id.webView)
        editText = findViewById(R.id.editText)
        progressBar = findViewById(R.id.progressBar)
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true//js和android交互
        settings.allowFileAccess = true // 允许访问文件
        settings.setAppCacheEnabled(true) //设置H5的缓存打开,默认关闭
        settings.useWideViewPort = true//设置webview自适应屏幕大小
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS//设置，可能的话使所有列的宽度不超过屏幕宽度
        settings.loadWithOverviewMode = true//设置webview自适应屏幕大小
        settings.domStorageEnabled = true//设置可以使用localStorage
        settings.setSupportZoom(false)//关闭zoom按钮
        settings.builtInZoomControls = false//关闭zoom
    }

    private fun initListener() {
        editText.setOnEditorActionListener { p0, p1, p2 ->
            if (p2?.keyCode == KeyEvent.KEYCODE_ENTER) {
                var url = editText.text.toString()
                if (!url.startsWith("http") && !url.startsWith("https")) {
                    url = "http://$url"
                }
                webView.loadUrl(url)
                ImTool.closeKeyBoard(baseContext, editText)
            }
            false
        }
        webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress >= 99) {
                    progressBar.visibility = View.GONE
                } else if (progressBar.visibility == View.GONE) {
                    progressBar.visibility = View.VISIBLE
                }
                progressBar.progress = newProgress
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                editText.setText(url)
            }
        }
        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    fun myclick(source: View?) {
        Toast.makeText(applicationContext, "按钮被点击了", Toast.LENGTH_SHORT).show()
        loadDefaultUrl()
    }

}
