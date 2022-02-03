import org.jzy3d.chart.Chart
import java.lang.Exception
import java.util.*

object ScatterDemoEmulGL {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val q: Quality = Quality.Advanced()
        q.setAnimated(false)
        q.setHiDPIEnabled(true) // need java 9+ to enable HiDPI & Retina displays
        val chart: Chart = EmulGLChartFactory().newChart(q)
        chart.add(scatter(50000))
        chart.open()
        chart.addMouseCameraController()
        val skin: EmulGLSkin = EmulGLSkin.on(chart)
        skin.getCanvas().setProfileDisplayMethod(true)
    }

    private fun scatter(size: Int): Scatter {
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
            a = 0.75f
            colors[i] = Color(x, y, z, a)
        }
        val scatter = Scatter(points, colors)
        scatter.setWidth(3)
        return scatter
    }
}