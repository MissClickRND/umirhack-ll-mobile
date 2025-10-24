package bob.colbaskin.hack_template.navigation.graphs

import kotlinx.serialization.Serializable

interface Graphs {

    @Serializable
    data object Main: Graphs

    @Serializable
    data object Onboarding: Graphs

    @Serializable
    data object Detailed: Graphs
}
