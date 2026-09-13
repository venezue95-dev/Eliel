package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
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
    assertEquals("ET VPN", appName)
  }

  @Test
  fun `verify global servers provider contains nodes with valid coordinates`() {
    val servers = com.example.vpn.data.VpnServerProvider.SERVERS
    org.junit.Assert.assertTrue("Should have extensive servers list", servers.size > 20)
    
    servers.forEach { server ->
      org.junit.Assert.assertTrue(
        "Latitude must be within valid range [-90, 90]",
        server.latitude in -90.0..90.0
      )
      org.junit.Assert.assertTrue(
        "Longitude must be within valid range [-180, 180]",
        server.longitude in -180.0..180.0
      )
      org.junit.Assert.assertTrue(
        "Coordinates formatted string must not be empty",
        server.formattedCoordinates().isNotEmpty()
      )
    }
  }

  @Test
  fun `verify theme options availability`() {
    val themes = com.example.vpn.model.AppThemeMode.entries
    assertEquals(6, themes.size)
    org.junit.Assert.assertTrue(themes.contains(com.example.vpn.model.AppThemeMode.SYSTEM))
    org.junit.Assert.assertTrue(themes.contains(com.example.vpn.model.AppThemeMode.WHITE_NAVY))
  }

  @Test
  fun `verify localization manager and language resolution`() {
    val esStrings = com.example.vpn.model.LocalizationManager.getStrings(com.example.vpn.model.AppLanguage.SPANISH)
    assertEquals("ET VPN", esStrings.appName)
    assertEquals("Servidores", esStrings.tabGateways)

    val enStrings = com.example.vpn.model.LocalizationManager.getStrings(com.example.vpn.model.AppLanguage.ENGLISH)
    assertEquals("Gateways", enStrings.tabGateways)

    val detectedLang = com.example.vpn.model.LocalizationManager.detectLanguageFromCountryCode("ES")
    assertEquals(com.example.vpn.model.AppLanguage.SPANISH, detectedLang)
  }
}
