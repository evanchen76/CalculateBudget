import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BudgetCalculator(val repository: IBudgetRepository ) {
    fun getBudget(startDate: LocalDate, endDate: LocalDate): Double {
        val budgets = repository.getAll()
        if (toYearMonthFormat(startDate) == toYearMonthFormat(endDate)) {

            val budget = budgets.find { it.yearMonth == toYearMonthFormat(startDate) }
            if (budget != null) {

                val days = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays() + 1


                val budgeDate = LocalDate.of(budget.yearMonth.substring(0, 4).toInt(), budget.yearMonth.substring(4).toInt(), 1)
                val budgetPerDay = budget.budgeMoney / budgeDate.lengthOfMonth()
                return budgetPerDay * days
            }
        }
        return 0.0
    }

    private fun toYearMonthFormat(localDate: LocalDate) = localDate.format(DateTimeFormatter.ofPattern("yyyyMM"))

}
