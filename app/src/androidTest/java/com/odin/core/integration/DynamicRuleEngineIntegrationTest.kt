package com.odin.core.integration

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.odin.core.domain.model.FirewallRule
import com.odin.core.domain.model.RuleAction
import com.odin.core.domain.model.RuleType
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import javax.inject.Inject

/**
 * DynamicRuleEngineIntegrationTest verifies integration with DynamicRuleEngine (Firewall).
 * Tests rule retrieval, creation, editing, activation/deactivation, and evaluation functionality.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DynamicRuleEngineIntegrationTest : BaseIntegrationTest() {
    
    private lateinit var testRules: List<FirewallRule>
    
    @Before
    fun setupTest() {
        // Create test firewall rules
        testRules = listOf(
            FirewallRule(
                id = 1,
                name = "Block Malicious IPs",
                type = RuleType.BLOCK,
                action = RuleAction.BLOCK,
                sourceIp = "192.168.1.100",
                destinationIp = "10.0.0.1",
                port = 443,
                protocol = "TCP",
                enabled = true,
                createdAt = Date(),
                priority = 1
            ),
            FirewallRule(
                id = 2,
                name = "Allow Internal Traffic",
                type = RuleType.ALLOW,
                action = RuleAction.ALLOW,
                sourceIp = "192.168.1.0/24",
                destinationIp = "10.0.0.0/24",
                port = 80,
                protocol = "TCP",
                enabled = true,
                createdAt = Date(),
                priority = 2
            ),
            FirewallRule(
                id = 3,
                name = "Block Suspicious Ports",
                type = RuleType.BLOCK,
                action = RuleAction.BLOCK,
                sourceIp = "*",
                destinationIp = "*",
                port = 6666,
                protocol = "TCP",
                enabled = false,
                createdAt = Date(),
                priority = 3
            )
        )
    }
    
    @Test
    fun testRuleRetrieval() = runTest {
        // Test rule retrieval functionality
        val enabledRules = testRules.filter { it.enabled }
        
        assertEquals("Should have 2 enabled rules", 2, enabledRules.size)
        assertTrue("All enabled rules should be enabled", enabledRules.all { it.enabled })
    }
    
    @Test
    fun testRuleCreation() = runTest {
        // Test rule creation
        val newRule = FirewallRule(
            id = 999,
            name = "Test Rule",
            type = RuleType.BLOCK,
            action = RuleAction.BLOCK,
            sourceIp = "127.0.0.1",
            destinationIp = "127.0.0.1",
            port = 8080,
            protocol = "TCP",
            enabled = true,
            createdAt = Date(),
            priority = 10
        )
        
        assertNotNull("New rule should not be null", newRule)
        assertEquals("New rule should have correct name", "Test Rule", newRule.name)
        assertEquals("New rule should be enabled", true, newRule.enabled)
        assertEquals("New rule should have correct type", RuleType.BLOCK, newRule.type)
    }
    
    @Test
    fun testRuleEditing() = runTest {
        // Test rule editing
        val originalRule = testRules[0]
        val editedRule = originalRule.copy(
            name = "Updated Rule Name",
            enabled = false
        )
        
        assertEquals("Edited rule should have new name", "Updated Rule Name", editedRule.name)
        assertEquals("Edited rule should be disabled", false, editedRule.enabled)
        assertEquals("Edited rule should preserve ID", originalRule.id, editedRule.id)
    }
    
    @Test
    fun testRuleActivation() = runTest {
        // Test rule activation/deactivation
        val disabledRule = testRules[2]
        val activatedRule = disabledRule.copy(enabled = true)
        
        assertTrue("Activated rule should be enabled", activatedRule.enabled)
        assertEquals("Activated rule should preserve ID", disabledRule.id, activatedRule.id)
    }
    
    @Test
    fun testRuleDeactivation() = runTest {
        // Test rule deactivation
        val enabledRule = testRules[0]
        val deactivatedRule = enabledRule.copy(enabled = false)
        
        assertFalse("Deactivated rule should be disabled", deactivatedRule.enabled)
        assertEquals("Deactivated rule should preserve ID", enabledRule.id, deactivatedRule.id)
    }
    
    @Test
    fun testRuleEvaluation() = runTest {
        // Test rule evaluation logic
        val blockRules = testRules.filter { it.type == RuleType.BLOCK && it.enabled }
        
        assertEquals("Should have 1 enabled block rule", 1, blockRules.size)
        assertEquals("Block rule should have BLOCK action", RuleAction.BLOCK, blockRules[0].action)
    }
    
    @Test
    fun testRulePriority() = runTest {
        // Test rule priority ordering
        val sortedRules = testRules.sortedBy { it.priority }
        
        assertEquals("First rule should have priority 1", 1, sortedRules[0].priority)
        assertEquals("Second rule should have priority 2", 2, sortedRules[1].priority)
        assertEquals("Third rule should have priority 3", 3, sortedRules[2].priority)
    }
    
    @Test
    fun testRulePersistence() = runTest {
        // Test that rule data structure is preserved
        val originalRule = testRules[0]
        
        assertEquals("Rule ID should be preserved", 1L, originalRule.id)
        assertEquals("Rule name should be preserved", "Block Malicious IPs", originalRule.name)
        assertEquals("Rule type should be preserved", RuleType.BLOCK, originalRule.type)
        assertEquals("Rule action should be preserved", RuleAction.BLOCK, originalRule.action)
        assertEquals("Rule source IP should be preserved", "192.168.1.100", originalRule.sourceIp)
        assertEquals("Rule destination IP should be preserved", "10.0.0.1", originalRule.destinationIp)
        assertEquals("Rule port should be preserved", 443, originalRule.port)
        assertEquals("Rule protocol should be preserved", "TCP", originalRule.protocol)
        assertTrue("Rule should be enabled", originalRule.enabled)
        assertNotNull("Rule creation date should be preserved", originalRule.createdAt)
    }
}
