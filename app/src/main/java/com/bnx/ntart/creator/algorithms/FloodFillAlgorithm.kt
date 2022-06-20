package com.bnx.ntart.creator.algorithms

import com.bnx.ntart.creator.models.BitmapActionData
import com.bnx.ntart.creator.models.Coordinates
import java.util.*

class FloodFillAlgorithm(private val algorithmInfo: AlgorithmInfoParameter) {
    fun compute(seed: Coordinates) {
        val colorAtSeed = algorithmInfo.bitmap.getPixel(seed.x, seed.y)

        val queue = LinkedList<Coordinates>()

        queue.offer(seed)

        while (queue.isNotEmpty() && colorAtSeed != algorithmInfo.color) {
            val current = queue.poll()

            if (algorithmInfo.bitmap.getPixel(current!!.x, current.y) != colorAtSeed) {
                continue
            }

            algorithmInfo.currentBitmapAction.actionData.add(BitmapActionData(
                Coordinates(current.x, current.y),
                algorithmInfo.bitmap.getPixel(current.x, current.y),
            ))
            algorithmInfo.bitmap.setPixel(current.x, current.y, algorithmInfo.color)

            val expandToNeighborsAlgorithmInstance = ExpandToNeighborsAlgorithm(algorithmInfo.bitmap)
            for (index in expandToNeighborsAlgorithmInstance.compute(current)) {
                queue.offer(index)
            }
        }
    }
}