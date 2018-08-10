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

        val budget = budgetCalculator.getBudget(startDate, endDate)
        Assert.assertEquals(10.0, budget, 0.001)
    }


    @Test
    fun different_Date() {

        val startDate = LocalDate.of(2018, 1, 1)
        val endDate = LocalDate.of(2018, 1, 2)

        val budgets = listOf(Budget("201801", 310.0))
        `when`(stubBudgetRepository.getAll()).thenReturn(budgets)

        val budget = budgetCalculator.getBudget(startDate, endDate)
        Assert.assertEquals(20.0, budget, 0.001)
    }


}