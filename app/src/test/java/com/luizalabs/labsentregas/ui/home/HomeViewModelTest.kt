package com.luizalabs.labsentregas.ui.home

import com.luizalabs.labsentregas.core.data.local.DeliveryEntity
import com.luizalabs.labsentregas.domain.repository.DeliveryRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val deliveryRepository: DeliveryRepository = mockk()
    private lateinit var viewModel: HomeViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        viewModel = HomeViewModel(deliveryRepository)
    }

    @Test
    fun `quando fetchDeliveries é chamado, carrega lista de entregas`() = runTest {

        val mockDeliveries = listOf(
            DeliveryEntity(
                deliveryId = 1,
                packageQuantity = 3,
                customerName = "John Doe",
                customerCpf = "123.456.789-10",
                deliveryDeadline = "2025-01-10",
                zipCode = "12345-678",
                state = "SP",
                city = "São Paulo",
                neighborhood = "Jardins",
                street = "Rua das Flores",
                number = "100",
                complement = "Apto 202"
            ),
            DeliveryEntity(
                deliveryId = 2,
                packageQuantity = 1,
                customerName = "Jane Smith",
                customerCpf = "987.654.321-00",
                deliveryDeadline = "2025-01-15",
                zipCode = "87654-321",
                state = "RJ",
                city = "Rio de Janeiro",
                neighborhood = "Copacabana",
                street = "Avenida Atlântica",
                number = "2000",
                complement = null
            )
        )

        coEvery { deliveryRepository.getDeliveries() } returns mockDeliveries

        viewModel.fetchDeliveries()

        assertEquals(mockDeliveries, viewModel.deliveries.first())
    }

    @Test
    fun `quando uma entrega é excluida, atualiza a lista de entregas`() = runTest {

        val deliveryToDelete = DeliveryEntity(
            1,
            3,
            "2025-01-10",
            "John Doe",
            "123.456.789-10",
            "12345-678",
            "SP",
            "São Paulo",
            "Jardins",
            "Rua das Flores",
            "100",
            "Apto 202"
        )

        val updatedDeliveries = listOf(
            DeliveryEntity(
                2,
                1,
                "2025-01-15",
                "Jane Smith",
                "987.654.321-00",
                "87654-321",
                "RJ",
                "Rio de Janeiro",
                "Copacabana",
                "Avenida Atlântica",
                "2000",
                null
            )
        )

        coEvery { deliveryRepository.getDeliveries() } returnsMany listOf(
            listOf(deliveryToDelete) + updatedDeliveries,
            updatedDeliveries
        )

        coEvery { deliveryRepository.deleteDelivery(deliveryToDelete) } just Runs

        val viewModel = HomeViewModel(deliveryRepository)

        viewModel.deleteDelivery(deliveryToDelete)

        assertEquals(updatedDeliveries, viewModel.deliveries.first())
    }
}

@ExperimentalCoroutinesApi
class MainCoroutineRule(private val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}