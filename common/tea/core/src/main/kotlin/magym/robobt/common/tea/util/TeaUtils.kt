package magym.robobt.common.tea.util

import kotlinx.coroutines.flow.merge
import magym.robobt.common.tea.component.Actor

internal fun <Command : Any, Event : Any> combineActors(
    actors: Set<Actor<Command, Event>>,
): Actor<Command, Event> {
    return Actor { commands ->
        actors
            .map { actor -> actor.act(commands) }
            .merge()
    }
}