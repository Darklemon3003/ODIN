package com.odin.core.data.repository

import android.content.Context
import com.odin.core.domain.model.FirewallRule
import com.odin.core.network.rules.DynamicRuleEngine
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FirewallRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var dynamicRuleEngine: DynamicRuleEngine
    private lateinit var repository: FirewallRepositoryImpl

    @BeforeEach
    fun setup() {
        context = mockk(relaxed = true)
        dynamicRuleEngine = mockk()
        
        val mockRules = listOf(
            FirewallRule(
                id = "rule1",
                name = "Block Malicious IP",
                sourceIp = "192.168.1.100",
                destinationIp = "0.0.0.0",
                sourcePort = 0,
                destinationPort = 0,
                protocol = "ANY",
                action = FirewallRule.Action.BLOCK,
                enabled = true,
                priority = 1
            ),
            FirewallRule(
                id = "rule2",
                name = "Allow Internal Traffic",
                sourceIp = "192.168.1.0/24",
                destinationIp = "192.168.1.0/24",
                sourcePort = 0,
                destinationPort = 0,
                protocol = "ANY",
                action = FirewallRule.Action.ALLOW,
                enabled = false,
                priority = 2
            )
        )
        
        every { dynamicRuleEngine.getAllRules() } returns mockRules
        every { DynamicRuleEngine.getInstance(context) } returns dynamicRuleEngine
        every { dynamicRuleEngine.addRule(any()) } returns Unit
        every { dynamicRuleEngine.updateRule(any()) } returns Unit
        every { dynamicRuleEngine.removeRule(any()) } returns Unit
        every { dynamicRuleEngine.toggleRule(any(), any()) } returns Unit
        
        repository = FirewallRepositoryImpl(context)
    }

    @Test
    fun `getAllRules returns loaded rules`() = runTest {
        val rules = repository.getAllRules().first()
        
        assertNotNull(rules)
        assertTrue(rules.isNotEmpty())
    }

    @Test
    fun `getActiveRules returns only enabled rules`() = runTest {
        val activeRules = repository.getActiveRules().first()
        
        assertNotNull(activeRules)
        assertTrue(activeRules.all { it.enabled })
    }

    @Test
    fun `createRule adds new rule`() = runTest {
        val newRule = FirewallRule(
            id = "rule3",
            name = "Block Suspicious Domain",
            sourceIp = "0.0.0.0",
            destinationIp = "10.0.0.50",
            sourcePort = 0,
            destinationPort = 443,
            protocol = "TCP",
            action = FirewallRule.Action.BLOCK,
            enabled = true,
            priority = 3
        )
        
        repository.createRule(newRule)
        
        val rules = repository.getAllRules().first()
        assertTrue(rules.any { it.id == "rule3" })
    }

    @Test
    fun `updateRule updates existing rule`() = runTest {
        val rules = repository.getAllRules().first()
        val ruleToUpdate = rules.first()
        
        val updatedRule = ruleToUpdate.copy(name = "Updated Rule Name")
        repository.updateRule(updatedRule)
        
        val rulesAfter = repository.getAllRules().first()
        val updated = rulesAfter.find { it.id == ruleToUpdate.id }
        
        assertNotNull(updated)
        assertEquals("Updated Rule Name", updated?.name)
    }

    @Test
    fun `deleteRule removes rule from list`() = runTest {
        val rulesBefore = repository.getAllRules().first()
        val ruleToDelete = rulesBefore.first()
        
        repository.deleteRule(ruleToDelete.id)
        
        val rulesAfter = repository.getAllRules().first()
        assertTrue(rulesAfter.none { it.id == ruleToDelete.id })
    }

    @Test
    fun `toggleRule changes enabled status`() = runTest {
        val rules = repository.getAllRules().first()
        val ruleToToggle = rules.first()
        val originalEnabled = ruleToToggle.enabled
        
        repository.toggleRule(ruleToToggle.id, !originalEnabled)
        
        val rulesAfter = repository.getAllRules().first()
        val toggled = rulesAfter.find { it.id == ruleToToggle.id }
        
        assertNotNull(toggled)
        assertEquals(!originalEnabled, toggled?.enabled)
    }

    @Test
    fun `refreshRules reloads from DynamicRuleEngine`() {
        assertDoesNotThrow {
            repository.refreshRules()
        }
    }

    @Test
    fun `handles DynamicRuleEngine errors gracefully`() = runTest {
        every { dynamicRuleEngine.getAllRules() } throws RuntimeException("Engine error")
        
        val errorRepository = FirewallRepositoryImpl(context)
        val rules = errorRepository.getAllRules().first()
        
        assertNotNull(rules)
    }

    @Test
    fun `createRule handles errors gracefully`() = runTest {
        every { dynamicRuleEngine.addRule(any()) } throws RuntimeException("Add failed")
        
        val newRule = FirewallRule(
            id = "rule4",
            name = "Test Rule",
            sourceIp = "0.0.0.0",
            destinationIp = "0.0.0.0",
            sourcePort = 0,
            destinationPort = 0,
            protocol = "ANY",
            action = FirewallRule.Action.BLOCK,
            enabled = true,
            priority = 1
        )
        
        assertDoesNotThrow {
            repository.createRule(newRule)
        }
    }
}
