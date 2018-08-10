import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BudgetCalculator(val repository: IBudgetRepository) {
    fun getBudget(startDate: LocalDate, endDate: LocalDate): Double {
        val budgets = repository.getAll()
        if (toYearMonthFormat(startDate) == toYearMonthFormat(endDate)) {

            val budget = getBudgetsByDate(budgets, startDate)
            if (budget != null) {

                val days = dayDuration(startDate, endDate)

                val budgeDate = budgetYearMonthToDate(budget)
                val budgetPerDay = budgetPerDay(budget, budgeDate)
                return budgetPerDay * days
            }
        }

        var firstBudgetAmount = 0.0
        var lastBudgetAmount = 0.0
        var middleMonthsAmount = 0.0

        val firstMonthBudget = getBudgetsByDate(budgets, startDate)
        val lastMonthBudget = getBudgetsByDate(budgets, endDate)
        val middleMonthBudget = budgets.filterNot { it.yearMonth == toYearMonthFormat(startDate) || it.yearMonth == toYearMonthFormat(endDate) }
        if (firstMonthBudget != null) {
            val budgetLastDate = LocalDate.of(budgetYearMonthToDate(firstMonthBudget).year, budgetYearMonthToDate(firstMonthBudget).month, budgetYearMonthToDate(firstMonthBudget).lengthOfMonth())

            val days = dayDuration(startDate, budgetLastDate)
            firstBudgetAmount = budgetAmount(firstMonthBudget, days)
        }

        if (lastMonthBudget != null) {
            val budgetFirstDate = LocalDate.of(budgetYearMonthToDate(lastMonthBudget).year, budgetYearMonthToDate(lastMonthBudget).month, 1)

            val days = dayDuration(budgetFirstDate, endDate)
            lastBudgetAmount = budgetAmount(lastMonthBudget, days)
        }

        for (budget in middleMonthBudget) {
            val days = budgetYearMonthToDate(budget).lengthOfMonth().toLong()
            middleMonthsAmount += budgetAmount(budget, days)
        }

        return firstBudgetAmount + lastBudgetAmount + middleMonthsAmount
    }

    private fun budgetAmount(monthBudget: Budget, daysInBudget: Long): Double {
        val budgeDate = budgetYearMonthToDate(monthBudget)
        val budgetPerDay = budgetPerDay(monthBudget, budgeDate)
        return budgetPerDay * daysInBudget
    }

    private fun getBudgetsByDate(budgets: List<Budget>, date: LocalDate) =
            budgets.find { it.yearMonth == toYearMonthFormat(date) }

    private fun dayDuration(startDate: LocalDate, endDate: LocalDate) =
            Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays() + 1

    private fun budgetPerDay(budget: Budget, budgeDate: LocalDate) = budget.budgeMoney / budgeDate.lengthOfMonth()

    private fun budgetYearMonthToDate(budget: Budget) =
            LocalDate.of(budget.yearMonth.substring(0, 4).toInt(), budget.yearMonth.substring(4).toInt(), 1)

    private fun toYearMonthFormat(localDate: LocalDate) = localDate.format(DateTimeFormatter.ofPattern("yyyyMM"))

}
