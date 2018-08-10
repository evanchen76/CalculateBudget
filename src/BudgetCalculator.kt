import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BudgetCalculator(val repository: IBudgetRepository) {
    fun getBudget(startDate: LocalDate, endDate: LocalDate): Double {
        val budgets = repository.getAll()
        if (toYearMonthFormat(startDate) == toYearMonthFormat(endDate)) {

            val budget = isBudgetExist(budgets, startDate)
            if (budget != null) {

                val days = searchDays(startDate, endDate)

                val budgeDate = budgetYearMonthToDate(budget)
                val budgetPerDay = budgetPerDay(budget, budgeDate)
                return budgetPerDay * days
            }
        }
        return 0.0
    }

    private fun isBudgetExist(budgets: List<Budget>, startDate: LocalDate) =
            budgets.find { it.yearMonth == toYearMonthFormat(startDate) }

    private fun searchDays(startDate: LocalDate, endDate: LocalDate) =
            Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays() + 1

    private fun budgetPerDay(budget: Budget, budgeDate: LocalDate) = budget.budgeMoney / budgeDate.lengthOfMonth()

    private fun budgetYearMonthToDate(budget: Budget) =
            LocalDate.of(budget.yearMonth.substring(0, 4).toInt(), budget.yearMonth.substring(4).toInt(), 1)

    private fun toYearMonthFormat(localDate: LocalDate) = localDate.format(DateTimeFormatter.ofPattern("yyyyMM"))

}
