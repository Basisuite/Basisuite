package io.basisuite.app

import android.os.Build
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.interfaces.LifeCircleListener
import com.kongzue.baseframework.util.JumpParameter

class MainActivity : BaseActivity() {

    override fun initViews(): Unit = setLifeCircleListener(mLifecycleListener)
    override fun initDatas(parameter: JumpParameter?): Unit = Unit
    override fun setEvents(): Unit = Unit

    private val mLifecycleListener: LifeCircleListener = object : LifeCircleListener() {
        override fun onCreate() {
            super.onCreate()

            enableEdgeToEdge()
            setContent {
                ContentScope {
                    BasisuiteContent()
                }
            }
        }

        override fun onDestroy() {
            super.onDestroy()

        }
    }

    private interface IColor {
        val purple80: Color
        val purpleGrey80: Color
        val pink80: Color

        val purple40: Color
        val purpleGrey40: Color
        val pink40: Color
    }

    private interface ITypography {
        val typography: Typography
    }

    private interface ITheme {

        @Composable
        @UiComposable
        fun BasisuiteTheme(
            darkTheme: Boolean = isSystemInDarkTheme(),
            dynamicColor: Boolean = true,
            content: @Composable () -> Unit,
        )
    }

    private interface IContent {

        @Composable
        @UiComposable
        fun BasisuiteContent()
    }

    private val mColor: IColor = object : IColor {
        override val purple80: Color = Color(0xFFD0BCFF)
        override val purpleGrey80: Color = Color(0xFFCCC2DC)
        override val pink80: Color = Color(0xFFEFB8C8)

        override val purple40: Color = Color(0xFF6650a4)
        override val purpleGrey40: Color = Color(0xFF625b71)
        override val pink40: Color = Color(0xFF7D5260)
    }

    private val mTypography: ITypography = object : ITypography {

        override val typography: Typography = Typography(
            bodyLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            )
        )
    }

    private val mTheme: ITheme = object : ITheme, IColor by mColor, ITypography by mTypography {

        private val DarkColorScheme = darkColorScheme(
            primary = purple80,
            secondary = purpleGrey80,
            tertiary = pink80,
        )

        private val LightColorScheme = lightColorScheme(
            primary = purple40,
            secondary = purpleGrey40,
            tertiary = pink40,
        )

        @Composable
        override fun BasisuiteTheme(
            darkTheme: Boolean,
            dynamicColor: Boolean,
            content: @Composable (() -> Unit),
        ) {
            val colorScheme = when {
                dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    if (darkTheme) {
                        dynamicDarkColorScheme(LocalContext.current)
                    } else {
                        dynamicLightColorScheme(LocalContext.current)
                    }
                }

                darkTheme -> DarkColorScheme
                else -> LightColorScheme
            }
            MaterialTheme(
                colorScheme = colorScheme,
                typography = typography,
                content = content,
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private val mContent: IContent = object : IContent, ITheme by mTheme {

        @Composable
        override fun BasisuiteContent() {
            BasisuiteTheme {
                ActivityMain()
            }
        }

        @Composable
        private fun ActivityMain() {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(
                                    id = R.string.app_name
                                ),
                            )
                        },
                    )
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                ) {

                }
            }
        }
    }

    @Composable
    private fun ContentScope(
        block: @Composable IContent.() -> Unit,
    ) = block.invoke(mContent)

    @Preview(showBackground = true)
    @Composable
    private fun UiPreview() {
        ContentScope {
            BasisuiteContent()
        }
    }
}

