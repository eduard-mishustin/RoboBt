package magym.robobt.common.tea

import magym.robobt.common.tea.component.Actor
import magym.robobt.common.tea.component.Reducer
import magym.robobt.common.tea.component.UiStateMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * [Store], TEA (The Elm Architecture) core, MVI like architecture
 *
 * Store accepts initial [State], set of [Actor]'s, [Reducer] and [UiStateMapper]
 *
 * It is also necessary to pass all parts of tea as generics. Exactly:
 * - [Command] - that triggers [Actor], see [Actor] for more details
 * - [Effect] - single live event
 * - [Event] - Event that triggers [Reducer], see [Reducer] for more details
 * - [UiEvent] - User intent. Extends from [Event]. Sent from the UI
 * - [State] - Presentation layer state
 * - [UiState] - UI layer state
 */
internal class TeaEngine<Command : Any, Effect : Any, Event : Any, UiEvent : Event, State : Any, UiState : Any>(
	internal val initialState: State,
	private val initialEvents: List<Event> = emptyList(),
	private val reducer: Reducer<Command, Effect, Event, State>,
	private val uiStateMapper: UiStateMapper<State, UiState>? = null,
	private val actor: Actor<Command, Event>,
	// TODO: Add UnhandledExceptionHandler for Store
) : Store<Effect, UiEvent, UiState> {

	override val effects: Flow<Effect>
		get() = effectsChannel.receiveAsFlow()

	override val state: StateFlow<UiState>
		get() = uiStateFlow.asStateFlow()

	private lateinit var storeScope: CoroutineScope

	internal val stateFlow = MutableStateFlow(initialState)

	private val uiStateFlow = MutableStateFlow(initialState.toUiState())
	private val commandsFlow = MutableSharedFlow<Command>(replay = 1)
	private val effectsChannel = Channel<Effect>(Channel.BUFFERED)
	private val eventsFlow = MutableSharedFlow<Event>(replay = 1)

	fun launch(scope: CoroutineScope) {
		storeScope = scope

		setupStateFlow()
		setupCommandsFlow(actor)
		setupEventsFlow(reducer)

		applyInitialEvents(initialEvents)
	}

	override fun accept(event: UiEvent) {
		checkLaunch()
		storeScope.launch { eventsFlow.emit(event) }
	}

	private fun setupStateFlow() {
		stateFlow
			.map { it.toUiState() }
			.onEach(uiStateFlow::emit)
			.launchIn(scope = storeScope)
	}

	private fun setupCommandsFlow(actor: Actor<Command, Event>) {
		actor.act(commandsFlow)
			.onEach(eventsFlow::emit)
			.launchIn(scope = storeScope)
	}

	private fun setupEventsFlow(reducer: Reducer<Command, Effect, Event, State>) {
		eventsFlow
			.map { event -> reducer.reduce(stateFlow.value, event) }
			.onEach { update -> update.state?.let { stateFlow.emit(it) } }
			.onEach { update -> update.commands.forEach { commandsFlow.emit(it) } }
			.onEach { update -> update.effects.forEach { effectsChannel.send(it) } }
			.launchIn(scope = storeScope)
	}

	private fun applyInitialEvents(initialEvents: List<Event>) {
		if (initialEvents.isEmpty()) return

		storeScope.launch {
			eventsFlow.emitAll(initialEvents.asFlow())
		}
	}

	private fun checkLaunch() {
		if (::storeScope.isInitialized) return
		error("TeaEngine hasn't launched. Did you call launch()?")
	}

	@Suppress("UNCHECKED_CAST")
	private fun State.toUiState(): UiState {
		return uiStateMapper?.map(this) ?: this as UiState
	}
}