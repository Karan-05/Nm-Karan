import org.jzy3d.analysis.AWTAbstractAnalysis
import java.lang.Exception

/**
 * Demonstrate how to use vertex and fragment shaders.
 *
 * @author Jacok Filik
 */
class BigWaterfallDemo : AWTAbstractAnalysis() {
    fun init() {
        val x = FloatArray(1000)
        for (i in x.indices) {
            x[i] = -3f + 6f * (i.toFloat() / (x.size - 1))
        }
        val y = FloatArray(100)
        for (i in y.indices) {
            y[i] = -3f + 2f * (i.toFloat() / (y.size - 1))
        }
        val z = getZ(x, y)
        val colorMapper = ColorMapper(ColorMapRainbow(), -2, 2, Color(1, 1, 1, .5f))
        val builder = ShaderWaterfallVBOBuilder(
            x, y, z,
            ColorMapper(ColorMapRainbow(), -1, 1, Color(1, 1, 1, .5f))
        )
        val shape = ShaderWaterfallDrawableVBO(builder, colorMapper)
        builder.earlyInitalise(shape)


        // Create a chart
        chart = AWTChartFactory.chart(Quality.Intermediate())
        chart.getScene().getGraph().add(shape)
        chart.getView()
    }

    private fun getZ(x: FloatArray, y: FloatArray): FloatArray {
        val z = FloatArray(x.size * y.size)
        for (i in y.indices) {
            for (j in x.indices) {
                z[j + x.size * i] = f(x[j].toDouble(), y[i].toDouble()).toFloat()
            }
        }
        return z
    }

    private fun f(x: Double, y: Double): Double {
        return x * Math.sin(x * y)
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            AnalysisLauncher.open(BigWaterfallDemo())
        }
    }
}