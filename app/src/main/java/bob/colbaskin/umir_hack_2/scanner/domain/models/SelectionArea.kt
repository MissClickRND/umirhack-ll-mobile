package bob.colbaskin.umir_hack_2.scanner.domain.models

data class SelectionArea(
    val topLeft: PointF,
    val topRight: PointF,
    val bottomLeft: PointF,
    val bottomRight: PointF
) {
    data class PointF(val x: Float, val y: Float)
}
