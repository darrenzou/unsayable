package com.darrenzou.unsayable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.darrenzou.unsayable.ui.UnsayableApp
import com.darrenzou.unsayable.ui.UnsayableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnsayableTheme {
                UnsayableApp()
            }
        }
    }
}
