import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.LocalDate

class BudgetCalculatorTests {

    @Mock
    lateinit var stubBudgetRepository: IBudgetRepository

    lateinit var budgetCalculator: BudgetCalculator

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        budgetCalculator = BudgetCalculator(stubBudgetRepository)
    }

    @Test
    fun sameDate() {

        val startDate = LocalDate.of(2018, 1, 1)
        val endDate = LocalDate.of(2018, 1, 1)

        val budgets = listOf(Budget("201801", 310.0))
        `when`(stubBudgetRepository.getAll()).thenReturn(budgets)

        bugdetShouldBe(startDate, endDate, 10.0)
    }


    @Test
    fun different_Date() {

        val startDate = LocalDate.of(2018, 1, 1)
        val endDate = LocalDate.of(2018, 1, 2)

        val budgets = listOf(Budget("201801", 310.0))
        `when`(stubBudgetRepository.getAll()).thenReturn(budgets)

        bugdetShouldBe(startDate, endDate, 20.0)

    }

    @Test
    fun cross_One_Month() {

        val startDate = LocalDate.of(2018, 1, 1)
        val endDate = LocalDate.of(2018, 2, 10)

        val budgets = listOf(Budget("201801", 310.0), Budget("201802", 2800.0))

        `when`(stubBudgetRepository.getAll()).thenReturn(budgets)

        bugdetShouldBe(startDate, endDate, 1310.0)
    }

    @Test
    fun cross_Multiple_Month() {

        val startDate = LocalDate.of(2018, 1, 30)
        val endDate = LocalDate.of(2018, 3, 10)

        val budgets = listOf(Budget("201801", 310.0), Budget("201802", 2800.0), Budget("201803", 6200.0))

        `when`(stubBudgetRepository.getAll()).thenReturn(budgets)

        bugdetShouldBe(startDate, endDate, 4820.0)
    }

    @Test
    fun cross_Multiple_Month_With_SomeMonth_No_Budget() {

        val startDate = LocalDate.of(2018, 1, 30)
        val endDate = LocalDate.of(2018, 3, 10)

        val budgets = listOf(Budget("201801", 310.0), Budget("201803", 6200.0))

        `when`(stubBudgetRepository.getAll()).thenReturn(budgets)

        bugdetShouldBe(startDate, endDate, 2020.0)
    }

    private fun bugdetShouldBe(startDate: LocalDate, endDate: LocalDate, expected: Double) {
        val budget = budgetCalculator.getBudget(startDate, endDate)
        Assert.assertEquals(expected, budget, 0.001)
    }

}