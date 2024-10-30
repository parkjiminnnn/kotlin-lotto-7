package lotto.domain

class WinningResult(private val lottos: List<Lotto>, private val inputNumbers: InputNumbers) {
    private var countByMatchCount = mutableListOf(0, 0, 0, 0, 0)

    private fun getMatchCount(): MutableList<Int> {
        for (i in lottos.indices)
            when (lottos[i].winningCount(inputNumbers)) {
                3 -> countByMatchCount[0]++
                4 -> countByMatchCount[1]++
                5 -> bonusCount(i)
                6 -> countByMatchCount[4]++
            }
        return countByMatchCount
    }

    private fun bonusCount(i: Int): Int {
        if (lottos[i].isBonus(inputNumbers)) return countByMatchCount[3]++
        return countByMatchCount[2]++
    }

    fun getRateOfReturn(purchasedPrice: String): Float {
        val totalPrize = Ranking.entries
            .mapIndexed { index, ranking -> countByMatchCount[index] * ranking.prize }
            .sum()
        val rate = totalPrize / purchasedPrice.toFloat() * 100
        val formatedRate = String.format("%.2f", rate).toFloat()
        return formatedRate
    }

    fun getPrizeRankMsg(): List<String> {
        countByMatchCount = getMatchCount()

        return Ranking.entries.mapIndexed { index, ranking ->
            "${ranking.formattedMsg()}${countByMatchCount[index]}"
        }
    }
}
