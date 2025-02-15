// Copyright (C) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in project root for information.

package com.microsoft.azure.synapse.ml.core.utils.utils

import com.microsoft.azure.synapse.ml.cognitive.text.TextSentiment
import com.microsoft.azure.synapse.ml.core.env.FileUtilities.join
import com.microsoft.azure.synapse.ml.core.test.base.TestBase
import com.microsoft.azure.synapse.ml.core.utils.ModelEquality
import com.microsoft.azure.synapse.ml.stages.DropColumns

class ModelEqualitySuite extends TestBase {
  spark

  test("Complex param equality") {
    val m1 = new TextSentiment().setLocation("eastus")
    val m2 = new TextSentiment().setLocation("eastus")
    ModelEquality.assertEqual(m1, m2)

    val p1 = join(tmpDir.toString, "1_m1.model").toString
    val p2 = join(tmpDir.toString, "1_m2.model").toString
    m1.write.overwrite().save(p1)
    m2.write.overwrite().save(p2)
    ModelEquality.assertEqual("com.microsoft.azure.synapse.ml.cognitive.text.TextSentiment", p1, p2)
  }

  test("Basic equality") {
    val m1 = new DropColumns().setCols(Array("c1", "c2"))
    val m2 = new DropColumns().setCols(Array("c1", "c2"))
    ModelEquality.assertEqual(m1, m2)

    val p1 = join(tmpDir.toString, "2_m1.model").toString
    val p2 = join(tmpDir.toString, "2_m2.model").toString
    m1.write.overwrite().save(p1)
    m2.write.overwrite().save(p2)
    ModelEquality.assertEqual("com.microsoft.azure.synapse.ml.stages.DropColumns", p1, p2)
  }

  test("Basic non equality") {
    val m1 = new DropColumns().setCols(Array("c1", "c2"))
    val m2 = new DropColumns().setCols(Array("c1", "c3"))
    assertThrows[AssertionError](ModelEquality.assertEqual(m1, m2))

    val p1 = join(tmpDir.toString, "3_m1.model").toString
    val p2 = join(tmpDir.toString, "3_m2.model").toString
    m1.write.overwrite().save(p1)
    m2.write.overwrite().save(p2)
    assertThrows[AssertionError](
      ModelEquality.assertEqual("com.microsoft.azure.synapse.ml.stages.DropColumns", p1, p2))
  }

}
