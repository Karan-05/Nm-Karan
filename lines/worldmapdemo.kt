import org.jzy3d.analysis.AWTAbstractAnalysis
import java.lang.Exception

class WorldMapDemo : AWTAbstractAnalysis() {
    @Throws(Exception::class)
    fun init() {
        // Create the world map chart
        val f: AWTChartFactory = object : AWTChartFactory() {
            fun newView(factory: IChartFactory?, scene: Scene?, canvas: ICanvas?, quality: Quality?): View {
                return CroppingView(factory, scene, canvas, quality)
            }
        }
        chart = f.newChart(Quality.Advanced())

        // Instantiate world map and parse the file
        val worldMap = WorldMapLoader()
        worldMap.parseFile("data/csvfiles/world_map.csv")

        // Add world map line stripe to chart
        chart.getScene().getGraph().add(worldMap.lineStrips)

        // Set axis labels for chart
        val axeLayout: IAxisLayout = chart.getAxisLayout()
        axeLayout.setXAxisLabel("Longitude (deg)")
        axeLayout.setYAxisLabel("Latitude (deg)")
        axeLayout.setZAxisLabel("Altitude (km)")

        // Set precision of tick values
        axeLayout.setXTickRenderer(IntegerTickRenderer())
        axeLayout.setYTickRenderer(IntegerTickRenderer())
        axeLayout.setZTickRenderer(IntegerTickRenderer())

        // Define ticks for axis
        axeLayout.setXTickProvider(SmartTickProvider(10))
        axeLayout.setYTickProvider(SmartTickProvider(10))
        axeLayout.setZTickProvider(SmartTickProvider(10))

        // Set map viewpoint
        chart.getView().setViewPoint(Coord3d(-2 * Math.PI / 3, Math.PI / 4, 0))

        // Animate bounds change for demo
        Executors.newCachedThreadPool().execute(shiftBoundsTask())
    }

    private fun shiftBoundsTask(): Runnable {
        return object : Runnable {
            var step = 1
            override fun run() {
                while (true) {
                    val b: BoundingBox3d = chart.getView().getBounds()
                    chart.getView().setScaleX(b.getXRange().add(step), false)
                    chart.getView().setScaleY(b.getYRange().add(step), false)
                    chart.getView().shoot()
                    try {
                        Thread.sleep(25)
                    } catch (e: InterruptedException) {
                    }
                }
            }
        }
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            AnalysisLauncher.open(WorldMapDemo())
        }
    }
}