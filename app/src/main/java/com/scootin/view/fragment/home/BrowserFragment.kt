package com.scootin.view.fragment.home

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentBrowserBinding
import com.scootin.util.fragment.autoCleared


class BrowserFragment : Fragment(R.layout.fragment_browser) {

    private var binding by autoCleared<FragmentBrowserBinding>()
    private val args: BrowserFragmentArgs by navArgs()

    private val url by lazy {
        args.url
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBrowserBinding.bind(view)


        binding.webview.getSettings().apply {
            javaScriptEnabled = true
        }
        binding.webview.webViewClient = MyWebViewClient()
        binding.webview.loadUrl(url)
    }

    class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            return false
        }
    }
}