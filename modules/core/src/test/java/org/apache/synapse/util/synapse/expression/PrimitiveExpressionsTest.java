/*
 *  Copyright (c) 2024, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.synapse.util.synapse.expression;

import org.apache.synapse.SynapseConstants;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for primitive expressions.
 */
public class PrimitiveExpressionsTest {

    @Test
    public void testEQ() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("-5.3 == -5.3"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 == 3"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("true == true"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("true == false"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("\"abc\" == \"abc\""));
        Assert.assertEquals("false", TestUtils.evaluateExpression("\"abc\" == \"pqr\""));
        Assert.assertEquals("true", TestUtils.evaluateExpression("null == null"));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayload("\"John\" == payload.name", 1));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayload("null == payload[\"null\"]", 1));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpressionWithPayload("\"abc\" == payload.age", 2));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayload("$.store.book[0] == $.store.book[0]", 2));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayload("$.store.book[0] == $.store.book[1]", 2));
    }

    @Test
    public void testNEQ() {
        Assert.assertEquals("false", TestUtils.evaluateExpression("-5.3 != -5.3"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 != 3"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("true != true"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("true != false"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("\"abc\" != \"abc\""));
        Assert.assertEquals("true", TestUtils.evaluateExpression("\"abc\" != \"pqr\""));
        Assert.assertEquals("false", TestUtils.evaluateExpression("null != null"));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayload("\"John\" != $.name",1));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpressionWithPayload("\"John\" != $.name",2));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayload("$.store.book[0] == null", 2));
    }

    @Test
    public void testGT() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 > 3"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 > -3.4"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("-5 > -3.4"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("5 > \"bla\""));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("5 > null"));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayloadAndVariables("$.age > var.num1",1,1));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpressionWithPayloadAndVariables("$.age > $[\"null\"]",1,1));
    }

    @Test
    public void testLT() {
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 < 3"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 < -3.4"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("-5 < -3.4"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 == 5"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpressionWithPayload("$.age < \"bla\"", 1));
    }

    @Test
    public void testGTE() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 >= 3"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 >= -3.4"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("-5 >= -3.4"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("true >= false"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpressionWithPayload("$.age >= \"bla\"", 1));
    }

    @Test
    public void testLTE() {
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 <= 3"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 <= -3.4"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("-5 <= -3.4"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpressionWithPayload("$.age <= \"bla\"", 1));
    }

    @Test
    public void testAnd() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("true and true"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("true and false"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("true && true && false"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("5 and \"bla\""));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("5 and null"));
    }

    @Test
    public void testOr() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("true or true"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("true or false"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("true || true || false"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("false or false"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("5 or \"bla\""));
    }

    @Test
    public void testAdd() {
        Assert.assertEquals("8.5", TestUtils.evaluateExpression("5.5 + 3"));
        Assert.assertEquals("7", TestUtils.evaluateExpression("5 + 3 + -1"));
        Assert.assertEquals("8.5", TestUtils.evaluateExpression("5.5 + 3"));
        Assert.assertEquals("9.0", TestUtils.evaluateExpression("5.5 + 3.5"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("\"abc\" + 5"));
        Assert.assertEquals("abcxyz", TestUtils.evaluateExpression("\"abc\" + \"xyz\""));
        Assert.assertEquals("7.5", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "var.num1 + var.num3", 2, 1));
        Assert.assertEquals("20", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "var.num1 + payload.expensive", 2, 1));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpressionWithPayloadAndVariables("5 + var.name", 2, 1));
        // clear the synCtx to remove previous payload and variables.
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("var.num99 + 5"));
    }

    @Test
    public void testSubtract() {
        Assert.assertEquals("-33", TestUtils.evaluateExpression("5 - 30 + 2 - 10"));
        Assert.assertEquals("2.5", TestUtils.evaluateExpression("5.5 - 3"));
        Assert.assertEquals("2.0", TestUtils.evaluateExpression("5.5 - 3.5"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("var.num99 - 5"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("5 - \"bla\""));
        Assert.assertEquals("12.5", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "var.num1 - var.num3", 2, 1));
    }

    @Test
    public void testMultiply() {
        Assert.assertEquals("-30", TestUtils.evaluateExpression("5 * 3 * -2"));
        Assert.assertEquals("16.5", TestUtils.evaluateExpression("5.5 * 3"));
        Assert.assertEquals("19.25", TestUtils.evaluateExpression("5.5 * 3.5"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("var.num99 * 5"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("5 * \"bla\""));
        Assert.assertEquals("-25.0", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "var.num1 * var.num3", 2, 1));
    }

    @Test
    public void testDivide() {
        Assert.assertEquals("-4.0", TestUtils.evaluateExpression("10 / 2 / -2.5 * 2"));
        Assert.assertEquals("-4.0", TestUtils.evaluateExpression("10 / 2 / -2.5 * 2"));
        Assert.assertEquals("3", TestUtils.evaluateExpression("9 / 3"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("var.num99 / 5"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("5 / \"bla\""));
        Assert.assertEquals("5", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "var.num1 / 2", 2, 1));
    }

    @Test
    public void testMod() {
        Assert.assertEquals("1", TestUtils.evaluateExpression("10 % 3"));
        Assert.assertEquals("2.5", TestUtils.evaluateExpression("5.5 % 3"));
        Assert.assertEquals("2.0", TestUtils.evaluateExpression("5.5 % 3.5"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("var.num99 % 5"));
        Assert.assertEquals(SynapseConstants.UNKNOWN, TestUtils.evaluateExpression("5 % \"bla\""));
        Assert.assertEquals("0", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "var.num1 % 2", 2, 1));
    }
}
