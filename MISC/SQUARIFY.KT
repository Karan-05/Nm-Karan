import org.jzy3d.analysis.AWTAbstractAnalysis
import java.lang.Exception

class SquarifyDemo : AWTAbstractAnalysis() {
    fun init() {
        // Define a function to plot
        val mapper: Mapper = object : Mapper() {
            fun f(x: Double, y: Double): Double {
                return x * Math.sin(x * y) * 10
            }
        }

        // Define range and precision for the function to plot
        val xrange = Range(-2.5f, 2.5f)
        val steps = 80
        val yrange = Range(-5, 5)

        // Create the object to represent the function over the given range.
        val surface: Shape = SurfaceBuilder().orthonormal(OrthonormalGrid(xrange, steps, yrange, steps), mapper)
        surface.setColorMapper(
            ColorMapper(
                ColorMapRainbow(), surface.getBounds().getZmin(),
                surface.getBounds().getZmax(), Color(1, 1, 1, .5f)
            )
        )
        surface.setFaceDisplayed(true)
        surface.setWireframeDisplayed(false)

        // Create a chart
        chart = AWTChartFactory.chart(Quality.Intermediate())

        // This addition keeps the aspect ratio of the X and Y data
        // but makes X and Z square
        chart.getView().setSquarifier(XZSquarifier())
        chart.getView().setSquared(true)
        chart.getScene().getGraph().add(surface)
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            AnalysisLauncher.open(SquarifyDemo())
        }
    }
}