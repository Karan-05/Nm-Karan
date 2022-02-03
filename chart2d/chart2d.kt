import org.jzy3d.chart.Chart
import java.awt.Color
import java.awt.Component
import java.lang.Exception
import java.util.ArrayList

object Chart2dDemo {
    var duration = 60f

    /** milisecond distance between two generated samples  */
    var interval = 50
    var maxfreq = 880
    var nOctave = 5
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val log = PitchAndAmplitudeCharts(duration, maxfreq, nOctave)
        TimeChartWindow(log.charts)
        generateSamplesInTime(log)
        // generateSamples(log, 500000);
    }

    @Throws(InterruptedException::class)
    fun generateSamples(log: PitchAndAmplitudeCharts, n: Int) {
        println("will generate $n samples")
        for (i in 0 until n) {
            // Random audio info
            val pitch = Math.random() * maxfreq
            val ampli = Math.random()

            // Add to time series
            log.seriePitch.add(time(n, i), pitch)
            log.serieAmpli.add(time(n, i), ampli)
        }
    }

    fun time(n: Int, i: Int): Double {
        return i.toDouble() / n * duration
    }

    @Throws(InterruptedException::class)
    fun generateSamplesInTime(log: PitchAndAmplitudeCharts) {
        println("will generate approx. " + duration * 1000 / interval + " samples")
        start()
        while (elapsed() < duration) {
            // Random audio info
            val pitch = Math.random() * maxfreq
            val ampli = Math.random()

            // Add to time series
            log.seriePitch.add(elapsed(), pitch)
            log.serieAmpli.add(elapsed(), ampli)

            // Wait a bit
            Thread.sleep(interval.toLong())
        }
    }

    /** Simple timer  */
    internal var start: Long = 0
    fun start() {
        start = System.nanoTime()
    }

    fun elapsed(): Double {
        return (System.nanoTime() - start) / 1000000000.0
    }

    /** Hold 2 charts, 2 time series, and 2 drawable lines  */
    class PitchAndAmplitudeCharts(timeMax: Float, freqMax: Int, nOctave: Int) {
        var pitchChart: Chart2d
        var ampliChart: Chart2d
        var seriePitch: Serie2d
        var serieAmpli: Serie2d
        var pitchLineStrip: ConcurrentLineStrip
        var amplitudeLineStrip: ConcurrentLineStrip

        init {
            pitchChart = Chart2d()
            pitchChart.asTimeChart(timeMax, 0, freqMax, "Time", "Frequency")
            val axe: IAxisLayout = pitchChart.getAxisLayout()
            axe.setYTickProvider(PitchTickProvider(nOctave))
            axe.setYTickRenderer(PitchTickRenderer())
            seriePitch = pitchChart.getSerie("frequency", Serie2d.Type.LINE)
            seriePitch.setColor(Color.BLUE)
            pitchLineStrip = seriePitch.getDrawable() as ConcurrentLineStrip
            ampliChart = Chart2d()
            ampliChart.asTimeChart(timeMax, 0, 1.1f, "Time", "Amplitude")
            serieAmpli = ampliChart.getSerie("amplitude", Serie2d.Type.LINE)
            serieAmpli.setColor(Color.RED)
            amplitudeLineStrip = serieAmpli.getDrawable() as ConcurrentLineStrip
        }

        val charts: List<Any>
            get() {
                val charts: MutableList<Chart> = ArrayList<Chart>()
                charts.add(pitchChart)
                charts.add(ampliChart)
                return charts
            }
    }

    /** A frame to show a list of charts  */
    class TimeChartWindow(charts: List<Chart>) : JFrame() {
        init {
            LookAndFeel.apply()
            val lines = "[300px]"
            val columns = "[500px,grow]"
            setLayout(MigLayout("", columns, lines))
            var k = 0
            for (c in charts) {
                addChart(c, k++)
            }
            windowExitListener()
            this.pack()
            show()
            setVisible(true)
        }

        fun addChart(chart: Chart, id: Int) {
            val canvas = chart.getCanvas() as Component
            val chartPanel = JPanel(BorderLayout())
            /*
             * chartPanel.setMaximumSize(null); chartPanel.setMinimumSize(null);
             * canvas.setMinimumSize(null); canvas.setMaximumSize(null);
             */
            val b: Border = BorderFactory.createLineBorder(Color.black)
            chartPanel.setBorder(b)
            chartPanel.add(canvas, BorderLayout.CENTER)
            add(chartPanel, "cell 0 $id, grow")
        }

        fun windowExitListener() {
            addWindowListener(object : WindowAdapter() {
                override fun windowClosing(e: WindowEvent) {
                    this@TimeChartWindow.dispose()
                    System.exit(0)
                }
            })
        }

        companion object {
            private const val serialVersionUID = 7519209038396190502L
        }
    }
}