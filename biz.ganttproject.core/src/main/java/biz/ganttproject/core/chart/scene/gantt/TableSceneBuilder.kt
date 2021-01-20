/*
Copyright 2021 BarD Software s.r.o, Dmitry Kazakov

This file is part of GanttProject, an open-source project management tool.

GanttProject is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

GanttProject is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with GanttProject.  If not, see <http://www.gnu.org/licenses/>.
*/
package biz.ganttproject.core.chart.scene.gantt

import biz.ganttproject.core.chart.canvas.Canvas

class TableSceneBuilder(private val config: Config) {
  fun build(rows: List<Row>, canvas: Canvas = Canvas()): Canvas {
    val dimensions = calculateDimensions(rows)
    val state = PaintState(config.rowHeight)

    rows.forEach {
      val rectangle = canvas.createRectangle(
        0, state.y, dimensions.width, config.rowHeight
      )
      if (state.rowNumber % 2 == 1) {
        rectangle.style = "odd-row"
      }
      paintRow(it, rectangle.middleY, canvas)
      state.toNextRow()
    }

    return canvas
  }

  private fun calculateDimensions(rows: List<Row>): Dimension {
    val height = config.rowHeight * rows.size
    val width = rows.map { it.width + it.indent + 2 * config.horizontalOffset }.maxOrNull() ?: 0
    return Dimension(height, width)
  }

  private fun paintRow(row: Row, y: Int, canvas: Canvas) {
    val text = canvas.createText(row.indent + config.horizontalOffset, y, row.text)
    text.setAlignment(Canvas.HAlignment.LEFT, Canvas.VAlignment.CENTER)
  }

  data class Row(val width: Int, val text: String, val indent: Int)

  data class Config(val rowHeight: Int, val horizontalOffset: Int)

  private data class Dimension(val height: Int, val width: Int)

  private class PaintState(private val rowHeight: Int) {
    var y = 0
      private set
    var rowNumber = 0
      private set

    fun toNextRow() {
      y += rowHeight
      rowNumber++
    }
  }
}
