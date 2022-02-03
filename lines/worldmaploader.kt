import org.jzy3d.colors.Color
import java.util.ArrayList

class WorldMapLoader {
    var lineStrips: MutableList<CroppableLineStrip> = ArrayList<CroppableLineStrip>()
    var newline = System.getProperty("line.separator")
    fun parseFile(filename: String?): List<CroppableLineStrip> {
        try {

            // Get world map csv location
            val worldMapFile = File(filename)

            // Create file reader and CSV reader from world map file
            val fileReader = FileReader(worldMapFile)
            val reader = CSVReader(fileReader)

            // Create row holder and line number counter
            var rowHolder: Array<String>
            var lineNumber = 1

            // Create local line strip and set color
            var lineStrip = CroppableLineStrip()
            lineStrip.setWireframeColor(Color.BLACK)

            // Loop through rows while a next row exists
            while (reader.readNext().also { rowHolder = it } != null) {
                when (rowHolder.size) {
                    1 -> {
                        if (rowHolder[0] == "") {

                            // If row is blank, add line strip to list of line
                            // strips and clear line strip
                            lineStrips.add(lineStrip)
                            lineStrip = CroppableLineStrip()
                            lineStrip.setWireframeColor(Color.BLACK)
                            break
                        } else {

                            // Throw error if a map point only has one coordinate
                            val oneCoordinateError =
                                "Error on line: " + lineNumber + newline + "The row contains only 1 coordinate"
                            JOptionPane.showMessageDialog(
                                null, oneCoordinateError,
                                "Incorrect number of coordinates", JOptionPane.ERROR_MESSAGE
                            )
                            System.exit(-1)
                        }
                        try {

                            // Add the map point to the line strip
                            lineStrip.add(
                                Point(
                                    Coord3d(
                                        java.lang.Float.valueOf(rowHolder[0]),
                                        java.lang.Float.valueOf(rowHolder[1]),
                                        0.0
                                    )
                                )
                            )
                        } catch (e: NumberFormatException) {

                            // Throw error if a map point coordinate cannot be
                            // converted to a Float
                            val malformedCoordinateError =
                                "Error on line: " + lineNumber + newline + "Coordinate is incorrectly formatted"
                            JOptionPane.showMessageDialog(
                                null, malformedCoordinateError, "Incorrect Format",
                                JOptionPane.ERROR_MESSAGE
                            )
                            e.printStackTrace()
                            System.exit(-1)
                        }
                    }
                    2 -> try {
                        lineStrip.add(
                            Point(
                                Coord3d(
                                    java.lang.Float.valueOf(rowHolder[0]),
                                    java.lang.Float.valueOf(rowHolder[1]),
                                    0.0
                                )
                            )
                        )
                    } catch (e: NumberFormatException) {
                        val malformedCoordinateError =
                            "Error on line: " + lineNumber + newline + "Coordinate is incorrectly formatted"
                        JOptionPane.showMessageDialog(
                            null, malformedCoordinateError, "Incorrect Format",
                            JOptionPane.ERROR_MESSAGE
                        )
                        e.printStackTrace()
                        System.exit(-1)
                    }
                    else -> {

                        // Throw error if the map point has more than three
                        // coordinates
                        val numCoordinateError = ("Error on line: " + lineNumber + newline
                                + "The row contains " + rowHolder.size + " coordinates")
                        JOptionPane.showMessageDialog(
                            null, numCoordinateError,
                            "Incorrect number of coordinates", JOptionPane.ERROR_MESSAGE
                        )
                        System.exit(-1)
                    }
                }

                // Add the final lineStrip after while loop is complete.
                lineStrips.add(lineStrip)
                lineNumber++
            }
            reader.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            println("WARNING: World map file not found")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return lineStrips
    }
}