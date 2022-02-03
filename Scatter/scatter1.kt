import org.jzy3d.analysis.AWTAbstractAnalysis
import java.lang.Exception
import java.util.*

class ScatterDemoAWT : AWTAbstractAnalysis() {
    fun init() {
        val size = 500000
        var x: Float
        var y: Float
        var z: Float
        var a: Float
        val points: Array<Coord3d?> = arrayOfNulls<Coord3d>(size)
        val colors: Array<Color?> = arrayOfNulls<Color>(size)
        val r = Random()
        r.setSeed(0)
        for (i in 0 until size) {
            x = r.nextFloat() - 0.5f
            y = r.nextFloat() - 0.5f
            z = r.nextFloat() - 0.5f
            points[i] = Coord3d(x, y, z)
            a = 0.25f
            colors[i] = Color(x, y, z, a)
        }
        val scatter = Scatter(points, colors)
        val q: Quality = Quality.Advanced()
        // q.setPreserveViewportSize(true);
        val c = GLCapabilities(GLProfile.get(GLProfile.GL2))
        val p: IPainterFactory = AWTPainterFactory(c)
        val f: IChartFactory = AWTChartFactory(p)
        chart = f.newChart(q)
        chart.getScene().add(scatter)
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            AnalysisLauncher.open(ScatterDemoAWT())
        }
    }
}