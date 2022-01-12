package github.alexzhirkevich.community.features.mediagrid

internal fun calculateComboAspectRadio(
    width1 : Float, height1 : Float, width2:Float,height2 : Float, isColumn : Boolean
) : Float {
    val aspect1 = width1 / height1
    val aspect2 = width2 / height2

    return if (isColumn) {
        if (aspect1 >= aspect2) {
            maxOf(width1, width2) / ((width1 / width2 * height2) + height1)
        } else {
            calculateComboAspectRadio(width2, height2, width1, height1, isColumn)
        }
    } else {
        if (aspect1 >= aspect2) {
            ((height1 / height2 * width2) + width1) / minOf(height1, height2)
        } else {
            calculateComboAspectRadio(width2,height2,width1,height1, isColumn)
        }
    }
}