import org.jzy3d.analysis.AWTAbstractAnalysis
import java.lang.Exception
import java.nio.ByteBuffer

/**
 * Get lizard file from http://download.jzy3d.org/objfiles/lizard.mat
 *
 * @author Jacok Filik
 */
class LizardVolumeDemo : AWTAbstractAnalysis() {
    fun init() {
        var buffer: ByteBuffer? = null
        var shape: IntArray? = null
        var max = Float.NEGATIVE_INFINITY
        var min = Float.POSITIVE_INFINITY
        try {
            val mfr = MatFileReader("data/lizard.mat")
            val data: MLNumericArray<Int> = mfr.getMLArray("data") as MLNumericArray<Int>
            shape = data.getDimensions()
            val size: Int = data.getSize()
            data.toString()
            buffer = GLBuffers.newDirectByteBuffer(size * 4)
            for (i in 0 until size) {
                val f: Float = data.get(i).floatValue()
                buffer.putFloat(f)
                if (f < min) {
                    min = f
                }
                if (f > max) {
                    max = f
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        val temp = shape[0]
        shape[0] = shape[2]
        shape[2] = temp
        val colorMapper = ColorMapper(ColorMapGrayscale(), min, max, Color(1, 1, 1, .5f))
        val volume = Texture3D(
            buffer, shape, min + (max - min) / 10,
            max - (max - min) / 10, colorMapper,
            BoundingBox3d(0, shape[2], 0, shape[1], 0, shape[0])
        )

        //Transform transform = new Transform();
        //transform.add(new Rotate(90, new Coord3d(0,1,0)));
        //volume.setTransformBefore(transform);

        // Create a chart
        chart = AWTChartFactory.chart(Quality.Intermediate())
        chart.getScene().getGraph().add(volume)
        // chart.getView().setBackgroundColor(new Color(0, 0, 0));
        // IAxeLayout axeLayout = chart.getAxeLayout();
        // axeLayout.setMainColor(new Color(0.7f, 0.7f, 0.7f));
        chart.getView().setSquared(false)
        chart.getView()
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            AnalysisLauncher.open(LizardVolumeDemo())
        }
    }
}