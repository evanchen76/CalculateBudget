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

        var totalAmount = 0.0
        var firstBudgetAmount = 0.0
        var lastBudgetAmount = 0.0

        val firstMonthBudget = getBudgetsByDate(budgets, startDate)
        val lastMonthBudget = getBudgetsByDate(budgets, endDate)
        if (firstMonthBudget != null) {
            val budgetLastDate = LocalDate.of(budgetYearMonthToDate(firstMonthBudget).year,budgetYearMonthToDate(firstMonthBudget).month, budgetYearMonthToDate(firstMonthBudget).lengthOfMonth())

            val days = dayDuration(startDate, budgetLastDate)
            val budgeDate = budgetYearMonthToDate(firstMonthBudget)
            val budgetPerDay = budgetPerDay(firstMonthBudget, budgeDate)
            firstBudgetAmount = budgetPerDay * days
        }

        if (lastMonthBudget != null) {
            val budgetFirstDate = LocalDate.of(budgetYearMonthToDate(lastMonthBudget).year,budgetYearMonthToDate(lastMonthBudget).month, 1)

            val days = dayDuration(budgetFirstDate, endDate)
            val budgeDate = budgetYearMonthToDate(lastMonthBudget)
            val budgetPerDay = budgetPerDay(lastMonthBudget, budgeDate)
            lastBudgetAmount = budgetPerDay * days
        }

        return firstBudgetAmount + lastBudgetAmount
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
