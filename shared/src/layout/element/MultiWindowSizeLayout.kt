package layout.element

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun MultiWindowSizeLayout(
    default: @Composable () -> Unit,
    expanded: (@Composable () -> Unit)? = null,
    portrait: (@Composable () -> Unit)? = null,
    portraitTablet: (@Composable () -> Unit)? = null,
    portraitPhone: (@Composable () -> Unit)? = null
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    when {
        windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT &&
                windowSizeClass.windowHeightSizeClass in listOf(
            WindowHeightSizeClass.MEDIUM,
            WindowHeightSizeClass.EXPANDED
        ) -> (portraitPhone ?: portrait ?: default)()

        windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM &&
                windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.EXPANDED ->
            (portraitTablet ?: portrait ?: default)()

        windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED &&
                windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.EXPANDED ->
            (expanded ?: default)()

        else -> default()
    }
}   