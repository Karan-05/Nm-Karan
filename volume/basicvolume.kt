import org.jzy3d.analysis.AWTAbstractAnalysis
import java.lang.Exception
import java.nio.ByteBuffer

/**
 *
 * @author Jacok Filik
 */
class BasicVolumeDemo : AWTAbstractAnalysis() {
    fun init() {
        val colorMapper = ColorMapper(ColorMapRainbow(), 0, 1, Color(1, 1, 1, 1.5f))
        val buffer: ByteBuffer = GLBuffers.newDirectByteBuffer(10 * 10 * 10 * 4)
        // make some kind of volume
        var x = 0f
        while (x < 2) {
            var y = 0f
            while (y < 2) {
                var z = 0f
                while (z < 2) {
                    buffer.putFloat(Math.sin((x * y * z).toDouble()).toFloat())
                    z += 0.2.toFloat()
                }
                y += 0.2.toFloat()
            }
            x += 0.2.toFloat()
        }
        val volume = Texture3D(
            buffer, intArrayOf(10, 10, 10), 0f, 1f,
            colorMapper, BoundingBox3d(1, 10, 1, 10, 1, 10)
        )

        // Create a chart
        chart = AWTChartFactory().newChart(Quality.Intermediate())
        chart.getScene().getGraph().add(volume)
        chart.getView()
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            AnalysisLauncher.open(BasicVolumeDemo())
        }
    }
}