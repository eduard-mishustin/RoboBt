package magym.robobt.common.tea.util

import magym.robobt.common.tea.component.Actor
import kotlinx.coroutines.flow.merge

internal fun <Command : Any, Event : Any> combineActors(
	actors: Set<Actor<Command, Event>>,
): Actor<Command, Event> {
	return Actor { commands ->
		actors
			.map { actor -> actor.act(commands) }
			.merge()
	}
}