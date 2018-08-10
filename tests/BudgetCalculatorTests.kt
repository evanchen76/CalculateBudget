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

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun sameDate() {

        val startDate = LocalDate.of(2018, 1, 1)
        val endDate = LocalDate.of(2018, 1, 1)

        val budgets = listOf(Budget("201801", 310.0))
        `when`(stubBudgetRepository.getAll()).thenReturn(budgets)

        val budgetCalculator = BudgetCalculator(stubBudgetRepository, startDate, endDate)
        val budget = budgetCalculator.getBudget()
        Assert.assertEquals(10.0, budget, 0.001)
    }

    @Test
    fun different_Date() {

        val startDate = LocalDate.of(2018, 1, 1)
        val endDate = LocalDate.of(2018, 1, 2)

        val budgets = listOf(Budget("201801", 310.0))
        `when`(stubBudgetRepository.getAll()).thenReturn(budgets)

        val budgetCalculator = BudgetCalculator(stubBudgetRepository, startDate, endDate)
        val budget = budgetCalculator.getBudget()
        Assert.assertEquals(20.0, budget, 0.001)
    }

//    @Test
//    fun cross_Date() {
//
//        val startDate = LocalDate.of(2018, 1, 1)
//        val endDate = LocalDate.of(2018, 3, 10)
//
//        val budgets = listOf(Budget("201801", 310.0), Budget("201802", 3000.0), Budget("201803", 6200.0))
//
//        `when`(stubBudgetRepository.getAll()).thenReturn(budgets)
//
//        val budgetCalculator = BudgetCalculator(stubBudgetRepository, startDate, endDate)
//        val budget = budgetCalculator.getBudget()
//        Assert.assertEquals(5220.0, budget, 0.001)
//    }


}