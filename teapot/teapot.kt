import org.jzy3d.chart.Chart

object TeapotDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val teapot = Teapot()
        teapot.setFaceDisplayed(true)
        teapot.setColor(Color.WHITE)
        teapot.setWireframeColor(Color.CYAN)
        teapot.setWireframeDisplayed(false)
        teapot.setWireframeWidth(2)
        teapot.setReflectLight(true)


        // ---------------------------------------------
        val factory: ChartFactory = AWTChartFactory()
        //ChartFactory factory = new EmulGLChartFactory();

        // Emulgl will show limitations
        // 1-wireframe and face do not mix cleanly (polygon offset fill)
        // 2-wireframe color tend to saturate (here in green)
        val q: Quality = Quality.Advanced()
        q.setDepthActivated(true)
        q.setAlphaActivated(false)
        q.setAnimated(false)
        q.setHiDPIEnabled(true)
        val chart: Chart = factory.newChart(q)
        chart.getView().setSquared(false)
        chart.getView().setBackgroundColor(Color.BLACK)
        chart.getView().getAxis().getLayout().setMainColor(Color.WHITE)
        // ---------------------------------------------
        chart.add(teapot)

        //Light light = chart.addLightOnCamera();
        val light: Light = chart.addLight(chart.getView().getBounds().getCorners().getXmaxYmaxZmax())
        //light.setRepresentationDisplayed(true);

        // ---------------------------------------------
        chart.open()
        chart.addMouseCameraController()
    }
}