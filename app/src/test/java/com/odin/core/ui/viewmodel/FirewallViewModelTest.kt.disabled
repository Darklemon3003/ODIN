package com.odin.core.ui.viewmodel

import com.odin.core.domain.model.FirewallRule
import com.odin.core.domain.repository.FirewallRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class FirewallViewModelTest {
    
    @Mock
    private lateinit var firewallRepository: FirewallRepository
    
    private lateinit var viewModel: FirewallViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = FirewallViewModel(firewallRepository)
    }
    
    @Test
    fun `toggle rule calls repository`() = runTest {
        val rule = FirewallRule(
            id = "1",
            name = "Test Rule",
            sourceIp = "192.168.1.1",
            sourcePort = 80,
            destinationIp = "10.0.0.1",
            destinationPort = 443,
            protocol = "TCP",
            enabled = true,
            createdAt = System.currentTimeMillis()
        )
        
        viewModel.toggleRule(rule)
        
        verify(firewallRepository).toggleRule(rule.id, false)
    }
    
    @Test
    fun `create rule validates input`() = runTest {
        val invalidRule = FirewallRule(
            id = "1",
            name = "",
            sourceIp = "",
            sourcePort = -1,
            destinationIp = "",
            destinationPort = -1,
            protocol = "",
            enabled = true,
            createdAt = System.currentTimeMillis()
        )
        
        viewModel.createRule(invalidRule)
        
        // Verify validation errors
    }
    
    @Test
    fun `delete rule calls repository`() = runTest {
        viewModel.deleteRule("1")
        
        verify(firewallRepository).deleteRule("1")
    }
}
