package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.engine.TimeEngine
import com.example.model.ModuleRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("WB Police Life", appName)
  }

  @Test
  fun `time engine formats irregular times without seconds`() {
    val timeEngine = TimeEngine()
    val formatted = timeEngine.formatTime(461)
    assertEquals("07:41 AM", formatted)
    val afternoon = timeEngine.formatTime(817)
    assertEquals("01:37 PM", afternoon)
  }

  @Test
  fun `permanent module registry contains 50 active modules`() {
    assertEquals(50, ModuleRegistry.PERMANENT_MODULES.size)
    assertTrue(ModuleRegistry.PERMANENT_MODULES.contains("WBP_HYPER_REALISM"))
    assertTrue(ModuleRegistry.PERMANENT_MODULES.contains("ANTI_CINEMATIC"))
    assertTrue(ModuleRegistry.PERMANENT_MODULES.contains("HEALTH_AND_VITALS_ENGINE"))
    assertTrue(ModuleRegistry.PERMANENT_MODULES.contains("MALKHANA_AND_EVIDENCE_ENGINE"))
    assertTrue(ModuleRegistry.PERMANENT_MODULES.contains("FORENSIC_AND_POSTMORTEM_ENGINE"))
    assertTrue(ModuleRegistry.PERMANENT_MODULES.contains("AUTO_TIME_PASS"))
    assertTrue(ModuleRegistry.PERMANENT_MODULES.contains("LIVE_PLUS_ONE_MINUTE"))
  }
}

