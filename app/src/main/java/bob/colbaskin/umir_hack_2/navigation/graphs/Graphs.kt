package bob.colbaskin.umir_hack_2.navigation.graphs

import kotlinx.serialization.Serializable

interface Graphs {

    @Serializable
    data object Main: Graphs

    @Serializable
    data object Onboarding: Graphs

    @Serializable
    data object Auth: Graphs

    @Serializable
    data object Detailed: Graphs
}
