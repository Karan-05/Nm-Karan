import org.jzy3d.analysis.AWTAbstractAnalysis
import java.lang.Exception

class WaterfallDemo : AWTAbstractAnalysis() {
    fun init() {
        val x = FloatArray(80)
        for (i in x.indices) {
            x[i] = -3f + 6f * (i.toFloat() / (x.size - 1))
        }
        val y = FloatArray(40)
        for (i in y.indices) {
            y[i] = -3f + 2f * (i.toFloat() / (y.size - 1))
        }
        val z = getZ(x, y)
        val waterfall = WaterfallTessellator()
        val build: Shape = waterfall.build(x, y, z)
        build.setColorMapper(
            ColorMapper(
                ColorMapRainbow(), build.getBounds().getZmin(),
                build.getBounds().getZmax(), Color(1, 1, 1, 1.0f)
            )
        )

        // Create a chart
        chart = AWTChartFactory.chart(Quality.Intermediate())
        chart.getScene().getGraph().add(build)
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
            AnalysisLauncher.open(WaterfallDemo())
        }
    }
}