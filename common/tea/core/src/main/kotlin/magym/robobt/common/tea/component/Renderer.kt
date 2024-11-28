package magym.robobt.common.tea.component

/**
 * [render] function with state or effect param that implemented in UI
 */
fun interface Renderer<Value : Any> {

    fun render(value: Value)
}