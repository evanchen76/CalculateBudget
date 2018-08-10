import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BudgetCalculator(val repository: IBudgetRepository) {
    fun getBudget(startDate: LocalDate, endDate: LocalDate): Double {
        val budgets = repository.getAll()

        if (isSameMonth(startDate, endDate)) {

            return sameMonthBudget(startDate, endDate, budgets)
        }

        val firstBudgetAmount = firstMonthBudget(budgets, startDate)
        val lastBudgetAmount = lastMonthBudget(budgets, endDate)
        val middleMonthsAmount = middleMonthBudget(budgets, startDate, endDate)

        return firstBudgetAmount + lastBudgetAmount + middleMonthsAmount
    }

    private fun sameMonthBudget(startDate: LocalDate, endDate: LocalDate, budgets: List<Budget>): Double {
        val budget = getBudgetsByDate(budgets, startDate)
        if (budget != null) {
            val days = dayDuration(startDate, endDate)

            val budgeDate = budgetYearMonthToDate(budget)
            val budgetPerDay = budgetPerDay(budget, budgeDate)
            return budgetPerDay * days
        }
        return 0.0
    }

    private fun middleMonthBudget(budgets: List<Budget>, startDate: LocalDate, endDate: LocalDate): Double {
        var returnAmount = 0.0
        val middleMonthBudget = budgets.filterNot { it.yearMonth == toYearMonthFormat(startDate) || it.yearMonth == toYearMonthFormat(endDate) }
        for (budget in middleMonthBudget) {
            val days = budgetYearMonthToDate(budget).lengthOfMonth().toLong()
            returnAmount += budgetAmount(budget, days)
        }
        return returnAmount
    }

    private fun lastMonthBudget(budgets: List<Budget>, endDate: LocalDate): Double {
        val lastMonthBudget = getBudgetsByDate(budgets, endDate)
        if (lastMonthBudget != null) {
            val budgetFirstDate = LocalDate.of(budgetYearMonthToDate(lastMonthBudget).year, budgetYearMonthToDate(lastMonthBudget).month, 1)

            val days = dayDuration(budgetFirstDate, endDate)
            return budgetAmount(lastMonthBudget, days)
        }
        return 0.0
    }

    private fun firstMonthBudget(budgets: List<Budget>, startDate: LocalDate): Double {

        val firstMonthBudget = getBudgetsByDate(budgets, startDate)
        if (firstMonthBudget != null) {
            val budgetLastDate = LocalDate.of(budgetYearMonthToDate(firstMonthBudget).year, budgetYearMonthToDate(firstMonthBudget).month, budgetYearMonthToDate(firstMonthBudget).lengthOfMonth())

            val days = dayDuration(startDate, budgetLastDate)
            return budgetAmount(firstMonthBudget, days)
        }
        return 0.0
    }

    private fun isSameMonth(startDate: LocalDate, endDate: LocalDate) =
            toYearMonthFormat(startDate) == toYearMonthFormat(endDate)

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
