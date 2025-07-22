package com.artemklymenko.common.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavGraphBuilder

interface FeatureApi {
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    )
}